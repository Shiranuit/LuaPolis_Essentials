package fr.shiranuit.luapolisessentials.Commands;

import java.util.Arrays;
import java.util.List;
import javax.annotation.Nullable;

import fr.shiranuit.luapolisessentials.Main;
import fr.shiranuit.luapolisessentials.Network.Packets.MessagePacket;
import fr.shiranuit.luapolisessentials.Network.Packets.PlayerStatsPacket;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

public class CommandMessage extends CommandBase
{
	@Override
	public List<String> getAliases()
    {
        return Arrays.<String>asList("w", "msg");
    }

    /**
     * Return the required permission level for this command.
     */
    @Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }




    /**
     * Callback for when the command is executed
     */
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
        if (args.length < 2)
        {
            throw new WrongUsageException("commands.message.usage", new Object[0]);
        }
        else
        {
            EntityPlayer entityplayer = getPlayer(server, sender, args[0]);

            if (entityplayer == sender)
            {
                throw new PlayerNotFoundException("commands.message.sameTarget");
            }
            else
            {
                ITextComponent itextcomponent = getChatComponentFromNthArg(sender, args, 1, !(sender instanceof EntityPlayer));
                itextcomponent = new TextComponentString(itextcomponent.getUnformattedText().replace("&", "\247"));
                
                TextComponentTranslation textcomponenttranslation = new TextComponentTranslation("commands.message.display.incoming", new Object[] {sender.getDisplayName(), itextcomponent.createCopy()});
                TextComponentTranslation textcomponenttranslation1 = new TextComponentTranslation("commands.message.display.outgoing", new Object[] {entityplayer.getDisplayName(), itextcomponent.createCopy()});
                textcomponenttranslation.getStyle().setColor(TextFormatting.GRAY).setItalic(Boolean.valueOf(true));
                textcomponenttranslation1.getStyle().setColor(TextFormatting.GRAY).setItalic(Boolean.valueOf(true));
                entityplayer.sendMessage(textcomponenttranslation);
                sender.sendMessage(textcomponenttranslation1);
                Main.instance.network.sendTo(new MessagePacket(itextcomponent.getUnformattedText(), sender.getName()), (EntityPlayerMP)entityplayer);
            }
        }
        
    }
    
    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
        return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
    }

    /**
     * Return whether the specified command parameter index is a username parameter.
     */
    public boolean isUsernameIndex(String[] args, int index)
    {
        return index == 0;
    }

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "tell";
	}

    /**
     * Gets the usage string for the command.
     */
	@Override
	public String getUsage(ICommandSender sender) {
		// TODO Auto-generated method stub
		return "commands.message.usage";
	}
}
