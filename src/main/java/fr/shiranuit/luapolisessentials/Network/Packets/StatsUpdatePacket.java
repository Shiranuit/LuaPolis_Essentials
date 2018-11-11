package fr.shiranuit.luapolisessentials.Network.Packets;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class StatsUpdatePacket implements IMessage {

	public String name = "";
	
	public StatsUpdatePacket() {}
	public StatsUpdatePacket(String name) {
		name = this.name;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.name = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, this.name);
	}

}
