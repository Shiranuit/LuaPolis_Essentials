package fr.shiranuit.luapolisessentials.Manager;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import fr.shiranuit.luapolisessentials.Main;
import fr.shiranuit.luapolisessentials.Network.Packets.PlayerStatsPacket;
import fr.shiranuit.luapolisessentials.Stats.PlayerStats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;;

public class PlayerManager {

	public static String modLogDir;
	public static Map<String, PlayerStats> PlayerStatsRegistry = new HashMap<String, PlayerStats>();
	public static void add(EntityPlayer player) {
		PlayerStatsRegistry.put(player.getGameProfile().getId().toString(), new PlayerStats(player));
	}
	public static PlayerStats get(EntityPlayer player) {
		return PlayerStatsRegistry.get(player.getGameProfile().getId().toString());
	}
	
	public static void update(EntityPlayer player, PlayerStats stats, boolean updateClient) {
		if (stats != null && player != null) {
			PlayerStatsRegistry.put(player.getGameProfile().getId().toString(), stats);
			if (updateClient) {
				player.setEntityInvulnerable(stats.godmode);
				Main.instance.network.sendTo(new PlayerStatsPacket(stats), (EntityPlayerMP)player);
			}
		}
	}
	
	public static void update(EntityPlayer player) {
		if (player != null) {
			PlayerStats stats = PlayerStatsRegistry.get(player.getGameProfile().getId().toString());
			if (stats != null) {
				player.setEntityInvulnerable(stats.godmode);
				Main.instance.network.sendTo(new PlayerStatsPacket(stats), (EntityPlayerMP)player);
			}
		}
	}
	
	public static void remove(EntityPlayer player) {
		PlayerStatsRegistry.remove(player.getGameProfile().getId().toString());
	}
	public static void save(EntityPlayer player) {
		PlayerStats pstats = PlayerStatsRegistry.get(player.getGameProfile().getId().toString());
		if (pstats != null) {
			pstats.save();
		}
	}
}
