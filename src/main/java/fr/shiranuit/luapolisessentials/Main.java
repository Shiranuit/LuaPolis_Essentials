package fr.shiranuit.luapolisessentials;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.http.util.ByteArrayBuffer;
import org.apache.logging.log4j.core.jmx.Server;
import org.lwjgl.Sys;

import akka.routing.Broadcast;
import fr.shiranuit.luapolisessentials.Commands.BroadcastCommand;
import fr.shiranuit.luapolisessentials.Commands.ChunkClearCommand;
import fr.shiranuit.luapolisessentials.Commands.CommandMessage;
import fr.shiranuit.luapolisessentials.Commands.CommandSpy;
import fr.shiranuit.luapolisessentials.Commands.DisableItemBlock;
import fr.shiranuit.luapolisessentials.Commands.EnableItemBlock;
import fr.shiranuit.luapolisessentials.Commands.GodMode;
import fr.shiranuit.luapolisessentials.Commands.JobCommand;
import fr.shiranuit.luapolisessentials.Commands.LFJCommand;
import fr.shiranuit.luapolisessentials.Commands.LFJSearchCommand;
import fr.shiranuit.luapolisessentials.Commands.RegionCommand;
import fr.shiranuit.luapolisessentials.Commands.UpdateConfig;
import fr.shiranuit.luapolisessentials.Events.Events;
import fr.shiranuit.luapolisessentials.Jobs.Alchemist;
import fr.shiranuit.luapolisessentials.Jobs.Developper;
import fr.shiranuit.luapolisessentials.Jobs.Farmer;
import fr.shiranuit.luapolisessentials.Jobs.Forger;
import fr.shiranuit.luapolisessentials.Jobs.Hunter;
import fr.shiranuit.luapolisessentials.Jobs.Miner;
import fr.shiranuit.luapolisessentials.Jobs.Woodcutter;
import fr.shiranuit.luapolisessentials.Manager.AreaManager;
import fr.shiranuit.luapolisessentials.Manager.ConfigManager;
import fr.shiranuit.luapolisessentials.Manager.InventoryManager;
import fr.shiranuit.luapolisessentials.Manager.ItemBlockManager;
import fr.shiranuit.luapolisessentials.Manager.JobManager;
import fr.shiranuit.luapolisessentials.Manager.LogForJusticeManager;
import fr.shiranuit.luapolisessentials.Manager.PlayerManager;
import fr.shiranuit.luapolisessentials.Manager.WorldManager;
import fr.shiranuit.luapolisessentials.Stats.PlayerStats;
import fr.shiranuit.luapolisessentials.Client.Gui.ScreenInfos;
import fr.shiranuit.luapolisessentials.Client.KeyHandler.KeyHandler;
import fr.shiranuit.luapolisessentials.Network.Client.PlayerStatsPacketHandler;
import fr.shiranuit.luapolisessentials.Proxy.Server.ServerProxy;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DimensionType;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameData;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = Reference.ModID, name = Reference.ModMame, version = Reference.ModVersion, acceptableRemoteVersions = "*")
public class Main {
	@SidedProxy(clientSide = Reference.ClientProxy, serverSide = Reference.ServerProxy)
	public static ServerProxy proxy;
	@Mod.Instance(value = Reference.ModID)
	public static Main instance;
	public static String modDir = "";
	public static ArrayList<String> DisabledItemsBlocks;
	public static String[] requiredAfter;
	public static MinecraftServer mcserver;
	public static final SimpleNetworkWrapper network = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.ModID);
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		modDir = event.getModConfigurationDirectory().getPath() + File.separatorChar + "LuaPolis";
		ConfigManager.modLogDir = modDir;
		ConfigManager.sync();
		
		MinecraftForge.EVENT_BUS.register(new Events());
		if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
			MinecraftForge.EVENT_BUS.register(new KeyHandler());
		}
		
		JobManager.modLogDir = modDir + File.separatorChar  + "Jobs";
		JobManager.register(Miner.class, 1, Miner.name(), true);
		JobManager.register(Woodcutter.class, 2, Woodcutter.name(), true);
		JobManager.register(Hunter.class, 3, Hunter.name(), true);
		JobManager.register(Developper.class, 4, Developper.name(), false);
		JobManager.register(Forger.class, 5, Forger.name(), true);
		JobManager.register(Farmer.class, 6, Farmer.name(), true);
		JobManager.register(Alchemist.class, 7, Alchemist.name(), false);
		JobManager.loadCfg();
		
		proxy.registerRenders();
		proxy.registerPackets();
		
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		
		if (requiredAfter.length > 0) {
			boolean wait = true;
			while (true) {
				for (String modname : requiredAfter) {
					if (!Loader.isModLoaded(modname)) {
						wait = false;
						break;
					}
				}
				if (wait) {
					break;
				}
			}
		}
		
		if (event.getSide() == Side.SERVER) {
		
			for (String name : DisabledItemsBlocks) {
				String data[] = name.split("[/]");
				if (data.length == 2) {
					try {
						String id = data[0];
						int meta = Integer.valueOf(data[1]);
						if (data[1].equals("*")) {
							meta = -1;
						}
						ItemBlockManager.disableRecipe(id, meta, false);
					} catch (Exception e) {
						String id = data[0];
						int meta = 0;
						if (data[1].equals("*")) {
							meta = -1;
						}
						ItemBlockManager.disableRecipe(id, meta, false);
					}
				} else {
					ItemBlockManager.disableRecipe(name, 0, false);
				}
				
			}
		}
		
		
    	List<ModContainer> mods = Loader.instance().getActiveModList();
    	try {
			FileWriter modsWriter = new FileWriter(modDir + File.separatorChar + "ModsList.txt");
			for (ModContainer mod : mods) {
				modsWriter.append(mod.getModId()+"\n");
			}
			modsWriter.close();
		} catch (IOException e) {}
	
	}
	
	public static InventoryManager overall = new InventoryManager("overall");
	public static InventoryManager lpe = new InventoryManager("lpe");
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		
	}
	

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event)
    {
    	File f = new File(DimensionManager.getCurrentSaveRootDirectory().getPath() + File.separatorChar + "LuaPolis");
    	if (!(f.exists() && f.isDirectory())) {
    		f.mkdir();
    	}
    	InventoryManager.modLogDir = DimensionManager.getCurrentSaveRootDirectory().getPath() + File.separatorChar + "LuaPolis" + File.separatorChar  + "InventoryData";
    	overall.loadPlayers();
    	lpe.loadPlayers();
    	
    	WorldManager.modLogDir = DimensionManager.getCurrentSaveRootDirectory().getPath() + File.separatorChar + "LuaPolis" + File.separatorChar  + "WorldData";
    	WorldManager.loadArtificial();
    	
    	LogForJusticeManager.modLogDir = DimensionManager.getCurrentSaveRootDirectory().getPath() + File.separatorChar + "LuaPolis" + File.separatorChar  + "LogForJustice";
    	LogForJusticeManager.oldDir = DimensionManager.getCurrentSaveRootDirectory().getPath() + File.separatorChar + "LuaPolis" + File.separatorChar  + "SWAP-LogForJustice";
    	LogForJusticeManager.setup();
    	LogForJusticeManager.loadLFJ();
    	
		
		AreaManager.modLogDir = DimensionManager.getCurrentSaveRootDirectory().getPath() + File.separatorChar + "LuaPolis" + File.separatorChar  + "Areas";
		AreaManager.reloadProtection();
		
		PlayerManager.modLogDir = DimensionManager.getCurrentSaveRootDirectory().getPath() + File.separatorChar + "LuaPolis" + File.separatorChar  + "PlayersStats";
		
    	mcserver = event.getServer();
    	
    	event.registerServerCommand(new DisableItemBlock());
    	event.registerServerCommand(new EnableItemBlock());
    	event.registerServerCommand(new UpdateConfig());
    	event.registerServerCommand(new GodMode());
    	event.registerServerCommand(new CommandMessage());
    	event.registerServerCommand(new BroadcastCommand());
    	event.registerServerCommand(new RegionCommand());
    	event.registerServerCommand(new JobCommand());
    	event.registerServerCommand(new CommandSpy());
    	event.registerServerCommand(new LFJCommand());
    	event.registerServerCommand(new LFJSearchCommand());
    	event.registerServerCommand(new ChunkClearCommand());
    }
    
    @EventHandler
    public void serverStopping(FMLServerStoppingEvent event) {
    	WorldManager.saveArtificial();
    	//LogForJusticeManager.saveLFJ();
    	for (String key : PlayerManager.PlayerStatsRegistry.keySet()) {
    		PlayerStats stats = PlayerManager.PlayerStatsRegistry.get(key);
    		stats.save();
    	}
    	
    	overall.savePlayers();
    	lpe.savePlayers();
    	
    }
}
