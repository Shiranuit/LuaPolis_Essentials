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

import com.google.common.io.Files;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraft.world.GameType;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class InventoryManager {
	
	public InventoryManager(String worldID) {
		this.worldID = worldID;
	}
	
	public String worldID = "";
	public static String modLogDir;
	
	public HashMap<String, PlayerData> info = new HashMap<String,PlayerData>();
	

		
		public void addPlayer(EntityPlayer pl) {
			String uuid = pl.getGameProfile().getId().toString();
			PlayerData data = new PlayerData();
			data.fromPlayer(pl);
			info.put(uuid, data);
			this.savePlayer(pl);
		}
		
		public void removePlayer(EntityPlayer pl) {
			String uuid = pl.getGameProfile().getId().toString();
			if (info.containsKey(uuid)) {
				info.remove(uuid);
			}
		}
		
		public PlayerData getPlayer(EntityPlayer pl) {
			String uuid = pl.getGameProfile().getId().toString();
			if (info.containsKey(uuid)) {
				return (PlayerData)info.get(uuid);
			}
			return null;
		}
		
		public void savePlayer(EntityPlayer pl) {
			String uuid = pl.getGameProfile().getId().toString();
			if (this.info.containsKey(uuid)) {
				ByteBuf data = Unpooled.buffer();
				ByteBufUtils.writeTag(data, this.info.get(uuid).toNBT());
				File file = new File(modLogDir + File.separatorChar + this.worldID + File.separatorChar + uuid);
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
		
		public void savePlayers() {
			for (String uuid : info.keySet()) {
				if (this.info.containsKey(uuid)) {
					ByteBuf data = Unpooled.buffer();
					ByteBufUtils.writeTag(data, this.info.get(uuid).toNBT());
					File file = new File(modLogDir + File.separatorChar + this.worldID + File.separatorChar + uuid);
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
		}
		
		
		public void loadPlayers() {
			info.clear();
			File dir = new File(modLogDir + File.separatorChar + this.worldID);
			if (dir.exists()) {
				File[] dimensions = dir.listFiles();
				for (File file : dimensions) {
					    ByteBuffer mBuf;
					    try {
					      byte[] bt = Files.toByteArray(file);
					      ByteBuf buf = Unpooled.wrappedBuffer(bt);
					      NBTTagCompound data = ByteBufUtils.readTag(buf);
					      PlayerData pData = new PlayerData();
					      pData.fromNBT(data);
					     this.info.put(file.getName(), pData); 
					    } catch (IOException exc) {
			
					    }
				}
			} else {
				File odir = new File(modLogDir);
				if (!odir.exists()) {
					odir.mkdir();	
				}
				
				File odir2 = new File(modLogDir + File.separatorChar + this.worldID);
				if (!odir2.exists()) {
					odir2.mkdir();	
				}
			}
		}
	
	
	public class PlayerData {
		public HashMap<Integer, ItemStack> dt;
		public boolean creative = false;
		public PlayerData() {
			this.dt =  new HashMap<Integer, ItemStack>();
		}

		
		public void fromPlayer(EntityPlayer pl) {
			this.dt.clear();
			for (int i=0; i<pl.inventory.getSizeInventory(); i++) {
				this.dt.put(i,pl.inventory.getStackInSlot(i));
			}
			this.creative = pl.capabilities.isCreativeMode;
		}
		
		public void toPlayer(EntityPlayer pl) {
			for (Integer k : this.dt.keySet()) {
				int i = k.intValue();
				pl.inventory.setInventorySlotContents(i, this.dt.get(i));
			}
			if (this.creative) {
				pl.setGameType(GameType.CREATIVE);
			} else {
				pl.setGameType(GameType.SURVIVAL);
			}
		}
		
		public NBTTagCompound toNBT() {
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setBoolean("creative", this.creative);
			saveAllItems(nbt,this.dt, true);
			return nbt;
		}
		
		public void fromNBT(NBTTagCompound nbt) {
			this.creative = nbt.getBoolean("creative");
			this.dt.clear();
			loadAllItems(nbt, this.dt);
		}
		
	    public NBTTagCompound saveAllItems(NBTTagCompound tag, HashMap<Integer, ItemStack> list, boolean p_191281_2_)
	    {
	        NBTTagList nbttaglist = new NBTTagList();

	        for (Integer k : list.keySet())
	        {
	        	int i = k.intValue();
	        	if (list.containsKey(i)) {
		            ItemStack itemstack = (ItemStack)list.get(i);
		            
		            if (!itemstack.isEmpty())
		            {
		                NBTTagCompound nbttagcompound = new NBTTagCompound();
		                nbttagcompound.setByte("Slot", (byte)i);
		                itemstack.writeToNBT(nbttagcompound);
		                nbttaglist.appendTag(nbttagcompound);
		            }
	        	}
	        }

	        if (!nbttaglist.hasNoTags() || p_191281_2_)
	        {
	            tag.setTag("Items", nbttaglist);
	        }

	        return tag;
	    }
		
	    public void loadAllItems(NBTTagCompound tag, HashMap<Integer, ItemStack> list)
	    {
	        NBTTagList nbttaglist = tag.getTagList("Items", 10);
	        for (int i = 0; i < nbttaglist.tagCount(); ++i)
	        {
	            NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
	            int j = nbttagcompound.getByte("Slot") & 255;
	            if (j >= 0)
	            {
	                list.put(j, new ItemStack(nbttagcompound));
	            }
	        }
	    }
		
	}
	
	
}
