package fr.shiranuit.luapolisessentials.Client.KeyHandler;

import org.lwjgl.input.Keyboard;

import fr.shiranuit.luapolisessentials.Main;
import fr.shiranuit.luapolisessentials.Client.Gui.JobGUI;
import fr.shiranuit.luapolisessentials.Network.Packets.StatsUpdatePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class KeyHandler {

	KeyBinding job  = new KeyBinding("Job", Keyboard.KEY_J, "LuaPolis");
	public KeyHandler()
	{
		ClientRegistry.registerKeyBinding(job);
	}
	@SideOnly(Side.CLIENT)
    @SubscribeEvent(receiveCanceled = true)
	public void onKeyPress(InputEvent e) {
		if (job.isPressed()) {
			Main.instance.network.sendToServer(new StatsUpdatePacket(Minecraft.getMinecraft().player.getDisplayNameString()));
			Minecraft.getMinecraft().displayGuiScreen(new JobGUI());
		}
	}
}
