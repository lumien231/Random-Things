package lumien.randomthings.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.pathfinding.PathType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;

public class PlatformBlock extends Block
{
	protected static final VoxelShape SHAPE = Block.makeCuboidShape(0, 14, 0, 16, 16, 16);

	public PlatformBlock()
	{
		super(Block.Properties.create(Material.WOOD, MaterialColor.WOOD).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD));
	}

	@Override
	public boolean isToolEffective(BlockState state, ToolType tool)
	{
		return tool == ToolType.AXE;
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		return SHAPE;
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		Entity entity = context.getEntity();

		if (entity instanceof PlayerEntity && ((PlayerEntity) entity).isSneaking() || entity != null && entity.posY < pos.getY() + 14F / 16F)
		{
			return VoxelShapes.empty();
		}
		return super.getCollisionShape(state, worldIn, pos, context);
	}
}
