package team.creative.creativecore;

import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Supplier;

import net.minecraft.world.level.LevelAccessor;
import team.creative.creativecore.client.ClientLoader;

public class CreativeFabricLoader implements ICreativeLoader {
    
    @Override
    public void registerDisplayTest(Supplier<String> suppliedVersion, BiPredicate<String, Boolean> remoteVersionTest) {}
    
    @Override
    public String ignoreServerNetworkConstant() {
        return "";
    }
    
    @Override
    public void registerClient(ClientLoader loader) {}
    
    @Override
    public void registerClientTick(Runnable run) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void registerClientRender(Runnable run) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void registerLoadLevel(Consumer<LevelAccessor> consumer) {
        // TODO Auto-generated method stub
        
    }
    
}
