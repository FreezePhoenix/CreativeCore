package team.creative.creativecore.mixin;

import net.fabricmc.fabric.impl.client.indigo.renderer.render.BlockRenderInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import team.creative.creativecore.client.render.model.BlockInfoExtension;

@Mixin(BlockRenderInfo.class)
public abstract class BlockInfoMixin implements BlockInfoExtension {
	@Unique
	public int customTint = -1;

	@Inject(at = @At(value = "HEAD"),
	        method = "blockColor",
	        cancellable = true, remap = false)
	public void getColorMultiplierHook(int tint, CallbackInfoReturnable<Integer> info) {
		if (customTint != -1)
			info.setReturnValue(customTint);
	}
	@Override
	public void setCustomTint(int tint) {
		this.customTint = tint;
	}
}
