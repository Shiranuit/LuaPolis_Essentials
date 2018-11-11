package fr.shiranuit.luapolisessentials.Network.Packets;

import java.nio.charset.StandardCharsets;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class ItemBlockDisablePacket implements IMessage{

	public String id;
	public int meta;
	public boolean disabled = false;
	public ItemBlockDisablePacket() {}
	public ItemBlockDisablePacket(String id, int meta, boolean disabled) {
		this.id = id;
		this.disabled = disabled;
		this.meta = meta;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		
		this.id = ByteBufUtils.readUTF8String(buf);
		this.meta=buf.readInt();
		this.disabled = buf.readBoolean();
		
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, this.id);
		buf.writeInt(this.meta);
		buf.writeBoolean(this.disabled);
	}

}
