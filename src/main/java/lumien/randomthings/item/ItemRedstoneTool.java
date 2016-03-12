package lumien.randomthings.item;

import lumien.randomthings.block.ModBlocks;
import lumien.randomthings.tileentity.redstoneinterface.TileEntityBasicRedstoneInterface;
import lumien.randomthings.tileentity.redstoneinterface.TileEntityRedstoneInterface;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemRedstoneTool extends ItemBase
{
	public ItemRedstoneTool()
	{
		super("redstoneTool");

		this.setMaxStackSize(1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack stack)
	{
		NBTTagCompound compound;

		if ((compound = stack.getTagCompound()) != null)
		{
			return compound.getBoolean("linking");
		}

		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isFull3D()
	{
		return true;
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		IBlockState state = worldIn.getBlockState(pos);

		if (!stack.hasTagCompound())
		{
			if (state.getBlock() == ModBlocks.basicRedstoneInterface)
			{
				stack.setTagCompound(new NBTTagCompound());
			}
			else
			{
				return false;
			}
		}

		NBTTagCompound compound = stack.getTagCompound();

		boolean linking = compound.getBoolean("linking");

		if (linking)
		{
			BlockPos linkingTo = new BlockPos(compound.getInteger("oX"), compound.getInteger("oY"), compound.getInteger("oZ"));
			if (!linkingTo.equals(pos))
			{
				IBlockState redstoneInterfaceState = worldIn.getBlockState(linkingTo);
				if (redstoneInterfaceState.getBlock() == ModBlocks.basicRedstoneInterface)
				{
					TileEntityBasicRedstoneInterface redstoneInterface = (TileEntityBasicRedstoneInterface) worldIn.getTileEntity(linkingTo);

					redstoneInterface.setTarget(pos);
				}
			}
			compound.setBoolean("linking", false);
			return true;
		}
		else
		{
			if (state.getBlock() == ModBlocks.basicRedstoneInterface)
			{
				compound.setBoolean("linking", true);
				compound.setInteger("oX", pos.getX());
				compound.setInteger("oY", pos.getY());
				compound.setInteger("oZ", pos.getZ());
				return true;
			}
		}

		return false;
	}
}
