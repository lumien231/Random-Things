package lumien.randomthings.block.plates;

import lumien.randomthings.block.BlockBase;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

public class BlockCollectionPlate extends BlockBase
{
	protected static final AxisAlignedBB AABB = null;
	protected static final AxisAlignedBB VISUAL_AABB = new AxisAlignedBB(0D, 0.0D, 0D, 1D, 0.03125D, 1D);

	public BlockCollectionPlate()
	{
		super("plate_collection", Material.GROUND);

		this.setHardness(0.3f);
		this.setSoundType(SoundType.STONE);
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block neighborBlock, BlockPos changedPos)
	{
		checkForDrop(worldIn, pos, state);
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
	{
		return canPlaceOn(worldIn, pos.down());
	}

	private boolean canPlaceOn(World worldIn, BlockPos pos)
	{
		return worldIn.isSideSolid(pos, EnumFacing.UP);
	}

	protected boolean checkForDrop(World worldIn, BlockPos pos, IBlockState state)
	{
		if (state.getBlock() == this && this.canPlaceOn(worldIn, pos.down()))
		{
			return true;
		}
		else
		{
			if (worldIn.getBlockState(pos).getBlock() == this)
			{
				this.dropBlockAsItem(worldIn, pos, state, 0);
				worldIn.setBlockToAir(pos);
			}

			return false;
		}
	}

	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
	{
		this.checkForDrop(worldIn, pos, state);
	}

	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_)
	{
		return BlockFaceShape.UNDEFINED;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return VISUAL_AABB;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
	{
		return AABB;
	}

	@Override
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT;
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
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
	{
		super.onEntityCollidedWithBlock(worldIn, pos, state, entityIn);

		if (!worldIn.isRemote && entityIn instanceof EntityItem)
		{
			ItemStack stack = ((EntityItem) entityIn).getItem();
			for (EnumFacing facing : EnumFacing.HORIZONTALS)
			{
				TileEntity te = worldIn.getTileEntity(pos.offset(facing));

				if (te != null && te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite()))
				{
					IItemHandler itemHandler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite());

					if (itemHandler != null)
					{
						ItemStack remains = ItemHandlerHelper.insertItemStacked(itemHandler, stack, false);

						((EntityItem) entityIn).setItem(remains);

						if (remains.isEmpty())
						{
							entityIn.setDead();
						}

						break;
					}
				}
			}
		}
	}
}
