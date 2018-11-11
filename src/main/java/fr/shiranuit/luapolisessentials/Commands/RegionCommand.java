package fr.shiranuit.luapolisessentials.Commands;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;

import org.apache.commons.lang3.ArrayUtils;

import fr.shiranuit.luapolisessentials.Main;
import fr.shiranuit.luapolisessentials.Area.Area;
import fr.shiranuit.luapolisessentials.Manager.AreaManager;
import fr.shiranuit.luapolisessentials.Utils.ArrayConverter;
import fr.shiranuit.luapolisessentials.Utils.Util;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class RegionCommand extends CommandBase {

	@Override
	public List<String> getAliases()
    {
        return Arrays.<String>asList("rg");
    }
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "region";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		// TODO Auto-generated method stub
		return "/region help";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length >= 1) {
			if (args[0].equals("add")) {
				if (args.length >= 8) {
					String name = args[1];
					if (!AreaManager.Areas.containsKey(name)) {
						int x = Integer.valueOf(args[2]);
						int y = Integer.valueOf(args[3]);
						int z = Integer.valueOf(args[4]);
						int dx = Integer.valueOf(args[5]);
						int dy = Integer.valueOf(args[6]);
						int dz = Integer.valueOf(args[7]);
						int dimensionID = Integer.valueOf(args[8]);
						AreaManager.writeProtection(name, x, y, z, dx, dy, dz, dimensionID, AreaManager.flagDefault);
						Util.sendMessage(sender,TextFormatting.RED+"The area "+name+" has been set");
					} else {
						Util.sendMessage(sender,TextFormatting.RED+"The area "+name+" already exist");
					}
				} else {
					Util.sendMessage(sender,TextFormatting.RED+"Usage : /region add <name> <x> <y> <z> <dx> <dy> <dz> <dimensionID>");
				}
			}
			if (args[0].equals("remove")) {
				if (args.length >= 2) {
					String name = args[1];
					AreaManager.deleteProtection(name);
					Util.sendMessage(sender,TextFormatting.RED+"The area "+name+" has been removed");
				} else {
					Util.sendMessage(sender,TextFormatting.RED+"Usage : /region remove <name>");
				}
			}
			if (args[0].equals("flag")) {
				if (args.length >= 4) {
					String name = args[1];
					String flag = args[2];
					if (AreaManager.Areas.containsKey(name)) {
						Boolean state = Boolean.valueOf(args[3]);
						Area area = AreaManager.Areas.get(name);
						area.flags.put(flag, state);
						AreaManager.writeProtection(area, name);
						Util.sendMessage(sender,TextFormatting.RED+"The '"+flag+"' flag of the '"+name+"' area has been set to : "+state);
					}
				}
			}
			if (args[0].equals("reload")) {
				Util.sendMessage(sender, TextFormatting.RED+"Areas reloaded");
				AreaManager.reloadProtection();
			}
			if (args[0].equals("help")) {
				ITextComponent text = new TextComponentString(TextFormatting.RED+"Usage : /region add <regionName> <x> <y> <z> <dx> <dy> <dz> <dimensionID>");
				sender.sendMessage(text);
				text = new TextComponentString(TextFormatting.RED+"Usage : /region remove <regionName>");
				sender.sendMessage(text);
				text = new TextComponentString(TextFormatting.RED+"Usage : /region reload");
				sender.sendMessage(text);
				text = new TextComponentString(TextFormatting.RED+"Usage : /region flag <regionName> <flag> <value>");
				sender.sendMessage(text);
				text = new TextComponentString(TextFormatting.RED+"Usage : /region help");
				sender.sendMessage(text);
			}
		} else {
			ITextComponent text = new TextComponentString(TextFormatting.RED+"Usage : /region help");
			sender.sendMessage(text);
		}
		
	}
	
	@Override
	public int getRequiredPermissionLevel() {
		return 4;
	}
	
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
    	int x = targetPos != null ? targetPos.getX() : sender.getPosition().getX();
    	int y = targetPos != null ? targetPos.getY() : sender.getPosition().getY();
    	int z = targetPos != null ? targetPos.getZ() : sender.getPosition().getZ();
    	switch (args.length) {
	    	case 1: {
	    		return getListOfStringsMatchingLastWord(args, ArrayConverter.convert(new String[] {"add","remove","reload","help","flag"}));
	    	}
	    	case 2: {
	    		if (args[0].equals("flag")) {
	    			return getListOfStringsMatchingLastWord(args, AreaManager.Areas.keySet());
	    		}
	    		if (args[0].equals("remove")) {
	    			return getListOfStringsMatchingLastWord(args, AreaManager.Areas.keySet());
	    		}
	    		return Collections.<String>emptyList();
	    	}
	    	case 3: {
	    		if (args[0].equals("flag")) {
	    			return getListOfStringsMatchingLastWord(args, AreaManager.flagDefault.keySet());
	    		}
	    		if (args[0].equals("add")) {
	    			return getListOfStringsMatchingLastWord(args, ArrayConverter.convert(new String[] {String.valueOf(x)}));
	    		}
	    		return Collections.<String>emptyList();
	    	}
	    	case 4: {
	    		if (args[0].equals("add")) {
	    			return getListOfStringsMatchingLastWord(args, ArrayConverter.convert(new String[] {String.valueOf(y)}));
	    		}
	    		return Collections.<String>emptyList();
	    	}
	    	case 5: {
	    		if (args[0].equals("add")) {
	    			return getListOfStringsMatchingLastWord(args, ArrayConverter.convert(new String[] {String.valueOf(z)}));
	    		}
	    		return Collections.<String>emptyList();
	    	}
	    	case 6: {
	    		if (args[0].equals("add")) {
	    			return getListOfStringsMatchingLastWord(args, ArrayConverter.convert(new String[] {String.valueOf(x)}));
	    		}
	    		return Collections.<String>emptyList();
	    	}
	    	case 7: {
	    		if (args[0].equals("add")) {
	    			return getListOfStringsMatchingLastWord(args, ArrayConverter.convert(new String[] {String.valueOf(y)}));
	    		}
	    		return Collections.<String>emptyList();
	    	}
	    	case 8: {
	    		if (args[0].equals("add")) {
	    			return getListOfStringsMatchingLastWord(args, ArrayConverter.convert(new String[] {String.valueOf(z)}));
	    		}
	    		return Collections.<String>emptyList();
	    	}
	    	case 9: {
	    		if (args[0].equals("add")) {
	    			return getListOfStringsMatchingLastWord(args, ArrayConverter.convert(new String[] {String.valueOf(sender.getEntityWorld().provider.getDimension())}));
	    		}
	    		return Collections.<String>emptyList();
	    	}
	    	default: {
	    		return Collections.<String>emptyList();
	    	}
    	}
    }

}
