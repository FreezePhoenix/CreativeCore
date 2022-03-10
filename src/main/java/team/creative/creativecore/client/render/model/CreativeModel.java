package team.creative.creativecore.client.render.model;

import com.google.common.base.Suppliers;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.Object2ObjectFunctions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import team.creative.creativecore.client.CreativeCoreClient;
import team.creative.creativecore.client.render.box.RenderBox;
import team.creative.creativecore.common.util.math.base.Facing;
import team.creative.creativecore.common.util.mc.ColorUtils;
import team.creative.creativecore.mixin.MinecraftAccessor;

import java.util.*;
import java.util.function.Function;

public class CreativeModel implements UnbakedModel, BakedModel {
	public static final CreativeModel INSTANCE = new CreativeModel();

	public static Minecraft mc = Minecraft.getInstance();
	public static ItemColors itemColores = null;
	public static TextureAtlasSprite woodenTexture;
	private static ItemStack lastItemStack = null;
	public static ItemOverrides customOverride = new ItemOverrides(null, null, resourceLocation -> {
		return null;
	}, List.of()) {

		@Override
		public BakedModel resolve(BakedModel original, ItemStack stack, ClientLevel level, LivingEntity entity, int p_173469_) {
			lastItemStack = stack;
			return original;
		}
	};
	Material WOODEN_MATERIAL = new Material(InventoryMenu.BLOCK_ATLAS,
	                                        new ResourceLocation("minecraft", "blocks/planks_oak")
	);

	public static void lateInit() {
		itemColores = ((MinecraftAccessor) mc).getItemColors();
	}

	public static void setLastItemStack(ItemStack stack) {
		lastItemStack = stack;
	}

	@SuppressWarnings("deprecation")
	public static TextureAtlasSprite getWoodenTexture() {
		if (woodenTexture == null) woodenTexture = Minecraft.getInstance()
		                                                    .getTextureAtlas(TextureAtlas.LOCATION_BLOCKS)
		                                                    .apply(new ResourceLocation(
				                                                    "minecraft",
				                                                    "blocks/planks_oak"
		                                                    ));
		return woodenTexture;
	}

	public static List<BakedQuad> compileBoxes(List<? extends RenderBox> boxes, Facing side, Random rand, boolean item) {
		List<BakedQuad> baked = new ArrayList<>();
		for (int i = 0; i < boxes.size(); i++) {
			RenderBox box = boxes.get(i);

			if (!box.renderSide(side)) continue;

			BlockState state = Blocks.AIR.defaultBlockState();
			if (box.state != null) state = box.state;

			BakedModel blockModel = mc.getBlockRenderer().getBlockModel(state);

			int defaultColor = ColorUtils.WHITE;
			if (item) defaultColor = itemColores.getColor(new ItemStack(state.getBlock()), defaultColor);

			baked.addAll(box.getBakedQuad(null,
			                              null,
			                              box.getOffset(),
			                              state,
			                              blockModel,
			                              side,
			                              rand,
			                              true,
			                              defaultColor
			));
		}
		return baked;
	}

	public static List<BakedQuad> getQuads(BlockState state, Facing facing, Random rand, boolean threaded) {
		if (state != null) {
			CreativeRenderBlock renderer = CreativeCoreClient.RENDERED_BLOCKS.get(state.getBlock());
			if (renderer != null) {
				return compileBoxes(renderer.getBoxes(state), facing, rand, false);
			}
			return Collections.EMPTY_LIST;
		}

		ItemStack stack = lastItemStack;
		CreativeRenderItem renderer = CreativeCoreClient.RENDERED_ITEMS.get(stack.getItem());

		if (renderer != null) {
			List<BakedQuad> cached = renderer.getCachedModel(facing, stack, threaded);
			if (cached != null) return cached;
			List<? extends RenderBox> boxes = renderer.getBoxes(stack);
			if (boxes != null) {
				cached = compileBoxes(boxes, facing, rand, true);
				renderer.saveCachedModel(facing, cached, stack, threaded);
				return cached;
			}
		}

		return Collections.EMPTY_LIST;
	}

	@Override
	public List<BakedQuad> getQuads(BlockState state, Direction side, Random rand) {
		return getQuads(state, Facing.get(side), rand, false);
	}

	@Override
	public boolean useAmbientOcclusion() {
		return true;
	}

	@Override
	public boolean isGui3d() {
		return true;
	}

//    @Override
//    public BakedModel handlePerspective(TransformType cameraTransformType, PoseStack poseStack) {
//        if (lastItemStack != null) {
//            CreativeRenderItem renderer = CreativeCoreClient.RENDERED_ITEMS.get(lastItemStack.getItem());
//            if (renderer != null)
//                renderer.applyCustomOpenGLHackery(poseStack, lastItemStack, cameraTransformType);
//        }
//        return BakedModel.super.handlePerspective(cameraTransformType, poseStack);
//    }

	@Override
	public boolean isCustomRenderer() {
		return false;
	}

//    @Override
//    public boolean isLayered() {
//        return true;
//    }
//
//    @Override
//    public List<com.mojang.datafixers.util.Pair<BakedModel, RenderType>> getLayerModels(ItemStack itemStack, boolean fabulous) {
//        CreativeRenderItem renderer = CreativeCoreClient.RENDERED_ITEMS.get(lastItemStack.getItem());
//        if (renderer != null) {
//            RenderType[] itemLayers = renderer.getLayers(itemStack, fabulous);
//            List<com.mojang.datafixers.util.Pair<BakedModel, RenderType>> layers = new ArrayList<>(itemLayers.length);
//            for (int i = 0; i < itemLayers.length; i++)
//                layers.add(new Pair<BakedModel, RenderType>(this, itemLayers[i]));
//            return layers;
//
//        }
//        return Collections.EMPTY_LIST;
//    }

	@Override
	public ItemOverrides getOverrides() {
		return customOverride;
	}

	@Override
	public boolean usesBlockLight() {
		return true;
	}

	@Override
	public TextureAtlasSprite getParticleIcon() {
		return getWoodenTexture();
	}

	@Override
	public ItemTransforms getTransforms() {
		return ItemTransforms.NO_TRANSFORMS;
	}

	@Override
	public BakedModel bake(@NotNull ModelBakery modelBakery, @NotNull Function<Material, TextureAtlasSprite> mapper, @NotNull ModelState modelState, @NotNull ResourceLocation resourceLocation) {
		customOverride = new ItemOverrides(modelBakery, null,Object2ObjectFunctions.EMPTY_FUNCTION, Collections.EMPTY_LIST) {
			@Override
			public BakedModel resolve(BakedModel original, ItemStack stack, ClientLevel level, LivingEntity entity, int p_173469_) {
				lastItemStack = stack;
				return original;
			}
		};
		return INSTANCE;
	}

	@Override
	public Collection<ResourceLocation> getDependencies() {
		return Collections.EMPTY_LIST;
	}

	@Override
	public Collection<Material> getMaterials(@NotNull Function<ResourceLocation, UnbakedModel> modelGetter, @NotNull Set<Pair<String, String>> missingTextureErrors) {
		return Collections.singleton(WOODEN_MATERIAL);
	}
}
