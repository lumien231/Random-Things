package lumien.randomthings.block;

import java.util.List;
import java.util.Random;

import lumien.randomthings.item.block.ItemBlockLuminous;
import lumien.randomthings.lib.ILuminousBlock;
import lumien.randomthings.tileentity.TileEntityFlooBrick;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class BlockFlooBrick extends BlockContainerBase implements ILuminousBlock
{

	protected BlockFlooBrick()
	{
		super("flooBrick", Material.ROCK, ItemBlockLuminous.class);

		this.setHardness(2.0F).setResistance(10.0F);

		this.setCreativeTab(null);
	}

	@Override
	public int quantityDropped(Random random)
	{
		return 0;
	}

	@Override
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced)
	{
		tooltip.add("�cINTERNAL BLOCK, DO NOT USE!");
	}

	@Override
	public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items)
	{

	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new TileEntityFlooBrick();
	}

	@Override
	public boolean shouldGlow(IBlockState state, int tintIndex)
	{
		return tintIndex == 0;
	}

}
