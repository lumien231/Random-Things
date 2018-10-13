package lumien.randomthings.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import lumien.randomthings.config.Features;
import lumien.randomthings.item.ItemIngredient;
import lumien.randomthings.item.ModItems;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;

public class BlockPod extends BlockBase
{
	static ResourceLocation lootTable = new ResourceLocation("randomthings","beanpod");

	protected BlockPod()
	{
		super("beanPod", Material.PLANTS);

		this.setHardness(1.5F);
	}
	
	@Override
	public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune)
	{
        if (!worldIn.isRemote && !worldIn.restoringBlockSnapshots)
        {
    		Random random = new Random();
    		
    		LootTable loottable = worldIn.getLootTableManager().getLootTableFromLocation(lootTable);

            LootContext.Builder lootcontext$builder = new LootContext.Builder((WorldServer)worldIn);

            List<ItemStack> drops = loottable.generateLootForPools(random, lootcontext$builder.build());
        	
    		if (Features.GOLDEN_EGG)
    		{
    			drops.add(new ItemStack(ModItems.ingredients, 1, ItemIngredient.INGREDIENT.GOLDEN_EGG.id));
    		}
            
            for (ItemStack drop : drops)
            {
                if (worldIn.rand.nextFloat() <= chance)
                {
                    spawnAsEntity(worldIn, pos, drop);
                }
            }
        }
	}
}
