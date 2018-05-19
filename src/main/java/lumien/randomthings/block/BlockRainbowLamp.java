package lumien.randomthings.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockRainbowLamp extends BlockBase
{
	public static final PropertyEnum COLOR = PropertyEnum.create("color", EnumDyeColor.class);

	public BlockRainbowLamp()
	{
		super("rainbowLamp", Material.REDSTONE_LIGHT);

		this.setHardness(0.3F);
		this.setSoundType(SoundType.GLASS);
		this.setDefaultState(this.blockState.getBaseState().withProperty(COLOR, EnumDyeColor.WHITE));

		this.setLightLevel(1.0F);
	}

	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
	{
		if (!worldIn.isRemote)
		{
			int redstoneLevel = worldIn.isBlockIndirectlyGettingPowered(pos);

			int colorIndex = ((EnumDyeColor) state.getValue(COLOR)).ordinal();

			if (redstoneLevel != colorIndex)
			{
				worldIn.setBlockState(pos, state.withProperty(COLOR, EnumDyeColor.values()[redstoneLevel]), 2);
			}
		}
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block neighborBlock, BlockPos changedPos)
	{
		if (!worldIn.isRemote)
		{
			int redstoneLevel = worldIn.isBlockIndirectlyGettingPowered(pos);

			int colorIndex = ((EnumDyeColor) state.getValue(COLOR)).ordinal();

			if (redstoneLevel != colorIndex)
			{
				worldIn.scheduleUpdate(pos, this, 4);
			}
		}
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		if (!worldIn.isRemote)
		{
			int redstoneLevel = worldIn.isBlockIndirectlyGettingPowered(pos);

			int colorIndex = ((EnumDyeColor) state.getValue(COLOR)).ordinal();

			if (redstoneLevel != colorIndex)
			{
				worldIn.setBlockState(pos, state.withProperty(COLOR, EnumDyeColor.values()[redstoneLevel]), 2);
			}
		}
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(COLOR, EnumDyeColor.byMetadata(meta));
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return ((EnumDyeColor) state.getValue(COLOR)).getMetadata();
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] { COLOR });
	}
}
