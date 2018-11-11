package fr.shiranuit.luapolisessentials.Jobs;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class Job implements IMessage, IJob{
	
	public int lvl = 0;
	public int xp = 0;
	public int points = 0;
	public int Ability1 = 0;
	public int Ability2 = 0;
	private int AbilityMax1 = 0;
	private int AbilityMax2 = 0;
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int lvl() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int xp() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int points() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int Ability1() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int Ability2() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int AbilityMin1() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int AbilityMin2() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int AbilityMax1() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int AbilityMax2() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public String AbilityName1() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String AbilityName2() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void tryLvlUp() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public int nextLvl() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void fromBytes(ByteBuf buf) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void toBytes(ByteBuf buf) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public int maxLvl() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void addXP(int xp) {
		// TODO Auto-generated method stub
		
	}
	
}
