package fr.shiranuit.luapolisessentials.Network.Server;

import fr.shiranuit.luapolisessentials.Network.Packets.MessagePacket;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessagePacketHandler implements IMessageHandler<MessagePacket, IMessage> {

	@Override
	public IMessage onMessage(MessagePacket message, MessageContext ctx) {
		// TODO Auto-generated method stub
		return null;
	}

}
