package fr.shiranuit.luapolisessentials.Network.Server;

import fr.shiranuit.luapolisessentials.Network.Packets.PlayerStatsPacket;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class StatsPacketHandler implements IMessageHandler<PlayerStatsPacket, IMessage> {

	
	@Override
	public IMessage onMessage(PlayerStatsPacket message, MessageContext ctx) {
		// TODO Auto-generated method stub

		return null;
	}

}
