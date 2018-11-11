package fr.shiranuit.luapolisessentials.Commands;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import fr.shiranuit.luapolisessentials.Main;
import fr.shiranuit.luapolisessentials.Manager.AreaManager;
import fr.shiranuit.luapolisessentials.Manager.ConfigManager;
import fr.shiranuit.luapolisessentials.Manager.ItemBlockManager;
import fr.shiranuit.luapolisessentials.Manager.JobManager;
import fr.shiranuit.luapolisessentials.Manager.WorldManager;
import net.minecraft.block.Block;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class UpdateConfig  extends CommandBase {

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "essentials:reloadconfig";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		// TODO Auto-generated method stub
		return "/essentials:reloadconfig";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		ConfigManager.sync();
		WorldManager.loadArtificial();
		JobManager.loadCfg();
		AreaManager.reloadProtection();
		ITextComponent text = new TextComponentString(TextFormatting.RED+"[LuaPolisEssentials] Config/WorldData/Areas/JobsConfig reloaded");
		sender.sendMessage(text);
	}
	
	@Override
	public int getRequiredPermissionLevel() {
		return 4;
	}
}