package lumien.randomthings.block;

import java.util.ArrayList;
import java.util.List;

import lumien.randomthings.RandomThings;
import lumien.randomthings.lib.INoItem;
import lumien.randomthings.lib.ISuperLubricent;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public abstract class BlockBase extends Block
{
	public static List<Block> rtBlockList = new ArrayList<>(20);

	protected BlockBase(String name, Material materialIn)
	{
		super(materialIn);

		this.setUnlocalizedName(name);
		this.setRegistryName(new ResourceLocation("randomthings", name));
		this.setCreativeTab(RandomThings.instance.creativeTab);

		GameRegistry.register(this);

		if (!(this instanceof INoItem))
		{
			GameRegistry.register(new ItemBlock(this).setRegistryName(this.getRegistryName()));
		}

		RandomThings.proxy.scheduleColor(this);

		if (this instanceof ISuperLubricent)
		{
			this.slipperiness = 1F / 0.98F;
		}

		rtBlockList.add(this);
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

		if (this instanceof ISuperLubricent)
		{
			this.slipperiness = 1F / 0.98F;
		}

		rtBlockList.add(this);
	}

	public static void registerBlock(String name, Block block)
	{
		block.setRegistryName(name);
		block.setCreativeTab(RandomThings.instance.creativeTab);
		block.setUnlocalizedName(name);

		GameRegistry.register(block);

		if (!(block instanceof INoItem))
		{
			GameRegistry.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
		}
		RandomThings.proxy.scheduleColor(block);

		rtBlockList.add(block);
	}
}
