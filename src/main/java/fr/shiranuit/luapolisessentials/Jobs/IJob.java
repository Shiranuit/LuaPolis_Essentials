package fr.shiranuit.luapolisessentials.Jobs;

import io.netty.buffer.ByteBuf;

public interface IJob {
	String getName();
	int lvl();
	int maxLvl();
	int xp();
	int points();
	int Ability1();
	int Ability2();
	int AbilityMin1();
	int AbilityMin2();
	int AbilityMax1();
	int AbilityMax2();
	String AbilityName1();
	String AbilityName2();
	void tryLvlUp();
	int nextLvl();
	void fromBytes(ByteBuf buf);
	void toBytes(ByteBuf buf);
	void addXP(int xp);
}
