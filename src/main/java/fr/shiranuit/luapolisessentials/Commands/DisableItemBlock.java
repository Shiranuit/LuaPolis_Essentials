package fr.shiranuit.luapolisessentials.Commands;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import fr.shiranuit.luapolisessentials.Main;
import fr.shiranuit.luapolisessentials.Manager.ItemBlockManager;
import fr.shiranuit.luapolisessentials.Utils.Util;
import net.minecraft.block.Block;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;

public class DisableItemBlock  extends CommandBase {

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "disable";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		// TODO Auto-generated method stub
		return "/disable <itemID/blockID>";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length < 1) {
			throw new WrongUsageException("/disable <itemID/blockID>", new Object[0]);
		} else {
				String[] data = args[0].split("[/]");
				if (data.length >= 2) {
					try {
						int meta = Integer.valueOf(data[1]);
						if (data[1].equals("*")) {
							meta = -1;
						}
						boolean success = ItemBlockManager.disableRecipe(data[0], meta, true);
						if (success) {
							Util.sendMessage(sender, TextFormatting.RED+"Disabled "+args[0].toString()+"/"+(meta > -1 ? meta : "*"));
						} else {
							Util.sendMessage(sender, TextFormatting.RED+"Unknown recipe for "+args[0].toString()+"/"+(meta > -1 ? meta : "*"));
						}
					} catch (Exception e) {
						int meta = 0;
						if (data[1].equals("*")) {
							meta = -1;
						}
						boolean success = ItemBlockManager.disableRecipe(data[0], meta, true);
						if (success) {
							Util.sendMessage(sender, TextFormatting.RED+"Disabled "+data[0].toString()+"/"+(meta > -1 ? meta : "*"));
						} else {
							Util.sendMessage(sender, TextFormatting.RED+"Unknown recipe for "+data[0].toString()+"/"+(meta > -1 ? meta : "*"));
						}
					}
				} else {
					boolean success = ItemBlockManager.disableRecipe(args[0], 0, true);
					if (success) {
						Util.sendMessage(sender, TextFormatting.RED+"Disabled "+args[0].toString()+"/0");
					} else {
						Util.sendMessage(sender, TextFormatting.RED+"Unknown recipe for "+args[0].toString()+"/0");
					}
				}
		}
		
	}
	
	@Override
	public int getRequiredPermissionLevel() {
		return 4;
	}
	
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
        return args.length == 1 ? (getListOfStringsMatchingLastWord(args, Item.REGISTRY.getKeys())!=null ? getListOfStringsMatchingLastWord(args, Item.REGISTRY.getKeys()) : getListOfStringsMatchingLastWord(args, Block.REGISTRY.getKeys())) : Collections.<String>emptyList();
    }

}