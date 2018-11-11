package fr.shiranuit.luapolisessentials.Network.Packets;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class UpdateAbilityPacket implements IMessage {

	public int ability1 = 0;
	public int ability2 = 0;
	public String playername = "";
	
	public UpdateAbilityPacket() {}
	public UpdateAbilityPacket(String playername, int ability1, int ability2) {
		this.playername = playername;
		this.ability1 = ability1;
		this.ability2 = ability2;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.playername = ByteBufUtils.readUTF8String(buf);
		this.ability1 = buf.readInt();
		this.ability2 = buf.readInt();
		
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, this.playername);
		buf.writeInt(this.ability1);
		buf.writeInt(this.ability2);	
	}

}
