package lumien.randomthings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lumien.randomthings.item.ItemBiomeCrystal;
import lumien.randomthings.item.ItemPositionFilter;
import lumien.randomthings.item.ModItems;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.registry.GameData;

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
	public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos)
	{
		if (args.length == 1)
		{
			return getListOfStringsMatchingLastWord(args, new String[] { "generateBiomeCrystalChests", "setBiomeCrystal", "tpFilter" });
		}
		else if (args.length == 2)
		{
			if (args[0].equals("setBiomeCrystal"))
			{
				return getListOfStringsMatchingLastWord(args, GameData.getBiomeRegistry().getKeys());
			}
		}
		return Collections.<String> emptyList();
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		if (args[0].equals("setBiomeCrystal"))
		{
			if (args.length == 2 && sender instanceof EntityPlayer)
			{
				String biomeName = args[1];

				EntityPlayer player = (EntityPlayer) sender;

				ItemStack equipped = player.getHeldItemMainhand();

				if (equipped != null && equipped.getItem() instanceof ItemBiomeCrystal)
				{
					if (equipped.getTagCompound() == null)
					{
						equipped.setTagCompound(new NBTTagCompound());
					}

					equipped.getTagCompound().setString("biomeName", args[1]);
				}
			}
		}
		else if (args[0].equals("generateBiomeCrystalChests"))
		{
			if (sender instanceof EntityPlayer)
			{
				List<ResourceLocation> biomeIds = new ArrayList<ResourceLocation>(Biome.REGISTRY.getKeys());

				int modX = 0;

				while (!biomeIds.isEmpty())
				{
					sender.getEntityWorld().setBlockState(sender.getPosition().add(modX, 0, 0), Blocks.CHEST.getDefaultState());

					IInventory inventory = (IInventory) sender.getEntityWorld().getTileEntity(sender.getPosition().add(modX, 0, 0));
					for (int i = 0; i < 27; i++)
					{
						if (!biomeIds.isEmpty())
						{
							ResourceLocation next = biomeIds.remove(biomeIds.size() - 1);
							ItemStack crystal = new ItemStack(ModItems.biomeCrystal);
							crystal.setTagCompound(new NBTTagCompound());
							crystal.getTagCompound().setString("biomeName", next.toString());
							inventory.setInventorySlotContents(i, crystal);
						}
					}

					modX += 2;
				}
			}
		}
		else if (args[0].equals("tpFilter"))
		{
			if (sender instanceof EntityPlayerMP)
			{
				EntityPlayerMP player = (EntityPlayerMP) sender;

				ItemStack held;
				if ((held = player.getHeldItemMainhand()) != null && held.getItem() == ModItems.positionFilter)
				{
					BlockPos pos = ItemPositionFilter.getPosition(held);

					player.connection.setPlayerLocation(pos.getX(), pos.getY() + 150, pos.getZ(), player.rotationYaw, player.rotationPitch);
				}
			}
		}
	}

}
