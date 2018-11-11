package fr.shiranuit.luapolisessentials.Commands;

import fr.shiranuit.luapolisessentials.Main;
import fr.shiranuit.luapolisessentials.Manager.ConfigManager;
import fr.shiranuit.luapolisessentials.Manager.LogForJusticeManager;
import fr.shiranuit.luapolisessentials.Manager.PlayerManager;
import fr.shiranuit.luapolisessentials.Network.Packets.BroadcastPacket;
import fr.shiranuit.luapolisessentials.Stats.PlayerStats;
import fr.shiranuit.luapolisessentials.Utils.Util;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class LFJCommand extends CommandBase {
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "lfj";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		// TODO Auto-generated method stub
		return "/lfj <on/off>";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length < 1) {
			throw new WrongUsageException("/lfj <true/false>", new Object[0]);
		} else {
			Boolean state = Boolean.valueOf(args[0]);
				if (state == true) {
					Util.sendMessage(sender,TextFormatting.RED+ " [LFJ Status] : Enable");
				} else {
					Util.sendMessage(sender,TextFormatting.RED+ " [LFJ Status] : Disable");
				}
				ConfigManager.EssentialsConfig.getCategory("logforjustice").get("enabled").set(state);
				ConfigManager.EssentialsConfig.save();
				LogForJusticeManager.enabled = state;
		}
	}
	
	@Override
	public int getRequiredPermissionLevel() {
		return 4;
	}
}
