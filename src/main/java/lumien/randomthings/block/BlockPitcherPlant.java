package lumien.randomthings.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockPitcherPlant extends BlockBase
{
	protected static final AxisAlignedBB PITCHER_AABB = new AxisAlignedBB(0.5F - 0.2F, 0.0F, 0.5F - 0.2F, 0.5F + 0.2F, 0.2F * 4.0F, 0.5F + 0.2F);

	
	protected BlockPitcherPlant()
	{
		super("pitcherPlant", Material.plants);

		float f = 0.2F;
		this.setSoundType(SoundType.PLANT);
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return PITCHER_AABB;
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		ItemStack equipped = heldItem;

		if (equipped != null && FluidContainerRegistry.isEmptyContainer(equipped))
		{
			int capacity = FluidContainerRegistry.getContainerCapacity(new FluidStack(FluidRegistry.WATER, 1000), equipped);

			ItemStack filledContainer = FluidContainerRegistry.fillFluidContainer(new FluidStack(FluidRegistry.WATER, capacity), equipped);

			if (filledContainer != null)
			{
				if (equipped.stackSize == 1)
				{
					playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, filledContainer);
				}
				else
				{
					equipped.stackSize--;

					boolean added = playerIn.inventory.addItemStackToInventory(filledContainer);

					if (!added)
					{
						playerIn.dropPlayerItemWithRandomChoice(filledContainer, false);
					}
				}

				return true;
			}
		}

		return false;
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
	{
		return super.canPlaceBlockAt(worldIn, pos) && canBlockStay(worldIn, pos, this.getDefaultState());
	}

	public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state)
	{
		Block soil = worldIn.getBlockState(pos.down()).getBlock();
		return this.canPlaceBlockOn(soil);
	}

	protected void checkAndDropBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		if (!this.canBlockStay(worldIn, pos, state))
		{
			this.dropBlockAsItem(worldIn, pos, state, 0);
			worldIn.setBlockState(pos, Blocks.air.getDefaultState(), 3);
		}
	}

	@Override
	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
	{
		super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
		this.checkAndDropBlock(worldIn, pos, state);
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		this.checkAndDropBlock(worldIn, pos, state);
	}

	protected boolean canPlaceBlockOn(Block ground)
	{
		return ground == Blocks.grass || ground == Blocks.dirt;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos)
    {
        return NULL_AABB;
    }

	@Override
	public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

	@Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}
}
