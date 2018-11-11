package fr.shiranuit.luapolisessentials.Stats;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import fr.shiranuit.luapolisessentials.Main;
import fr.shiranuit.luapolisessentials.Jobs.Job;
import fr.shiranuit.luapolisessentials.Jobs.Miner;
import fr.shiranuit.luapolisessentials.Manager.ConfigManager;
import fr.shiranuit.luapolisessentials.Manager.JobManager;
import fr.shiranuit.luapolisessentials.Manager.PlayerManager;
import fr.shiranuit.luapolisessentials.Network.Packets.PlayerStatsPacket;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PlayerStats {
	public EntityPlayer player;
	public boolean godmode = false;
	public Job job;
	private Configuration config;
	public PlayerStats(EntityPlayer player) {
		this.player = player;
		if (FMLCommonHandler.instance().getSide().isServer()) {
			this.config = new Configuration(new File(PlayerManager.modLogDir + File.separatorChar + player.getGameProfile().getId().toString() + ".stats"));
			this.load();
		}
	}

	public void load() {
		if (FMLCommonHandler.instance().getSide().isServer()) {
			this.config.load();
			this.godmode = this.config.get("Stats","godmode",false).getBoolean();
			this.job = JobManager.JobFromConfig(this.config);
		}
	}
	
	public void save() {
		if (FMLCommonHandler.instance().getSide().isServer()) {
			this.config.getCategory("Stats").get("godmode").set(this.godmode);
			JobManager.JobToConfig(config, this.job);
			this.config.save();
		}
	}
	
	public void sync() {
		if (FMLCommonHandler.instance().getSide().isServer()) {
			this.config = new Configuration(new File(PlayerManager.modLogDir + File.separatorChar + player.getGameProfile().getId().toString() + ".stats"));
			this.job = JobManager.JobFromConfig(this.config);
			this.godmode = this.config.get("Stats","godmode",false).getBoolean();
		}
	}
	
	public PlayerStats(PlayerStatsPacket packet) {
		this.godmode = packet.godmode;
		this.job = packet.job;
	}
}
