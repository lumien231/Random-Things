package lumien.randomthings.item;

import lumien.randomthings.potion.ModPotions;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
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
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems)
	{
		if (this.isInCreativeTab(tab))
		{
			for (int i = 0; i < 4; i++)
			{
				subItems.add(new ItemStack(this, 1, i));
			}
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
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand)
	{
		ItemStack itemStackIn = playerIn.getHeldItem(hand);
		playerIn.setActiveHand(hand);
		return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase livingEntity)
	{
		if (livingEntity instanceof EntityPlayer)
		{
			EntityPlayer playerIn = (EntityPlayer) livingEntity;
			if (!playerIn.capabilities.isCreativeMode)
			{
				stack.shrink(1);
			}

			if (!worldIn.isRemote)
			{
				clearImbues(playerIn);

				switch (stack.getItemDamage())
				{
				case FIRE:
					playerIn.addPotionEffect(new PotionEffect(ModPotions.imbueFire, 60 * 20 * 20, 0, false, false));
					break;
				case POISON:
					playerIn.addPotionEffect(new PotionEffect(ModPotions.imbuePoison, 60 * 20 * 20, 0, false, false));
					break;
				case EXPERIENCE:
					playerIn.addPotionEffect(new PotionEffect(ModPotions.imbueExperience, 60 * 20 * 20, 0, false, false));
					break;
				case WITHER:
					playerIn.addPotionEffect(new PotionEffect(ModPotions.imbueWither, 60 * 20 * 20, 0, false, false));
					break;
				case COLLAPSE:
					playerIn.addPotionEffect(new PotionEffect(ModPotions.imbueCollapse, 60 * 20 * 20, 0, false, false));
					break;
				}
			}

			if (!playerIn.capabilities.isCreativeMode)
			{
				if (stack.getCount() <= 0)
				{
					return new ItemStack(Items.GLASS_BOTTLE);
				}
				else
				{
					playerIn.inventory.addItemStackToInventory(new ItemStack(Items.GLASS_BOTTLE));
				}
			}
		}
		return stack;
	}

	private void clearImbues(EntityPlayer player)
	{
		if (player.isPotionActive(ModPotions.imbueFire))
		{
			player.removePotionEffect(ModPotions.imbueFire);
		}
		else if (player.isPotionActive(ModPotions.imbuePoison))
		{
			player.removePotionEffect(ModPotions.imbuePoison);
		}
		else if (player.isPotionActive(ModPotions.imbueExperience))
		{
			player.removePotionEffect(ModPotions.imbueExperience);
		}
		else if (player.isPotionActive(ModPotions.imbueWither))
		{
			player.removePotionEffect(ModPotions.imbueWither);
		}
		else if (player.isPotionActive(ModPotions.imbueCollapse))
		{
			player.removePotionEffect(ModPotions.imbueCollapse);
		}
	}
}
