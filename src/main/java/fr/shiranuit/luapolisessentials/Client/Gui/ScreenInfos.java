package fr.shiranuit.luapolisessentials.Client.Gui;

import java.awt.Color;
import java.awt.GradientPaint;
import java.io.IOException;
import java.math.BigDecimal;

import org.lwjgl.opengl.GL11;

import fr.shiranuit.luapolisessentials.Reference;
import fr.shiranuit.luapolisessentials.Jobs.Job;
import fr.shiranuit.luapolisessentials.Manager.PlayerManager;
import fr.shiranuit.luapolisessentials.Network.Packets.BroadcastPacket;
import fr.shiranuit.luapolisessentials.Network.Packets.MessagePacket;
import fr.shiranuit.luapolisessentials.Stats.PlayerStats;
import fr.shiranuit.luapolisessentials.Utils.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.entity.RenderEntity;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.init.MobEffects;
import net.minecraft.init.PotionTypes;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.client.resources.I18n;

import org.lwjgl.LWJGLUtil;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
@SideOnly(Side.CLIENT)
public class ScreenInfos extends GuiIngameForge  {
	
	private static MessagePacket message = null;
	private static long broadcastTime = 0;
	private static BroadcastPacket broadcast = null; 
	private static long broadcastReception = 0;
	private static long messageTime = 0;
	private static long messageReception = 0;
	private static final ResourceLocation lettre = new ResourceLocation(Reference.ModID, "textures/gui/lettre.png");
	private static Minecraft mc;
	public ScreenInfos(Minecraft mc) {
		super(mc);
		ScreenInfos.mc = mc;
		//GuiIngameForge.renderExperiance = false;
		
	}
	private int barW = 81, barH = 9;
	private int innerBarW = barW - 2;
	int bottomScreen, halfHeight, rightScreen, halfWidth = 0;
	@SubscribeEvent
	public void onRenderOverlay(RenderGameOverlayEvent event)
	{
		
		ScaledResolution res = event.getResolution();
		bottomScreen = res.getScaledHeight();
		halfHeight = bottomScreen / 2;
		rightScreen = res.getScaledWidth();
		halfWidth = rightScreen / 2;
		if (event.getType() == ElementType.HEALTH ) {
			event.setCanceled(true);


			PotionEffect effect = mc.player.getActivePotionEffect(MobEffects.REGENERATION);
			int regeneration = 1;
			if (effect != null) {
				regeneration = effect.getAmplifier();
			}
			int currentbarwidth = 0;
			int xPos = halfWidth - 10 - this.barW;
			int yPos = bottomScreen - 22 - ((this.barH * 2) - 1);
			PlayerStats stats = PlayerManager.get(mc.player);
			if (stats != null) {
				drawHealthBar(xPos, yPos, 81, 9, mc.player.getHealth(), mc.player.getMaxHealth(), mc.player.getAbsorptionAmount(), stats.godmode, regeneration);
			} else {
				drawHealthBar(xPos, yPos, 81, 9, mc.player.getHealth(), mc.player.getMaxHealth(), mc.player.getAbsorptionAmount(), mc.player.getIsInvulnerable(), regeneration);
			}
			
		}
		if (event.getType() == ElementType.HOTBAR) {
			PlayerStats stats = PlayerManager.get(mc.player);
			if (stats != null && stats.job != null) {
				int w = 200;
				int h = 100;
				float sw = res.getScaledWidth() / 960f;
				float sh = res.getScaledHeight() / 509f;
				
				GlStateManager.pushMatrix();
				GL11.glScalef(sw, sh, 0f);
				drawJob((int)(rightScreen * (1f/(sw)) - w),(int)((bottomScreen) * (1f/(sh)) - h), w, h, stats.job);
				GlStateManager.popMatrix();
				mc.getTextureManager().bindTexture(ICONS);
			}
			if (message != null) {
				drawMessage(0,0,250,110, message.message, message.source);
			}
			if (broadcast != null) {
				drawBroadcast(rightScreen-250,0,250,110, broadcast.message);
			}
		}

		
	}
	
	public static void newBroadcast(BroadcastPacket msg) {
		broadcastTime = 0;
		broadcastReception = System.currentTimeMillis();
		broadcast = msg;
	}
	
	public static void newMessage(MessagePacket msg) {
		messageTime = 0;
		messageReception = System.currentTimeMillis();
		message = msg;
	}
	
	public float map(float value, float istart, float istop, float ostart, float ostop) {
	      return ostart + (ostop - ostart) * ((value - istart) / (istop - istart));
	}
	
	public void drawBroadcast(int x, int y, int width, int height, String message) {
		broadcastTime = System.currentTimeMillis();
		int mx = (int)map(Mouse.getX(), 0, Minecraft.getMinecraft().displayWidth, 0, rightScreen);
		int my = (int)map(Mouse.getY(), 0, Minecraft.getMinecraft().displayHeight, bottomScreen, 0);
		if (mx>=x+1 && mx <= x+1+mc.fontRendererObj.getStringWidth("X") && my >= y+1 && my <= y+1+mc.fontRendererObj.FONT_HEIGHT && Mouse.isButtonDown(0)) {
			this.broadcast=null;
			return;
		}
		if (MathHelper.absFloor((broadcastReception-broadcastTime)/1000) > 60) {
			this.broadcast=null;
			return;
		}
		drawRectangle(x, y, width, height, new Color(35,35,35,125).getRGB());
		drawRectangle(x, y, width, mc.fontRendererObj.FONT_HEIGHT+2, new Color(35,35,35,125).getRGB());
		drawRectangle(x+32+8, y+mc.fontRendererObj.FONT_HEIGHT*3, width-48, height-(y+mc.fontRendererObj.FONT_HEIGHT*3+8), new Color(35,35,35,125).getRGB());
		
			mc.fontRendererObj.drawString("X", x+1, y+1, Color.RED.getRGB());
			String text = "Annonce Serveur";
			mc.fontRendererObj.drawString(text, x+width/2-mc.fontRendererObj.getStringWidth(text)/2, y+1, Color.GREEN.getRGB());
			String time = ""+(60-MathHelper.absFloor((broadcastReception-broadcastTime)/1000));
			mc.fontRendererObj.drawString(time, x+width-mc.fontRendererObj.getStringWidth(time), y+1, Color.GREEN.getRGB());
			

		mc.fontRendererObj.drawString("Message : ", x+40, y+mc.fontRendererObj.FONT_HEIGHT+5, Color.red.darker().getRGB());
		mc.fontRendererObj.drawSplitString(message, x+40, y+mc.fontRendererObj.FONT_HEIGHT*3, width - 48, Color.white.getRGB());
		drawTexture(lettre , x+4, y+mc.fontRendererObj.FONT_HEIGHT+5+16, 32, 32);
		
		mc.getTextureManager().bindTexture(ICONS);

	}
	
	public void drawMessage(int x, int y, int width, int height, String message, String source) {
		messageTime = System.currentTimeMillis();
		int mx = (int)map(Mouse.getX(), 0, Minecraft.getMinecraft().displayWidth, 0, rightScreen);
		int my = (int)map(Mouse.getY(), 0, Minecraft.getMinecraft().displayHeight, bottomScreen, 0);
		if (mx>=x+1 && mx <= x+1+mc.fontRendererObj.getStringWidth("X") && my >= y+1 && my <= y+1+mc.fontRendererObj.FONT_HEIGHT && Mouse.isButtonDown(0)) {
			this.message=null;
			return;
		}
		if (MathHelper.absFloor((messageReception-messageTime)/1000) > 60) {
			this.message=null;
			return;
		}
		drawRectangle(x, y, width, height, new Color(35,35,35,125).getRGB());
		drawRectangle(x, y, width, mc.fontRendererObj.FONT_HEIGHT+2, new Color(35,35,35,125).getRGB());
		drawRectangle(x+32+8, y+mc.fontRendererObj.FONT_HEIGHT*3, width-48, height-(y+mc.fontRendererObj.FONT_HEIGHT*3+8), new Color(35,35,35,125).getRGB());
			
			mc.fontRendererObj.drawString("X", x+1, y+1, Color.RED.getRGB());
			String text = "Nouveau message de "+source;
			mc.fontRendererObj.drawString(text, x+width/2-mc.fontRendererObj.getStringWidth(text)/2, y+1, Color.ORANGE.getRGB());
			String time = ""+(60-MathHelper.absFloor((messageReception-messageTime)/1000));
			mc.fontRendererObj.drawString(time, x+width-mc.fontRendererObj.getStringWidth(time), y+1, Color.ORANGE.getRGB());
		

		mc.fontRendererObj.drawString("Message : ", x+40, y+mc.fontRendererObj.FONT_HEIGHT+5, Color.red.darker().getRGB());
		mc.fontRendererObj.drawSplitString(message, x+40, y+mc.fontRendererObj.FONT_HEIGHT*3, width - 48, Color.white.getRGB());
		drawTexture(lettre , x+4, y+mc.fontRendererObj.FONT_HEIGHT+5+16, 32, 32);
		
		mc.getTextureManager().bindTexture(ICONS);

	}
	
	public static void drawJob(int x, int y, int width, int height, Job job) {
		drawRectangle(x, y, width, height, new Color(35,35,35,125).getRGB());
		drawRectangle(x, y, width, mc.fontRendererObj.FONT_HEIGHT+2, new Color(35,35,35,125).getRGB());
		mc.fontRendererObj.drawString(Util.translate("job."+job.getName()), x+width/2-mc.fontRendererObj.getStringWidth(job.getName())/2, y+1, Color.white.getRGB());
		String ability1 = Util.translate("job.ability."+job.AbilityName1()) + " :";
		String ability2 = Util.translate("job.ability."+job.AbilityName2()) + " :";
		String lvl = Util.translate("job.level")+" : ";
		String xp = Util.translate("job.xp")+" : ";
		String abilityMax = ability1;
		if (abilityMax.length() < ability2.length()) {
			abilityMax = ability2;
		}
		if (abilityMax.length() < lvl.length()) {
			abilityMax = lvl;
		}
		if (abilityMax.length() < xp.length()) {
			abilityMax = xp;
		}
		mc.fontRendererObj.drawString(ability1, x+10+mc.fontRendererObj.getStringWidth(abilityMax)-mc.fontRendererObj.getStringWidth(ability1), y+1+mc.fontRendererObj.FONT_HEIGHT*2, Color.white.getRGB());
		drawProgressBar(x+mc.fontRendererObj.getStringWidth(abilityMax)+15, y+mc.fontRendererObj.FONT_HEIGHT*2, width-mc.fontRendererObj.getStringWidth(abilityMax)-25, 9, job.Ability1(), job.AbilityMax1());
				
		mc.fontRendererObj.drawString(ability2, x+10+mc.fontRendererObj.getStringWidth(abilityMax)-mc.fontRendererObj.getStringWidth(ability2), y+1+mc.fontRendererObj.FONT_HEIGHT*4, Color.white.getRGB());
		drawProgressBar(x+mc.fontRendererObj.getStringWidth(abilityMax)+15, y+mc.fontRendererObj.FONT_HEIGHT*4, width-mc.fontRendererObj.getStringWidth(abilityMax)-25, 9, job.Ability2(), job.AbilityMax2());
		
		mc.fontRendererObj.drawString(lvl, x+10+mc.fontRendererObj.getStringWidth(abilityMax)-mc.fontRendererObj.getStringWidth(lvl), y+1+mc.fontRendererObj.FONT_HEIGHT*6, Color.white.getRGB());
		drawProgressBar(x+mc.fontRendererObj.getStringWidth(abilityMax)+15, y+mc.fontRendererObj.FONT_HEIGHT*6, width-mc.fontRendererObj.getStringWidth(abilityMax)-25, 9, job.lvl(), job. maxLvl());
		
		mc.fontRendererObj.drawString(xp, x+10+mc.fontRendererObj.getStringWidth(abilityMax)-mc.fontRendererObj.getStringWidth(xp), y+1+mc.fontRendererObj.FONT_HEIGHT*8, Color.white.getRGB());
		drawProgressBar(x+mc.fontRendererObj.getStringWidth(abilityMax)+15, y+mc.fontRendererObj.FONT_HEIGHT*8, width-mc.fontRendererObj.getStringWidth(abilityMax)-25, 9, job.xp(), job.nextLvl());
		
		mc.getTextureManager().bindTexture(ICONS);
	}
	
	
	
	public static void drawRectangle(int x, int y, int width, int height, int color) {
		drawRect(x, y, x+width, y+height, color);
	}
	
	public static void drawProgressBar(int x, int y, int width, int height, float value, float maxValue) {
		drawRect(x, y, x+width, y+height, new Color(35,35,35).getRGB());
		double percent = 0f;
		if (value == maxValue) {
			percent = 1f;
		} else {
			percent = value/maxValue;
		}
		int currentbarwidth = MathHelper.clamp((int) (percent * (width+1)), 0, width+1);
		drawRect(x, y, x+currentbarwidth, y+height, new Color(55,85,255).getRGB());
		String txt = round(value,2) + " / " + round(maxValue,2);
		mc.fontRendererObj.drawString(txt, x+width/2-mc.fontRendererObj.getStringWidth(txt)/2, y+1, Color.white.getRGB());
	}
	
	  public static float round(float d, int decimalPlace) {
	        BigDecimal bd = new BigDecimal(Float.toString(d));
	        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
	        return bd.floatValue();
	    }
	
	public static void drawHealthBar(int x, int y, int width, int height, float hp, float maxhp, float absorption, boolean godmode, int regeneration) {
		drawRect(x, y, x+width, y+height, new Color(35,35,35).getRGB());
		int currentbarwidth = MathHelper.clamp((int) ((hp / maxhp) * (width+1)), 0, width+1);
		double percent = hp/maxhp;
		int red = MathHelper.clamp((int)(255*(1-percent)), 0, 255);
		int green = MathHelper.clamp((int)(255*percent), 0, 255);
		if (godmode) {
			drawRect(x, y, x+currentbarwidth, y+height, new Color(100,0,100).getRGB());
		} else {
			drawRect(x, y, x+currentbarwidth, y+height, new Color(red, green, 0).getRGB());
		}
	
		String txt = round(hp,2)+round(absorption,2) + " / " + round(maxhp,2);
		if (godmode) {
			txt="Immortal Object";
		}
		String regentxt = "+"+regeneration;

			if (absorption > 0) {
				
				mc.fontRendererObj.drawString(txt, x+width/2-mc.fontRendererObj.getStringWidth(txt)/2, y+1, Color.orange.getRGB());
				if (!godmode)
				drawString(regentxt, x+width-mc.fontRendererObj.getStringWidth(regentxt)*0.75, y+height-mc.fontRendererObj.FONT_HEIGHT*0.75, 0.75f, Color.orange.getRGB());
			} else {
				mc.fontRendererObj.drawString(txt, x+width/2-mc.fontRendererObj.getStringWidth(txt)/2, y+1, Color.white.getRGB());
				if (!godmode)
				drawString(regentxt, x+width-mc.fontRendererObj.getStringWidth(regentxt)*0.75, y+height-mc.fontRendererObj.FONT_HEIGHT*0.75, 0.75f, Color.white.getRGB());
			}
		
		mc.getTextureManager().bindTexture(ICONS);
	}
	 
	public void drawTexture(ResourceLocation tex, int x, int y, int width, int height) {
		mc.getTextureManager().bindTexture(tex);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(255f, 255f, 255f, 255f);
		drawModalRectWithCustomSizedTexture(x, y, 0, 0, width, height, width, height);	
		
		mc.getTextureManager().bindTexture(ICONS);
	}
	
	public static void drawString(String txt, double x, double y, float size, int color) {
		float multiplier = 1f/size;
		GL11.glScalef(size, size, size);
		mc.fontRendererObj.drawString(txt, (int)(x*multiplier), (int)(y*multiplier), color);
		GL11.glScalef(multiplier, multiplier, multiplier);
	}
	
}
