package fr.shiranuit.luapolisessentials.Network.Server;

import fr.shiranuit.luapolisessentials.Network.Packets.BroadcastPacket;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class BroadcastPacketHandler implements IMessageHandler<BroadcastPacket, IMessage>{

	@Override
	public IMessage onMessage(BroadcastPacket message, MessageContext ctx) {
		// TODO Auto-generated method stub
		return null;
	}

}
