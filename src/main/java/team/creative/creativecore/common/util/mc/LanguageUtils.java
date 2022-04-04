package team.creative.creativecore.common.util.mc;

import java.util.IllegalFormatException;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.locale.Language;

public class LanguageUtils {
    
    public static boolean can(String name) {
        return I18n.exists(name);
    }
    
    public static String translate(String name) {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT)
            return I18n.get(name);
        return Language.getInstance().getOrDefault(name);
    }
    
    public static String translateOr(String name, String defaultString) {
        String result = translate(name);
        if (name.equals(result))
            return defaultString;
        return name;
    }
    
    public static String translate(String name, Object... args) {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT)
            return I18n.get(name, args);
        try {
            return String.format(Language.getInstance().getOrDefault(name), args);
        } catch (IllegalFormatException illegalformatexception) {
            return "Format error: " + name;
        }
    }
    
}
