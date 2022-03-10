package team.creative.creativecore.common.config.premade;

import net.fabricmc.api.EnvType;
import team.creative.creativecore.common.config.api.CreativeConfig;
import team.creative.creativecore.common.config.api.ICreativeConfig;

import java.util.Random;

public class IntMinMax implements ICreativeConfig {

	@CreativeConfig
	public int min;
	@CreativeConfig
	public int max;

	public IntMinMax(int min, int max) {
		this.min = min;
		this.max = max;
	}

	public int next(Random rand) {
		if (min == max)
			return min;
		return min + rand.nextInt(max - min);
	}

	@Override
	public void configured(EnvType side) {
		if (min > max) {
			int temp = min;
			this.min = max;
			this.max = temp;
		}
	}

	public int spanLength() {
		return max - min;
	}

}
