package lumien.randomthings.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import lumien.randomthings.item.ItemIngredient;
import lumien.randomthings.item.ModItems;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockPod extends BlockBase
{

	protected BlockPod()
	{
		super("beanPod", Material.PLANTS);

		this.setHardness(1.5F);
	}

	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
		ArrayList<ItemStack> drops = new ArrayList<>();
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
			drops.add(new ItemStack(Items.IRON_INGOT, ironAmount));
		}

		if (goldAmount > 0)
		{
			drops.add(new ItemStack(Items.GOLD_INGOT, goldAmount));
		}

		if (diamondAmount > 0)
		{
			drops.add(new ItemStack(Items.DIAMOND, diamondAmount));
		}

		if (emeraldAmount > 0)
		{
			drops.add(new ItemStack(Items.EMERALD, emeraldAmount));
		}
		
		drops.add(new ItemStack(ModItems.ingredients,1,ItemIngredient.INGREDIENT.GOLDEN_EGG.id));

		return drops;
	}
}
