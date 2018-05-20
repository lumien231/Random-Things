package lumien.randomthings.block;

import java.util.Random;

import javax.annotation.Nullable;

import lumien.randomthings.lib.ILuminousBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockGlowingMushroom extends BlockBase implements IPlantable, ILuminousBlock
{
	protected static final AxisAlignedBB MUSHROOM_AABB = new AxisAlignedBB(0.30000001192092896D, 0.0D, 0.30000001192092896D, 0.699999988079071D, 0.4000000059604645D, 0.699999988079071D);

	protected BlockGlowingMushroom()
	{
		super("glowingMushroom", Material.PLANTS);

		this.setTickRandomly(true);
		this.setSoundType(SoundType.PLANT);
	}

	@Override
	@Nullable
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
	{
		return NULL_AABB;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return MUSHROOM_AABB;
	}

	@Override
	public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos)
	{
		return EnumPlantType.Cave;
	}

	@Override
	public IBlockState getPlant(IBlockAccess world, BlockPos pos)
	{
		return world.getBlockState(pos);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_)
	{
		return BlockFaceShape.UNDEFINED;
	}

	@Override
	public boolean shouldGlow(IBlockState state, int tintIndex)
	{
		return tintIndex == 0;
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
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
	{
		IBlockState soil = worldIn.getBlockState(pos.down());
		return super.canPlaceBlockAt(worldIn, pos) && canBlockStay(worldIn, pos, this.getDefaultState());
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
	{
		super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
		this.checkAndDropBlock(worldIn, pos, state);
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		if (rand.nextInt(20) == 0)
		{
			int i = 5;
			int j = 4;

			for (BlockPos blockpos : BlockPos.getAllInBoxMutable(pos.add(-4, -1, -4), pos.add(4, 1, 4)))
			{
				if (worldIn.getBlockState(blockpos).getBlock() == this)
				{
					--i;

					if (i <= 0)
					{
						return;
					}
				}
			}

			BlockPos blockpos1 = pos.add(rand.nextInt(3) - 1, rand.nextInt(2) - rand.nextInt(2), rand.nextInt(3) - 1);

			for (int k = 0; k < 4; ++k)
			{
				if (worldIn.isAirBlock(blockpos1) && this.canBlockStay(worldIn, blockpos1, this.getDefaultState()))
				{
					pos = blockpos1;
				}

				blockpos1 = pos.add(rand.nextInt(3) - 1, rand.nextInt(2) - rand.nextInt(2), rand.nextInt(3) - 1);
			}

			if (worldIn.isAirBlock(blockpos1) && this.canBlockStay(worldIn, blockpos1, this.getDefaultState()))
			{
				worldIn.setBlockState(blockpos1, this.getDefaultState(), 2);
			}
		}

		this.checkAndDropBlock(worldIn, pos, state);
	}

	protected void checkAndDropBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		if (!this.canBlockStay(worldIn, pos, state))
		{
			this.dropBlockAsItem(worldIn, pos, state, 0);
			worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
		}
	}

	public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state)
	{
		if (pos.getY() >= 0 && pos.getY() < 256)
		{
			IBlockState soil = worldIn.getBlockState(pos.down());

			if (soil.getBlock() == Blocks.MYCELIUM)
			{
				return true;
			}
			else
			{
				return worldIn.getLight(pos) < 13 && soil.getBlock().canSustainPlant(soil, worldIn, pos.down(), net.minecraft.util.EnumFacing.UP, this) && canSustainBush(soil);
			}
		}
		else
		{
			return false;
		}
	}

	protected boolean canSustainBush(IBlockState state)
	{
		return state.getBlock() == Blocks.GRASS || state.getBlock() == Blocks.DIRT || state.getBlock() == Blocks.STONE || state.getBlock() == ModBlocks.fertilizedDirt;
	}
}
