package lumien.randomthings.item;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional;

@Optional.Interface(iface = "baubles.api.IBauble", modid = "baubles")
public class ItemObsidianSkullRing extends ItemBase implements IBauble
{
	public ItemObsidianSkullRing()
	{
		super("obsidianSkullRing");

		this.setMaxStackSize(1);
	}

	@Override
	public BaubleType getBaubleType(ItemStack itemstack)
	{
		return BaubleType.RING;
	}

	@Override
	public void onWornTick(ItemStack itemstack, EntityLivingBase player)
	{
	}

	@Override
	public void onEquipped(ItemStack itemstack, EntityLivingBase player)
	{
	}

	@Override
	public void onUnequipped(ItemStack itemstack, EntityLivingBase player)
	{
	}

	@Override
	public boolean canEquip(ItemStack itemstack, EntityLivingBase player)
	{
		return true;
	}

	@Override
	public boolean canUnequip(ItemStack itemstack, EntityLivingBase player)
	{
		return true;
	}

}
