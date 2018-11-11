package fr.shiranuit.luapolisessentials.Proxy.Server;

import fr.shiranuit.luapolisessentials.Main;
import fr.shiranuit.luapolisessentials.Stats.PlayerStats;
import fr.shiranuit.luapolisessentials.Network.Server.MessagePacketHandler;
import fr.shiranuit.luapolisessentials.Network.Server.NewJobPacketHandler;
import fr.shiranuit.luapolisessentials.Network.Server.UpdateAbilityPacketHandler;
import fr.shiranuit.luapolisessentials.Network.Server.StatsUpdatePacketHandler;
import fr.shiranuit.luapolisessentials.Network.Packets.BroadcastPacket;
import fr.shiranuit.luapolisessentials.Network.Packets.ItemBlockDisablePacket;
import fr.shiranuit.luapolisessentials.Network.Packets.MessagePacket;
import fr.shiranuit.luapolisessentials.Network.Packets.NewJobPacket;
import fr.shiranuit.luapolisessentials.Network.Packets.PlayerStatsPacket;
import fr.shiranuit.luapolisessentials.Network.Packets.StatsUpdatePacket;
import fr.shiranuit.luapolisessentials.Network.Packets.UpdateAbilityPacket;
import fr.shiranuit.luapolisessentials.Network.Server.BroadcastPacketHandler;
import fr.shiranuit.luapolisessentials.Network.Server.ItemBlockDisablePacketHandler;
import fr.shiranuit.luapolisessentials.Network.Server.StatsPacketHandler;
import net.minecraft.block.Block;
import net.minecraftforge.fml.relauncher.Side;

public class ServerProxy {
	
		
		public void registerRenders(){}
		
		public void registerBlockTexture(final Block block, final String blockName) {}


	    public void registerBlockTexture(final Block block, final String blockName, int meta) {}
	
	    public void registerPackets() {
	    	int i = 0;
	    	Main.network.registerMessage(StatsPacketHandler.class, PlayerStatsPacket.class, i++, Side.CLIENT);
	    	Main.network.registerMessage(StatsPacketHandler.class, PlayerStatsPacket.class, i++, Side.SERVER);
	    	
	    	Main.network.registerMessage(ItemBlockDisablePacketHandler.class, ItemBlockDisablePacket.class, i++, Side.CLIENT);
	    	Main.network.registerMessage(ItemBlockDisablePacketHandler.class, ItemBlockDisablePacket.class, i++, Side.SERVER);
	    	

	    	Main.instance.network.registerMessage(MessagePacketHandler.class, MessagePacket.class, i++, Side.CLIENT);
	    	Main.instance.network.registerMessage(MessagePacketHandler.class, MessagePacket.class, i++, Side.SERVER);

	     	Main.instance.network.registerMessage(BroadcastPacketHandler.class, BroadcastPacket.class, i++, Side.CLIENT);
	    	Main.instance.network.registerMessage(BroadcastPacketHandler.class, BroadcastPacket.class, i++, Side.SERVER);
	    	
	    	Main.instance.network.registerMessage(NewJobPacketHandler.class, NewJobPacket.class, i++, Side.CLIENT);
	    	Main.instance.network.registerMessage(NewJobPacketHandler.class, NewJobPacket.class, i++, Side.SERVER);
	    	
	    	Main.instance.network.registerMessage(UpdateAbilityPacketHandler.class, UpdateAbilityPacket.class, i++, Side.CLIENT);
	    	Main.instance.network.registerMessage(UpdateAbilityPacketHandler.class, UpdateAbilityPacket.class, i++, Side.SERVER);
	    	
	    	Main.instance.network.registerMessage(StatsUpdatePacketHandler.class, StatsUpdatePacket.class, i++, Side.CLIENT);
	    	Main.instance.network.registerMessage(StatsUpdatePacketHandler.class, StatsUpdatePacket.class, i++, Side.SERVER);
	    }
}
