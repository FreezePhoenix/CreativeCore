package team.creative.creativecore.client;

import com.google.common.collect.ImmutableMap;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.minecraft.client.gui.screens.Screen;
import team.creative.creativecore.Side;
import team.creative.creativecore.common.config.gui.ConfigGuiLayer;
import team.creative.creativecore.common.config.holder.CreativeConfigRegistry;
import team.creative.creativecore.common.config.holder.ICreativeConfigHolder;
import team.creative.creativecore.common.gui.integration.GuiScreenIntegration;

import java.util.HashMap;
import java.util.Map;

public class ModMenuImpl implements ModMenuApi {
    @Override
    public ConfigScreenFactory<Screen> getModConfigScreenFactory() {
        return (screen) -> new GuiScreenIntegration(new ConfigGuiLayer(CreativeConfigRegistry.ROOT, Side.CLIENT)) {
            public void onClose() {
                this.minecraft.setScreen(screen);
            }
        };
    }
    public Map<String, ConfigScreenFactory<?>> getProvidedConfigScreenFactories() {
        Map<String, ConfigScreenFactory<?>> MAP = new HashMap<>();
        for(String MODID : CreativeCoreClient.MODS) {
            ICreativeConfigHolder holder = CreativeConfigRegistry.ROOT.followPath(MODID);
            if (holder != null && !holder.isEmpty(Side.CLIENT))
                MAP.put(MODID, (screen) -> new GuiScreenIntegration(new ConfigGuiLayer(holder, Side.CLIENT)) {
                    public void onClose() {
                        this.minecraft.setScreen(screen);
                    }
                });
        }
        return MAP;
    }
}
