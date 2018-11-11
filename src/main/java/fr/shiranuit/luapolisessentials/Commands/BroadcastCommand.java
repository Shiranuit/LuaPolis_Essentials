package fr.shiranuit.luapolisessentials.Commands;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import fr.shiranuit.luapolisessentials.Main;
import fr.shiranuit.luapolisessentials.Manager.ConfigManager;
import fr.shiranuit.luapolisessentials.Manager.ItemBlockManager;
import fr.shiranuit.luapolisessentials.Network.Packets.BroadcastPacket;
import net.minecraft.block.Block;
import net.minecraft.block.state.BlockStateBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

public class BroadcastCommand  extends CommandBase {

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "broadcast";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		// TODO Auto-generated method stub
		return "/broadcast <message>";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		 ITextComponent itextcomponent = getChatComponentFromNthArg(sender, args, 0, true);
		 String txt = itextcomponent.getUnformattedText();
		 txt = TextFormatting.GREEN + "["+TextFormatting.RED+"Annonce Serveur"+TextFormatting.GREEN+"] "+TextFormatting.RED + txt.replace("&", "\247");
		 ITextComponent msg = new TextComponentString(txt);
		 server.getPlayerList().sendMessage(msg);
		 Main.network.sendToAll(new BroadcastPacket(itextcomponent.getUnformattedText().replace("&", "\247")));
	}
	
	@Override
	public int getRequiredPermissionLevel() {
		return 4;
	}
}