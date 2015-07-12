package lumien.randomthings.block;

import lumien.randomthings.RandomThings;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public abstract class BlockContainerBase extends Block
{
	protected BlockContainerBase(String name, Material materialIn)
	{
		super(materialIn);

		this.setCreativeTab(RandomThings.instance.creativeTab);
		this.setUnlocalizedName(name);

		GameRegistry.registerBlock(this, name);
	}

	protected BlockContainerBase(String name, Material materialIn, Class<? extends ItemBlock> itemBlock)
	{
		super(materialIn);

		this.setCreativeTab(RandomThings.instance.creativeTab);
		this.setUnlocalizedName(name);

		GameRegistry.registerBlock(this, itemBlock, name);
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state)
	{
		return true;
	}
	
	@Override
	public abstract TileEntity createTileEntity(World world, IBlockState state);
}
