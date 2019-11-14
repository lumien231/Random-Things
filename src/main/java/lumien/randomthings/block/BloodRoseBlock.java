package lumien.randomthings.block;

import lumien.randomthings.tileentity.BloodRoseTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BushBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.tags.BlockTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

public class BloodRoseBlock extends BushBlock
{
	protected static final VoxelShape SHAPE = Block.makeCuboidShape(5.0D, 0.0D, 5.0D, 11.0D, 15.0D, 11.0D);

	public BloodRoseBlock()
	{
		super(Block.Properties.create(Material.PLANTS).hardnessAndResistance(0F).sound(SoundType.PLANT).doesNotBlockMovement());
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		Vec3d vec3d = state.getOffset(worldIn, pos);
		return SHAPE.withOffset(vec3d.x, vec3d.y, vec3d.z);
	}

	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new BloodRoseTileEntity();
	}

	@Override
	public boolean isValidGround(BlockState state, IBlockReader worldIn, BlockPos pos)
	{
		if (BlockTags.DIRT_LIKE.contains(state.getBlock()))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public Block.OffsetType getOffsetType()
	{
		return Block.OffsetType.XZ;
	}
}
