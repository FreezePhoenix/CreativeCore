package team.creative.creativecore.client;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.api.ClientModInitializer;

public interface ClientLoader extends ClientModInitializer {
    @Override
    void onInitializeClient();

    /**
     * Code should implement like this:
     *
     *     public <T> void registerClientCommands(CommandDispatcher<T> dispatcher) {
     *         dispatcher.register(LiteralArgumentBuilder.<T>literal("ambient-debug").executes(x -> {
     *             TICK_HANDLER.showDebugInfo = !TICK_HANDLER.showDebugInfo;
     *             return Command.SINGLE_SUCCESS;
     *         }));
     *         dispatcher.register(LiteralArgumentBuilder.<T>literal("ambient-reload").executes(x -> {
     *             AmbientSounds.reload();
     *             return Command.SINGLE_SUCCESS;
     *         }));
     *     }
     * @param dispatcher The command dispatcher. Templated.
     * @param <T> The template type.
     */
    default <T> void registerClientCommands(CommandDispatcher<T> dispatcher) {

    }
}
