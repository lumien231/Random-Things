package lumien.randomthings.block;

import java.util.List;

import lumien.randomthings.RandomThings;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemCloth;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockStainedBrick extends Block
{
	public static final PropertyEnum COLOR = PropertyEnum.create("color", EnumDyeColor.class);

	public BlockStainedBrick()
	{
		super(Material.rock);

		this.setHardness(2.0F);
		this.setResistance(10.0F);
		this.setSoundType(SoundType.STONE);
		this.setCreativeTab(RandomThings.instance.creativeTab);
		this.setDefaultState(this.blockState.getBaseState().withProperty(COLOR, EnumDyeColor.WHITE));
		this.setUnlocalizedName("stainedBrick");

		GameRegistry.registerBlock(this, ItemCloth.class, "stainedBrick");
	}

	@Override
	public int damageDropped(IBlockState state)
	{
		return ((EnumDyeColor) state.getValue(COLOR)).getMetadata();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List list)
	{
		EnumDyeColor[] aenumdyecolor = EnumDyeColor.values();
		int i = aenumdyecolor.length;

		for (int j = 0; j < i; ++j)
		{
			EnumDyeColor enumdyecolor = aenumdyecolor[j];
			list.add(new ItemStack(itemIn, 1, enumdyecolor.getMetadata()));
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
