package fr.shiranuit.luapolisessentials.Network.Client;

import fr.shiranuit.luapolisessentials.Main;
import fr.shiranuit.luapolisessentials.Jobs.Job;
import fr.shiranuit.luapolisessentials.Manager.JobManager;
import fr.shiranuit.luapolisessentials.Manager.PlayerManager;
import fr.shiranuit.luapolisessentials.Network.Packets.UpdateAbilityPacket;
import fr.shiranuit.luapolisessentials.Stats.PlayerStats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class UpdateAbilityPacketHandler implements IMessageHandler<UpdateAbilityPacket, IMessage>{

	@Override
	public IMessage onMessage(UpdateAbilityPacket message, MessageContext ctx) {
		if (Main.mcserver.isSinglePlayer()) {
			if (ctx.side == Side.SERVER) {
				EntityPlayer player = Main.mcserver.getPlayerList().getPlayerByUsername(message.playername);
				if (player != null) {
					PlayerStats stats = PlayerManager.get(player);
					if (stats != null && stats.job != null) {
						int points = stats.job.points();
						int ab1 = 0;
						int ab2 = 0;
						for (int i=0; i<message.ability1; i++) {
							if (points-1 >= 0 && stats.job.lvl() >= stats.job.AbilityMin1()) {
								points--;
								ab1++;
							}
						}
						
						for (int i=0; i<message.ability2; i++) {
							if (points-1 >= 0 && stats.job.lvl() >= stats.job.AbilityMin2()) {
								points--;
								ab2++;
							}
						}
						
						Job job = JobManager.JobFromName(stats.job.getName(), stats.job.lvl(), stats.job.xp(), points,ab1+stats.job.Ability1(),ab2+stats.job.Ability2());
						stats.job=job;
						PlayerManager.update(player, stats, true);
					}
				}
			}
			return null;
		}
		return null;
	}

}
