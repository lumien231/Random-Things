package lumien.randomthings.handler.compability.baubles;

import java.util.ArrayList;

import org.apache.logging.log4j.Level;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import lumien.randomthings.RandomThings;
import lumien.randomthings.util.WorldUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Loader;

public class BaublesSpectreAnchor
{
	public static void handleClone(PlayerEvent.Clone event)
	{
		if (Loader.isModLoaded("baubles"))
		{
			actualHandleClone(event);
		}
		else
		{
			return;
		}
	}
	
	public static void handleDropsEarly(PlayerDropsEvent event)
	{
		if (Loader.isModLoaded("baubles"))
		{
			actualHandleDropsEarly(event);
		}
		else
		{
			return;
		}
	}
	
	public static void handleDropsLate(PlayerDropsEvent event)
	{
		if (Loader.isModLoaded("baubles"))
		{
			actualHandleDropsLate(event);
		}
		else
		{
			return;
		}
	}
	
	private static void actualHandleClone(PlayerEvent.Clone event)
	{
		EntityPlayer oldPlayer = event.getOriginal();
		EntityPlayer newPlayer = event.getEntityPlayer();
		
		IBaublesItemHandler oldHandler = BaublesApi.getBaublesHandler(oldPlayer);
		IBaublesItemHandler newHandler = BaublesApi.getBaublesHandler(newPlayer);
		
		for (int i = 0; i < oldHandler.getSlots(); i++)
		{
			ItemStack is = oldHandler.getStackInSlot(i);

			if (!is.isEmpty() && is.hasTagCompound() && is.getTagCompound().hasKey("spectreAnchor"))
			{
				ItemStack newIs = newHandler.getStackInSlot(i);

				if (newIs.isEmpty())
				{
					newHandler.setStackInSlot(i, is.copy());
				}
				else
				{
					// Another mod put an ItemStack into the Baubles Slot
					ItemStack existing = newIs;

					int emptyStack = newPlayer.inventory.getFirstEmptyStack();
					if (emptyStack != -1)
					{
						newPlayer.inventory.setInventorySlotContents(emptyStack, existing);
						newPlayer.inventory.setInventorySlotContents(i, is.copy());
					}
					else
					{
						RandomThings.instance.logger.log(Level.INFO, "Couldn't keep Anchored Item in the Inventory");
						WorldUtil.spawnItemStack(oldPlayer.world, oldPlayer.posX, oldPlayer.posY, oldPlayer.posZ, is);
					}
				}
			}
		}
	}
	
	private static ItemStack[] savedBaubles = new ItemStack[20];
	
	private static void actualHandleDropsEarly(PlayerDropsEvent event)
	{
		IBaublesItemHandler oldHandler = BaublesApi.getBaublesHandler(event.getEntityPlayer());
		
		for (int i = 0; i < oldHandler.getSlots(); i++)
		{
			ItemStack is = oldHandler.getStackInSlot(i);
			
			if (!is.isEmpty() && is.hasTagCompound() && is.getTagCompound().hasKey("spectreAnchor"))
			{
				// Hiding Anchored item from Baubles so it doesn't drop it
				savedBaubles[i] = is;
				oldHandler.setStackInSlot(i, ItemStack.EMPTY);
			}
		}
	}
	
	private static void actualHandleDropsLate(PlayerDropsEvent event)
	{
		IBaublesItemHandler newHandler = BaublesApi.getBaublesHandler(event.getEntityPlayer());
		
		for (int slot = 0;slot<savedBaubles.length;slot++)
		{
			ItemStack is = savedBaubles[slot];
			
			if (is != null)
			{
				savedBaubles[slot] = null;
				
				if (!is.isEmpty())
				{
					// Reinsert item into baubles slot
					newHandler.setStackInSlot(slot, is);
				}
			}
		}
	}
}
