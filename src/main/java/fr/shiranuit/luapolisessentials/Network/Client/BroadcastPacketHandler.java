package fr.shiranuit.luapolisessentials.Network.Client;

import fr.shiranuit.luapolisessentials.Client.Gui.ScreenInfos;
import fr.shiranuit.luapolisessentials.Network.Packets.BroadcastPacket;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class BroadcastPacketHandler implements IMessageHandler<BroadcastPacket, IMessage> {

	@Override
	public IMessage onMessage(BroadcastPacket message, MessageContext ctx) {
		if (ctx.side == Side.CLIENT) {
			ScreenInfos.newBroadcast(message);
		}
		return null;
	}

}
