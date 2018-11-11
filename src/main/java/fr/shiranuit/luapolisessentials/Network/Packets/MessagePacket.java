package fr.shiranuit.luapolisessentials.Network.Packets;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MessagePacket implements IMessage {

	public String message = "";
	public String source = "";
	public MessagePacket() {}
	public MessagePacket(String message, String source) {
		this.message = message;
		this.source = source;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.source=ByteBufUtils.readUTF8String(buf);
		this.message=ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, this.source);
		ByteBufUtils.writeUTF8String(buf, this.message);
	}

}
