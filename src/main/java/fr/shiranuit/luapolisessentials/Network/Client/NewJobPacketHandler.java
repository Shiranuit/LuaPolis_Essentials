package fr.shiranuit.luapolisessentials.Network.Client;

import fr.shiranuit.luapolisessentials.Main;
import fr.shiranuit.luapolisessentials.Manager.JobManager;
import fr.shiranuit.luapolisessentials.Manager.PlayerManager;
import fr.shiranuit.luapolisessentials.Network.Packets.NewJobPacket;
import fr.shiranuit.luapolisessentials.Stats.PlayerStats;
import fr.shiranuit.luapolisessentials.Utils.Util;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class NewJobPacketHandler implements IMessageHandler<NewJobPacket, IMessage> {

	@Override
	public IMessage onMessage(NewJobPacket message, MessageContext ctx) {
		if (Main.mcserver.isSinglePlayer()) {
			if (ctx.side == Side.SERVER) {
				 EntityPlayer player = Main.mcserver.getPlayerList().getPlayerByUsername(message.playername);
				 if (player != null) {
					 PlayerStats stats = PlayerManager.get(player);
					 if (stats != null) {
						 stats.job = JobManager.JobFromName(message.jobname, (byte)0, 0, (byte)0, (byte)0, (byte)0);
						 PlayerManager.update(player, stats, true);
					 }
						if (Util.isOp(player)) {
							return null;
						}
						for (int i=0; i<player.inventory.mainInventory.size(); i++) {
							ItemStack item = player.inventory.mainInventory.get(i);
							if (item.getItem().getRegistryName().getResourceDomain().equals("psi")) {
								player.inventory.mainInventory.set(i, ItemStack.EMPTY);
								EntityItem it = new EntityItem(player.world, player.posX, player.posY, player.posZ, item);
								it.setDefaultPickupDelay();
								player.world.spawnEntity(it);
							}
						}
				 }
			}
		}
		return null;
	}
	

}
