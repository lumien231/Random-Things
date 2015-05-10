package lumien.randomthings.item;

import java.util.List;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Optional.Interface(iface = "baubles.api.IBauble", modid = "Baubles")
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
