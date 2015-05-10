package lumien.randomthings.item;

import lumien.randomthings.lib.PlayerAbilitiesProperty;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemImmortalityChalice extends ItemBase
{
	public ItemImmortalityChalice()
	{
		super("chaliceOfImmortality");
		
		this.setMaxStackSize(1);
	}

	@Override
	public EnumRarity getRarity(ItemStack stack)
	{
		return EnumRarity.EPIC;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack)
	{
		return 60;
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
            PlayerAbilitiesProperty abilities = (PlayerAbilitiesProperty) playerIn.getExtendedProperties(PlayerAbilitiesProperty.KEY);
            abilities.setImmortal(true);
        }

        if (!playerIn.capabilities.isCreativeMode)
        {
            if (stack.stackSize <= 0)
            {
                return new ItemStack(Items.glass_bottle);
            }

            playerIn.inventory.addItemStackToInventory(new ItemStack(Items.glass_bottle));
        }

        return stack;
    }
}
