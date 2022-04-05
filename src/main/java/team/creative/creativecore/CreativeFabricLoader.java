package team.creative.creativecore;

import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Supplier;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.eventbus.api.Event;
import team.creative.creativecore.client.ClientLoader;
import team.creative.creativecore.common.CommonLoader;

public class CreativeFabricLoader implements ICreativeLoader {
    
    @Override
    public void registerDisplayTest(Supplier<String> suppliedVersion, BiPredicate<String, Boolean> remoteVersionTest) {}
    
    @Override
    public String ignoreServerNetworkConstant() {
        return "";
    }
    
    @Override
    public void register(CommonLoader loader) {}
    
    @Override
    public void registerClient(ClientLoader loader) {
        CommandRegistrationCallback.EVENT.register((dispatcher, server) -> {
            if (!server)
                loader.registerClientCommands(dispatcher);
        });
    }
    
    @Override
    public void registerClientTick(Runnable run) {
        ClientTickEvents.END_CLIENT_TICK.register(x -> run.run());
    }
    
    @Override
    public void registerClientRender(Runnable run) {
        HudRenderCallback.EVENT.register((matrix, partialTicks) -> run.run());
    }
    
    @Override
    public void registerLoadLevel(Consumer<LevelAccessor> consumer) {
        ServerWorldEvents.LOAD.register((server, level) -> consumer.accept(level));
    }
    
    @Override
    public <T> void registerListener(Consumer<T> consumer) {}
    
    @Override
    public void registerClientStarted(Runnable run) {
        ClientLifecycleEvents.CLIENT_STARTED.register(x -> run.run());
    }
    
    @Override
    public void postForge(Event event) {}
    
    @Override
    public boolean isModLoaded(String modid) {
        return false;
    }
    
}
