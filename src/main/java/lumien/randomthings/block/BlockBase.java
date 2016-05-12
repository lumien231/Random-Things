package lumien.randomthings.block;

import lumien.randomthings.RandomThings;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public abstract class BlockBase extends Block
{
	protected BlockBase(String name, Material materialIn)
	{
		super(materialIn);

		this.setUnlocalizedName(name);
		this.setRegistryName(new ResourceLocation(name));
		this.setCreativeTab(RandomThings.instance.creativeTab);

		GameRegistry.register(this);
		
		RandomThings.proxy.scheduleColor(this);
	}

	protected BlockBase(String name, Material materialIn, Class<? extends ItemBlock> itemBlock)
	{
		super(materialIn);

		this.setUnlocalizedName(name);
		this.setRegistryName(new ResourceLocation(name));
		this.setCreativeTab(RandomThings.instance.creativeTab);
		
		GameRegistry.register(this);
		
		RandomThings.proxy.scheduleColor(this);
	}
}
