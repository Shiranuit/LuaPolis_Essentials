package fr.shiranuit.luapolisessentials.Manager;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.lwjgl.Sys;

import fr.shiranuit.luapolisessentials.Main;
import fr.shiranuit.luapolisessentials.Network.Packets.ItemBlockDisablePacket;
import fr.shiranuit.luapolisessentials.Utils.ArrayConverter;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.config.Property;

public class ItemBlockManager {

	public static HashMap<String, IRecipe> removedRecipes = new HashMap<String, IRecipe>();

	public static boolean disableRecipe(String id, int meta, boolean updateClient) {
		List<IRecipe> recipes =	CraftingManager.getInstance().getRecipeList();
		Iterator<IRecipe> Leash = recipes.iterator();
		while (Leash.hasNext()) {
			IRecipe recipe = Leash.next();
			ItemStack is = recipe.getRecipeOutput();
			
			
				String ItemBlock = id;
				Item ItemfromName = Item.getByNameOrId(ItemBlock);
				Block block = Block.getBlockFromName(ItemBlock);
				
				Item BlockFromName = null;
				if (block != null) {
					BlockFromName  = Item.getItemFromBlock(block);
				}
				ItemStack check = null; 
				ItemStack check2 = null;
				if (ItemfromName != null) {
					check = new ItemStack(ItemfromName, 1, meta > -1 ? meta : 0);
				}
				if (BlockFromName != null) {
					check2 = new ItemStack(BlockFromName, 1, meta > -1 ? meta : 0);
				}
				if (is != null) {
					if (check != null && ItemStack.areItemsEqualIgnoreDurability(is, check)) {
						if (check.getMetadata() == meta  || meta < 0) {
							removedRecipes.put(id+"/"+(meta > -1 ? meta : "*"), recipe);
							add(id+"/"+(meta > -1 ? meta : "*"));
							Leash.remove();
							if (updateClient) {
								Main.instance.network.sendToAll(new ItemBlockDisablePacket(id, meta, true));
							}
							return true;
						}
					}
					if (check2 != null && ItemStack.areItemsEqualIgnoreDurability(is, check2)) {
						if (check2.getMetadata() == meta || meta < 0) {
							removedRecipes.put(id+"/"+(meta > -1 ? meta : "*"), recipe);
							add(id+"/"+(meta > -1 ? meta : "*"));
							Leash.remove();
							if (updateClient) {
								Main.instance.network.sendToAll(new ItemBlockDisablePacket(id, meta, true));
							}
							return true;
						}
					}
				}
			}
			return false;
		}
	
	public static boolean enableRecipe(String id, int meta, boolean updateClient) {
		if (removedRecipes.containsKey(id+"/"+(meta > -1 ? meta : "*"))) {
			CraftingManager.getInstance().addRecipe(removedRecipes.get(id+"/"+(meta > -1 ? meta : "*")));
			remove(id+"/"+(meta > -1 ? meta : "*"));
			removedRecipes.remove(id+"/"+(meta > -1 ? meta : "*"));
			if (updateClient) {
				Main.instance.network.sendToAll(new ItemBlockDisablePacket(id,  meta, false));
			}
			return true;
		}
		return false;
	}
	
	private static void add(String id) {
		if (!Main.DisabledItemsBlocks.contains(id)) {
			Main.DisabledItemsBlocks.add(id);
			ConfigManager.EssentialsConfig.getCategory("Disable").get("ItemsBlocks").set(ArrayConverter.convert(Main.DisabledItemsBlocks));
			ConfigManager.EssentialsConfig.save();
		}
	}
	
	private static void remove(String id) {
		if (Main.DisabledItemsBlocks.contains(id)) {
			Main.DisabledItemsBlocks.remove(id);
			ConfigManager.EssentialsConfig.getCategory("Disable").get("ItemsBlocks").set(ArrayConverter.convert(Main.DisabledItemsBlocks));
			ConfigManager.EssentialsConfig.save();
		}
	}
}
