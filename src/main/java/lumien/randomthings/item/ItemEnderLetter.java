package lumien.randomthings.item;

import java.util.List;

import lumien.randomthings.RandomThings;
import lumien.randomthings.lib.GuiIds;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class ItemEnderLetter extends ItemBase
{
	public ItemEnderLetter()
	{
		super("enderLetter");

		this.setMaxStackSize(1);
	}

	@Override
	public boolean hasEffect(ItemStack stack)
	{
		NBTTagCompound compound;

		if ((compound = stack.getTagCompound()) != null)
		{
			return compound.getBoolean("received");
		}

		return super.hasEffect(stack);
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged)
	{
		return oldStack.getItem() != newStack.getItem();
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List tooltip, boolean advanced)
	{
		super.addInformation(stack, playerIn, tooltip, advanced);

		NBTTagCompound compound;

		if ((compound = stack.getTagCompound()) != null)
		{
			if (compound.hasKey("sender"))
			{
				tooltip.add(I18n.format("item.enderLetter.sender", compound.getString("sender")));
			}
			if (compound.hasKey("receiver"))
			{
				tooltip.add(I18n.format("item.enderLetter.receiver", compound.getString("receiver")));
			}
		}
	}

	@Override
	public boolean doesSneakBypassUse(World world, BlockPos pos, EntityPlayer player)
	{
		return true;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn)
	{
		if (!worldIn.isRemote)
		{
			playerIn.openGui(RandomThings.instance, GuiIds.ENDER_LETTER, worldIn, 0, 0, 0);
		}

		return itemStackIn;
	}
}
