package fr.shiranuit.luapolisessentials.Manager;

import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class SaveManager {
	public static boolean canSave(World w) {
		if (FMLCommonHandler.instance().getSide().isServer()) {
			return true;
		} else {
			if (w!=null && w.isRemote) {
				return true;
			}
		}
		return false;
	}
}
