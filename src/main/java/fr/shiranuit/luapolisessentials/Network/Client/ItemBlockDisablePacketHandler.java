package fr.shiranuit.luapolisessentials.Network.Client;

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
		if (ctx.side == Side.CLIENT) {
			System.out.println("ID : "+message.id);
			if (message.disabled) {
				ItemBlockManager.disableRecipe(message.id, message.meta, false);
			} else {
				ItemBlockManager.enableRecipe(message.id, message.meta, false);
			}
		}
		return null;
	}

}
