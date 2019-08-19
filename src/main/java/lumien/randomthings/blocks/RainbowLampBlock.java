package lumien.randomthings.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.DyeColor;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RainbowLampBlock extends Block
{
	public static final EnumProperty<DyeColor> COLOR = EnumProperty.create("color", DyeColor.class);

	public RainbowLampBlock()
	{
		super(Block.Properties.create(Material.REDSTONE_LIGHT).lightValue(15).hardnessAndResistance(0.3F).sound(SoundType.GLASS));

		this.setDefaultState(this.stateContainer.getBaseState().with(COLOR, DyeColor.WHITE));
	}

	@Override
	protected void fillStateContainer(Builder<Block, BlockState> builder)
	{
		super.fillStateContainer(builder);

		builder.add(COLOR);
	}

	@Override
	public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState state2, boolean what)
	{
		if (!worldIn.isRemote)
		{
			updateColor(state, worldIn, pos);
		}
	}

	@Override
	public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block neighborBlock, BlockPos changedPos, boolean what)
	{
		if (!worldIn.isRemote)
		{
			updateColor(state, worldIn, pos);
		}
	}

	private void updateColor(BlockState state, World worldIn, BlockPos pos)
	{
		int redstoneLevel = worldIn.getRedstonePowerFromNeighbors(pos);

		int colorIndex = state.get(COLOR).ordinal();

		if (redstoneLevel != colorIndex)
		{
			worldIn.setBlockState(pos, state.with(COLOR, DyeColor.values()[redstoneLevel]), 2);
		}
	}
}