package fr.shiranuit.luapolisessentials.Jobs;

import io.netty.buffer.ByteBuf;

public class Forger extends Job implements IJob {

	public int lvl = 0;
	public int xp = 0;
	public int points = 0;
	public int Ability1 = 0;
	public int Ability2 = 0;
	public int AbilityMin1 = 0;
	public int AbilityMin2 = 0;
	public int AbilityMax1 = 0;
	public int AbilityMax2 = 10;
	
	@Override
	public int nextLvl() {
		if (this.lvl<5) {
			return (int) (70*Math.pow(2,this.lvl));
		} else {
			return (int) (100*Math.pow(2,this.lvl));
		}
	}
	
	@Override
	public void tryLvlUp() {
		while (this.xp >= nextLvl() && this.lvl < this.maxLvl()) {
			this.xp -= nextLvl();
			this.lvl++;
			this.points++;
		}
	}
	
	
	public static String name() {
		return "forger";
	}
	
	@Override
	public String getName() {
		return "forger";
	}
	
	@Override
	public int lvl() {
		return this.lvl;
	}
	
	@Override
	public int xp() {
		return this.xp;
	}
	
	@Override
	public int points() {
		return this.points;
	}
	
	@Override
	public int Ability1() {
		return this.Ability1;
	}
	
	@Override
	public int Ability2() {
		return this.Ability2;
	}
	
	@Override
	public int AbilityMin1() {
		return this.AbilityMin1;
	}
	@Override
	public int AbilityMin2() {
		return this.AbilityMin2;
	}
	
	public int AbilityMax1() {
		return this.AbilityMax1;
	}
	public int AbilityMax2() {
		return this.AbilityMax2;
	}
	
	@Override
	public String AbilityName1() {
		return "norepaircost";
	}
	
	@Override
	public String AbilityName2() {
		return "craftingboost";
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.lvl = buf.readInt();
		this.xp = buf.readInt();
		this.points = buf.readInt();
		this.Ability1 = buf.readInt();
		this.Ability2 = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.lvl);
		buf.writeInt(this.xp);
		buf.writeInt(this.points);
		buf.writeInt(this.Ability1);
		buf.writeInt(this.Ability2);
	}
	@Override
	public int maxLvl() {
		return this.AbilityMax1+this.AbilityMax2;
	}
	
	@Override
	public void addXP(int xp) {
		this.xp += xp;
		tryLvlUp();
	}

}
