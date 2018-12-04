package lumien.randomthings.item;

import java.awt.Color;
import java.util.List;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import lumien.randomthings.config.Numbers;
import lumien.randomthings.handler.spectrecoils.SpectreCoilHandler;
import lumien.randomthings.lib.ILuminousItem;
import lumien.randomthings.lib.IRTBlockColor;
import lumien.randomthings.lib.IRTItemColor;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Optional.Interface(iface = "baubles.api.IBauble", modid = "baubles")
public class ItemSpectreCharger extends ItemBase implements IRTItemColor, ILuminousItem, IBauble
{
	public enum TIER
	{
		NORMAL("normal", Color.CYAN.getRGB()), REDSTONE("redstone", Color.RED.getRGB()), ENDER("ender", new Color(200, 0, 210).getRGB()), GENESIS("genesis", Color.ORANGE.getRGB());

		int color;
		String name;

		private TIER(String name, int color)
		{
			this.name = name;
			this.color = color;
		}

		public String getName()
		{
			return name;
		}
	}

	public ItemSpectreCharger()
	{
		super("spectreCharger");

		this.setHasSubtypes(true);
		this.setMaxStackSize(1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced)
	{
		String display;

		switch (TIER.values()[stack.getItemDamage()])
		{
			case NORMAL:
				display = I18n.format("item.spectreCharger.charge", "1024");
				break;
			case REDSTONE:
				display = I18n.format("item.spectreCharger.charge", "4096");
				break;
			case ENDER:
				display = I18n.format("item.spectreCharger.charge", "20480");
				break;
			case GENESIS:
				display = I18n.format("item.spectreCharger.charge", "Infinite");
				break;
			default:
				display = I18n.format("item.spectreCharger.charge", "???");
				break;
		}

		tooltip.add(display);
	}

	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return super.getUnlocalizedName(stack) + "." + TIER.values()[stack.getItemDamage()].name;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		ItemStack me = playerIn.getHeldItem(handIn);
		if (worldIn.isRemote)
		{
			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, me);
		}

		NBTTagCompound options = me.getOrCreateSubCompound("options");

		boolean enabled = options.getBoolean("enabled");

		options.setBoolean("enabled", !enabled);

		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, me);
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
	{
		if (!worldIn.isRemote && entityIn instanceof EntityPlayerMP)
		{
			NBTTagCompound options = stack.getOrCreateSubCompound("options");

			if (options.getBoolean("enabled"))
			{
				EntityPlayerMP player = (EntityPlayerMP) entityIn;

				IEnergyStorage storage = SpectreCoilHandler.get(worldIn).getStorageCoil(player.getGameProfile().getId());

				int rate = 1;

				TIER tier = TIER.values()[stack.getItemDamage()];

				if (tier == TIER.NORMAL)
				{
					rate = 1024;
				}
				else if (tier == TIER.REDSTONE)
				{
					rate = 4096;
				}
				else if (tier == TIER.ENDER)
				{
					rate = 20480;
				}

				for (int slot = 0; slot < player.inventory.getSizeInventory(); slot++)
				{
					if (storage.getEnergyStored() == 0 && tier != TIER.GENESIS)
					{
						break;
					}

					ItemStack targetStack = player.inventory.getStackInSlot(slot);

					if (targetStack.hasCapability(CapabilityEnergy.ENERGY, null))
					{
						IEnergyStorage itemStorage = targetStack.getCapability(CapabilityEnergy.ENERGY, null);

						if (itemStorage != null)
						{
							int missingEnergy = itemStorage.getMaxEnergyStored() - itemStorage.getEnergyStored();

							if (tier == TIER.GENESIS)
							{
								itemStorage.receiveEnergy(missingEnergy, false);
								continue;
							}

							if (missingEnergy > 0)
							{
								int attemptExtractEnergy = Math.min(rate, missingEnergy);

								int energyExtracted = storage.extractEnergy(attemptExtractEnergy, false);

								int remainder = energyExtracted - itemStorage.receiveEnergy(energyExtracted, false);

								if (remainder > 0)
								{
									storage.receiveEnergy(remainder, false);
								}
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
	{
		if (tab == this.getCreativeTab())
		{
			for (TIER t : TIER.values())
			{
				items.add(new ItemStack(this, 1, t.ordinal()));
			}
		}
	}

	@Override
	public boolean shouldGlow(ItemStack stack, int tintIndex)
	{
		return tintIndex == 1;
	}

	@Override
	public int getColorFromItemstack(ItemStack stack, int tintIndex)
	{
		return tintIndex == 1 ? (TIER.values()[stack.getItemDamage()].color) : -1;
	}

	@Override
	public BaubleType getBaubleType(ItemStack itemstack)
	{
		return BaubleType.BELT;
	}
	
	@Override
	public void onWornTick(ItemStack itemstack, EntityLivingBase player)
	{
		onUpdate(itemstack, player.world, player, 0, false);
	}
}
