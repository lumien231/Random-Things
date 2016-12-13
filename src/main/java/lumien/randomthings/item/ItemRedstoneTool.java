package lumien.randomthings.item;

import lumien.randomthings.block.ModBlocks;
import lumien.randomthings.tileentity.TileEntityRedstoneObserver;
import lumien.randomthings.tileentity.redstoneinterface.TileEntityBasicRedstoneInterface;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
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
	public EnumActionResult onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		ItemStack stack = playerIn.getHeldItem(hand);
		IBlockState state = worldIn.getBlockState(pos);

		if (!stack.hasTagCompound())
		{
			if (state.getBlock() == ModBlocks.basicRedstoneInterface || state.getBlock() == ModBlocks.redstoneObserver)
			{
				stack.setTagCompound(new NBTTagCompound());
			}
			else
			{
				return EnumActionResult.FAIL;
			}
		}

		NBTTagCompound compound = stack.getTagCompound();

		boolean linking = compound.getBoolean("linking");

		if (linking)
		{
			BlockPos linkingTo = new BlockPos(compound.getInteger("oX"), compound.getInteger("oY"), compound.getInteger("oZ"));
			if (!linkingTo.equals(pos))
			{
				IBlockState linkingState = worldIn.getBlockState(linkingTo);
				if (linkingState.getBlock() == ModBlocks.basicRedstoneInterface)
				{
					TileEntityBasicRedstoneInterface redstoneInterface = (TileEntityBasicRedstoneInterface) worldIn.getTileEntity(linkingTo);

					redstoneInterface.setTarget(pos);
				}
				else if (linkingState.getBlock() == ModBlocks.redstoneObserver)
				{
					TileEntityRedstoneObserver redstoneObserver = (TileEntityRedstoneObserver) worldIn.getTileEntity(linkingTo);

					redstoneObserver.setTarget(pos);
				}
			}
			compound.setBoolean("linking", false);
			return EnumActionResult.SUCCESS;
		}
		else
		{
			if (state.getBlock() == ModBlocks.basicRedstoneInterface || state.getBlock() == ModBlocks.redstoneObserver)
			{
				compound.setBoolean("linking", true);
				compound.setInteger("oX", pos.getX());
				compound.setInteger("oY", pos.getY());
				compound.setInteger("oZ", pos.getZ());
				return EnumActionResult.SUCCESS;
			}
		}

		return EnumActionResult.FAIL;
	}
}
