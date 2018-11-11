package fr.shiranuit.luapolisessentials.Commands;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import fr.shiranuit.luapolisessentials.Main;
import fr.shiranuit.luapolisessentials.Jobs.Job;
import fr.shiranuit.luapolisessentials.Manager.ItemBlockManager;
import fr.shiranuit.luapolisessentials.Manager.JobManager;
import fr.shiranuit.luapolisessentials.Manager.PlayerManager;
import fr.shiranuit.luapolisessentials.Stats.PlayerStats;
import fr.shiranuit.luapolisessentials.Utils.ArrayConverter;
import fr.shiranuit.luapolisessentials.Utils.Util;
import net.minecraft.block.Block;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class JobCommand  extends CommandBase {

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "job";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		// TODO Auto-generated method stub
		return "/job <player> <job> <level> <ability1Level> <ability2Level>";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length < 5) {
			throw new WrongUsageException("/job <player> <job> <level> <ability1Level> <ability2Level>", new Object[0]);
		} else {
			EntityPlayer player = Main.mcserver.getPlayerList().getPlayerByUsername(args[0]);
			if (player != null && JobManager.jobsname.containsKey(args[1].toString())) {
				PlayerStats stats = PlayerManager.get(player);
				Job j = JobManager.JobFromName(args[1].toString(), Integer.parseInt(args[2]), 0, (byte)0, Integer.parseInt(args[3]), Integer.parseInt(args[4]));
				stats.job = j;
				PlayerManager.update(player, stats, true);
				Util.sendMessage(sender,player.getDisplayNameString() + " Job : " + args[1].toString());
			}
		}
		
	}
	
	@Override
	public int getRequiredPermissionLevel() {
		return 4;
	}
	
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
    	switch (args.length) {
	    	case 1: {
	    		return getListOfStringsMatchingLastWord(args, ArrayConverter.convert(Main.mcserver.getOnlinePlayerNames()));
	    	}
	    	case 2: {
	    		return getListOfStringsMatchingLastWord(args, JobManager.jobsname.keySet());
	    	}
	    	default: {
	    		return Collections.<String>emptyList();
	    	}
    	}
    }

}
