package fr.shiranuit.luapolisessentials.Network.Server;

import fr.shiranuit.luapolisessentials.Manager.ItemBlockManager;
import fr.shiranuit.luapolisessentials.Network.Packets.ItemBlockDisablePacket;
import fr.shiranuit.luapolisessentials.Network.Packets.PlayerStatsPacket;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;


public class ItemBlockDisablePacketHandler implements IMessageHandler<ItemBlockDisablePacket, IMessage> {

	@Override
	public IMessage onMessage(ItemBlockDisablePacket message, MessageContext ctx) {
		return null;
	}

}
