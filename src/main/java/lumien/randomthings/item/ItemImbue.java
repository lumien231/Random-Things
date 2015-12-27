package lumien.randomthings.item;

import java.util.List;

import lumien.randomthings.potion.ModPotions;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemImbue extends ItemBase
{
	final static int FIRE = 0;
	final static int POISON = 1;
	final static int EXPERIENCE = 2;
	final static int WITHER = 3;
	final static int COLLAPSE = 4;

	public ItemImbue()
	{
		super("imbue");

		this.setHasSubtypes(true);
		this.setMaxDamage(0);
	}

	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		switch (stack.getItemDamage())
		{
			case FIRE:
				return "item.imbue.fire";
			case POISON:
				return "item.imbue.poison";
			case EXPERIENCE:
				return "item.imbue.experience";
			case WITHER:
				return "item.imbue.wither";
			case COLLAPSE:
				return "item.imbue.collapse";
		}
		return "item.imbue.invalid";
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item itemIn, CreativeTabs tab, List subItems)
	{
		for (int i = 0; i < 5; i++)
		{
			subItems.add(new ItemStack(this, 1, i));
		}
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack)
	{
		return 32;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack)
	{
		return EnumAction.DRINK;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn)
	{
		playerIn.setItemInUse(itemStackIn, this.getMaxItemUseDuration(itemStackIn));
		return itemStackIn;
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityPlayer playerIn)
	{
		if (!playerIn.capabilities.isCreativeMode)
		{
			--stack.stackSize;
		}

		if (!worldIn.isRemote)
		{
			clearImbues(playerIn);

			switch (stack.getItemDamage())
			{
				case FIRE:
					playerIn.addPotionEffect(new PotionEffect(ModPotions.imbueFire.id, 60 * 5 * 20));
					break;
				case POISON:
					playerIn.addPotionEffect(new PotionEffect(ModPotions.imbuePoison.id, 60 * 5 * 20));
					break;
				case EXPERIENCE:
					playerIn.addPotionEffect(new PotionEffect(ModPotions.imbueExperience.id, 60 * 5 * 20));
					break;
				case WITHER:
					playerIn.addPotionEffect(new PotionEffect(ModPotions.imbueWither.id, 60 * 5 * 20));
					break;
				case COLLAPSE:
					playerIn.addPotionEffect(new PotionEffect(ModPotions.imbueCollapse.id, 60 * 5 * 20));
					break;
			}
		}

		if (!playerIn.capabilities.isCreativeMode)
		{
			if (stack.stackSize <= 0)
			{
				return new ItemStack(Items.glass_bottle);
			}
			else
			{
				playerIn.inventory.addItemStackToInventory(new ItemStack(Items.glass_bottle));
			}
		}

		return stack;
	}

	private void clearImbues(EntityPlayer player)
	{
		if (player.isPotionActive(ModPotions.imbueFire))
		{
			player.removePotionEffect(ModPotions.imbueFire.id);
		}
		else if (player.isPotionActive(ModPotions.imbuePoison))
		{
			player.removePotionEffect(ModPotions.imbuePoison.id);
		}
		else if (player.isPotionActive(ModPotions.imbueExperience))
		{
			player.removePotionEffect(ModPotions.imbueExperience.id);
		}
		else if (player.isPotionActive(ModPotions.imbueWither))
		{
			player.removePotionEffect(ModPotions.imbueWither.id);
		}
		else if (player.isPotionActive(ModPotions.imbueCollapse))
		{
			player.removePotionEffect(ModPotions.imbueCollapse.id);
		}
	}
}
