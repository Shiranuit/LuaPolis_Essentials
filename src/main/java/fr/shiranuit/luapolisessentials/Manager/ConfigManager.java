package fr.shiranuit.luapolisessentials.Manager;

import java.io.File;

import fr.shiranuit.luapolisessentials.Main;
import fr.shiranuit.luapolisessentials.Utils.ArrayConverter;
import net.minecraftforge.common.config.Configuration;

public class ConfigManager {
	public static String modLogDir;
	
	public static Configuration EssentialsConfig;
	public static void sync() {
		if (EssentialsConfig != null) {
			EssentialsConfig.load();
		} else {
			EssentialsConfig = new Configuration(new File(modLogDir + File.separatorChar + "Essentials.cfg"));
		}
		Main.DisabledItemsBlocks = ArrayConverter.convert(EssentialsConfig.get("Disable", "ItemsBlocks", new String[] {}, "List of items and blocks to disable").getStringList());
		Main.requiredAfter = EssentialsConfig.get("RequiredAfter", "Mods", new String[] {}).getStringList();
		AreaManager.flagDefault.put("pvp", EssentialsConfig.get("World", "pvp", false).getBoolean());
		AreaManager.flagDefault.put("explosion", EssentialsConfig.get("World", "explosion", true).getBoolean());
		LogForJusticeManager.enabled = EssentialsConfig.get("LogForJustice", "enabled", true).getBoolean();
		LogForJusticeManager.swapSize = EssentialsConfig.get("LogForJustice", "swapsize", 50).getInt();
		
		EssentialsConfig.save();
	}
}
