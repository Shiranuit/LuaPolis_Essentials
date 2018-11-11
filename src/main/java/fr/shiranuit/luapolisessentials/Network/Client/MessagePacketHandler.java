package fr.shiranuit.luapolisessentials.Network.Client;

import fr.shiranuit.luapolisessentials.Client.Gui.ScreenInfos;
import fr.shiranuit.luapolisessentials.Network.Packets.MessagePacket;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessagePacketHandler implements IMessageHandler<MessagePacket, IMessage> {

	@Override
	public IMessage onMessage(MessagePacket message, MessageContext ctx) {
		if (ctx.side == Side.CLIENT) {
			ScreenInfos.newMessage(message);
		}
		return null;
	}

}
