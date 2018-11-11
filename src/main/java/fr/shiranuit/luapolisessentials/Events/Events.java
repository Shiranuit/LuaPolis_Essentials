package fr.shiranuit.luapolisessentials.Events;

import java.awt.TextComponent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Syntax;

import org.lwjgl.opengl.RenderTexture;

import com.google.common.collect.Multimap;

import fr.shiranuit.luapolisessentials.Main;
import fr.shiranuit.luapolisessentials.Area.Area;
import fr.shiranuit.luapolisessentials.Jobs.Alchemist;
import fr.shiranuit.luapolisessentials.Jobs.Developper;
import fr.shiranuit.luapolisessentials.Jobs.Farmer;
import fr.shiranuit.luapolisessentials.Jobs.Forger;
import fr.shiranuit.luapolisessentials.Jobs.Hunter;
import fr.shiranuit.luapolisessentials.Jobs.Miner;
import fr.shiranuit.luapolisessentials.Jobs.Woodcutter;
import fr.shiranuit.luapolisessentials.LogForJustice.BlockInfos;
import fr.shiranuit.luapolisessentials.LogForJustice.EnumPlayerAction;
import fr.shiranuit.luapolisessentials.Manager.AreaManager;
import fr.shiranuit.luapolisessentials.Manager.ChunkManager;
import fr.shiranuit.luapolisessentials.Manager.CommandSpyManager;
import fr.shiranuit.luapolisessentials.Manager.InventoryManager.PlayerData;
import fr.shiranuit.luapolisessentials.Manager.ItemBlockManager;
import fr.shiranuit.luapolisessentials.Manager.JobManager;
import fr.shiranuit.luapolisessentials.Manager.PlayerManager;
import fr.shiranuit.luapolisessentials.Manager.WorldManager;
import fr.shiranuit.luapolisessentials.Manager.JobManager.ForgerParam;
import fr.shiranuit.luapolisessentials.Manager.LogForJusticeManager;
import fr.shiranuit.luapolisessentials.Network.Packets.ItemBlockDisablePacket;
import fr.shiranuit.luapolisessentials.Network.Packets.StatsUpdatePacket;
import fr.shiranuit.luapolisessentials.Stats.PlayerStats;
import fr.shiranuit.luapolisessentials.Utils.Rnd;
import fr.shiranuit.luapolisessentials.Utils.Util;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.BlockBreakable;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockMelon;
import net.minecraft.block.BlockNewLog;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockOre;
import net.minecraft.block.BlockPumpkin;
import net.minecraft.block.BlockStone;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentDamage;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntityShulker;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IInventoryChangedListener;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemLingeringPotion;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemSplashPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.item.crafting.RecipeRepairItem;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.profiler.ISnooperInfo;
import net.minecraft.server.management.UserListOps;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.GameType;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ServerConnectionFromClientEvent;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;
import net.minecraftforge.fml.common.registry.GameData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LootingLevelEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.brewing.PlayerBrewedPotionEvent;
import net.minecraftforge.event.brewing.PotionBrewEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.event.world.ChunkEvent;


public class Events {

	@SubscribeEvent
	public void onExplosion(ExplosionEvent e) {
		boolean explode = AreaManager.flagDefault.get("explosion");
		int x = Integer.MIN_VALUE;
		int y = Integer.MIN_VALUE;
		int z = Integer.MIN_VALUE;
		int dx = Integer.MAX_VALUE;
		int dy = Integer.MAX_VALUE;
		int dz = Integer.MAX_VALUE;
		for (String areaname : AreaManager.Areas.keySet()) {
			Area area = AreaManager.Areas.get(areaname);
			if (area.isIn(e.getWorld(), e.getExplosion().getPosition())) {
				if (area.x>=x && area.y>=y && area.z>=z && area.dx+1<=dx && area.dy<=dy && area.dz+1<=dz) {
					x = area.x;
					y = area.y;
					z = area.z;
					dx = area.dx;
					dy = area.dy;
					dz = area.dz;
					explode = area.flags.get("explosion");
				}
			}
		}
		if (!explode) {
			e.getExplosion().clearAffectedBlockPositions();
		}
		
	}
	
	@SubscribeEvent
	public void onPlayerBrewedPotionEvent(PlayerBrewedPotionEvent e) {
		if (e.getEntity() instanceof EntityPlayer) {
			PlayerStats stats = PlayerManager.get(e.getEntityPlayer());
			if (stats != null && stats.job != null)  {
				if (stats.job.getName().equals(Alchemist.name())) {
					if (e.getStack().getItem() instanceof ItemPotion || e.getStack().getItem() instanceof ItemLingeringPotion || e.getStack().getItem() instanceof ItemSplashPotion) {
						NBTTagCompound nbt = e.getStack().getTagCompound();
						if (!nbt.hasKey("PoweredUp")) {
							List<PotionEffect> effects = PotionUtils.getEffectsFromStack(e.getStack());
							List<PotionEffect> newEffects = new ArrayList<PotionEffect>();
							for (PotionEffect potion : effects) {
								newEffects.add(new PotionEffect(potion.getPotion(), potion.getDuration()+60*20*stats.job.Ability2(), potion.getAmplifier()+stats.job.Ability1()));
							}
							nbt.setTag("CustomPotionEffects", new NBTTagList());
							nbt.setBoolean("PoweredUp", true);
							e.getStack().setTagCompound(nbt);
							PotionUtils.appendEffects(e.getStack(), newEffects);
						}
					}
				}
			}
		}
	}
	
	public double distance(double x, double y, double z, double dx, double dy, double dz) {
		return Math.sqrt(Math.pow(dx-x,2)+Math.pow(dy-y,2)+Math.pow(dz-z,2));
	}
	
	@SubscribeEvent
	public void onGrow(BlockEvent.CropGrowEvent.Post e) {
		boolean find = false;
		int radius = 32;
		PlayerStats stats = null;
		List<EntityPlayer> ent = e.getWorld().getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(e.getPos().getX() - radius, e.getPos().getY() - radius, e.getPos().getZ() - radius, e.getPos().getX() + radius, e.getPos().getY() + radius, e.getPos().getZ() + radius));
		for (EntityPlayer player : ent) {
			stats = PlayerManager.get(player);
			if (stats != null && stats.job != null)  {
				if (stats.job.getName().equals(Farmer.name())) {
					if (distance(e.getPos().getX(),e.getPos().getY(),e.getPos().getZ(),player.posX,player.posY,player.posZ) <= Math.pow(2,stats.job.Ability1())) {
						find = true;
						break;
					}
				}
			}
		}
		if (find) {
			if (e.getState().getBlock() instanceof IGrowable) {
				for (int i=0; i < 2; i++) {
					IBlockState iblockstate = e.getWorld().getBlockState(e.getPos());
					IGrowable igrowable = (IGrowable)e.getState().getBlock();
		            if (igrowable.canGrow(e.getWorld(), e.getPos(), iblockstate, e.getWorld().isRemote))
		            {
		                if (!e.getWorld().isRemote)
		                {
		                    if (igrowable.canUseBonemeal(e.getWorld(),e.getWorld().rand, e.getPos(), iblockstate))
		                    {
		                        igrowable.grow(e.getWorld(), new Rnd((int)Math.max(stats.job.AbilityMax1()-stats.job.Ability1(),0)), e.getPos(), iblockstate);
		                    }
		                }
		            }
				}
			}
		}
	}
	
	public PlayerStats ForgerXP(PlayerStats stats, ItemStack stack, NBTTagCompound data) {
		if (stack.getItem() instanceof ItemSword) {
			String material = data.hasKey("material") ? data.getString("material") : "";
			if (material.equals("DIAMOND")) {
				stats.job.addXP(ForgerParam.diamondsword);
			}
			if (material.equals("GOLD")) {
				stats.job.addXP(ForgerParam.goldsword);
			}
			if (material.equals("IRON")) {
				stats.job.addXP(ForgerParam.ironsword);
			}
			if (material.equals("STONE")) {
				stats.job.addXP(ForgerParam.stonesword);
			}
			if (material.equals("WOOD")) {
				stats.job.addXP(ForgerParam.woodsword);
			}
		}
		if (stack.getItem() instanceof ItemArmor) {
			String material = data.hasKey("material") ? data.getString("material") : "";
			String slot = data.hasKey("slot") ? data.getString("slot") : "";
			
			if (material.equals("DIAMOND")) {
				if (slot.equals("helmet")) {
					stats.job.addXP(ForgerParam.diamondhelmet);
				}
				if (slot.equals("chestplate")) {
					stats.job.addXP(ForgerParam.diamondchestplate);
				}
				if (slot.equals("leggings")) {
					stats.job.addXP(ForgerParam.diamondleggings);
				}
				if (slot.equals("boots")) {
					stats.job.addXP(ForgerParam.diamondboots);
				}
			}
			if (material.equals("GOLD")) {
				if (slot.equals("helmet")) {
					stats.job.addXP(ForgerParam.goldhelmet);
				}
				if (slot.equals("chestplate")) {
					stats.job.addXP(ForgerParam.goldchestplate);
				}
				if (slot.equals("leggings")) {
					stats.job.addXP(ForgerParam.goldleggings);
				}
				if (slot.equals("boots")) {
					stats.job.addXP(ForgerParam.goldboots);
				}
			}
			if (material.equals("IRON")) {
				stats.job.addXP(ForgerParam.ironsword);if (slot.equals("helmet")) {
					stats.job.addXP(ForgerParam.ironhelmet);
				}
				if (slot.equals("chestplate")) {
					stats.job.addXP(ForgerParam.ironchestplate);
				}
				if (slot.equals("leggings")) {
					stats.job.addXP(ForgerParam.ironleggings);
				}
				if (slot.equals("boots")) {
					stats.job.addXP(ForgerParam.ironboots);
				}
			}
			if (material.equals("LEATHER")) {
				if (slot.equals("helmet")) {
					stats.job.addXP(ForgerParam.leatherhelmet);
				}
				if (slot.equals("chestplate")) {
					stats.job.addXP(ForgerParam.leatherchestplate);
				}
				if (slot.equals("leggings")) {
					stats.job.addXP(ForgerParam.leatherleggings);
				}
				if (slot.equals("boots")) {
					stats.job.addXP(ForgerParam.leatherboots);
				}
			}
		}
		return stats;
	}
	 
	@SubscribeEvent
	public void onPlayerInteract(PlayerInteractEvent.RightClickBlock e) {
		if (e.getEntity() instanceof EntityPlayer) {
			IBlockState iblock = e.getWorld().getBlockState(e.getPos());
			if (iblock.getBlock() instanceof BlockEnderChest) {
				if (e.getEntity().dimension == 42) {
					e.setCanceled(true);
					return;
				}
			}
		}
		if (e.getEntity() instanceof EntityPlayer) {
			if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
				if (e.getHand() == EnumHand.MAIN_HAND || e.getEntityPlayer().getHeldItem(e.getHand()) != ItemStack.EMPTY) {
					EntityPlayer player = (EntityPlayer)e.getEntity();
					IBlockState iblock = e.getWorld().getBlockState(e.getPos());
					Block block = iblock.getBlock();
					ItemStack hand = player.getHeldItem(e.getHand());
					Item ihand = hand.getItem();
					String name = ihand.getRegistryName().getResourceDomain() + ":" + ihand.getRegistryName().getResourcePath();
					if (name.equals("minecraft:wooden_pickaxe") && Util.isOp(e.getEntityPlayer())) {
						
						ITextComponent text = new TextComponentString(TextFormatting.DARK_AQUA+"Block changes at ["+e.getPos().getX()+", "+e.getPos().getY()+", "+e.getPos().getZ()+"] in dimension ["+player.dimension+"]");
						player.sendMessage(text);
						
						HashMap<BlockPos, List<BlockInfos>> log = LogForJusticeManager.LFJ.get(player.dimension);
						if (log != null) {
							List<BlockInfos> data = log.get(e.getPos());
							if (data != null && data.size() > 0) {
								for (BlockInfos binfo : data) {	
									if (binfo.action == EnumPlayerAction.PLACE) {
										String blockname = Block.getBlockById(binfo.id).getRegistryName().getResourceDomain() + ":" + Block.getBlockById(binfo.id).getRegistryName().getResourcePath() + "/" + binfo.meta;
										text = new TextComponentString(TextFormatting.GOLD+binfo.date+" "+binfo.playername+" created "+blockname);
										player.sendMessage(text);
									}
									if (binfo.action == EnumPlayerAction.BREAK) {
										String blockname = Block.getBlockById(binfo.id).getRegistryName().getResourceDomain() + ":" + Block.getBlockById(binfo.id).getRegistryName().getResourcePath() + "/" + binfo.meta;
										String itemname = Item.getItemById(binfo.itemID).getRegistryName().getResourceDomain() + ":" + Item.getItemById(binfo.itemID).getRegistryName().getResourcePath() + "/" + binfo.itemMeta;
										text = new TextComponentString(TextFormatting.GOLD+binfo.date+" "+binfo.playername+" breaked "+blockname + " with " + itemname);
										player.sendMessage(text);
									}
									if (binfo.action == EnumPlayerAction.INTERACT) {
										String blockname = Block.getBlockById(binfo.id).getRegistryName().getResourceDomain() + ":" + Block.getBlockById(binfo.id).getRegistryName().getResourcePath() + "/" + binfo.meta;
										String itemname = Item.getItemById(binfo.itemID).getRegistryName().getResourceDomain() + ":" + Item.getItemById(binfo.itemID).getRegistryName().getResourcePath() + "/" + binfo.itemMeta;
										text = new TextComponentString(TextFormatting.GOLD+binfo.date+" "+binfo.playername+" interacted "+blockname + " with " + itemname);
										player.sendMessage(text);
									}
								}
							} else {
								text = new TextComponentString(TextFormatting.DARK_AQUA+"No results found.");
								player.sendMessage(text);
							}
							
						}
					} else {
						String maDate = Util.getDate();
						BlockInfos info = new BlockInfos(EnumPlayerAction.INTERACT, player.getDisplayNameString(), Block.getIdFromBlock(block), block.getMetaFromState(iblock), Item.getIdFromItem(ihand), ihand.getMetadata(hand), maDate);
						LogForJusticeManager.addLog(player.dimension, e.getPos(), info, true);
					}
				}
				
				
				
				PlayerStats stats = PlayerManager.get(e.getEntityPlayer());
				if (e.getItemStack() != ItemStack.EMPTY && e.getWorld().getBlockState(e.getPos()).getBlock() instanceof BlockAnvil) {
					if (stats != null && stats.job != null) {
						if (stats.job.getName().equals(Forger.name())) {
							if (e.getItemStack().isItemDamaged()) {
								if (e.getItemStack().getRepairCost()-1 > 0) {
									stats.job.addXP(e.getItemStack().getRepairCost());
									PlayerManager.update((EntityPlayer)e.getEntity(), stats, true);
								}
								ItemStack item = e.getEntityPlayer().getHeldItemMainhand();
								if (e.getItemStack().getItem().isRepairable()) {
									e.getItemStack().setItemDamage(0);
								}
							}
								
								NBTTagCompound nbt = e.getItemStack().getTagCompound();
								if (nbt == null) {
									nbt = new NBTTagCompound();
								}
								if (!nbt.hasKey("ForgerBoost")) {
	
										if (e.getItemStack().getItem() instanceof ItemSword) {
											ItemSword sword = (ItemSword) e.getItemStack().getItem();
											NBTTagCompound data = new NBTTagCompound();
											data.setString("material", sword.getToolMaterialName());
											int attack = 0;
											for (AttributeModifier att : e.getItemStack().getAttributeModifiers(EntityEquipmentSlot.MAINHAND).get(SharedMonsterAttributes.ATTACK_DAMAGE.getName())) {
												attack = (int) att.getAmount()+1;
											}
											
											int Sharpness = EnchantmentHelper.getEnchantmentLevel(Enchantments.SHARPNESS,e.getItemStack());
											if (Sharpness > 0) {
												attack++;
												if (Sharpness-1>0) {
													attack += (Sharpness-1)*0.5;
												}
											}
											if (stats.job.Ability2()<=5) {
												e.getItemStack().addAttributeModifier(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), attack + (int)(Math.floor(Math.random()*stats.job.Ability2())), 0), EntityEquipmentSlot.MAINHAND);
											} else {
												e.getItemStack().addAttributeModifier(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), attack * (1+(int)(Math.floor(Math.random()*(stats.job.Ability2()-4)))), 0), EntityEquipmentSlot.MAINHAND);
											}
											nbt = e.getItemStack().getTagCompound();
											nbt.setBoolean("ForgerBoost", true);
											e.getItemStack().setTagCompound(nbt);
											stats = ForgerXP(stats, e.getItemStack(), data);
											PlayerManager.update(e.getEntityPlayer(), stats, true);
											return;
										}
										if (e.getItemStack().getItem() instanceof ItemArmor) {
											ItemArmor armor = (ItemArmor)e.getItemStack().getItem();
											NBTTagCompound data = new NBTTagCompound();
											data.setString("material", armor.getArmorMaterial().name());
												if (armor.armorType == EntityEquipmentSlot.HEAD) {
													data.setString("slot", "helmet");
												}
												if (armor.armorType == EntityEquipmentSlot.CHEST) {
													data.setString("slot", "chestplate");
												}
												if (armor.armorType == EntityEquipmentSlot.LEGS) {
													data.setString("slot", "leggings");
												}
												if (armor.armorType == EntityEquipmentSlot.FEET) {
													data.setString("slot", "boots");
												}
											if (stats.job.Ability2() <= stats.job.AbilityMax2()/2) {
												e.getItemStack().addAttributeModifier(SharedMonsterAttributes.ARMOR_TOUGHNESS.getName(), new AttributeModifier(SharedMonsterAttributes.ARMOR_TOUGHNESS.getName(), armor.damageReduceAmount + (int)(Math.floor(Math.random()*stats.job.Ability2())), 0), null);
												e.getItemStack().addAttributeModifier(SharedMonsterAttributes.KNOCKBACK_RESISTANCE.getName(), new AttributeModifier(SharedMonsterAttributes.KNOCKBACK_RESISTANCE.getName(), armor.damageReduceAmount + (int)(Math.floor(Math.random()*stats.job.Ability2())), 0), null);
												e.getItemStack().addAttributeModifier(SharedMonsterAttributes.ARMOR.getName(), new AttributeModifier(SharedMonsterAttributes.ARMOR.getName(), armor.damageReduceAmount + (int)(Math.floor(Math.random()*stats.job.Ability2())), 0), null);
												e.getItemStack().addAttributeModifier(SharedMonsterAttributes.MAX_HEALTH.getName(), new AttributeModifier(SharedMonsterAttributes.MAX_HEALTH.getName(), (int)(Math.floor(Math.random()*stats.job.Ability2()/2)), 0), null);
											} else {
												e.getItemStack().addAttributeModifier(SharedMonsterAttributes.ARMOR_TOUGHNESS.getName(), new AttributeModifier(SharedMonsterAttributes.ARMOR_TOUGHNESS.getName(), armor.damageReduceAmount * (1+(int)(Math.floor(Math.random()*(stats.job.Ability2()-4)))), 0), null);
												e.getItemStack().addAttributeModifier(SharedMonsterAttributes.KNOCKBACK_RESISTANCE.getName(), new AttributeModifier(SharedMonsterAttributes.KNOCKBACK_RESISTANCE.getName(), armor.damageReduceAmount * (1+(int)(Math.floor(Math.random()*(stats.job.Ability2()-4)))), 0), null);
												e.getItemStack().addAttributeModifier(SharedMonsterAttributes.ARMOR.getName(), new AttributeModifier(SharedMonsterAttributes.ARMOR.getName(), armor.damageReduceAmount * (1+(int)(Math.floor(Math.random()*(stats.job.Ability2()-4)))), 0), null);
												e.getItemStack().addAttributeModifier(SharedMonsterAttributes.MAX_HEALTH.getName(), new AttributeModifier(SharedMonsterAttributes.MAX_HEALTH.getName(), 1+(int)(Math.floor(Math.random()*stats.job.Ability2())*(stats.job.Ability2()-5)), 0), null);
											}
											nbt = e.getItemStack().getTagCompound();
											nbt.setBoolean("ForgerBoost", true);
											e.getItemStack().setTagCompound(nbt);
											stats = ForgerXP(stats, e.getItemStack(), data);
											PlayerManager.update(e.getEntityPlayer(), stats, true);
											return;
										}
								}
						}
					}				
				}
			}
			IBlockState iblock = e.getWorld().getBlockState(e.getPos());
			Block block = iblock.getBlock();
			String name = block.getRegistryName().getResourceDomain() + block.getRegistryName().getResourcePath();
			if (name.equals("psi:programmer")) {
				e.setCanceled(true);
			}

		}
	}
	
	@SubscribeEvent
	public void onContainerClose(PlayerContainerEvent.Close e) {
		/*if (e.getEntity() instanceof EntityPlayer) {
			if (Util.isOp(e.getEntityPlayer())) {
				return;
			}
			for (int i=0; i<e.getEntityPlayer().inventory.mainInventory.size(); i++) {
				ItemStack item = e.getEntityPlayer().inventory.mainInventory.get(i);
				if (item.getItem().getRegistryName().getResourceDomain().equals("psi")) {
					e.getEntityPlayer().inventory.mainInventory.set(i, ItemStack.EMPTY);
					EntityItem it = new EntityItem(e.getEntityPlayer().world, e.getEntityPlayer().posX, e.getEntityPlayer().posY, e.getEntityPlayer().posZ, item);
					it.setDefaultPickupDelay();
					e.getEntityPlayer().world.spawnEntity(it);
					Util.sendMessage(e.getEntityPlayer(), TextFormatting.RED+Util.translate("cantusepsi"));
				}
			}
		}*/
	}
	
	@SubscribeEvent
	public void onContainerOpen(PlayerContainerEvent.Open e) {
		/*if (e.getEntity() instanceof EntityPlayer) {
			if (Util.isOp(e.getEntityPlayer())) {
				return;
			}
			for (int i=0; i<e.getEntityPlayer().inventory.mainInventory.size(); i++) {
				ItemStack item = e.getEntityPlayer().inventory.mainInventory.get(i);
				if (item.getItem().getRegistryName().getResourceDomain().equals("psi")) {
					e.getEntityPlayer().inventory.mainInventory.set(i, ItemStack.EMPTY);
					EntityItem it = new EntityItem(e.getEntityPlayer().world, e.getEntityPlayer().posX, e.getEntityPlayer().posY, e.getEntityPlayer().posZ, item);
					it.setDefaultPickupDelay();
					e.getEntityPlayer().world.spawnEntity(it);
					Util.sendMessage(e.getEntityPlayer(), TextFormatting.RED+Util.translate("cantusepsi"));
				}
			}
		}*/
	}
	
	@SubscribeEvent
	public void onItemPickup(EntityItemPickupEvent e) {
		/*if (e.getItem().getEntityItem().getItem().getRegistryName().getResourceDomain().equals("psi")) {
			if (e.getEntity() instanceof EntityPlayer) {
				if (Util.isOp(e.getEntityPlayer())) {
					return;
				}
				PlayerStats stats = PlayerManager.get(e.getEntityPlayer());
				if (stats != null && stats.job != null) {
					if (stats.job.getName().equals(Developper.name())) {
						return;
					}
				}
				e.setCanceled(true);
			}
		}*/
	}
	
	@SubscribeEvent
	public void onBlockPlaced(BlockEvent.PlaceEvent event) {
		EntityPlayer player = event.getPlayer();
		IBlockState iblock = event.getPlacedBlock();
		if (event.getState() instanceof BlockEnderChest) {
			if (player.dimension == 42) {
				event.setCanceled(true);
				return;
			}
		}
		Block block = iblock.getBlock();
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
			String maDate = Util.getDate();
			BlockInfos info = new BlockInfos(EnumPlayerAction.PLACE,player.getDisplayNameString(), Block.getIdFromBlock(block), block.getMetaFromState(iblock), -1, 0, maDate);
			LogForJusticeManager.addLog(player.dimension, event.getPos(), info, true);
		}
		
		ResourceLocation loc = GameData.getBlockRegistry().getNameForObjectBypass(event.getState().getBlock());
		String id = loc.getResourceDomain() + ":" + loc.getResourcePath();
		int meta = block.getMetaFromState(event.getState());
		if (ItemBlockManager.removedRecipes.containsKey(id+"/"+meta) || ItemBlockManager.removedRecipes.containsKey(id+"/*")) {
			if (!Util.isOp(event.getPlayer())) {
				Util.sendMessage(event.getPlayer(), "Block disabled");
				event.setCanceled(true);
			} else {
				WorldManager.addArtificial(event.getWorld(), event.getPos());
			}
		} else {
			WorldManager.addArtificial(event.getWorld(), event.getPos());
		}
	}
	
	@SubscribeEvent
	public void onPlayerLoggedOut(PlayerLoggedOutEvent e) {
		PlayerManager.save(e.player);
		PlayerManager.remove(e.player);
		if (FMLCommonHandler.instance().getSide().isServer()) {
			for (String name : Main.DisabledItemsBlocks) {
				String data[] = name.split("[/]");
				if (data.length == 2) {
					try {
						String id = data[0];
						int meta = Integer.valueOf(data[1]);
						if (data[1].equals("*")) {
							meta = -1;
						}
						Main.instance.network.sendTo(new ItemBlockDisablePacket(id, meta, false), (EntityPlayerMP)e.player);
					} catch (Exception ex) {
						String id = data[0];
						int meta = 0;
						if (data[1].equals("*")) {
							meta = -1;
						}
						Main.instance.network.sendTo(new ItemBlockDisablePacket(id, meta, false), (EntityPlayerMP)e.player);
					}
					
				} else {
					Main.instance.network.sendTo(new ItemBlockDisablePacket(name, 0, false), (EntityPlayerMP)e.player);
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onPlayerLoggedIn(PlayerLoggedInEvent e) {
		 PlayerManager.add(e.player);
		if (FMLCommonHandler.instance().getSide().isServer()) {
			for (String name : Main.DisabledItemsBlocks) {
				String data[] = name.split("[/]");
				if (data.length == 2) {
					try {
						String id = data[0];
						int meta = Integer.valueOf(data[1]);
						if (data[1].equals("*")) {
							meta = -1;
						}
						Main.instance.network.sendTo(new ItemBlockDisablePacket(id, meta, true), (EntityPlayerMP)e.player);
					} catch (Exception ex) {
						String id = data[0];
						int meta = 0;
						if (data[1].equals("*")) {
							meta = -1;
						}
						Main.instance.network.sendTo(new ItemBlockDisablePacket(id, meta, true), (EntityPlayerMP)e.player);
					}
				} else {
					Main.instance.network.sendTo(new ItemBlockDisablePacket(name, 0, true), (EntityPlayerMP)e.player);
				}
			}
			PlayerManager.update(e.player);
		}
		if (FMLCommonHandler.instance().getSide().isClient()) {
			Main.instance.network.sendToServer(new StatsUpdatePacket(e.player.getDisplayNameString()));
		}
	}
	
	@SubscribeEvent
	public void onBlockBreak(BlockEvent.BreakEvent e)  {
		EntityPlayer player = e.getPlayer();
		IBlockState iblock = e.getState();
		Block block = iblock.getBlock();
		ItemStack hand = player.getHeldItemMainhand();
		Item ihand = hand.getItem();
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
		    String maDate = Util.getDate();
			BlockInfos info = new BlockInfos(EnumPlayerAction.BREAK,player.getDisplayNameString(), Block.getIdFromBlock(block), block.getMetaFromState(iblock), Item.getIdFromItem(ihand), ihand.getMetadata(hand), maDate);
			LogForJusticeManager.addLog(player.dimension, e.getPos(), info, true);
		}
		
		PlayerStats stats = PlayerManager.get(e.getPlayer());
		String name = iblock.getBlock().getRegistryName().getResourceDomain() + ":" + iblock.getBlock().getRegistryName().getResourcePath();
		int meta =iblock.getBlock().getMetaFromState(iblock);
		if (stats != null && stats.job != null) {
			if (stats.job.getName().equals(Miner.name())) {
				if (WorldManager.isNatural(e.getWorld(), e.getPos())) {
					if (iblock.getBlock() instanceof BlockStone) {
						stats.job.addXP(JobManager.MinerParam.stone);
						PlayerManager.update(e.getPlayer(), stats, true);
					}
					if (iblock.getBlock() instanceof BlockOre) {
						BlockOre b = (BlockOre) iblock.getBlock();
						if (name.equals("minecraft:coal_ore")) {
							stats.job.addXP(JobManager.MinerParam.coal);
						}
						if (name.equals("minecraft:iron_ore")) {
							stats.job.addXP(JobManager.MinerParam.iron);
						}
						if (name.equals("minecraft:gold_ore")) {
							stats.job.addXP(JobManager.MinerParam.gold);
						}
						if (name.equals("minecraft:diamond_ore")) {
							stats.job.addXP(JobManager.MinerParam.diamond);
						}
						if (name.equals("minecraft:emerald_ore")) {
							stats.job.addXP(JobManager.MinerParam.emerald);
						}
						if (name.equals("minecraft:quartz_ore")) {
							stats.job.addXP(JobManager.MinerParam.quartz);
						}
						PlayerManager.update(e.getPlayer(), stats, true);
					}
				}
			}
			if (stats.job.getName().equals(Woodcutter.name())) {
				if (WorldManager.isNatural(e.getWorld(), e.getPos())) {
					if (iblock.getBlock() instanceof BlockOldLog) {
						if (meta == 0) {
							stats.job.addXP(JobManager.WoodcutterParam.oak);
						}
						if (meta == 1) {
							stats.job.addXP(JobManager.WoodcutterParam.spruce);
						}
						if (meta == 2) {
							stats.job.addXP(JobManager.WoodcutterParam.birch);
						}
						if (meta == 3) {
							stats.job.addXP(JobManager.WoodcutterParam.jungle);
						}
						PlayerManager.update(e.getPlayer(), stats, true);
					}
					if (iblock.getBlock() instanceof BlockNewLog) {
						if (meta == 0) {
							stats.job.addXP(JobManager.WoodcutterParam.acacia);
						}
						if (meta == 1) {
							stats.job.addXP(JobManager.WoodcutterParam.darkoak);
						}
						PlayerManager.update(e.getPlayer(), stats, true);
					}
				}
			}
			if (stats.job.getName().equals(Farmer.name())) {
				if (name.equals("minecraft:wheat") && meta == 7) {
					stats.job.addXP(JobManager.FarmerParam.wheat);
				}
				if (name.equals("minecraft:beetroots") && meta == 3) {
					stats.job.addXP(JobManager.FarmerParam.beetroots);
				}
				if (name.equals("minecraft:carrots") && meta == 7) {
					stats.job.addXP(JobManager.FarmerParam.carrots);
				}
				if (name.equals("minecraft:potatoes") && meta == 7) {
					stats.job.addXP(JobManager.FarmerParam.potatoes);
				}
				if (name.equals("minecraft:melon_block")) {
					stats.job.addXP(JobManager.FarmerParam.melon);
				}
				if (name.equals("minecraft:pumpkin")) {
					stats.job.addXP(JobManager.FarmerParam.pumpkin);
				}
				PlayerManager.update(e.getPlayer(), stats, true);
			}
		}
		if (e.getPlayer().isCreative()) {
			WorldManager.removeArtificial(e.getWorld(), e.getPos());
		}
	}
	
	@SubscribeEvent
	public void onBlockStartBreak(PlayerEvent.BreakSpeed e) {
		PlayerStats stats = PlayerManager.get(e.getEntityPlayer());
		IBlockState iblock = e.getState();
		ItemStack hand = e.getEntityPlayer().getHeldItemMainhand();
		if (stats != null && stats.job != null) {
			if (stats.job.getName().equals(Miner.name())) {
				if (hand.getItem() instanceof ItemPickaxe) {
					if (ForgeHooks.isToolEffective(e.getEntityPlayer().world, e.getPos(), hand)) {
						e.setNewSpeed(e.getOriginalSpeed()*(stats.job.Ability1()+1));
					}
				}
			}
			if (stats.job.getName().equals(Woodcutter.name())) {
				if (hand.getItem() instanceof ItemAxe) {
					if (ForgeHooks.isToolEffective(e.getEntityPlayer().world, e.getPos(), hand)) {
						e.setNewSpeed(e.getOriginalSpeed()*(stats.job.Ability1()+1));
					}
				}
			}
		}
	}
	@SubscribeEvent
	public void onBlockHarvest(HarvestDropsEvent e) {
		if (!e.isSilkTouching()) {
			if (e.getHarvester() != null) {
				PlayerStats stats = PlayerManager.get(e.getHarvester());
				IBlockState iblock = e.getState();
				ItemStack hand = e.getHarvester().getHeldItemMainhand();
				if (stats != null && stats.job != null) {
					if (stats.job.getName().equals(Miner.name())) {
						if (hand.getItem() instanceof ItemPickaxe && e.getState().getBlock() instanceof BlockOre) {
							if (Util.isToolEffective(iblock, hand)) {
								if (WorldManager.isNatural(e.getWorld(), e.getPos())) {
									List<ItemStack> drops = e.getDrops();
									if (drops.size() > 0) {
										ItemStack stack = drops.get(0);
										if (stack != null && stack != ItemStack.EMPTY) {
											stack.setCount(stack.getCount() + (int)Math.ceil(Math.random()*stats.job.Ability2()));
											drops.set(0, stack);
										}
									}
								}
							}
						}
					}
					if (stats != null && stats.job.getName().equals(Woodcutter.name())) {
						if (hand.getItem() instanceof ItemAxe && e.getState().getBlock() instanceof BlockLog) {
							if (Util.isToolEffective(iblock, hand)) {
								if (WorldManager.isNatural(e.getWorld(), e.getPos())) {
									List<ItemStack> drops = e.getDrops();
									if (drops.size() > 0) {
										ItemStack stack = drops.get(0);
										if (stack != null && stack != ItemStack.EMPTY) {
											stack.setCount(stack.getCount() + (int)Math.ceil(Math.random()*stats.job.Ability2()));
											drops.set(0, stack);
										}
									}
								}
							}
						}
					}
					if (stats != null && stats.job.getName().equals(Farmer.name())) {
						if (e.getState().getBlock() instanceof BlockMelon || e.getState().getBlock() instanceof BlockPumpkin || e.getState().getBlock() instanceof IGrowable) {	
							List<ItemStack> drops = e.getDrops();
							if (drops.size() > 0) {
								ItemStack stack = drops.get(0);
								if (stack != null && stack != ItemStack.EMPTY) {
									stack.setCount(stack.getCount() + (int)Math.ceil(Math.random()*stats.job.Ability2()));
									drops.set(0, stack);
								}		
							}
						}
					}
				}
			}
		}
		WorldManager.removeArtificial(e.getWorld(), e.getPos());
	}
	
	@SubscribeEvent
	public void onLoot(LootingLevelEvent e) {
		Entity ent = e.getDamageSource().getEntity();
		if (ent instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer)ent;
			ItemStack hand = player.getHeldItemMainhand();
			PlayerStats stats = PlayerManager.get(player);
			if (stats != null && stats.job != null && hand != null) {
				if (stats.job.getName().equals(Hunter.name())) {
					if (hand.getItem() instanceof ItemSword || hand.getItem() instanceof ItemBow) {
						e.setLootingLevel(e.getLootingLevel()+stats.job.Ability2());
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onEntityDeath(LivingDeathEvent e) {
		Entity ent = e.getSource().getEntity();
		if (ent instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer)ent;
			ItemStack hand = player.getHeldItemMainhand();
			PlayerStats stats = PlayerManager.get(player);
			if (stats != null && stats.job != null && hand != null) {
				if (stats.job.getName().equals(Hunter.name())) {
					if (hand.getItem() instanceof ItemSword || hand.getItem() instanceof ItemBow) {
						if (e.getEntityLiving() instanceof EntityWither) {
							stats.job.addXP(JobManager.HunterParam.wither);
						}
						if (e.getEntityLiving() instanceof EntityEnderman) {
							stats.job.addXP(JobManager.HunterParam.enderman);
						}
						if (e.getEntityLiving() instanceof EntityCreeper) {
							stats.job.addXP(JobManager.HunterParam.creeper);
						}
						if (e.getEntityLiving() instanceof EntityZombie) {
							stats.job.addXP(JobManager.HunterParam.zombie);
						}
						if (e.getEntityLiving() instanceof EntitySkeleton) {
							stats.job.addXP(JobManager.HunterParam.skeleton);
						}
						if (e.getEntityLiving() instanceof EntityShulker) {
							stats.job.addXP(JobManager.HunterParam.shulker);
						}
						if (e.getEntityLiving() instanceof EntityGhast) {
							stats.job.addXP(JobManager.HunterParam.ghast);
						}
						if (e.getEntityLiving() instanceof EntityZombieVillager) {
							stats.job.addXP(JobManager.HunterParam.zombievillager);
						}
						if (e.getEntityLiving() instanceof EntityPigZombie) {
							stats.job.addXP(JobManager.HunterParam.pigzombie);
						}
						if (e.getEntityLiving() instanceof EntityDragon) {
							stats.job.addXP(JobManager.HunterParam.enderdragon);
						}
						if (e.getEntityLiving() instanceof EntitySpider) {
							stats.job.addXP(JobManager.HunterParam.spider);
						}
						if (e.getEntityLiving() instanceof EntityCaveSpider) {
							stats.job.addXP(JobManager.HunterParam.cavespider);
						}
						if (e.getEntityLiving() instanceof EntityWitch) {
							stats.job.addXP(JobManager.HunterParam.witch);
						}
						if (e.getEntityLiving() instanceof EntityBlaze) {
							stats.job.addXP(JobManager.HunterParam.blaze);
						}
						if (e.getEntityLiving() instanceof EntityAnimal) {
							stats.job.addXP(JobManager.HunterParam.animals);
						}
						PlayerManager.update(player, stats, true);
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onEntityHurt(LivingHurtEvent e) {
		boolean hurt = AreaManager.flagDefault.get("pvp");
		int x = Integer.MIN_VALUE;
		int y = Integer.MIN_VALUE;
		int z = Integer.MIN_VALUE;
		int dx = Integer.MAX_VALUE;
		int dy = Integer.MAX_VALUE;
		int dz = Integer.MAX_VALUE;
		
		Entity ent = e.getSource().getEntity();
		if (e.getEntity() instanceof EntityPlayer && ent instanceof EntityPlayer) {
			for (String areaname : AreaManager.Areas.keySet()) {
				Area area = AreaManager.Areas.get(areaname);
				if (area.isIn(e.getEntity())) {
					if (area.x>=x && area.y>=y && area.z>=z && area.dx+1<=dx && area.dy<=dy && area.dz+1<=dz) {
						x = area.x;
						y = area.y;
						z = area.z;
						dx = area.dx;
						dy = area.dy;
						dz = area.dz;
						hurt = area.flags.get("pvp");
					}
					
				}
			}
			if (!hurt) {
				e.setCanceled(true);
				return;
			}			
		}
		if (ent instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer)ent;
			ItemStack hand = player.getHeldItemMainhand();
			PlayerStats stats = PlayerManager.get(player);
			if (stats != null && stats.job != null && hand != null) {
				if (stats.job.getName().equals(Hunter.name())) {
					if (hand.getItem() instanceof ItemSword || hand.getItem() instanceof ItemBow) {
						e.setAmount(e.getAmount()+stats.job.Ability1());
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onEntityJoin(EntityJoinWorldEvent event) {
		boolean result = false;
		int x = Integer.MIN_VALUE;
		int y = Integer.MIN_VALUE;
		int z = Integer.MIN_VALUE;
		int dx = Integer.MAX_VALUE;
		int dy = Integer.MAX_VALUE;
		int dz = Integer.MAX_VALUE;
		for (String name : AreaManager.Areas.keySet()) {
			String EntityName = EntityList.getEntityString(event.getEntity());
			Area area = AreaManager.Areas.get(name);
			if (area.isIn(event.getEntity())) {
				if (area.x>=x && area.y>=y && area.z>=z && area.dx+1<=dx && area.dy<=dy && area.dz+1<=dz) {
					x = area.x;
					y = area.y;
					z = area.z;
					dx = area.dx;
					dy = area.dy;
					dz = area.dz;
					if (event.getEntity() instanceof EntityLiving) {	
						String[] entity;
						if (area.flags.get("mode_whitelist")) {
							entity = area.whitelist;
						} else {
							entity = area.blacklist;
						}
						boolean find = false;
						for (int i=0; i<entity.length; i++) {
							if (entity[i].equals(EntityName)) {
								find = true;
								break;
							}
						}
						if (area.flags.get("mode_whitelist")) {
							if (!find) {
								result = true;
							} else {
								result = false;
							}
						} else {
							if (find) {
								result = true;
							} else {
								result = false;
							}
						}
					}	
				}
			}
		}
		event.setCanceled(result);
	}
	
	@SubscribeEvent
	public void WorldSave(WorldEvent.Save e) {
		WorldManager.saveArtificial();
		//LogForJusticeManager.saveLFJ();
    	for (String key : PlayerManager.PlayerStatsRegistry.keySet()) {
    		PlayerStats stats = PlayerManager.PlayerStatsRegistry.get(key);
    		stats.save();
    	}
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void OnGuiOpen(GuiOpenEvent e){
		EntityPlayerSP player = Minecraft.getMinecraft().player;
		if (player != null && player.dimension != 42) {
			if (e.getGui() != null) {
				if (e.getGui().getClass().getName().contains("vazkii.psi")) {
					PlayerStats stats = PlayerManager.get(Minecraft.getMinecraft().player);
					if (e.getGui().getClass().getSimpleName().equals("GuiProgrammer")) {
						if (stats != null && stats.job != null && stats.job.getName().equals(Developper.name())) {
							return;
						} else {
							e.setGui(null);
							e.setCanceled(true);
						}
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void PlayerSaveToFile(PlayerEvent.SaveToFile e) {
		PlayerManager.save(e.getEntityPlayer());
	}

	
	@SubscribeEvent
	public void onDimChange(net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent e) {
		System.out.println(e.fromDim + " " + e.toDim);
		if (e.toDim == 42) {
			
			Main.overall.addPlayer(e.player);
			e.player.inventory.clear();
			PlayerData dt = Main.lpe.getPlayer(e.player);
			if (dt != null) {
				dt.toPlayer(e.player);
			}
			e.player.inventory.inventoryChanged=true;
			e.player.setGameType(GameType.CREATIVE);
			
		} else if (e.fromDim == 42) {
			Main.lpe.addPlayer(e.player);
			e.player.inventory.clear();
			PlayerData dt = Main.overall.getPlayer(e.player);
			if (dt != null) {
				dt.toPlayer(e.player);
			}
			e.player.inventory.inventoryChanged=true;
		}
	}
	
	@SubscribeEvent
	public void onCommandEvent(CommandEvent e) {
		String cmd = "<"+e.getSender().getName()+"> /"+e.getCommand().getName() + " " + String.join(" ", e.getParameters());
		for (EntityPlayer pl : CommandSpyManager.spy.keySet()) {
			if (CommandSpyManager.spy.get(pl).booleanValue()) {
				TextComponentString txt = new TextComponentString(TextFormatting.GOLD+"[CMD] "+cmd);
				pl.sendMessage(txt);
			}
		}
	}
	
	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent e) {
		ItemStack itemChest = e.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
		if (itemChest != ItemStack.EMPTY && (itemChest.getItem() == Items.ELYTRA || itemChest.getItem().getRegistryName().getResourceDomain().equals("psi"))) {
			itemChest.setItemDamage(0);
		}
		ItemStack itemFeet = e.player.getItemStackFromSlot(EntityEquipmentSlot.FEET);
		if (itemFeet != ItemStack.EMPTY && itemFeet.getItem().getRegistryName().getResourceDomain().equals("psi")) {
			itemFeet.setItemDamage(0);
		}
		ItemStack itemHead = e.player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
		if (itemHead != ItemStack.EMPTY && itemHead.getItem().getRegistryName().getResourceDomain().equals("psi")) {
			itemHead.setItemDamage(0);
		}
		ItemStack itemLegs = e.player.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
		if (itemLegs != ItemStack.EMPTY && itemLegs.getItem().getRegistryName().getResourceDomain().equals("psi")) {
			itemLegs.setItemDamage(0);
		}
		ItemStack itemMain = e.player.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
		if (itemMain != ItemStack.EMPTY && itemMain.getItem().getRegistryName().getResourceDomain().equals("psi")) {
			if (itemMain.getItem().isRepairable() && itemMain.isItemDamaged()) {
				itemMain.setItemDamage(0);
			}
		}
	}
	
	@SubscribeEvent
	public void onChunkLoad(ChunkEvent.Load e) {
		if (ChunkManager.check) {
			if (!ChunkManager.analysed.contains(e.getChunk())) {
				int xs = e.getChunk().xPosition*16;
				int zs = e.getChunk().zPosition*16;	
				for (int x=xs; x<xs+16; x++) {
					for (int z=zs; z<zs+16; z++) {
						for (int y=0; y<Util.maxHeight(e.getWorld(), x, z); y++) {
							BlockPos pos = new BlockPos(x,y,z);
							IBlockState iblock = e.getWorld().getBlockState(pos);
							ResourceLocation loc = GameData.getBlockRegistry().getNameForObjectBypass(iblock.getBlock());
							String id = loc.getResourceDomain() + ":" + loc.getResourcePath()+"/"+iblock.getBlock().getMetaFromState(iblock);
							if (ChunkManager.block2remove.contains(id)) {
								e.getWorld().setBlockToAir(pos);
							}
						}
					}
				}
				System.out.println("Clear chunk ["+e.getChunk().xPosition+", "+e.getChunk().zPosition+"]");
				ChunkManager.analysed.add(e.getChunk());
			}
		}
		
		
		
	}
}
