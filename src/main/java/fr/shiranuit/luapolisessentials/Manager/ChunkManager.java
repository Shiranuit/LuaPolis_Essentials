package fr.shiranuit.luapolisessentials.Manager;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import fr.shiranuit.luapolisessentials.Main;
import fr.shiranuit.luapolisessentials.Utils.Util;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.registry.GameData;

public class ChunkManager {
	public static boolean check = false;
	public static ArrayList<String> block2remove = new ArrayList<String>();
	public static ArrayList<Chunk> analysed = new ArrayList<Chunk>();
	
	public static void Enable() {
		ChunkManager.check=true;
	}
	
	public static void Disable() {
		ChunkManager.check=false;
		ChunkManager.analysed.clear();
	}
	
	public static String Add(String block) {
		String data[] = block.split("[/]");
		if (data.length >= 2) {
			try {
				int meta = Integer.valueOf(data[1]);
				ChunkManager.block2remove.add(data[0]+"/"+meta);
				return data[0]+"/"+meta;
			} catch (Exception e) {
				int meta = 0;
				ChunkManager.block2remove.add(data[0]+"/"+meta);
				return data[0]+"/"+meta;
			}

		} else {
			int meta = 0;
			ChunkManager.block2remove.add(block+"/"+meta);
			return block+"/"+meta;
		}
	}
	
	public static String Remove(String block) {
		String data[] = block.split("[/]");
		if (data.length >= 2) {
			try {
				int meta = Integer.valueOf(data[1]);
				if (block2remove.contains(data[0]+"/"+meta)) {
					ChunkManager.block2remove.remove(data[0]+"/"+meta);
				}
				return data[0]+"/"+meta;
			} catch (Exception e) {
				int meta = 0;
				if (block2remove.contains(data[0]+"/"+meta)) {
					ChunkManager.block2remove.remove(data[0]+"/"+meta);
				}
				return data[0]+"/"+meta;
			}

		} else {
			int meta = 0;
			if (block2remove.contains(data[0]+"/"+meta)) {
				ChunkManager.block2remove.remove(data[0]+"/"+meta);
			}
			return block+"/"+meta;
		}
	}
	
	
	
	public static void Clear(ICommandSender player) {
		Util.sendMessage(player, TextFormatting.RED+"[LPEChunk] World number to scan ["+Main.mcserver.worlds.length+"]");
		int totalchunk = 0;
		for (WorldServer w : Main.mcserver.worlds){
			int chunks = w.getChunkProvider().id2ChunkMap.size();
			String name = w.provider.getDimensionType().getName();
			Util.sendMessage(player, TextFormatting.RED+"[LPEChunk] "+chunks+" Chunks in world ["+name+"]");
			totalchunk += w.getChunkProvider().id2ChunkMap.size();
		}
		int before=0;
		Util.sendMessage(player, TextFormatting.RED+"[LPEChunk] Total chunks to scan : "+totalchunk);
		int totalblock=0;
		int scanned = 0;
		for (WorldServer w : Main.mcserver.worlds){
			Long2ObjectMap chunks = w.getChunkProvider().id2ChunkMap;
			for (Long l : chunks.keySet()) {
				Chunk c = (Chunk)chunks.get(l);
				//if (!analysed.contains(c)) {
					int xs = c.xPosition*16;
					int zs = c.zPosition*16;
					for (int x=xs; x<xs+16; x++) {
						for (int z=zs; z<zs+16; z++) {
							for (int y=0; y<Util.maxHeight(w, x, z); y++) {
								BlockPos pos = new BlockPos(x,y,z);
								IBlockState iblock = w.getBlockState(pos);
								ResourceLocation loc = GameData.getBlockRegistry().getNameForObjectBypass(iblock.getBlock());
								String id = loc.getResourceDomain() + ":" + loc.getResourcePath()+"/"+iblock.getBlock().getMetaFromState(iblock);
								if (block2remove.contains(id)) {
									w.setBlockToAir(pos);
									
									totalblock++;
								}
							}
						}
					}
					scanned++;
					int percent = (int)Math.floor((double)scanned/(double)totalchunk*100.0);
					if (before != percent) {
						System.out.println("[LPEChunk] Cleaning : "+percent+"%");
						before = percent;
					}
					//analysed.add(c);
				//}
			}
		}
		Util.sendMessage(player, TextFormatting.RED+"[LPEChunk] Total block removed : "+totalblock);
	}
}
