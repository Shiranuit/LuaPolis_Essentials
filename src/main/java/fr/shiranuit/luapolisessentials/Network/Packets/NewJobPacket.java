package fr.shiranuit.luapolisessentials.Network.Packets;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class NewJobPacket implements IMessage{

	public String jobname = "";
	public String playername = "";
	
	public NewJobPacket() {}
	public NewJobPacket(String playername, String jobname) {
		this.jobname=jobname;
		this.playername=playername;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		// TODO Auto-generated method stub
		this.playername = ByteBufUtils.readUTF8String(buf);
		this.jobname = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		// TODO Auto-generated method stub
		ByteBufUtils.writeUTF8String(buf, this.playername);
		ByteBufUtils.writeUTF8String(buf, this.jobname);
	}

}
