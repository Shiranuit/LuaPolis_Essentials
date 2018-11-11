package fr.shiranuit.luapolisessentials.Network.Client;

import fr.shiranuit.luapolisessentials.Manager.PlayerManager;
import fr.shiranuit.luapolisessentials.Network.Packets.PlayerStatsPacket;
import fr.shiranuit.luapolisessentials.Stats.PlayerStats;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PlayerStatsPacketHandler implements IMessageHandler<PlayerStatsPacket, IMessage> {

	
	@Override
	public IMessage onMessage(PlayerStatsPacket message, MessageContext ctx) {
		if (ctx.side == Side.CLIENT) {
			EntityPlayer player = Minecraft.getMinecraft().player;
			PlayerManager.update(player, new PlayerStats(message), false);
		}
		return null;
	}

}
