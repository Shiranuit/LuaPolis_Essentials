package fr.shiranuit.luapolisessentials.Manager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.google.common.io.Files;
import com.sun.jna.platform.unix.X11.XClientMessageEvent.Data;

import fr.shiranuit.luapolisessentials.LogForJustice.BlockData;
import fr.shiranuit.luapolisessentials.LogForJustice.BlockInfos;
import fr.shiranuit.luapolisessentials.LogForJustice.EnumPlayerAction;
import fr.shiranuit.luapolisessentials.Utils.Util;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class LogForJusticeManager {
	public static String modLogDir;
	public static String oldDir;
	public static boolean enabled = true;
	public static HashMap<Integer, HashMap<BlockPos,  List<BlockInfos>>> LFJ = new HashMap<Integer, HashMap<BlockPos,  List<BlockInfos>>>();
	public static FileWriter file;
	public static int swapSize = 50;
	
	public static void setup() {
		String log = modLogDir + File.separatorChar + Util.getDate() + ".log";

			File oDir = new File(oldDir);
			if (!(oDir.exists() && oDir.isDirectory())) {
				oDir.mkdir();
			}
		
			File dir = new File(modLogDir);
			if (!(dir.exists() && dir.isDirectory())) {
				dir.mkdir();
			} else {
				if (Util.getFolderSize(dir) >= swapSize * 1024 * 1024) {
					for (File f : dir.listFiles()) {
						f.renameTo(new File(oldDir + File.separatorChar + f.getName()));
					}
				}
			}
		
			try {
				File f = new File(log);
				if (!f.exists()) {
					f.createNewFile();
				}
				file = new FileWriter(log,true);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}
	
	public static String format(int dimension, BlockPos pos, BlockInfos info) {
		return info.date+"|"+dimension+"|"+pos.getX()+"|"+pos.getY()+"|"+pos.getZ()+"|"+info.playername+"|"+info.action.name()+"|"+info.id+"|"+info.meta+"|"+info.itemID+"|"+info.itemMeta;
	}
	
	public static void saveOne(int dimension, BlockPos pos, BlockInfos info) {
		try {
			file.append(format(dimension, pos, info)+"\n");
			file.flush();
		}catch (Exception e) {
			
		}
	}
	
	public static BlockData parse(String line) {
		try {
			String data[] = line.split("[|]");
			BlockInfos info = new BlockInfos(EnumPlayerAction.valueOf(data[6]), data[5], Integer.valueOf(data[7]), Integer.valueOf(data[8]), Integer.valueOf(data[9]), Integer.valueOf(data[10]), data[0]);
			BlockPos pos = new BlockPos(Integer.valueOf(data[2]), Integer.valueOf(data[3]), Integer.valueOf(data[4]));
			int dimension = Integer.valueOf(data[1]);
			return new BlockData(dimension, pos, info);
		} catch (Exception e) {
			
		}	
		return null;
	}
	public static void addLog(int dimension, BlockPos pos, BlockInfos info, boolean write) {
		if (enabled) {
			HashMap<BlockPos, List<BlockInfos>> log = new HashMap<BlockPos,  List<BlockInfos>>();
			 List<BlockInfos> binfo = new ArrayList<BlockInfos>();
			 if (LFJ.containsKey(dimension)) {
				 log = LFJ.get(dimension);
			 }
			 if (log.containsKey(pos)) {
				 binfo = log.get(pos);
			 }
			 binfo.add(info);
			 log.put(pos, binfo);
			 LFJ.put(dimension, log);
			 if (write) {
				 saveOne(dimension, pos, info);
			 }
		}
	}
	
/*	public static void saveLFJ() {
		if (enabled) {
			ByteBuf data = Unpooled.buffer();
			data.writeInt(LFJ.keySet().size());
			for (Integer dimension : LFJ.keySet()) {
				data.writeInt(dimension);
				HashMap<BlockPos,  List<BlockInfos>> log = LFJ.get(dimension);
				data.writeInt(log.keySet().size());
				for (BlockPos pos : log.keySet()) {
					data.writeInt(pos.getX());
					data.writeInt(pos.getY());
					data.writeInt(pos.getZ());
					data.writeInt(log.get(pos).size());
					for (BlockInfos binfo : log.get(pos)) {
						if (binfo.action == EnumPlayerAction.PLACE) {
							data.writeByte(0);
							ByteBufUtils.writeUTF8String(data, binfo.playername);
							data.writeInt(binfo.id);
							data.writeInt(binfo.meta);
							ByteBufUtils.writeUTF8String(data, binfo.date);
						}
						if (binfo.action == EnumPlayerAction.BREAK) {
							data.writeByte(1);
							ByteBufUtils.writeUTF8String(data, binfo.playername);
							data.writeInt(binfo.id);
							data.writeInt(binfo.meta);
							data.writeInt(binfo.itemID);
							data.writeInt(binfo.itemMeta);
							ByteBufUtils.writeUTF8String(data, binfo.date);		
						}
						if (binfo.action == EnumPlayerAction.INTERACT) {
							data.writeByte(2);
							ByteBufUtils.writeUTF8String(data, binfo.playername);
							data.writeInt(binfo.id);
							data.writeInt(binfo.meta);
							data.writeInt(binfo.itemID);
							data.writeInt(binfo.itemMeta);
							ByteBufUtils.writeUTF8String(data, binfo.date);	
						}
					}
				}
			}
			File dir = new File(modLogDir);
			if (!(dir.exists() && dir.isDirectory())) {
				dir.mkdir();
			}
			File file = new File(modLogDir + File.separatorChar + "LogForJustice.data");
			FileChannel wChannel;
			try {
				wChannel = new FileOutputStream(file, false).getChannel();
				wChannel.write(data.nioBuffer());
				wChannel.close();
			} catch (FileNotFoundException e1) {
	
			} catch (IOException e) {
	
			}
		}
	} */
	
	/*public static void loadLFJ() {
		if (enabled) {
			File dir = new File(modLogDir);
			if (!(dir.exists() && dir.isDirectory())) {
				dir.mkdir();
			}
			LFJ.clear();
			File file = new File(modLogDir + File.separatorChar + "LogForJustice.data");
			FileInputStream fIn;
		    FileChannel fChan;
		    long fSize;
		    ByteBuffer mBuf;
		    try {
			      byte[] bt = Files.toByteArray(file);
			      ByteBuf buf = Unpooled.wrappedBuffer(bt);
			      int totalDimension = buf.readInt();
			      for (int dimIndex = 0; dimIndex<totalDimension; dimIndex++) {
			    	  int dimension = buf.readInt();
			    	  int totalBlock = buf.readInt();
			    	  HashMap<BlockPos, List<BlockInfos>> log = new HashMap<BlockPos, List<BlockInfos>>();
			    	  for (int blockIndex = 0; blockIndex < totalBlock; blockIndex++) {
			    		  int x = buf.readInt();
			    		  int y = buf.readInt();
			    		  int z = buf.readInt();
				    	  int totalLog = buf.readInt();
				    	  
				  		  List<BlockInfos> data = new ArrayList<BlockInfos>();
				    	  for (int i=0; i<totalLog; i++) {
				    		  byte action = buf.readByte();
				    		  if (action == 0) {
				    			  String player = ByteBufUtils.readUTF8String(buf);
				    			  int id = buf.readInt();
				    			  int meta = buf.readInt();
				    			  String date = ByteBufUtils.readUTF8String(buf);
				    			  BlockPos pos = new BlockPos(x,y,z);
				    			  BlockInfos binfo = new BlockInfos(EnumPlayerAction.PLACE, player, id, meta, -1, 0, date);
				    			  data.add(binfo);
				    		  }
				    		  if (action == 1) {
				    			  String player = ByteBufUtils.readUTF8String(buf);
				    			  int id = buf.readInt();
				    			  int meta = buf.readInt();
				    			  int itemID = buf.readInt();
				    			  int itemMeta = buf.readInt();
				    			  String date = ByteBufUtils.readUTF8String(buf);
				    			  BlockPos pos = new BlockPos(x,y,z);
				    			  BlockInfos binfo = new BlockInfos(EnumPlayerAction.BREAK, player, id, meta, itemID, itemMeta, date);
				    			  data.add(binfo);
				    		  }
				    		  if (action == 2) {
				    			  String player = ByteBufUtils.readUTF8String(buf);
				    			  int id = buf.readInt();
				    			  int meta = buf.readInt();
				    			  int itemID = buf.readInt();
				    			  int itemMeta = buf.readInt();
				    			  String date = ByteBufUtils.readUTF8String(buf);
				    			  BlockPos pos = new BlockPos(x,y,z);
				    			  BlockInfos binfo = new BlockInfos(EnumPlayerAction.INTERACT, player, id, meta, itemID, itemMeta, date);
				    			  data.add(binfo);
				    		  }
				    	  }  
					  	  log.put(new BlockPos(x,y,z), data);
			    	  }
				    LFJ.put(dimension, log); 
				    
			      }
			    } catch (IOException exc) {
			    	System.out.println(exc.getMessage());
			    }
		}
	}*/
	
	public static boolean checkDim(int dimension, HashMap<String,  Object> arg) {
		boolean find = true;
		if (arg.containsKey("dimension")) {
			int dim = ((Integer)arg.get("dimension")).intValue();
			if (dim != dimension) {
				find = false;
			}
		}
		
		if (arg.containsKey("dim")) {
			int dim = ((Integer)arg.get("dim")).intValue();
			if (dim != dimension) {
				find = false;
			}
		}
		
		return find;
	}
	
	public static boolean checkPos(BlockPos bpos, HashMap<String,  Object> arg) {
		boolean find = true;
		if (arg.containsKey("x")) {
			int x = ((Integer)arg.get("x")).intValue();
			if (x != bpos.getX()) {
				find = false;
			}
		}
		
		if (arg.containsKey("y")) {
			int y = ((Integer)arg.get("y")).intValue();
			if (y != bpos.getY()) {
				find = false;
			}
		}
		
		if (arg.containsKey("z")) {
			int z = ((Integer)arg.get("z")).intValue();
			if (z != bpos.getZ()) {
				find = false;
			}
		}
		return find;
	}
	
	public static boolean checkInfo(BlockInfos info, HashMap<String,  Object> arg) {
		boolean findinfos = true;
		
		if (arg.containsKey("samedate")) {
			Date date = (Date) arg.get("samedate");
			try {
				Date dateblock =  Util.parseDate(info.date);
				if (dateblock.compareTo(date) != 0) {
					findinfos = false;
				}
			} catch (Exception e) {
				findinfos = false;
			}
		}
		
		if (arg.containsKey("sd")) {
			Date date = (Date) arg.get("sd");
			try {
				Date dateblock =  Util.parseDate(info.date);
				if (dateblock.compareTo(date) != 0) {
					findinfos = false;
				}
			} catch (Exception e) {
				findinfos = false;
			}
		}
		
		if (arg.containsKey("beforedate")) {
			Date date = (Date) arg.get("beforedate");
			try {
				Date dateblock =  Util.parseDate(info.date);
				if (!dateblock.before(date)) {
					findinfos = false;
				}
			} catch (Exception e) {
				findinfos = false;
			}
		}
		
		if (arg.containsKey("bd")) {
			Date date = (Date) arg.get("bd");
			try {
				Date dateblock =  Util.parseDate(info.date);
				if (!dateblock.before(date)) {
					findinfos = false;
				}
			} catch (Exception e) {
				findinfos = false;
			}
		}
		
		if (arg.containsKey("afterdate")) {
			Date date = (Date) arg.get("afterdate");
			try {
				Date dateblock =  Util.parseDate(info.date);
				if (!dateblock.after(date)) {
					findinfos = false;
				}
			} catch (Exception e) {
				findinfos = false;
			}
		}
		if (arg.containsKey("ad")) {
			Date date = (Date) arg.get("ad");
			try {
				Date dateblock =  Util.parseDate(info.date);
				if (!dateblock.after(date)) {
					findinfos = false;
				}
			} catch (Exception e) {
				findinfos = false;
			}
		}
		
		if (arg.containsKey("player")) {
			String playername = (String) arg.get("player");
			if (!info.playername.toLowerCase().equals(playername)) {
				findinfos = false;
			}
		}
		
		if (arg.containsKey("id")) {
			int id = ((Integer) arg.get("id")).intValue();
			if (info.id != id) {
				findinfos = false;
			}
		}
		
		if (arg.containsKey("meta")) {
			int meta = ((Integer) arg.get("meta")).intValue();
			if (info.meta != meta) {
				findinfos = false;
			}
		}
		
		if (arg.containsKey("itemid")) {
			int itemid = ((Integer) arg.get("itemid")).intValue();
			if (info.itemID != itemid) {
				findinfos = false;
			}
		}
		
		if (arg.containsKey("itemmeta")) {
			int itemmeta = ((Integer) arg.get("itemmeta")).intValue();
			if (info.itemMeta != itemmeta) {
				findinfos = false;
			}
		}
		
		if (arg.containsKey("action")) {
			String action = (String) arg.get("action");
			if (!info.action.name().toLowerCase().equals(action)) {
				findinfos = false;
			}
		}
		
		return findinfos;
	}
	
	public static HashMap<Integer, HashMap<BlockPos,  List<BlockInfos>>> search(HashMap<String, Object> arg) {
		HashMap<Integer, HashMap<BlockPos,  List<BlockInfos>>> data = new HashMap<Integer, HashMap<BlockPos,  List<BlockInfos>>>();
		for (Integer dimension : LFJ.keySet()) {
			if (checkDim(dimension, arg)) {
				HashMap<BlockPos, List<BlockInfos>> binfo = new HashMap<BlockPos, List<BlockInfos>>();
				HashMap<BlockPos, List<BlockInfos>> log = LFJ.get(dimension);
				for (BlockPos bpos : log.keySet()) {
					List<BlockInfos> linfos = new ArrayList<BlockInfos>();
					if (checkPos(bpos, arg)) {
						List<BlockInfos> infos = log.get(bpos);
						for (BlockInfos info : infos) {
							if (checkInfo(info, arg)) {
								linfos.add(info);
							}
						}
					}
					binfo.put(bpos, linfos);
				}
				data.put(dimension, binfo);
			}
		}
		return data;
	}
	
	public static void loadLFJ() {
		if (enabled) {
			File dir = new File(modLogDir);
			LFJ.clear();
			ArrayList<File> files = Util.GetFiles(dir,".log");
			for (File f : files) {
				try {
					ArrayList<String> lines = Util.readFileLines(f);
					for (String line : lines) {
						BlockData data = parse(line);
						if (data != null) {
							addLog(data.dimension, data.pos, data.info, false);
						}
					}
				} catch (IOException e) {

				}
			}
		}
	}
}
