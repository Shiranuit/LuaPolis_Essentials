package fr.shiranuit.luapolisessentials.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import javax.annotation.Nonnull;

import com.mojang.realmsclient.gui.ChatFormatting;

import fr.shiranuit.luapolisessentials.Main;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.management.UserListOps;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;

public class Util {

	public static String translate(String txt) {
		return I18n.format("luapolisessentials."+txt);
	}
	
	public static void sendMessage(EntityPlayer player, String message) {
		ITextComponent txt = new TextComponentString(TextFormatting.RED + "[LuaPolisEssentials] "+TextFormatting.WHITE+message);
		player.sendMessage(txt);
	}
	public static void sendMessage(ICommandSender player, String message) {
		ITextComponent txt = new TextComponentString(TextFormatting.RED + "[LuaPolisEssentials] "+TextFormatting.WHITE+message);
		player.sendMessage(txt);
	}
	
	public static EntityPlayer playerByName(String name) {
		return Main.mcserver.getPlayerList().getPlayerByUsername(name);
	}
	
	  public static boolean isOp(String player) {
		  	if (Main.mcserver.isSinglePlayer()) {
		  		return true;
		  	}
			UserListOps op = Main.mcserver.getPlayerList().getOppedPlayers();
			if (op.getGameProfileFromName(player) != null) {
				return true;
			}
			return false;
	   }
	  
	  public static boolean isOp(EntityPlayer player) {
		  	if (Main.mcserver.isSinglePlayer()) {
		  		return true;
		  	}
			UserListOps op = Main.mcserver.getPlayerList().getOppedPlayers();
			if (op.getEntry(player.getGameProfile()) != null) {
				return true;
			}
			return false;
	   }
	  
	   public static boolean isToolEffective(IBlockState state, @Nonnull ItemStack stack)
	    {
	        for (String type : stack.getItem().getToolClasses(stack))
	        {
	            if (state.getBlock().isToolEffective(type, state))
	                return true;
	        }
	        return false;
	    }
	
	   public static String getDate()
	   {
		  SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		  Date date = new Date();
		  return formater.format(date);
	   }
	   
	   public static ArrayList<File> GetFiles(File dir) {
			  ArrayList<File> folder = new ArrayList<File>();
			  ArrayList<File> data = new ArrayList<File>();
			  folder.add(dir);
			  while (folder.size() > 0)
			  {
				  File[] f = folder.get(0).listFiles();
				  for (int i=0;i<f.length;i++) {
					  if (f[i].isDirectory()) {
						  folder.add(f[i]);
					  } else {
						data.add(f[i]);
					  }
				  }
				  folder.remove(0);
			  }
			  return data;
	   }
	   
	   public static ArrayList<File> GetFiles(File dir, String ext) {
			  ArrayList<File> folder = new ArrayList<File>();
			  ArrayList<File> data = new ArrayList<File>();
			  folder.add(dir);
			  while (folder.size() > 0)
			  {
				  File[] f = folder.get(0).listFiles();
				  for (File file : f) {
					  if (file.isDirectory()) {
						  folder.add(file);
					  } else {
						  if (file.getName().endsWith(ext)){
							  data.add(file);
						  }
					  }
				  }
				  folder.remove(0);
			  }
			  return data;
	   }
	   
	   public static ArrayList<String> readFileLines(File fin) throws IOException {
			  if (fin.exists() && !fin.isDirectory()) {
				  ArrayList<String> lines = new ArrayList<String>();
				FileInputStream fis = new FileInputStream(fin);
				BufferedReader br = new BufferedReader(new InputStreamReader(fis));
				String line = null;
				while ((line = br.readLine()) != null) {
					line = line.replace("\n", "");
					lines.add(line);
				}
				br.close();
				return lines;
			  }
			  return null;
		}
	   
	   public static Date parseDate(String date) throws Exception {
			String dateData[] = date.split("-");
			if (dateData.length >= 6) {
				try {
					int year = Integer.valueOf(dateData[0]);
					int month = Integer.valueOf(dateData[1]);
					int day = Integer.valueOf(dateData[2]);
					int hour = Integer.valueOf(dateData[3]);
					int minute = Integer.valueOf(dateData[4]);
					int second = Integer.valueOf(dateData[5]);
					Date d = new Date(year, month, day, hour, minute, second);
					return d;
				}catch (Exception e) {
					
				}
			} else {
				throw new Exception("Formatting Error : Date must be formatted like this (year-month-day-hour-minute-second) example : "+getDate());
			}
			return null;
	   }
	   
	   public static HashMap GetTags(NBTTagCompound Ctag) {
			HashMap infos = new HashMap();
			if (Ctag != null) {
				Iterator iterator = Ctag.getKeySet().iterator();
				while (iterator.hasNext()) {
					String currentTag = (String)iterator.next();
					if (Ctag.hasKey(currentTag, NBT.TAG_END)) {
						//Rien a mettre ici
					}
					else if (Ctag.hasKey(currentTag, NBT.TAG_BYTE)) {
						infos.put(currentTag, ((Byte)Ctag.getByte(currentTag)).intValue());
					}
					else if (Ctag.hasKey(currentTag, NBT.TAG_SHORT)) {
						infos.put(currentTag, ((Short)Ctag.getShort(currentTag)).intValue());
					}
					else if (Ctag.hasKey(currentTag, NBT.TAG_INT)) {
						infos.put(currentTag, ((Integer)Ctag.getInteger(currentTag)).intValue());
					}
					else if (Ctag.hasKey(currentTag, NBT.TAG_LONG)) {
						infos.put(currentTag, ((Long)Ctag.getLong(currentTag)).intValue());
					}
					else if (Ctag.hasKey(currentTag, NBT.TAG_FLOAT)) {
						infos.put(currentTag, ((Float)Ctag.getFloat(currentTag)).intValue());
					}
					else if (Ctag.hasKey(currentTag, NBT.TAG_DOUBLE)) {
						infos.put(currentTag, ((Double)Ctag.getDouble(currentTag)).intValue());
					}
					else if (Ctag.hasKey(currentTag, NBT.TAG_STRING)) {
						infos.put(currentTag, Ctag.getString(currentTag).toString());
					}
					else if (Ctag.hasKey(currentTag, NBT.TAG_LIST)) {
						HashMap newInfos = new HashMap();
						NBTTagList lst = Ctag.getTagList(currentTag, NBT.TAG_COMPOUND);
						for (int i=0; i < lst.tagCount(); i++) {
							newInfos.put(i+1, GetTags(lst.getCompoundTagAt(i)));
						}
						infos.put(currentTag, newInfos);
					}
					else if (Ctag.hasKey(currentTag, NBT.TAG_COMPOUND)) {
						infos.put(currentTag, GetTags(Ctag.getCompoundTag(currentTag)));
					}
				}
			}
			return infos;
		}
	
	   public static int maxHeight(World w, int x, int z) {
			int a = w.getTopSolidOrLiquidBlock(new BlockPos(x,256,z)).getY();
			int b = w.getPrecipitationHeight(new BlockPos(x,256,z)).getY();
			int c = w.getHeight(new BlockPos(x,256,z)).getY();
		   return Math.min(Math.max(Math.max(a, b),c)+2,255);
	   }
	   
	   public static long getFolderSize(File folder) {
		    long length = 0;
		    File[] files = folder.listFiles();
		 
		    int count = files.length;
		 
		    for (int i = 0; i < count; i++) {
		        if (files[i].isFile()) {
		            length += files[i].length();
		        }
		        else {
		            length += getFolderSize(files[i]);
		        }
		    }
		    return length;
		}
}
