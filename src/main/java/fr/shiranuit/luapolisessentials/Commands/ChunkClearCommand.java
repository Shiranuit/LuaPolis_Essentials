package fr.shiranuit.luapolisessentials.Commands;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import fr.shiranuit.luapolisessentials.Manager.AreaManager;
import fr.shiranuit.luapolisessentials.Manager.ChunkManager;
import fr.shiranuit.luapolisessentials.Utils.ArrayConverter;
import fr.shiranuit.luapolisessentials.Utils.Util;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class ChunkClearCommand extends CommandBase {

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "lpechunk";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		// TODO Auto-generated method stub
		return "/lpechunk help";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length < 1) {
			throw new WrongUsageException("/lpechunk help", new Object[0]);
		} else if (args.length >= 1) {
			if (args[0].equals("addID")) {
				if (args.length >= 2) {
					Util.sendMessage(sender,TextFormatting.RED+"[LPEChunk] Block added : "+ChunkManager.Add(args[1]));
				} else {
					Util.sendMessage(sender,TextFormatting.RED+"Usage : /lpechunk add <ID>");
				}
			} else if (args[0].equals("removeID")) {
				if (args.length >= 2) {
					Util.sendMessage(sender,TextFormatting.RED+"[LPEChunk] Block removed : "+ChunkManager.Remove(args[1]));
				} else {
					Util.sendMessage(sender,TextFormatting.RED+"Usage : /lpechunk remove <ID>");
				}
			} else if (args[0].equals("listID")) {
				Util.sendMessage(sender,TextFormatting.RED+"[LPEChunk] ID List: ["+String.join(", ",ChunkManager.block2remove)+"]");
			} else if (args[0].equals("enable")) {
				ChunkManager.Enable();
				Util.sendMessage(sender,TextFormatting.RED+"[LPEChunk] ClearMode Enabled");
			} else if (args[0].equals("disable")) {
				ChunkManager.Disable();
				Util.sendMessage(sender,TextFormatting.RED+"[LPEChunk] ClearMode Disabled");
			} else if (args[0].equals("clearWorld")) {
				ChunkManager.Clear(sender);
				Util.sendMessage(sender,TextFormatting.RED+"[LPEChunk] Clear finished");
			} else if (args[0].equals("clearIDList")) {
				ChunkManager.block2remove.clear();
				Util.sendMessage(sender,TextFormatting.RED+"[LPEChunk] ID List cleared");
			} else if (args[0].equals("test")) {
				Util.sendMessage(sender, sender.getEntityWorld().getTopSolidOrLiquidBlock(sender.getPosition()).getY()+"");
				Util.sendMessage(sender, sender.getEntityWorld().getPrecipitationHeight(sender.getPosition()).getY()+"");
				Util.sendMessage(sender, sender.getEntityWorld().getHeight(sender.getPosition()).getY()+"");
			//	World w = sender.getEntityWorld();
//				IBlockState iblock = w.getBlockState(new BlockPos(sender.getPosition().getX(), 255, sender.getPosition().getZ()));
	//			RayTraceResult r = iblock.collisionRayTrace(w, new BlockPos(sender.getPosition().getX(), 255, sender.getPosition().getZ()), new Vec3d(sender.getPosition().getX(), 255, sender.getPosition().getZ()), new Vec3d(sender.getPosition().getX(), 0, sender.getPosition().getZ()));
				
		//		Util.sendMessage(sender, ""+r.hitVec.yCoord);
			} else if (args[0].equals("clearAnalysed")) {
				ChunkManager.analysed.clear();
				Util.sendMessage(sender,TextFormatting.RED+"[LPEChunk] Analysed Chunks cleared");
			} else if (args[0].equals("help")) {
				ITextComponent text = new TextComponentString(TextFormatting.RED+"Usage : /lpechunk addID <ID>");
				sender.sendMessage(text);
				text = new TextComponentString(TextFormatting.RED+"Usage : /lpechunk removeID <ID>");
				sender.sendMessage(text);
				text = new TextComponentString(TextFormatting.RED+"Usage : /lpechunk listID");
				sender.sendMessage(text);
				text = new TextComponentString(TextFormatting.RED+"Usage : /lpechunk enable");
				sender.sendMessage(text);
				text = new TextComponentString(TextFormatting.RED+"Usage : /lpechunk disable");
				sender.sendMessage(text);
				text = new TextComponentString(TextFormatting.RED+"Usage : /lpechunk clearWorld");
				sender.sendMessage(text);
				text = new TextComponentString(TextFormatting.RED+"Usage : /lpechunk clearAnalysed");
				sender.sendMessage(text);
				text = new TextComponentString(TextFormatting.RED+"Usage : /lpechunk clearIDList");
				sender.sendMessage(text);
				text = new TextComponentString(TextFormatting.RED+"Usage : /lpechunk help");
				sender.sendMessage(text);
			}
		} else {
			throw new WrongUsageException("/lpechunk help", new Object[0]);
		}
		
	}
	
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
    	switch (args.length) {
	    	case 1: {
	    		return getListOfStringsMatchingLastWord(args, ArrayConverter.convert(new String[] {"help","addID","removeID","listID","enable","disable","clearWorld","clearAnalysed","clearIDList"}));
	    	}
	    	case 2: {
	    		if (args[0].equals("addID")) {
	    			return getListOfStringsMatchingLastWord(args, Block.REGISTRY.getKeys());
	    		}
	    		if (args[0].equals("removeID")) {
	    			return getListOfStringsMatchingLastWord(args, ChunkManager.block2remove);
	    		}
	    		return Collections.<String>emptyList();
	    	}
	    	default: {
	    		return Collections.<String>emptyList();
	    	}
    	}
    }
}
