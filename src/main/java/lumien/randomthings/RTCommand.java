package lumien.randomthings;

import lumien.randomthings.handler.spectre.SpectreHandler;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.server.CommandBanIp;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;

public class RTCommand extends CommandBase
{

	@Override
	public String getCommandName()
	{
		return "rt";
	}

	@Override
	public String getCommandUsage(ICommandSender sender)
	{
		return "/rt";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException
	{
		if (args[0].equals("teleport"))
		{
			SpectreHandler.getInstance().teleportPlayerToSpectreCube((EntityPlayerMP) sender);
		}
		else if (args[0].equals("info"))
		{
			sender.addChatMessage(new ChatComponentText(SpectreHandler.getInstance().getSpectreCubeFromPos(sender.getEntityWorld(), sender.getPosition()).toString()));
		}
	}

}
