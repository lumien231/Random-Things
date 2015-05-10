package lumien.randomthings.block;

import lumien.randomthings.RandomThings;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

public abstract class BlockContainerBase extends BlockContainer
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

		GameRegistry.registerBlock(this,itemBlock, name);
	}
}
