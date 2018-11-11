package fr.shiranuit.luapolisessentials.Manager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

public class CommandSpyManager {
	public static String modLogDir = "";
	public static ArrayList<String> commands = new ArrayList<String>();
	public static HashMap<EntityPlayer, Boolean> spy = new HashMap<EntityPlayer, Boolean>();
	
	public static void add(ICommand cmd, String[] param, ICommandSender sender) {
	    SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");;
	    Date date = new Date();
	    String maDate = formater.format(date);
	    BlockPos pos = sender.getPosition();
	    commands.add("["+maDate+"] [X="+pos.getX()+",Y="+pos.getY()+",Z="+pos.getZ()+"] <"+sender.getName()+"> /"+cmd.getName()+" "+String.join(" ", param));
	}
	
	public static void spyMode(EntityPlayer player, boolean state) {
		spy.put(player, state);
	}
	
	public static void load() {
		
	}
}
