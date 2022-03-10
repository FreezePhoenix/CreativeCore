package team.creative.creativecore.client.render.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;

import net.fabricmc.fabric.api.client.model.ModelProviderContext;
import net.fabricmc.fabric.api.client.model.ModelProviderException;
import net.fabricmc.fabric.api.client.model.ModelResourceProvider;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import org.jetbrains.annotations.Nullable;
import team.creative.creativecore.CreativeCore;

public class CreativeModelLoader implements ModelResourceProvider {
    private static final ResourceLocation MODEL_ID = new ResourceLocation(CreativeCore.MODID, "rendered");
    private static final UnbakedModel BAKED_MODEL = new CreativeModel();
    public CreativeModelLoader(ResourceManager rm) {

    }
    @Override
    public @Nullable UnbakedModel loadModelResource(ResourceLocation resourceId, ModelProviderContext context) throws ModelProviderException {
        if(resourceId.equals(MODEL_ID)) {
            return BAKED_MODEL;
        }
        return null;
    }
}
