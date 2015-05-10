package lumien.randomthings.item;

import java.util.List;

import lumien.randomthings.RandomThings;
import lumien.randomthings.lib.GuiIds;
import lumien.randomthings.util.InventoryUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemCraftingRecipe extends ItemBase
{
	public ItemCraftingRecipe()
	{
		super("craftingRecipe");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List tooltip, boolean advanced)
	{
		super.addInformation(stack, playerIn, tooltip, advanced);

		NBTTagCompound compound;

		if ((compound = stack.getTagCompound()) != null)
		{
			if (compound.hasKey("display"))
			{
				tooltip.add(compound.getString("display"));
			}
		}
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn)
	{
		if (!worldIn.isRemote)
		{
			playerIn.openGui(RandomThings.instance, GuiIds.CRAFTING_RECIPE, worldIn, playerIn.getPosition().getX(), playerIn.getPosition().getY(), playerIn.getPosition().getZ());
		}

		return itemStackIn;
	}

	public static void save(ItemStack toSave, InventoryCrafting craftMatrix, IInventory craftResult)
	{
		NBTTagCompound compound = toSave.getTagCompound();
		if (compound == null)
		{
			compound = new NBTTagCompound();
			toSave.setTagCompound(compound);
		}
		NBTTagCompound matrix = new NBTTagCompound();
		NBTTagCompound result = new NBTTagCompound();
		InventoryUtil.saveInventoryInCompound(matrix, craftMatrix);
		InventoryUtil.saveInventoryInCompound(result, craftResult);

		compound.setTag("matrix", matrix);
		compound.setTag("result", result);

		if (craftResult.getStackInSlot(0) != null)
		{
			compound.setString("display", craftResult.getStackInSlot(0).getDisplayName());
		}
	}

	public static void load(ItemStack openedWith, InventoryCrafting craftMatrix, IInventory craftResult)
	{
		NBTTagCompound compound;
		if ((compound = openedWith.getTagCompound()) != null)
		{
			InventoryUtil.readInventoryFromCompound(compound.getCompoundTag("matrix"), craftMatrix);
			InventoryUtil.readInventoryFromCompound(compound.getCompoundTag("result"), craftResult);
		}
	}
}
