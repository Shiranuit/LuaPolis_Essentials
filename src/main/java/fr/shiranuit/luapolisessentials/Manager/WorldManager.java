package fr.shiranuit.luapolisessentials.Manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.crypto.Data;

import com.google.common.io.Files;

import fr.shiranuit.luapolisessentials.LogForJustice.BlockInfos;
import fr.shiranuit.luapolisessentials.LogForJustice.EnumPlayerAction;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.item.EnumAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class WorldManager {
	public static String modLogDir;
	public static HashMap<Integer, ArrayList<Integer>> artificial = new HashMap<Integer, ArrayList<Integer>>();
	
	public static boolean isNatural(Integer worldName, BlockPos pos) {
		ArrayList<Integer> data = artificial.get(worldName);
		if (data != null) {
			return !data.contains(pos.hashCode());
		}
		return true;
	}
	
	public static boolean isNatural(World world, BlockPos pos) {
		ArrayList<Integer> data = artificial.get(world.provider.getDimension());
		if (data != null) {
			return !data.contains(pos.hashCode());
		}
		return true;
	}
	
	public static void addArtificial(Integer worldName, BlockPos pos) {
		ArrayList<Integer> data = new ArrayList<Integer>();
		if (artificial.containsKey(worldName)) {
			data = artificial.get(worldName);
		}
		if (!data.contains(pos.hashCode())) {
			data.add(pos.hashCode());
		}
		artificial.put(worldName, data);
	}
	
	public static void addArtificial(World world, BlockPos pos) {
		ArrayList<Integer> data = new ArrayList<Integer>();
		if (artificial.containsKey(world.provider.getDimension())) {
			data = artificial.get(world.provider.getDimension());
		}
		if (!data.contains((Integer)pos.hashCode())) {
			data.add(pos.hashCode());
		}
		artificial.put(world.provider.getDimension(), data);
	}
	
	public static void removeArtificial(World world, BlockPos pos) {
		if (artificial.containsKey(world.provider.getDimension())) {
			ArrayList<Integer> data = artificial.get(world.provider.getDimension());
			if (data.contains(pos.hashCode())) {
				data.remove((Integer)pos.hashCode());
				artificial.put(world.provider.getDimension(), data);
			}
		}
	}
	
	public static void removeArtificial(Integer worldName, BlockPos pos) {
		if (artificial.containsKey(worldName)) {
			ArrayList<Integer> data = artificial.get(worldName);
			if (data.contains(pos.hashCode())) {
				data.remove(pos.hashCode());
				artificial.put(worldName, data);
			}
		}
	}
	
	public static void saveArtificial() {
		for (Integer dimension : artificial.keySet()) {
			ByteBuf data = Unpooled.buffer();
			ArrayList<Integer> list = artificial.get(dimension);
			data.writeInt(list.size());
			for (Integer val : list) {
				data.writeInt(val);
			}
			File file = new File(modLogDir + File.separatorChar + dimension);
			FileChannel wChannel;
			try {
				wChannel = new FileOutputStream(file, false).getChannel();
				wChannel.write(data.nioBuffer());
				wChannel.close();
			} catch (FileNotFoundException e1) {

			} catch (IOException e) {

			}
		}
	}
	
	public static void loadArtificial() {
		artificial.clear();
		File dir = new File(modLogDir);
		if (dir.exists()) {
			File[] dimensions = dir.listFiles();
			for (File file : dimensions) {
			    FileInputStream fIn;
			    FileChannel fChan;
			    long fSize;
			    ByteBuffer mBuf;
	
			    try {
			      byte[] bt = Files.toByteArray(file);
			      ByteBuf buf = Unpooled.wrappedBuffer(bt);
			      int size = buf.readInt();
			      ArrayList<Integer> data = new ArrayList<Integer>();
			      for (int i = 0; i < size; i++) {
			    	  int val = buf.readInt();
			    	  data.add(val);
			      }
			      System.out.println(data);
			      artificial.put(Integer.parseInt(file.getName()), data);
			    } catch (IOException exc) {
	
			    }
			}
		} else {
			dir.mkdir();
		}
	}
	
}
