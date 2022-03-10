package team.creative.creativecore.common.config.holder;

import com.google.gson.JsonObject;
import net.fabricmc.api.EnvType;
import team.creative.creativecore.common.config.sync.ConfigSynchronization;

import java.util.Collection;

public interface ICreativeConfigHolder {

	ICreativeConfigHolder parent();

	default String getName() {
		return path()[path().length - 1];
	}

	String[] path();

	Collection<? extends ConfigKey> fields();

	Collection<String> names();

	Object get(String key);

	ConfigKey getField(String key);

	boolean isDefault(EnvType side);

	void restoreDefault(EnvType side, boolean ignoreRestart);

	void load(boolean loadDefault, boolean ignoreRestart, JsonObject json, EnvType side);

	JsonObject save(boolean saveDefault, boolean ignoreRestart, EnvType side);

	boolean isEmpty(EnvType side);

	boolean isEmptyWithoutForce(EnvType side);

	ConfigSynchronization synchronization();

}
