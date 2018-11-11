package fr.shiranuit.luapolisessentials.Manager;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import fr.shiranuit.luapolisessentials.Jobs.Alchemist;
import fr.shiranuit.luapolisessentials.Jobs.Developper;
import fr.shiranuit.luapolisessentials.Jobs.Farmer;
import fr.shiranuit.luapolisessentials.Jobs.Forger;
import fr.shiranuit.luapolisessentials.Jobs.Hunter;
import fr.shiranuit.luapolisessentials.Jobs.Job;
import fr.shiranuit.luapolisessentials.Jobs.Miner;
import fr.shiranuit.luapolisessentials.Jobs.Woodcutter;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class JobManager {

	public static String modLogDir;
	public static Configuration MinerCfg;
	public static Configuration WoodcutterCfg;
	public static Configuration HunterCfg;
	public static Configuration FarmerCfg;
	public static Configuration ForgerCfg;
	public static Configuration AlchemistCfg;
	public static Configuration DevelopperCfg;
	public static HashMap<Class, Byte> jobs = new HashMap<Class, Byte>();
	public static HashMap<String, Class> jobsname = new HashMap<String, Class>();
	public static HashMap<String, Class> jobsnameselectable = new HashMap<String, Class>();
	public static void register(Class job, int id, String name, boolean selectable) {
		jobs.put(job, (byte)id);
		jobsname.put(name, job);
		if (selectable) {
			jobsnameselectable.put(name, job);
		}
	}
	
	public static class MinerParam {
		public static int stone = 1;
		public static int coal = 1;
		public static int iron = 1;
		public static int gold = 1;
		public static int diamond = 1;
		public static int emerald = 1;
		public static int quartz = 1;
	}
	
	public static class WoodcutterParam {
		public static int oak = 1;
		public static int spruce = 1;
		public static int birch = 1;
		public static int jungle = 1;
		public static int acacia = 1;
		public static int darkoak = 1;
	}
	
	public static class ForgerParam {
		public static int diamondsword = 1;
		public static int goldsword = 1;
		public static int ironsword = 1;
		public static int stonesword = 1;
		public static int woodsword = 1;
		
		public static int diamondhelmet = 1;
		public static int goldhelmet = 1;
		public static int ironhelmet = 1;
		public static int leatherhelmet = 1;
		
		public static int diamondchestplate = 1;
		public static int goldchestplate = 1;
		public static int ironchestplate = 1;
		public static int leatherchestplate = 1;
		
		public static int diamondleggings = 1;
		public static int goldleggings = 1;
		public static int ironleggings = 1;
		public static int leatherleggings = 1;
		
		public static int diamondboots = 1;
		public static int goldboots = 1;
		public static int ironboots = 1;
		public static int leatherboots = 1;
		
	}
	
	public static class FarmerParam {
		public static int wheat = 1;
		public static int beetroots = 1;
		public static int carrots = 1;
		public static int potatoes = 1;
		public static int melon = 1;
		public static int pumpkin = 1;
	}
	
	public static class HunterParam {
		public static int creeper = 1;
		public static int skeleton = 1;
		public static int zombie = 1;
		public static int blaze = 1;
		public static int ghast = 1;
		public static int zombievillager = 1;
		public static int pigzombie = 1;
		public static int spider = 1;
		public static int cavespider = 1;
		public static int wither = 1;
		public static int enderdragon = 1;
		public static int enderman = 1;
		public static int shulker = 1;
		public static int animals = 1;
		public static int witch = 1;
	}
	
	public static void loadCfg() {
		System.out.println(modLogDir);
		MinerCfg = new Configuration(new File(modLogDir + File.separatorChar + "Miner.cfg"));
		WoodcutterCfg = new Configuration(new File(modLogDir + File.separatorChar + "Woodcutter.cfg"));
		HunterCfg = new Configuration(new File(modLogDir + File.separatorChar + "Hunter.cfg"));
		FarmerCfg = new Configuration(new File(modLogDir + File.separatorChar + "Farmer.cfg"));
		ForgerCfg = new Configuration(new File(modLogDir + File.separatorChar + "Forger.cfg"));
		AlchemistCfg = new Configuration(new File(modLogDir + File.separatorChar + "Alchemist.cfg"));
		DevelopperCfg = new Configuration(new File(modLogDir + File.separatorChar + "Developper.cfg"));
		
		MinerParam.stone = MinerCfg.get("XPAmount", "stone", 1).getInt();
		MinerParam.coal = MinerCfg.get("XPAmount", "coal", 1).getInt();
		MinerParam.iron = MinerCfg.get("XPAmount", "iron", 1).getInt();
		MinerParam.gold = MinerCfg.get("XPAmount", "gold", 1).getInt();
		MinerParam.diamond = MinerCfg.get("XPAmount", "diamond", 1).getInt();
		MinerParam.emerald = MinerCfg.get("XPAmount", "emerald", 1).getInt();
		MinerParam.quartz = MinerCfg.get("XPAmount", "quartz", 1).getInt();
		
		HunterParam.creeper = HunterCfg.get("XPAmount", "creeper", 1).getInt();
		HunterParam.zombie = HunterCfg.get("XPAmount", "zombie", 1).getInt();
		HunterParam.skeleton = HunterCfg.get("XPAmount", "skeleton", 1).getInt();
		HunterParam.blaze = HunterCfg.get("XPAmount", "blaze", 1).getInt();
		HunterParam.ghast = HunterCfg.get("XPAmount", "ghast", 1).getInt();
		HunterParam.zombievillager = HunterCfg.get("XPAmount", "zombievillager", 1).getInt();
		HunterParam.wither = HunterCfg.get("XPAmount", "wither", 1).getInt();
		HunterParam.enderdragon = HunterCfg.get("XPAmount", "enderdragon", 1).getInt();
		HunterParam.enderman = HunterCfg.get("XPAmount", "enderman", 1).getInt();
		HunterParam.shulker = HunterCfg.get("XPAmount", "shulker", 1).getInt();
		HunterParam.witch = HunterCfg.get("XPAmount", "witch", 1).getInt();
		HunterParam.animals = HunterCfg.get("XPAmount", "animals", 1).getInt();
		HunterParam.pigzombie = HunterCfg.get("XPAmount", "pigzombie", 1).getInt();
		HunterParam.spider = HunterCfg.get("XPAmount", "spider", 1).getInt();
		HunterParam.cavespider = HunterCfg.get("XPAmount", "cavespider", 1).getInt();
		
		WoodcutterParam.oak = WoodcutterCfg.get("XPAmount", "oak", 1).getInt();
		WoodcutterParam.spruce = WoodcutterCfg.get("XPAmount", "spruce", 1).getInt();
		WoodcutterParam.birch = WoodcutterCfg.get("XPAmount", "birch", 1).getInt();
		WoodcutterParam.jungle = WoodcutterCfg.get("XPAmount", "jungle", 1).getInt();
		WoodcutterParam.acacia = WoodcutterCfg.get("XPAmount", "acacia", 1).getInt();
		WoodcutterParam.darkoak = WoodcutterCfg.get("XPAmount", "darkoak", 1).getInt();
		
		ForgerParam.diamondsword = ForgerCfg.get("XPAmountDiamond", "sword", 1).getInt();
		ForgerParam.diamondhelmet = ForgerCfg.get("XPAmountDiamond", "helmet", 1).getInt();
		ForgerParam.diamondchestplate = ForgerCfg.get("XPAmountDiamond", "chestplate", 1).getInt();
		ForgerParam.diamondleggings = ForgerCfg.get("XPAmountDiamond", "leggins", 1).getInt();
		ForgerParam.diamondboots = ForgerCfg.get("XPAmountDiamond", "boots", 1).getInt();
		
		ForgerParam.goldsword = ForgerCfg.get("XPAmountGold", "sword", 1).getInt();
		ForgerParam.goldhelmet = ForgerCfg.get("XPAmountGold", "helmet", 1).getInt();
		ForgerParam.goldchestplate = ForgerCfg.get("XPAmountGold", "chestplate", 1).getInt();
		ForgerParam.goldleggings = ForgerCfg.get("XPAmountGold", "leggins", 1).getInt();
		ForgerParam.goldboots = ForgerCfg.get("XPAmountGold", "boots", 1).getInt();
		
		ForgerParam.ironsword = ForgerCfg.get("XPAmountIron", "sword", 1).getInt();
		ForgerParam.ironhelmet = ForgerCfg.get("XPAmountIron", "helmet", 1).getInt();
		ForgerParam.ironchestplate = ForgerCfg.get("XPAmountIron", "chestplate", 1).getInt();
		ForgerParam.ironleggings = ForgerCfg.get("XPAmountIron", "leggins", 1).getInt();
		ForgerParam.ironboots = ForgerCfg.get("XPAmountIron", "boots", 1).getInt();
		
	
		ForgerParam.leatherhelmet = ForgerCfg.get("XPAmountLeather", "helmet", 1).getInt();
		ForgerParam.leatherchestplate = ForgerCfg.get("XPAmountLeather", "chestplate", 1).getInt();
		ForgerParam.leatherleggings = ForgerCfg.get("XPAmountLeather", "leggins", 1).getInt();
		ForgerParam.leatherboots = ForgerCfg.get("XPAmountLeather", "boots", 1).getInt();
		
		ForgerParam.stonesword = ForgerCfg.get("XPAmountStone", "sword", 1).getInt();
		ForgerParam.woodsword = ForgerCfg.get("XPAmountWood", "sword", 1).getInt();

		FarmerParam.wheat = FarmerCfg.get("XPAmount", "wheat", 1).getInt();
		FarmerParam.beetroots = FarmerCfg.get("XPAmount", "beetroots", 1).getInt();
		FarmerParam.carrots = FarmerCfg.get("XPAmount", "carrots", 1).getInt();
		FarmerParam.potatoes = FarmerCfg.get("XPAmount", "potatoes", 1).getInt();
		FarmerParam.melon = FarmerCfg.get("XPAmount", "melon", 1).getInt();
		FarmerParam.pumpkin = FarmerCfg.get("XPAmount", "pumpkin", 1).getInt();
		
		MinerCfg.save();
		HunterCfg.save();
		WoodcutterCfg.save();
		FarmerCfg.save();
		ForgerCfg.save();
		AlchemistCfg.save();
		DevelopperCfg.save();
	}
	
	public static void JobToConfig(Configuration config, Job job) {
		if (job != null) {
			config.getCategory("Job").get("xp").set(job.xp());
			config.getCategory("Job").get("lvl").set(job.lvl());
			config.getCategory("Job").get("points").set(job.points());
			config.getCategory("Job").get("lvlability1").set(job.Ability1());
			config.getCategory("Job").get("lvlability2").set(job.Ability2());
			config.getCategory("Job").get("type").set(job.getName());
		}
	}
	
	public static Job JobFromName(String name, int lvl, int xp, int points, int ability1, int ability2) {
		if (name.equals(Miner.name())) {
			Miner j = new Miner();
			j.xp = xp;
			j.lvl = lvl;
			j.Ability1 = ability1;
			j.Ability2 = ability2;
			j.points = points;
			return j;
		}
		if (name.equals(Woodcutter.name())) {
			Woodcutter j = new Woodcutter();
			j.xp = xp;
			j.lvl = lvl;
			j.Ability1 = ability1;
			j.Ability2 = ability2;
			j.points = points;
			return j;
		}
		if (name.equals(Hunter.name())) {
			Hunter j = new Hunter();
			j.xp = xp;
			j.lvl = lvl;
			j.Ability1 = ability1;
			j.Ability2 = ability2;
			j.points = points;
			return j;
		}
		if (name.equals(Developper.name())) {
			Developper j = new Developper();
			j.xp = xp;
			j.lvl = lvl;
			j.Ability1 = ability1;
			j.Ability2 = ability2;
			j.points = points;
			return j;
		}
		if (name.equals(Forger.name())) {
			Forger j = new Forger();
			j.xp = xp;
			j.lvl = lvl;
			j.Ability1 = ability1;
			j.Ability2 = ability2;
			j.points = points;
			return j;
		}
		if (name.equals(Farmer.name())) {
			Farmer j = new Farmer();
			j.xp = xp;
			j.lvl = lvl;
			j.Ability1 = ability1;
			j.Ability2 = ability2;
			j.points = points;
			return j;
		}
		if (name.equals(Alchemist.name())) {
			Alchemist j = new Alchemist();
			j.xp = xp;
			j.lvl = lvl;
			j.Ability1 = ability1;
			j.Ability2 = ability2;
			j.points = points;
			return j;
		}
		return null;
	}
	
	public static Job JobFromConfig(Configuration config) {
		String name = config.get("Job","type","").getString();
		int lvl = config.get("Job","lvl",0).getInt();
		int xp = config.get("Job","xp",0).getInt();
		int points = config.get("Job","points",0).getInt();
		int ability1 = config.get("Job","lvlability1",0).getInt();
		int ability2 = config.get("Job","lvlability2",0).getInt();
		return JobFromName(name, lvl, xp, points, ability1, ability2);
	}
	
	public static Job fromBytes(ByteBuf buf) {
		byte id = buf.readByte();
		for (Class cl : jobs.keySet()) {
			byte bid = jobs.get(cl);
			if (bid == id) {
				try {
					Job j = (Job)cl.newInstance();
					j.fromBytes(buf);
					return j;
				} catch (InstantiationException e) {
					
				} catch (  IllegalAccessException e) {
					
				}
				
				break;
			}
		}

		return null;
	}
	
	public static void toBytes(ByteBuf buf, Job job) {
		if (job != null) {
			Byte id = jobs.get(job.getClass());
			if (id != null) {
				buf.writeByte(id);	
				job.toBytes(buf);
			} else {
				buf.writeByte(0);
			}
		} else {
			buf.writeByte(0);
		}
	}
	
}
