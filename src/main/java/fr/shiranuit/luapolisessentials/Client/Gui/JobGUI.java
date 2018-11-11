package fr.shiranuit.luapolisessentials.Client.Gui;

import java.awt.Color;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.lwjgl.opengl.GL11;

import fr.shiranuit.luapolisessentials.Main;
import fr.shiranuit.luapolisessentials.Jobs.Job;
import fr.shiranuit.luapolisessentials.Manager.JobManager;
import fr.shiranuit.luapolisessentials.Manager.PlayerManager;
import fr.shiranuit.luapolisessentials.Network.Packets.NewJobPacket;
import fr.shiranuit.luapolisessentials.Network.Packets.UpdateAbilityPacket;
import fr.shiranuit.luapolisessentials.Stats.PlayerStats;
import fr.shiranuit.luapolisessentials.Utils.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class JobGUI extends GuiScreen {
	Job job;
	Job backupjob;
	int points = 0;
	int a1add = 0;
	int a2add = 0;
	public static Minecraft mc  = Minecraft.getMinecraft();
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        
        for (int i = 0; i < buttonList.size(); i++) {
        	if (buttonList.get(i) instanceof GuiButton) {
        		GuiButton btn = (GuiButton) buttonList.get(i);
        		if (btn.isMouseOver()) {
        			String[] desc = null;
        			switch (btn.id) {
						case 0: {
							desc = new String[] { Util.translate("job."+(job!=null?job.getName():"none")+".desc") };
							break;
						}
						default:
							break;
        			}
        			if (desc != null) {
	                    List temp = Arrays.asList(desc);
	                    drawHoveringText(temp, mouseX, mouseY, fontRendererObj);
        			}
        		}
        	}
        }
        
        
        drawCenteredString(mc.fontRendererObj, Util.translate("job.ability."+(job!=null?job.AbilityName1():"??")), width/2, height/4+30, new Color(255,255,255).getRGB());
        drawCenteredString(mc.fontRendererObj, Util.translate("job.ability."+(job!=null?job.AbilityName2():"??")), width/2, height/4+70, new Color(255,255,255).getRGB());
        drawString(Util.translate("job.points") + " : " + points, width/2-mx/2-20, height/4+110, 1, new Color(255,255,255).getRGB());
        if (job != null) {
        	drawString(Util.translate("job.lvlrequired")+" : " + job.AbilityMin1(), bjoba1p.xPosition + bjoba1p.width+10, bjoba1p.yPosition + bjoba1p.height / 2 - mc.fontRendererObj.FONT_HEIGHT/2, 1, new Color(255,255,255).getRGB());
        	drawString(Util.translate("job.lvlrequired")+" : " + job.AbilityMin2(), bjoba2p.xPosition + bjoba2p.width+10, bjoba2p.yPosition + bjoba2p.height / 2 - mc.fontRendererObj.FONT_HEIGHT/2, 1, new Color(255,255,255).getRGB());
        }
        if (job != null) {
        	drawProgressBar(width/2-mx/2, height/4+40, mx, 20, job.Ability1(), job.AbilityMax1());
        	drawProgressBar(width/2-mx/2, height/4+80, mx, 20, job.Ability2(), job.AbilityMax2());
        } else {
        	drawProgressBar(width/2-mx/2, height/4+40, mx, 20, 0, 1);
        	drawProgressBar(width/2-mx/2, height/4+80, mx, 20, 0, 1);
        }
    }
    @Override
    protected void actionPerformed(GuiButton button) 
    {
    	if (button.id == bljob.id) {
    		String j = job!=null?job.getName():"none";
    		Object[] jobs = JobManager.jobsnameselectable.keySet().toArray();
    		int index = 0;
    		for (int i=0; i<jobs.length; i++) {
    			if (jobs[i].toString().equals(j)) {
    				index = i;
    				break;
    			}
    		}
    		
    		String njob = jobs[((index-1)+jobs.length) % jobs.length].toString();
    		bjob.displayString=Util.translate("job."+njob);
    		points = 0;
    		a1add=0;
    		a2add=0;
    			
    		if (njob.equals(backupjob!=null?backupjob.getName():"none")) {
    			job = backupjob;
    			points = job.points();
    			Confirm.enabled=false;
    		} else {
    			job = JobManager.JobFromName(njob, 0, 0, 0, 0, 0);
    			Confirm.enabled=true;
    		}
    		bjoba1m.enabled=false;
    		bjoba2m.enabled=false;
            if (job != null) {
    	        bjoba1p.enabled=(job.lvl() >= job.AbilityMin1());
    	        bjoba2p.enabled=(job.lvl() >= job.AbilityMin2());
            } else {
            	bjoba1p.enabled=false;
            	bjoba2p.enabled=false;
            }
    	}
    	if (button.id == brjob.id) {
    		String j = job!=null?job.getName():"none";
    		Object[] jobs = JobManager.jobsnameselectable.keySet().toArray();
    		int index = 0;
    		for (int i=0; i<jobs.length; i++) {
    			if (jobs[i].toString().equals(j)) {
    				index = i;
    				break;
    			}
    		}
    		
    		String njob = jobs[((index+1)+jobs.length) % jobs.length].toString();
    		bjob.displayString=Util.translate("job."+njob);
    		points = 0;
    		a1add=0;
    		a2add=0;
    		if (njob.equals(backupjob!=null?backupjob.getName():"none")) {
    			job = backupjob;
    			points = job.points();
    			Confirm.enabled=false;
    		} else {
    			Confirm.enabled=true;
    			job = JobManager.JobFromName(njob, 0, 0, 0, 0, 0);
    		}
    		bjoba1m.enabled=false;
    		bjoba2m.enabled=false;
            if (job != null) {
    	        bjoba1p.enabled=(job.lvl() >= job.AbilityMin1());
    	        bjoba2p.enabled=(job.lvl() >= job.AbilityMin2());
            } else {
            	bjoba1p.enabled=false;
            	bjoba2p.enabled=false;
            }
    	}
    	if (button.id == bjoba1p.id) {
    		if (job != null) {
	    		if (points-1>=0 && job.Ability1()+1 <= job.AbilityMax1() && job.lvl() >= job.AbilityMin1() ) {
	    			points--;
	    			a1add++;
	    			job = JobManager.JobFromName(job.getName(), job.lvl(), job.xp(), points, (byte)(job.Ability1()+1), job.Ability2());
	    			bjoba1m.enabled=true;
	    			Confirm.enabled=true;
	    		}
    		}
    	}
    	if (button.id == bjoba2p.id) {
    		if (job != null) {
	    		if (points-1>=0 && job.Ability2()+1 <= job.AbilityMax2() && job.lvl() >= job.AbilityMin2()) {
	    			points--;
	    			a2add++;
	    			job = JobManager.JobFromName(job.getName(), job.lvl(), job.xp(), points, job.Ability1(), (byte)(job.Ability2()+1));
	    			bjoba2m.enabled=true;
	    			Confirm.enabled=true;
	    		}
    		}
    	}
    	if (button.id == bjoba1m.id) {
    		if (a1add-1>=0) {
    			if (job != null) {
	    			points++;
	    			a1add--;
	    			job = JobManager.JobFromName(job.getName(), job.lvl(), job.xp(), points, (byte)(job.Ability1()-1), job.Ability2());
	    			if (a1add<=0) { 
	    				bjoba1m.enabled=false;
	    			}
	    			if (a1add<=0 && a2add <= 0) {
	    				Confirm.enabled=false;
	    			}
    			}
    		}
    	}
    	if (button.id == bjoba2m.id) {
    		if (a2add-1>=0) {
    			if (job != null) {
	    			points++;
	    			a2add--;
	    			job = JobManager.JobFromName(job.getName(), job.lvl(), job.xp(), points, job.Ability1(),  (byte)(job.Ability2()-1));
	    			if (a2add<=0) { 
	    				bjoba2m.enabled=false;
	    			}
	    			if (a1add<=0 && a2add <= 0) {
	    				Confirm.enabled=false;
	    			}
    			}
    		}
    	}
    	if (button.id == Confirm.id) {
    		if (job != null) {
	    		if (job.getName().equals(backupjob!=null?backupjob.getName():"none")) {
	    			Main.network.sendToServer(new UpdateAbilityPacket(mc.player.getDisplayNameString(), a1add, a2add));
	    		} else {
	    			Main.network.sendToServer(new NewJobPacket(mc.player.getDisplayNameString(), job.getName()));
	    		}
	    		mc.displayGuiScreen(null);
    		}
    	}
    	if (button.id == Cancel.id) {
    		mc.displayGuiScreen(null);
    	}
    }
    
    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
    
    GuiButton bjob;
    GuiButton bljob;
    GuiButton brjob;
    GuiButton bjoba1p;
    GuiButton bjoba1m;
    GuiButton bjoba2p;
    GuiButton bjoba2m;
    GuiButton Confirm;
    GuiButton Cancel;
    
    int mx = 0;
    @Override
    public void initGui() {
        int x = (width - 248) / 2;
        int y = (height - 166) / 2;
        drawRectangle(x, y, 248, 166, new Color(35,35,35));
        
        for (String name : JobManager.jobsname.keySet()) {
        	int taille = mc.fontRendererObj.getStringWidth(Util.translate("job."+name));
        	if (taille > mx) {
        		mx = taille;
        	}
        }
        mx += 10;
        PlayerStats stats = PlayerManager.get(mc.player);
        String jobname = Util.translate("job.none");
        if (stats != null && stats.job != null) {
        	jobname = Util.translate("job."+stats.job.getName());
        	job = stats.job;
        	backupjob=stats.job;
        	points = job.points();
        }
        
        int size = 20;
        
        bjob = new GuiButton(0, width/2-mx/2, height/4, mx, 20, jobname);
        bljob = new GuiButton(1, width/2-mx/2-size-4, height/4,size, 20, "<");
        brjob = new GuiButton(2, width/2+mx/2+4, height/4,size, 20, ">");
        
        bjoba1m = new GuiButton(3, width/2-mx/2-size-4, height/4+40,size, 20, "-");
        bjoba1p = new GuiButton(4, width/2+mx/2+4, height/4+40,size, 20, "+");
        
        bjoba2m = new GuiButton(5, width/2-mx/2-size-4, height/4+80,size, 20, "-");
        bjoba2p = new GuiButton(6, width/2+mx/2+4, height/4+80,size, 20, "+");
        
        Confirm = new GuiButton(7, width/2-mx/2-20, height/4+120,mx+2*size, 20, Util.translate("button.confirm"));
        
        Cancel = new GuiButton(8, width/2-mx/2-20, height/4+144,mx+2*size, 20, Util.translate("button.cancel"));
        
        bjoba1m.enabled=false;
        bjoba2m.enabled=false;
  
        if (job != null) {
	        bjoba1p.enabled=(job.lvl() >= job.AbilityMin1());
	        bjoba2p.enabled=(job.lvl() >= job.AbilityMin2());
        } else {
        	bjoba1p.enabled=false;
        	bjoba2p.enabled=false;
        }
        
        Confirm.enabled=false;
        
        buttonList.add(bjob);
        buttonList.add(bljob);
        buttonList.add(brjob);
        buttonList.add(bjoba1m);
        buttonList.add(bjoba1p);
        buttonList.add(bjoba2m);
        buttonList.add(bjoba2p);
        buttonList.add(Confirm);
        buttonList.add(Cancel);
        
        
        mc.getTextureManager().bindTexture(ICONS);
    }
    
    
    
    public static void drawRectangle(int x, int y, int width, int height, int color) {
		drawRect(x, y, x+width, y+height, color);
	}
    
    public static void drawRectangle(int x, int y, int width, int height, Color color) {
		drawRect(x, y, x+width, y+height, color.getRGB());
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
		mc.fontRendererObj.drawString(txt, x+width/2-mc.fontRendererObj.getStringWidth(txt)/2, (y+1)+height/2-mc.fontRendererObj.FONT_HEIGHT/2, Color.white.getRGB());
	}
	
	  public static float round(float d, int decimalPlace) {
	        BigDecimal bd = new BigDecimal(Float.toString(d));
	        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
	        return bd.floatValue();
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
