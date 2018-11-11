package fr.shiranuit.luapolisessentials.Network.Packets;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class BroadcastPacket implements IMessage {

	public String message = "";
	public BroadcastPacket() {}
	public BroadcastPacket(String message) {
		this.message = message;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.message=ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, this.message);
	}

}
