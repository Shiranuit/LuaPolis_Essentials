package fr.shiranuit.luapolisessentials.Network.Packets;

import fr.shiranuit.luapolisessentials.Jobs.Job;
import fr.shiranuit.luapolisessentials.Manager.JobManager;
import fr.shiranuit.luapolisessentials.Stats.PlayerStats;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PlayerStatsPacket implements IMessage {

	public boolean godmode = false;
	public Job job;
	private Configuration config;
	public PlayerStatsPacket() {}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.godmode = buf.readBoolean();
		this.job = JobManager.fromBytes(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBoolean(this.godmode);
		JobManager.toBytes(buf, this.job);
	}

	public PlayerStatsPacket(PlayerStats stats) {
		this.godmode = stats.godmode;
		this.job = stats.job;
	}
}
