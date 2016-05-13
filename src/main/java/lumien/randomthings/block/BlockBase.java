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
		this.setRegistryName(new ResourceLocation("randomthings", name));
		this.setCreativeTab(RandomThings.instance.creativeTab);

		GameRegistry.register(this);
		GameRegistry.register(new ItemBlock(this).setRegistryName(this.getRegistryName()));

		RandomThings.proxy.scheduleColor(this);
	}

	protected BlockBase(String name, Material materialIn, Class<? extends ItemBlock> itemBlock)
	{
		super(materialIn);

		this.setUnlocalizedName(name);
		this.setRegistryName(new ResourceLocation("randomthings", name));
		this.setCreativeTab(RandomThings.instance.creativeTab);

		GameRegistry.register(this);
		try
		{
			GameRegistry.register(itemBlock.getConstructor(Block.class).newInstance(this).setRegistryName(this.getRegistryName()));
		}
		catch (Exception e)
		{
			System.out.println("Error Registering ItemBlock for " + name);
			e.printStackTrace();
		}
		RandomThings.proxy.scheduleColor(this);
	}
}
