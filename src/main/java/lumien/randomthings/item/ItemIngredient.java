package lumien.randomthings.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemIngredient extends ItemBase
{
	static int counter = 0;

	public enum INGREDIENT
	{
		SAKANADE_SPORES("sakanadeSpores");

		public String name;

		public int id;

		INGREDIENT(String name)
		{
			this.name = name;
			this.id = counter++;
		}
	}

	public ItemIngredient()
	{
		super("ingredient");
		
		this.setHasSubtypes(true);
	}

	@Override
	public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems)
	{
		for (INGREDIENT i : INGREDIENT.values())
		{
			subItems.add(new ItemStack(this, 1, i.id));
		}
	}

	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		int id = stack.getItemDamage();

		if (id >= 0 && id < INGREDIENT.values().length)
		{
			return "item.ingredient." + INGREDIENT.values()[id].name;
		}
		else
		{
			return "item.ingredient.invalid";
		}
	}
}
