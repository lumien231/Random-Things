package lumien.randomthings.recipes;

import lumien.randomthings.block.ModBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class RecipeWorkbench extends ShapedOreRecipe
{

	public RecipeWorkbench()
	{
		super(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModBlocks.customWorkbench), "www", "wxw", "www", 'w', "plankWood", 'x', Blocks.CRAFTING_TABLE);

		this.setRegistryName(new ResourceLocation("randomthings", "customWorkbench"));
	}

	@Override
	public boolean matches(InventoryCrafting inv, World world)
	{
		boolean recipeMatches = super.matches(inv, world);

		ItemStack stack = ItemStack.EMPTY;

		if (recipeMatches)
		{
			for (int i = 0; i < inv.getSizeInventory(); i++)
			{
				ItemStack is = inv.getStackInSlot(i);

				if (!is.isEmpty() && !(is.getItem() == Item.getItemFromBlock(Blocks.CRAFTING_TABLE)))
				{
					if (!stack.isEmpty())
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

		ItemStack plank = ItemStack.EMPTY;

		for (int i = 0; i < var1.getSizeInventory(); i++)
		{
			if (!var1.getStackInSlot(i).isEmpty())
			{
				plank = var1.getStackInSlot(i);
			}
		}

		if (!plank.isEmpty())
		{
			compound.setString("woodName", ((ItemBlock) plank.getItem()).getBlock().getRegistryName().toString());
			compound.setInteger("woodMeta", plank.getItemDamage());

			return result;
		}
		else
		{
			return plank;
		}
	}
}
