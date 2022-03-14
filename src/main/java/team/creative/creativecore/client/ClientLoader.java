package team.creative.creativecore.client;

import com.mojang.brigadier.CommandDispatcher;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.commands.CommandSourceStack;

public interface ClientLoader extends ClientModInitializer {
    
    @Override
    public void onInitializeClient();
    
    public default void registerClientCommands(CommandDispatcher<CommandSourceStack> dispatcher) {}
    
}
