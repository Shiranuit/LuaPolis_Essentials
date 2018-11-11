package fr.shiranuit.luapolisessentials.Manager;

import java.io.File;
import java.util.HashMap;

import fr.shiranuit.luapolisessentials.Area.Area;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class AreaManager {
	public static String modLogDir;
	public static HashMap<String,Area> Areas = new HashMap<String,Area>();
	public static HashMap<String, Boolean> flagDefault = new HashMap<String, Boolean>();
	
	static {
		flagDefault.put("mode_whitelist", false);
	}
	
	public static void reloadProtection() {
		File dir = new File(modLogDir);
		Areas = new HashMap<String,Area>();
		 if (!dir.exists()) {
			 dir.mkdirs();
		 }
		 File[] lst = dir.listFiles();
		 for (File f : lst) {
			 Configuration config = new Configuration(f);
			int x = config.get("pos","x",0).getInt();
			int y = config.get("pos","y",0).getInt();
			int z = config.get("pos","z",0).getInt();
			
			int dx = config.get("pos","dx",0).getInt();
			int dy = config.get("pos","dy",0).getInt();
			int dz = config.get("pos","dz",0).getInt();
			
			int dimensionID = config.get("pos","dimensionID",0).getInt();
			
			
			String[] whitelist = config.get("protect","whitelist",new String[] {}).getStringList();
			String[] blacklist = config.get("protect","blacklist",new String[] {}).getStringList();
			
			HashMap flags = new HashMap();
			ConfigCategory Cflags = config.getCategory("flags");
			
			for (String key : flagDefault.keySet()) {
				flags.put(key, config.get("flags",key, flagDefault.get(key)).getBoolean());
			}
			
			Areas.put(f.getName(), new Area(x,y,z,dx,dy,dz,whitelist,blacklist,dimensionID,flags));
		 }
	}
	
	public static void writeProtection(String name, int x, int y, int z, int dx, int dy, int dz, int dimensionID, HashMap<String, Boolean> flags) {
		writeProtection(new Area(x, y, z, dx, dy, dz, new String[]{},new String[]{}, dimensionID, flags), name);
	}
	
	public static void writeProtection(Area p, String name) {
		File f = new File(modLogDir + File.separatorChar + name);
		if (f.exists()) {
			f.delete();
		}
		Configuration config = new Configuration(f);
		 config.get("pos","x",p.x);
		 config.get("pos","y",p.y);
		 config.get("pos","z",p.z);
		
		 config.get("pos","dx",p.dx);
		 config.get("pos","dy",p.dy);
		 config.get("pos","dz",p.dz);
		
		 config.get("pos","dimensionID",p.dimensionID);
		
		 
		 config.get("protect","whitelist",p.whitelist);
		 config.get("protect","blacklist",p.blacklist);
		 
		 
		for (String key : flagDefault.keySet()) {
			config.get("flags", key, p.flags.containsKey(key) ? p.flags.get(key) : flagDefault.get(key));
		}
		 
		 Areas.put(f.getName(), p);
		 config.save();
	}
	
	public static void deleteProtection(String name) {
		if (Areas.containsKey(name)) {
			Areas.remove(name);
		}
		File f = new File(modLogDir + File.separatorChar + name);
		if (f.exists()) {
			f.delete();
		}
	}
	
}
