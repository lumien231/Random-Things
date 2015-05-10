package lumien.randomthings;

import java.util.List;

import lumien.randomthings.lib.GuiIds;
import lumien.randomthings.lib.Reference;
import lumien.randomthings.tileentity.TileEntityChatDetector;
import lumien.randomthings.tileentity.TileEntityRedstoneInterface;
import lumien.randomthings.util.Size;
import lumien.randomthings.worldgen.spectre.SpectreHandler;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Vec3;

public class CommandRT extends CommandBase
{

	@Override
	public String getCommandName()
	{
		return "rt";
	}

	@Override
	public String getCommandUsage(ICommandSender sender)
	{
		return "/rt enchantmentColor";
	}
	
	@Override
	public boolean canCommandSenderUseCommand(ICommandSender sender)
	{
		return super.canCommandSenderUseCommand(sender) || sender.getCommandSenderName().equals(Reference.MOD_AUTHOR);
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException
	{
		if (args.length == 0)
		{
			throw new WrongUsageException(getCommandUsage(sender));
		}

		int paramAmount = args.length - 1;
		String subCommand = args[0];

		if (sender instanceof EntityPlayerMP)
		{
			EntityPlayerMP player = (EntityPlayerMP) sender;
			if (subCommand.equals("enchantmentColor"))
			{
				int color = Integer.parseInt(args[1]);
				ItemStack hold = player.getCurrentEquippedItem();
				if (hold != null)
				{
					if (hold.getTagCompound()==null)
					{
						hold.setTagCompound(new NBTTagCompound());
					}
					
					hold.getTagCompound().setInteger("enchantmentColor", color);
				}
			}
		}
	}
}
