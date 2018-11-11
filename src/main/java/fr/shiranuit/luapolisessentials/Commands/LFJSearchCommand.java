package fr.shiranuit.luapolisessentials.Commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fr.shiranuit.luapolisessentials.Main;
import fr.shiranuit.luapolisessentials.LogForJustice.BlockData;
import fr.shiranuit.luapolisessentials.LogForJustice.BlockInfos;
import fr.shiranuit.luapolisessentials.LogForJustice.EnumPlayerAction;
import fr.shiranuit.luapolisessentials.Manager.ConfigManager;
import fr.shiranuit.luapolisessentials.Manager.LogForJusticeManager;
import fr.shiranuit.luapolisessentials.Manager.PlayerManager;
import fr.shiranuit.luapolisessentials.Network.Packets.BroadcastPacket;
import fr.shiranuit.luapolisessentials.Stats.PlayerStats;
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

public class LFJSearchCommand extends CommandBase {
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "lfjsearch";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		// TODO Auto-generated method stub
		return "/lfjsearch help";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length < 1) {
			throw new WrongUsageException("/lfjsearch help", new Object[0]);
		} else {
			if (args[0].equals("help")) {
				ITextComponent text = new TextComponentString(TextFormatting.RED+"Usage : /lfjsearch param1=... param2=... param...");
				sender.sendMessage(text);
				text = new TextComponentString(TextFormatting.RED+"Parameters : Dimension,dim,X,Y,Z,Player,SameDate,SD,BeforeDate,BD,AfterDate,AD,Action,ID,Meta,ItemID,itemMeta");
				sender.sendMessage(text);
				text = new TextComponentString(TextFormatting.RED+"Actions : BREAK, PLACE, INTERACT");
				sender.sendMessage(text);
				text = new TextComponentString(TextFormatting.RED+"DateFormat : year-month-day-hour-minute-second");
				sender.sendMessage(text);
				//(year-month-day-hour-minute-second)
			} else {
				HashMap<String, Object> arg = new HashMap<String, Object>();
				for (int i=0; i<args.length; i++) {
					String data[] = args[i].toLowerCase().split("=");
					if (data.length == 2) {
						if (data[0].equals("dimension") || data[0].equals("dim") || data[0].equals("x") || data[0].equals("y") || data[0].equals("z") || data[0].equals("id") || data[0].equals("meta") || data[0].equals("itemid") || data[0].equals("itemmeta")) {
							try {
								arg.put(data[0], Integer.valueOf(data[1]));
							} catch (Exception e) {
								ITextComponent text = new TextComponentString(TextFormatting.RED+"Formatting Error : "+data[0]+" must be a number");
								sender.sendMessage(text);
							}
						} else if (data[0].equals("samedata") || data[0].equals("sd") || data[0].equals("beforedata") || data[0].equals("bd") || data[0].equals("afterdate") || data[0].equals("ad")) {
							try {
								arg.put(data[0], Util.parseDate(data[1]));
							}catch (Exception e) {
								ITextComponent text = new TextComponentString(TextFormatting.RED+e.getMessage());
								sender.sendMessage(text);
							}
						} else {
							arg.put(data[0], data[1]);
						}
					}
				}
				HashMap<Integer, HashMap<BlockPos,  List<BlockInfos>>> bdata = LogForJusticeManager.search(arg);
				for (Integer dimension : bdata.keySet()) {
					HashMap<BlockPos,  List<BlockInfos>> log = bdata.get(dimension);
					if (log != null) {
						for (BlockPos e : log.keySet()) {
							ITextComponent text = new TextComponentString(TextFormatting.DARK_AQUA+"Block changes at ["+e.getX()+", "+e.getY()+", "+e.getZ()+"] in dimension ["+dimension+"]");
							sender.sendMessage(text);
							List<BlockInfos> data = log.get(e);
							if (data != null && data.size() > 0) {
								for (BlockInfos binfo : data) {	
									if (binfo.action == EnumPlayerAction.PLACE) {
										String blockname = Block.getBlockById(binfo.id).getRegistryName().getResourceDomain() + ":" + Block.getBlockById(binfo.id).getRegistryName().getResourcePath() + "/" + binfo.meta;
										text = new TextComponentString(TextFormatting.GOLD+binfo.date+" "+binfo.playername+" created "+blockname);
										sender.sendMessage(text);
									}
									if (binfo.action == EnumPlayerAction.BREAK) {
										String blockname = Block.getBlockById(binfo.id).getRegistryName().getResourceDomain() + ":" + Block.getBlockById(binfo.id).getRegistryName().getResourcePath() + "/" + binfo.meta;
										String itemname = Item.getItemById(binfo.itemID).getRegistryName().getResourceDomain() + ":" + Item.getItemById(binfo.itemID).getRegistryName().getResourcePath() + "/" + binfo.itemMeta;
										text = new TextComponentString(TextFormatting.GOLD+binfo.date+" "+binfo.playername+" breaked "+blockname + " with " + itemname);
										sender.sendMessage(text);
									}
									if (binfo.action == EnumPlayerAction.INTERACT) {
										String blockname = Block.getBlockById(binfo.id).getRegistryName().getResourceDomain() + ":" + Block.getBlockById(binfo.id).getRegistryName().getResourcePath() + "/" + binfo.meta;
										String itemname = Item.getItemById(binfo.itemID).getRegistryName().getResourceDomain() + ":" + Item.getItemById(binfo.itemID).getRegistryName().getResourcePath() + "/" + binfo.itemMeta;
										text = new TextComponentString(TextFormatting.GOLD+binfo.date+" "+binfo.playername+" interacted "+blockname + " with " + itemname);
										sender.sendMessage(text);
									}
								}
							}
						}
					}
				}

			}
		}
	}
	
	@Override
	public int getRequiredPermissionLevel() {
		return 4;
	}
}
