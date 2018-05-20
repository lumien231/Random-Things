package lumien.randomthings.block;

import lumien.randomthings.item.block.ItemBlockClothLuminous;
import lumien.randomthings.lib.ILuminousBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockBlockLuminousTranslucent extends BlockBase implements ILuminousBlock
{
	public static final PropertyEnum COLOR = PropertyEnum.create("color", EnumDyeColor.class);

	protected BlockBlockLuminousTranslucent()
	{
		super("translucentLuminousBlock", Material.GROUND, ItemBlockClothLuminous.class);

		this.setSoundType(SoundType.GLASS);
		this.setHardness(0.3F);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockState state, IBlockAccess worldIn, BlockPos pos, EnumFacing side)
	{
		IBlockState iblockstate = worldIn.getBlockState(pos.offset(side));
		Block block = iblockstate.getBlock();

		if (state.getBlock() != iblockstate.getBlock())
		{
			return true;
		}

		if (block == this)
		{
			return false;
		}

		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	@Override
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.TRANSLUCENT;
	}

	@Override
	public int damageDropped(IBlockState state)
	{
		return ((EnumDyeColor) state.getValue(COLOR)).getMetadata();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(CreativeTabs tab, NonNullList list)
	{
		EnumDyeColor[] aenumdyecolor = EnumDyeColor.values();
		int i = aenumdyecolor.length;

		for (int j = 0; j < i; ++j)
		{
			EnumDyeColor enumdyecolor = aenumdyecolor[j];
			list.add(new ItemStack(this, 1, enumdyecolor.getMetadata()));
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

	@Override
	public boolean shouldGlow(IBlockState state, int tintIndex)
	{
		return true;
	}
}
