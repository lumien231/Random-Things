package lumien.randomthings.block;

import lumien.randomthings.item.block.ItemBlockClothLuminous;
import lumien.randomthings.lib.ILuminousBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemCloth;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockStainedBrick extends BlockBase implements ILuminousBlock
{
	public static final PropertyEnum COLOR = PropertyEnum.create("color", EnumDyeColor.class);

	boolean luminous;

	public BlockStainedBrick(boolean luminous)
	{
		super(luminous ? "luminousStainedBrick" : "stainedBrick", Material.ROCK, luminous ? ItemBlockClothLuminous.class : ItemCloth.class);

		this.setHardness(2.0F);
		this.setResistance(10.0F);
		this.setSoundType(SoundType.STONE);
		this.setDefaultState(this.blockState.getBaseState().withProperty(COLOR, EnumDyeColor.WHITE));

		this.luminous = luminous;
	}

	@Override
	public BlockRenderLayer getBlockLayer()
	{
		if (luminous)
		{
			return BlockRenderLayer.CUTOUT_MIPPED;
		}
		else
		{
			return super.getBlockLayer();
		}
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
		return luminous;
	}
}
