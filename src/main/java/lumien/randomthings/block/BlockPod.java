package lumien.randomthings.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import lumien.randomthings.item.ModItems;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockPod extends BlockBase
{

	protected BlockPod()
	{
		super("beanPod", Material.plants);
	}

	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
		Random rng = new Random();
		rng.setSeed(System.currentTimeMillis());

		int ironAmount = rng.nextInt(13) + 8;
		int goldAmount = rng.nextInt(12) + 4;
		int diamondAmount = rng.nextInt(5) + 1;
		int emeraldAmount = rng.nextInt(3);
		int beanAmount = rng.nextInt(5) + 4;

		if (beanAmount > 0)
		{
			drops.add(new ItemStack(ModItems.beans, beanAmount));
		}

		if (ironAmount > 0)
		{
			drops.add(new ItemStack(Items.iron_ingot, ironAmount));
		}

		if (goldAmount > 0)
		{
			drops.add(new ItemStack(Items.gold_ingot, goldAmount));
		}

		if (diamondAmount > 0)
		{
			drops.add(new ItemStack(Items.diamond, diamondAmount));
		}

		if (emeraldAmount > 0)
		{
			drops.add(new ItemStack(Items.emerald, emeraldAmount));
		}

		return drops;
	}
}
