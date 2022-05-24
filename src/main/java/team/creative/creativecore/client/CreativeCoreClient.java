package team.creative.creativecore.client;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import team.creative.creativecore.CreativeCore;
import team.creative.creativecore.Side;
import team.creative.creativecore.common.config.gui.ConfigGuiLayer;
import team.creative.creativecore.common.config.holder.CreativeConfigRegistry;
import team.creative.creativecore.common.gui.IScaleableGuiScreen;
import team.creative.creativecore.common.gui.integration.ContainerIntegration;
import team.creative.creativecore.common.gui.integration.ContainerScreenIntegration;
import team.creative.creativecore.common.gui.integration.GuiEventHandler;
import team.creative.creativecore.common.gui.integration.GuiScreenIntegration;
import team.creative.creativecore.common.gui.style.GuiStyle;

import java.util.HashSet;
import java.util.Set;

public class CreativeCoreClient implements ClientModInitializer {
    
    private static final Minecraft mc = Minecraft.getInstance();

    protected static Set<String> MODS = new HashSet<>();
    public static void registerClientConfig(String modid) {
        MODS.add(modid);
    }
    
    public static float getDeltaFrameTime() {
        if (mc.isPaused())
            return 1.0F;
        return mc.getDeltaFrameTime();
    }
    
    public static void commands(CommandDispatcher<CommandSourceStack> dispatcher, boolean dedicated) {
        dispatcher.register(Commands.literal("cmdclientconfig").executes((CommandContext<CommandSourceStack> x) -> {
            GuiEventHandler.queueScreen(new GuiScreenIntegration(new ConfigGuiLayer(CreativeConfigRegistry.ROOT, Side.CLIENT)));
            return 0;
        }));
    }
    
    @Override
    public void onInitializeClient() {
        ClientTickEvents.START_CLIENT_TICK.register(GuiEventHandler::onTick);
        ClientLifecycleEvents.CLIENT_STARTED.register(client -> GuiStyle.reload());
        ScreenRegistry.register(CreativeCore.GUI_CONTAINER, (ContainerIntegration container, Inventory inventory, Component p_create_3_) -> {
            return new ContainerScreenIntegration(container, inventory);
        });
        CommandRegistrationCallback.EVENT.register(CreativeCoreClient::commands);
        ClientTickEvents.START_CLIENT_TICK.register(CreativeCoreClient::clientTick);
    }
    
    public static void clientTick(Minecraft client) {
        if (client.screen instanceof IScaleableGuiScreen scaleableGuiScreen)
            scaleableGuiScreen.clientTick();
    }
}
