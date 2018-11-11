package fr.shiranuit.luapolisessentials.Commands;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import fr.shiranuit.luapolisessentials.Main;
import fr.shiranuit.luapolisessentials.Manager.ItemBlockManager;
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

public class GodMode  extends CommandBase {

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "god";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		// TODO Auto-generated method stub
		return "/god <player> <true/false>";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length < 2) {
			throw new WrongUsageException("/god <player> <true/false>", new Object[0]);
		} else {
			Boolean state = Boolean.valueOf(args[1]);
			EntityPlayer player = Main.mcserver.getPlayerList().getPlayerByUsername(args[0]);
			if (player != null && state != null) {
				PlayerStats stats = PlayerManager.get(player);
				stats.godmode = state;
				PlayerManager.update(player, stats, true);
				player.setEntityInvulnerable(state);
				Util.sendMessage(sender,TextFormatting.RED+player.getDisplayNameString() + " GodMode : " + state.toString());	
			}
		}
		
	}
	
	@Override
	public int getRequiredPermissionLevel() {
		return 4;
	}
	
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, ArrayConverter.convert(Main.mcserver.getOnlinePlayerNames())): Collections.<String>emptyList();
    }

}
