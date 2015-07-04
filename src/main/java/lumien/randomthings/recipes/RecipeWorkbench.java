package lumien.randomthings.recipes;

import lumien.randomthings.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.UniqueIdentifier;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class RecipeWorkbench extends ShapedOreRecipe
{

	public RecipeWorkbench()
	{
		super(new ItemStack(ModBlocks.customWorkbench), "www", "wxw", "www", 'w', "plankWood", 'x', Blocks.crafting_table);
	}

	@Override
	public boolean matches(InventoryCrafting inv, World world)
	{
		boolean recipeMatches = super.matches(inv, world);

		ItemStack stack = null;

		if (recipeMatches)
		{
			for (int i = 0; i < inv.getSizeInventory(); i++)
			{
				ItemStack is = inv.getStackInSlot(i);

				if (is != null && !(is.getItem() == Item.getItemFromBlock(Blocks.crafting_table)))
				{
					if (stack != null)
					{
						if (!(ItemStack.areItemsEqual(stack, is)) || is.getMetadata() > 15 && !(is.getItem() instanceof ItemBlock))
						{
							return false;
						}
					}
					else
					{
						stack = is;
					}
				}
			}
		}

		return recipeMatches;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting var1)
	{
		ItemStack result = this.output.copy();

		result.setTagCompound(new NBTTagCompound());

		NBTTagCompound compound = result.getTagCompound();

		ItemStack plank = null;

		for (int i = 0; i < var1.getSizeInventory(); i++)
		{
			if (var1.getStackInSlot(i) != null)
			{
				plank = var1.getStackInSlot(i);
			}
		}

		if (plank != null)
		{
			UniqueIdentifier ui = GameRegistry.findUniqueIdentifierFor(((ItemBlock) plank.getItem()).getBlock());
			compound.setString("woodName", ui.toString());
			compound.setInteger("woodMeta", plank.getItemDamage());

			return result;
		}
		else
		{
			return null;
		}
	}
}
