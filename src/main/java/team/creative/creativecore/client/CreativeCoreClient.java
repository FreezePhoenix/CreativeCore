package team.creative.creativecore.client;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.tags.TagKey;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import team.creative.creativecore.CreativeCore;
import team.creative.creativecore.client.render.model.CreativeModelLoader;
import team.creative.creativecore.client.render.model.CreativeRenderBlock;
import team.creative.creativecore.client.render.model.CreativeRenderItem;
import team.creative.creativecore.common.config.gui.ConfigGuiLayer;
import team.creative.creativecore.common.config.holder.CreativeConfigRegistry;
import team.creative.creativecore.common.config.holder.ICreativeConfigHolder;
import team.creative.creativecore.common.gui.IScaleableGuiScreen;
import team.creative.creativecore.common.gui.integration.ContainerIntegration;
import team.creative.creativecore.common.gui.integration.ContainerScreenIntegration;
import team.creative.creativecore.common.gui.integration.GuiEventHandler;
import team.creative.creativecore.common.gui.integration.GuiScreenIntegration;
import team.creative.creativecore.common.gui.style.GuiStyle;
import team.creative.creativecore.common.util.registry.FilteredHandlerRegistry;
import team.creative.creativecore.mixin.MinecraftAccessor;

public class CreativeCoreClient implements ClientModInitializer {
	public static void registerClientConfig(String modid) {
		// NOOP
	}

	public static final FilteredHandlerRegistry<Item, CreativeRenderItem> RENDERED_ITEMS = new FilteredHandlerRegistry<>(
			null);
	public static final FilteredHandlerRegistry<Block, CreativeRenderBlock> RENDERED_BLOCKS = new FilteredHandlerRegistry<>(
			null);
	private static final ItemColor ITEM_COLOR = (stack, tint) -> tint;
	private static final Minecraft mc = Minecraft.getInstance();

	public static void registerBlocks(CreativeRenderBlock renderer, Block... blocks) {
		for (int i = 0; i < blocks.length; i++)
			RENDERED_BLOCKS.register(blocks[i], renderer);
	}

	public static void registerBlock(CreativeRenderBlock renderer, Block block) {
		RENDERED_BLOCKS.register(block, renderer);
	}

	public static void registerItem(CreativeRenderItem renderer, Item item) {
		RENDERED_ITEMS.register(item, renderer);
		((MinecraftAccessor) mc).getItemColors().register(ITEM_COLOR, item);
	}

	public static float getDeltaFrameTime() {
		if (mc.isPaused())
			return 1.0F;
		return mc.getDeltaFrameTime();
	}

	public static void commands(CommandDispatcher<CommandSourceStack> dispatcher, boolean dedicated) {
		dispatcher.register(Commands.literal("cmdclientconfig").executes((CommandContext<CommandSourceStack> x) -> {
			CreativeCore.CONFIG_CLIENT_OPEN.open(x.getSource().getPlayerOrException());
			return 0;
		}));
	}

	@Override
	public void onInitializeClient() {
		ModelLoadingRegistry.INSTANCE.registerResourceProvider(CreativeModelLoader::new);
		ClientLifecycleEvents.CLIENT_STARTED.register(client -> {

			System.out.println("Heh");
			Registry.BLOCK.getTags().forEach((Pair<TagKey<Block>, HolderSet.Named<Block>> tagKeyNamedPair) -> {
				System.out.println(tagKeyNamedPair.getFirst().location());
			});
			GuiStyle.reload();
			Minecraft minecraft = Minecraft.getInstance();
			ReloadableResourceManager reloadableResourceManager = (ReloadableResourceManager) minecraft.getResourceManager();
			reloadableResourceManager.registerReloadListener(new SimplePreparableReloadListener() {

				@Override
				protected Object prepare(ResourceManager p_10796_, ProfilerFiller p_10797_) {
					return GuiStyle.class; // No idea
				}

				@Override
				protected void apply(Object p_10793_, ResourceManager p_10794_, ProfilerFiller p_10795_) {
					GuiStyle.reload();
					for (CreativeRenderItem handler : RENDERED_ITEMS.handlers())
						handler.reload();
				}
			});
		});
		ScreenRegistry.register(
				CreativeCore.GUI_CONTAINER,
				(ContainerIntegration container, Inventory inventory, Component p_create_3_) -> {
					return new ContainerScreenIntegration(container, inventory);
				}
		);
		CommandRegistrationCallback.EVENT.register(CreativeCoreClient::commands);
		ClientTickEvents.START_CLIENT_TICK.register(CreativeCoreClient::clientTick);
	}

	public static void clientTick(Minecraft client) {
		if (client.screen instanceof IScaleableGuiScreen scaleableGuiScreen) {
			scaleableGuiScreen.clientTick();
		}
	}
}
