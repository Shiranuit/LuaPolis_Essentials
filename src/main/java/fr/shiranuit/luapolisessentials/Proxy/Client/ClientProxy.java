package fr.shiranuit.luapolisessentials.Proxy.Client;

import fr.shiranuit.luapolisessentials.Main;
import fr.shiranuit.luapolisessentials.Stats.PlayerStats;
import fr.shiranuit.luapolisessentials.Client.Gui.ScreenInfos;
import fr.shiranuit.luapolisessentials.Network.Client.BroadcastPacketHandler;
import fr.shiranuit.luapolisessentials.Network.Client.ItemBlockDisablePacketHandler;
import fr.shiranuit.luapolisessentials.Network.Client.MessagePacketHandler;
import fr.shiranuit.luapolisessentials.Network.Client.NewJobPacketHandler;
import fr.shiranuit.luapolisessentials.Network.Client.PlayerStatsPacketHandler;
import fr.shiranuit.luapolisessentials.Network.Client.StatsUpdatePacketHandler;
import fr.shiranuit.luapolisessentials.Network.Client.UpdateAbilityPacketHandler;
import fr.shiranuit.luapolisessentials.Network.Packets.BroadcastPacket;
import fr.shiranuit.luapolisessentials.Network.Packets.ItemBlockDisablePacket;
import fr.shiranuit.luapolisessentials.Network.Packets.MessagePacket;
import fr.shiranuit.luapolisessentials.Network.Packets.NewJobPacket;
import fr.shiranuit.luapolisessentials.Network.Packets.PlayerStatsPacket;
import fr.shiranuit.luapolisessentials.Network.Packets.StatsUpdatePacket;
import fr.shiranuit.luapolisessentials.Network.Packets.UpdateAbilityPacket;
import fr.shiranuit.luapolisessentials.Proxy.Server.ServerProxy;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.EntityLiving;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry.EntityRegistration;
import net.minecraftforge.fml.relauncher.Side;

public class ClientProxy extends ServerProxy {
	@Override
	public void registerRenders(){
		 MinecraftForge.EVENT_BUS.register(new ScreenInfos(FMLClientHandler.instance().getClient()));
	}
	
    @Override
    public void registerBlockTexture(final Block block, final String blockName) {
 
    }
    
    @Override
    public void registerBlockTexture(final Block block, final String blockName, int meta) {
        
    }
    
    @Override
    public void registerPackets() {
    	int i = 0;
    	Main.instance.network.registerMessage(PlayerStatsPacketHandler.class, PlayerStatsPacket.class, i++, Side.CLIENT);
    	Main.instance.network.registerMessage(PlayerStatsPacketHandler.class, PlayerStatsPacket.class, i++, Side.SERVER);
    	
    	Main.instance.network.registerMessage(ItemBlockDisablePacketHandler.class, ItemBlockDisablePacket.class, i++, Side.CLIENT);
    	Main.instance.network.registerMessage(ItemBlockDisablePacketHandler.class, ItemBlockDisablePacket.class, i++, Side.SERVER);

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
