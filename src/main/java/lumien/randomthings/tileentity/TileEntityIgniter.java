package lumien.randomthings.tileentity;

import java.util.Random;

import lumien.randomthings.block.BlockIgniter;
import lumien.randomthings.lib.ContainerSynced;
import lumien.randomthings.lib.IRedstoneSensitive;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityIgniter extends TileEntityBase implements IRedstoneSensitive
{
	public enum MODE
	{
		TOGGLE, IGNITE, KEEP_IGNITED;
	}
	
	@ContainerSynced
	MODE mode;
	
	public TileEntityIgniter()
	{
		this.mode = MODE.TOGGLE;
	}
	
	@Override
	public void writeDataToNBT(NBTTagCompound compound, boolean sync)
	{
		if (!sync)
		{
			compound.setInteger("mode", this.mode.ordinal());
		}
	}
	
	@Override
	public void readDataFromNBT(NBTTagCompound compound, boolean sync)
	{
		if (!sync)
		{
			this.mode = MODE.values()[compound.getInteger("mode")];
		}
	}
	
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block neighborBlock, BlockPos changedPos)
	{
		super.neighborChanged(state, worldIn, pos, neighborBlock, changedPos);
		
		if (mode == MODE.KEEP_IGNITED)
		{
			EnumFacing facing = state.getValue(BlockIgniter.FACING);
			BlockPos frontPos = pos.offset(facing);
			IBlockState front = world.getBlockState(frontPos);

			if (world.isAirBlock(frontPos) && Blocks.FIRE.canPlaceBlockAt(worldIn, frontPos))
			{
				world.playSound(null, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, new Random().nextFloat() * 0.4F + 0.8F);
				world.setBlockState(frontPos, Blocks.FIRE.getDefaultState());
			}
		}
	}

	@Override
	public void redstoneChange(boolean oldState, boolean newState)
	{
		IBlockState state = this.world.getBlockState(this.pos);
		EnumFacing facing = state.getValue(BlockIgniter.FACING);
		
		if (oldState && !newState)
		{
			IBlockState front = world.getBlockState(pos.offset(facing));

			if (front.getBlock() == Blocks.FIRE && mode == MODE.TOGGLE)
			{
				world.setBlockToAir(pos.offset(facing));
			}
		}
		else if (newState && !oldState)
		{
			if (world.isAirBlock(pos.offset(facing)) && mode != MODE.KEEP_IGNITED && Blocks.FIRE.canPlaceBlockAt(world, pos.offset(facing)))
			{
				world.playSound(null, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, new Random().nextFloat() * 0.4F + 0.8F);
				world.setBlockState(pos.offset(facing), Blocks.FIRE.getDefaultState());
			}
		}
		else
		{
			return;
		}
	}

	public void rotateMode()
	{
		switch (mode)
		{
			case IGNITE:
				this.mode = MODE.KEEP_IGNITED;
				
				IBlockState state = world.getBlockState(this.pos);
				EnumFacing facing = state.getValue(BlockIgniter.FACING);
				BlockPos frontPos = pos.offset(facing);
				IBlockState front = world.getBlockState(frontPos);

				if (world.isAirBlock(frontPos) && Blocks.FIRE.canPlaceBlockAt(world, frontPos))
				{
					world.playSound(null, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, new Random().nextFloat() * 0.4F + 0.8F);
					world.setBlockState(frontPos, Blocks.FIRE.getDefaultState());
				}
				
				break;
			case KEEP_IGNITED:
				this.mode = MODE.TOGGLE;
				break;
			case TOGGLE:
				this.mode = MODE.IGNITE;
				break;
			default:
				break;
			
		}
	}
}
