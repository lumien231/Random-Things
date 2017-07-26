package lumien.randomthings.item;

import java.util.List;

import org.lwjgl.input.Keyboard;

import lumien.randomthings.config.Features;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemPositionFilter extends ItemBase
{
	public ItemPositionFilter()
	{
		super("positionFilter");
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, World world, List par3List, ITooltipFlag par4)
	{
		if (!Features.HIDE_CORDS)
		{
			if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
			{
				NBTTagCompound compound;
				if ((compound = par1ItemStack.getTagCompound()) != null && compound.getBoolean("hasPosition"))
				{
					int dimension = compound.getInteger("dimension");
					int filterX = compound.getInteger("filterX");
					int filterY = compound.getInteger("filterY");
					int filterZ = compound.getInteger("filterZ");

					par3List.add(I18n.format("tooltip.positionFilter.dimension", dimension));
					par3List.add(I18n.format("tooltip.positionFilter.x", filterX));
					par3List.add(I18n.format("tooltip.positionFilter.y", filterY));
					par3List.add(I18n.format("tooltip.positionFilter.z", filterZ));
				}
			}
			else
			{
				par3List.add(I18n.format("tooltip.general.shift"));
			}
		}
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		ItemStack stack = playerIn.getHeldItem(hand);
		if (!worldIn.isRemote)
		{
			setPosition(stack, worldIn.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ());
		}
		return EnumActionResult.SUCCESS;
	}

	public static void setPosition(ItemStack positionFilter, int dimension, int posX, int posY, int posZ)
	{
		NBTTagCompound compound;
		compound = positionFilter.getTagCompound();
		if (compound == null)
		{
			positionFilter.setTagCompound(compound = new NBTTagCompound());
		}

		compound.setBoolean("hasPosition", true);
		compound.setInteger("dimension", dimension);
		compound.setInteger("filterX", posX);
		compound.setInteger("filterY", posY);
		compound.setInteger("filterZ", posZ);
	}

	public static int getDimension(ItemStack positionFilter)
	{
		return positionFilter.getTagCompound().getInteger("dimension");
	}

	public static BlockPos getPosition(ItemStack positionFilter)
	{
		NBTTagCompound compound;
		if ((compound = positionFilter.getTagCompound()) == null || !compound.getBoolean("hasPosition"))
		{
			return null;
		}

		return new BlockPos(compound.getInteger("filterX"), compound.getInteger("filterY"), compound.getInteger("filterZ"));
	}
}
