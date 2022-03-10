package team.creative.creativecore.client.render.model;

import java.util.List;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.level.block.state.BlockState;
import team.creative.creativecore.client.render.box.RenderBox;

@Environment(EnvType.CLIENT)
public abstract class CreativeRenderBlock {
    
    public abstract List<? extends RenderBox> getBoxes(BlockState state);
    
}