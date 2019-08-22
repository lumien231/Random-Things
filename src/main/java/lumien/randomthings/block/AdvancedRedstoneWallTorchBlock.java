package lumien.randomthings.block;

import java.util.Random;

import javax.annotation.Nullable;

import lumien.randomthings.tileentity.AdvancedRedstoneTorchTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.WallTorchBlock;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class AdvancedRedstoneWallTorchBlock extends AdvancedRedstoneTorchBlock
{
	public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
	public static final EnumProperty<AdvancedRedstoneTorchBlock.COLOR> COLOR_PROPERTY = AdvancedRedstoneTorchBlock.COLOR_PROPERTY;

	protected AdvancedRedstoneWallTorchBlock()
	{
		super(Block.Properties.create(Material.MISCELLANEOUS).doesNotBlockMovement().hardnessAndResistance(0).lightValue(7).sound(SoundType.WOOD));
		this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH).with(COLOR_PROPERTY, AdvancedRedstoneTorchBlock.COLOR.RED));
	}

	/**
	 * Returns the unlocalized name of the block with "tile." appended to the front.
	 */
	public String getTranslationKey()
	{
		return this.asItem().getTranslationKey();
	}

	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		return WallTorchBlock.func_220289_j(state);
	}

	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos)
	{
		return Blocks.WALL_TORCH.isValidPosition(state, worldIn, pos);
	}

	/**
	 * Update the provided state given the provided neighbor facing and neighbor state, returning a new state. For example, fences make their connections to the passed in state if possible, and wet concrete powder immediately returns its solidified counterpart. Note that this method should ideally consider only the specific face passed in.
	 */
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos)
	{
		return Blocks.WALL_TORCH.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}

	@Nullable
	public BlockState getStateForPlacement(BlockItemUseContext context)
	{
		BlockState blockstate = Blocks.WALL_TORCH.getStateForPlacement(context);
		return blockstate == null ? null : this.getDefaultState().with(FACING, blockstate.get(FACING));
	}

	/**
	 * Called periodically clientside on blocks near the player to show effects (like furnace fire particles). Note that this method is unrelated to {@link randomTick} and {@link #needsRandomTick}, and will always be called regardless of whether the block can receive random update ticks
	 */
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		if (stateIn.get(COLOR_PROPERTY) == COLOR.RED)
		{
			Direction direction = stateIn.get(FACING).getOpposite();
			double d0 = 0.27D;
			double d1 = (double) pos.getX() + 0.5D + (rand.nextDouble() - 0.5D) * 0.2D + 0.27D * (double) direction.getXOffset();
			double d2 = (double) pos.getY() + 0.7D + (rand.nextDouble() - 0.5D) * 0.2D + 0.22D;
			double d3 = (double) pos.getZ() + 0.5D + (rand.nextDouble() - 0.5D) * 0.2D + 0.27D * (double) direction.getZOffset();
			worldIn.addParticle(RedstoneParticleData.REDSTONE_DUST, d1, d2, d3, 0.0D, 0.0D, 0.0D);
		}
		else
		{
			Direction direction = stateIn.get(FACING).getOpposite();
			double d0 = 0.27D;
			double d1 = (double) pos.getX() + 0.5D + (rand.nextDouble() - 0.5D) * 0.2D + 0.27D * (double) direction.getXOffset();
			double d2 = (double) pos.getY() + 0.7D + (rand.nextDouble() - 0.5D) * 0.2D + 0.22D;
			double d3 = (double) pos.getZ() + 0.5D + (rand.nextDouble() - 0.5D) * 0.2D + 0.27D * (double) direction.getZOffset();
			worldIn.addParticle(GREEN_DUST, d1, d2, d3, 0.0D, 0.0D, 0.0D);
		}
	}

	@Override
	protected boolean shouldBeGreen(World worldIn, BlockPos pos, BlockState state)
	{
		Direction direction = state.get(FACING).getOpposite();
		return worldIn.isSidePowered(pos.offset(direction), direction);
	}

	/**
	 * @deprecated call via {@link IBlockState#getWeakPower(IBlockAccess,BlockPos,EnumFacing)} whenever possible. Implementing/overriding is fine.
	 */
	public int getWeakPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side)
	{
		AdvancedRedstoneTorchTileEntity te = (AdvancedRedstoneTorchTileEntity) blockAccess.getTileEntity(pos);

		int strength = blockState.get(COLOR_PROPERTY) == COLOR.RED ? te.signalStrengthRed() : te.signalStrengthGreen();

		return blockState.get(FACING) != side ? strength : 0;
	}

	/**
	 * Returns the blockstate with the given rotation from the passed blockstate. If inapplicable, returns the passed blockstate.
	 * 
	 * @deprecated call via {@link IBlockState#withRotation(Rotation)} whenever possible. Implementing/overriding is fine.
	 */
	public BlockState rotate(BlockState state, Rotation rot)
	{
		return Blocks.WALL_TORCH.rotate(state, rot);
	}

	/**
	 * Returns the blockstate with the given mirror of the passed blockstate. If inapplicable, returns the passed blockstate.
	 * 
	 * @deprecated call via {@link IBlockState#withMirror(Mirror)} whenever possible. Implementing/overriding is fine.
	 */
	public BlockState mirror(BlockState state, Mirror mirrorIn)
	{
		return Blocks.WALL_TORCH.mirror(state, mirrorIn);
	}

	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(FACING, COLOR_PROPERTY);
	}
}