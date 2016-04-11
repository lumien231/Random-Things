package lumien.randomthings.item;

import java.util.List;

import lumien.randomthings.potion.ModPotions;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemIngredient extends ItemBase
{
	static int counter = 0;

	public enum INGREDIENT
	{
		SAKANADE_SPORES("sakanadeSpores"), EVIL_TEAR("evilTear"), ECTO_PLASM("ectoPlasm"), SPECTRE_INGOT("spectreIngot");

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

	public INGREDIENT getIngredient(ItemStack stack)
	{
		return INGREDIENT.values()[stack.getItemDamage()];
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
	{
		if (!worldIn.isRemote)
		{
			if (getIngredient(itemStackIn) == INGREDIENT.EVIL_TEAR && !playerIn.isPotionActive(ModPotions.boss))
			{
				playerIn.addPotionEffect(new PotionEffect(ModPotions.boss, 20 * 60 * 20));
				worldIn.playSound(null,playerIn.getPosition(), SoundEvents.ENTITY_PLAYER_BURP,SoundCategory.PLAYERS, 0.5F, worldIn.rand.nextFloat() * 0.1F + 0.4F);

				ItemStack reduce = itemStackIn.copy();
				if (!playerIn.capabilities.isCreativeMode)
				{
					reduce.stackSize--;
				}

				return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, reduce);
			}
		}

		return super.onItemRightClick(itemStackIn, worldIn, playerIn, hand);
	}
}
