package lumien.randomthings.item;

import java.util.List;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemPositionFilter extends ItemBase
{
	public ItemPositionFilter()
	{
		super("positionFilter");
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
	{
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
		{
			NBTTagCompound compound;
			if ((compound = par1ItemStack.getTagCompound()) != null)
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

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (!worldIn.isRemote)
		{
			NBTTagCompound compound;
			compound = stack.getTagCompound();
			if (compound == null)
			{
				stack.setTagCompound(compound = new NBTTagCompound());
			}

			compound.setInteger("dimension", worldIn.provider.getDimensionId());
			compound.setInteger("filterX", pos.getX());
			compound.setInteger("filterY", pos.getY());
			compound.setInteger("filterZ", pos.getZ());
		}
		return true;
	}

	public static int getDimension(ItemStack positionFilter)
	{
		return positionFilter.getTagCompound().getInteger("dimension");
	}

	public static BlockPos getPosition(ItemStack positionFilter)
	{
		return new BlockPos(positionFilter.getTagCompound().getInteger("filterX"), positionFilter.getTagCompound().getInteger("filterY"), positionFilter.getTagCompound().getInteger("filterZ"));
	}
}
