package lumien.randomthings.util;

import javax.annotation.Nonnull;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.PlayerMainInvWrapper;
import net.minecraftforge.oredict.OreDictionary;

public class ItemUtil
{
	public static boolean areItemStackContentEqual(ItemStack is1, ItemStack is2)
	{
		if (is1.isEmpty() || is2.isEmpty())
		{
			return false;
		}

		if (is1.getItem() != is2.getItem())
		{
			return false;
		}

		if (!ItemStack.areItemStackTagsEqual(is1, is2))
		{
			return false;
		}

		return is1.getItemDamage() == is2.getItemDamage();
	}

	public static boolean areOreDictionaried(ItemStack is1, ItemStack is2)
	{
		if (is1.isEmpty() || is2.isEmpty())
		{
			return false;
		}
		int[] ids1 = OreDictionary.getOreIDs(is1);
		int[] ids2 = OreDictionary.getOreIDs(is2);

		for (int id1 : ids1)
		{
			for (int id2 : ids2)
			{
				if (id1 == id2)
				{
					return true;
				}
			}
		}

		return false;
	}
	
	public static void giveItemToPlayerSilent(EntityPlayer player, @Nonnull ItemStack stack, int preferredSlot)
    {
        IItemHandler inventory = new PlayerMainInvWrapper(player.inventory);
        World world = player.world;

        // try adding it into the inventory
        ItemStack remainder = stack;
        // insert into preferred slot first
        if(preferredSlot >= 0)
        {
            remainder = inventory.insertItem(preferredSlot, stack, false);
        }
        // then into the inventory in general
        if(!remainder.isEmpty())
        {
            remainder = ItemHandlerHelper.insertItemStacked(inventory, remainder, false);
        }

        // play sound if something got picked up
        if (!(player instanceof FakePlayer) && (remainder.isEmpty() || remainder.getCount() != stack.getCount()))
        {
            world.playSound(null, player.posX, player.posY + 0.5, player.posZ,
                    SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((world.rand.nextFloat() - world.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
        }

        // drop remaining itemstack into the world
        if (!remainder.isEmpty() && !world.isRemote)
        {
            EntityItem entityitem = new EntityItem(world, player.posX, player.posY + 0.5, player.posZ, stack);
            entityitem.setPickupDelay(40);
            entityitem.motionX = 0;
            entityitem.motionZ = 0;

            world.spawnEntity(entityitem);
        }
    }
	
    public static void setHeldItemSilent(EntityPlayer player, EnumHand hand, ItemStack stack)
    {
        if (hand == EnumHand.MAIN_HAND)
        {
        	setItemStackToSlotSilent(player, EntityEquipmentSlot.MAINHAND, stack);
        }
        else
        {
        	setItemStackToSlotSilent(player, EntityEquipmentSlot.OFFHAND, stack);
        }
    }
    
    public static void setItemStackToSlotSilent(EntityPlayer player,EntityEquipmentSlot slotIn, ItemStack stack)
    {
        if (slotIn == EntityEquipmentSlot.MAINHAND)
        {
        	player.inventory.mainInventory.set(player.inventory.currentItem, stack);
        }
        else if (slotIn == EntityEquipmentSlot.OFFHAND)
        {
            player.inventory.offHandInventory.set(0, stack);
        }
        else if (slotIn.getSlotType() == EntityEquipmentSlot.Type.ARMOR)
        {
            player.inventory.armorInventory.set(slotIn.getIndex(), stack);
        }
    }
}
