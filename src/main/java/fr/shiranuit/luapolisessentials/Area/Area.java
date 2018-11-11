package fr.shiranuit.luapolisessentials.Area;

import java.util.HashMap;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class Area {
	public int x;
	public int y;
	public int z;
	public int dx;
	public int dy;
	public int dz;
	public String[] whitelist;
	public String[] blacklist;
	public int dimensionID; 
	public HashMap<String, Boolean> flags;
	public Area(int x, int y, int z, int dx, int dy, int dz, String[] whitelist, String[] blacklist, int dimensionID, HashMap<String, Boolean> flags) {
		this.x = Math.min(x,dx);
		this.y = Math.min(y,dy);
		this.z = Math.min(z,dz);
		
		this.dx = Math.max(x,dx);
		this.dy = Math.max(y,dy);
		this.dz = Math.max(z,dz);
		
		this.whitelist = whitelist;
		this.blacklist = blacklist;
		
		this.dimensionID = dimensionID;
		this.flags = flags;
	}
	
	public boolean isIn(Entity ent) {
		if (ent.world.provider.getDimension() == this.dimensionID && ent.posX >= this.x && ent.posX <= this.dx+1 && ent.posY >= this.y && ent.posY <= this.dy && ent.posZ >= this.z && ent.posZ <= this.dz+1) {
			return true;
		}
		return false;
	}
	public boolean isIn(World w, BlockPos pos) {
		if (w.provider.getDimension() == this.dimensionID && pos.getX() >= this.x && pos.getX() <= this.dx+1 && pos.getY() >= this.y && pos.getY() <= this.dy && pos.getZ() >= this.z && pos.getZ() <= this.dz+1) {
			return true;
		}
		return false;
	}
	public boolean isIn(World w, Vec3d pos) {
		if (w.provider.getDimension() == this.dimensionID && pos.xCoord >= this.x && pos.xCoord <= this.dx+1 && pos.yCoord >= this.y && pos.yCoord <= this.dy && pos.zCoord >= this.z && pos.zCoord <= this.dz+1) {
			return true;
		}
		return false;
	}
}
