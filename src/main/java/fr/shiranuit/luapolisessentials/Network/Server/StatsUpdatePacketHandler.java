package fr.shiranuit.luapolisessentials.Network.Server;

import fr.shiranuit.luapolisessentials.Manager.PlayerManager;
import fr.shiranuit.luapolisessentials.Network.Packets.StatsUpdatePacket;
import fr.shiranuit.luapolisessentials.Utils.Util;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class StatsUpdatePacketHandler implements IMessageHandler<StatsUpdatePacket, IMessage>{

	@Override
	public IMessage onMessage(StatsUpdatePacket message, MessageContext ctx) {
		if (ctx.side == Side.SERVER) {
			PlayerManager.update(Util.playerByName(message.name));
		}
		return null;
	}

}
