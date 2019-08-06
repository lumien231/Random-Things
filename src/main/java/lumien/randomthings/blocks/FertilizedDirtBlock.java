package lumien.randomthings.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;
import net.minecraftforge.common.ToolType;

@RTBlock("fertilized_dirt")
public class FertilizedDirtBlock extends Block
{
	private static final VoxelShape SHAPE_TILLED = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 15.0D, 16.0D);

	public static final BooleanProperty TILLED = BooleanProperty.create("tilled");

	public FertilizedDirtBlock()
	{
		super(Block.Properties.create(Material.EARTH, MaterialColor.DIRT).hardnessAndResistance(0.5F).sound(SoundType.GROUND).tickRandomly());

		this.setDefaultState(this.stateContainer.getBaseState().with(TILLED, false));
	}
	
	@Override
	public void tick(BlockState state, World worldIn, BlockPos pos, Random random)
	{
		super.tick(state, worldIn, pos, random);

		for (int i = 0; i < 3; i++)
		{
			BlockState aboveState = worldIn.getBlockState(pos.up());
			Block aboveBlock = aboveState.getBlock();

			if (aboveBlock instanceof IPlantable && aboveState.ticksRandomly())
			{
				aboveState.randomTick(worldIn, pos.up(), random);
			}
			else
			{
				break;
			}
		}
	}
	
	@Override
	public boolean isToolEffective(BlockState state, ToolType tool)
	{
		return tool == ToolType.SHOVEL;
	}

	public boolean func_220074_n(BlockState state)
	{
		return state.get(TILLED);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		return state.get(TILLED) ? SHAPE_TILLED : VoxelShapes.fullCube();
	}

	@Override
	protected void fillStateContainer(Builder<Block, BlockState> builder)
	{
		builder.add(TILLED);
	}

	public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type)
	{
		return false;
	}

	@Override
	public boolean canSustainPlant(BlockState state, IBlockReader world, BlockPos pos, Direction facing, IPlantable plantable)
	{
		PlantType plantType = plantable.getPlantType(world, pos.up());
		boolean tilled = state.get(TILLED);
		
		switch (plantType)
		{
			case Desert:
				return !tilled;
			case Nether:
				return false;
			case Crop:
				return tilled;
			case Cave:
				return !tilled;
			case Plains:
				return !tilled || tilled && world.getBlockState(pos.up()).getBlock() == Blocks.BEETROOTS;
			case Water:
				return false;
			case Beach:
				return !tilled;
		}

		return false;
	}
	
	@Override
	public boolean isFertile(BlockState state, IBlockReader world, BlockPos pos)
	{
		return true;
	}
}
