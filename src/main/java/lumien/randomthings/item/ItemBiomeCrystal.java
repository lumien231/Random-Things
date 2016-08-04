package lumien.randomthings.item;

import java.awt.Color;
import java.util.List;

import lumien.randomthings.lib.IRTItemColor;
import lumien.randomthings.util.client.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBiomeCrystal extends ItemBase implements IRTItemColor
{
	public ItemBiomeCrystal()
	{
		super("biomeCrystal");

		this.setMaxStackSize(1);
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack)
	{
		String myName = super.getItemStackDisplayName(stack);

		Biome biome;

		if ((biome = getBiome(stack)) != null)
		{
			myName = myName + " (" + biome.getBiomeName() + ")";
		}

		return myName;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
	{
		super.addInformation(stack, playerIn, tooltip, advanced);

		Biome biome;

		if ((biome = getBiome(stack)) != null)
		{
			tooltip.add(biome.getBiomeName());
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemstack(ItemStack stack, int tintIndex)
	{
		Biome biome;

		if ((biome = getBiome(stack)) != null)
		{
			return RenderUtils.getBiomeColor(null, biome, Minecraft.getMinecraft().thePlayer.getPosition());
		}

		return Color.WHITE.getRGB();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack stack)
	{
		Biome biome;

		if ((biome = getBiome(stack)) != null)
		{
			return BiomeDictionary.isBiomeOfType(biome, Type.MAGICAL);
		}

		return false;
	}

	public static Biome getBiome(ItemStack stack)
	{
		NBTTagCompound compound;

		if ((compound = stack.getTagCompound()) != null && compound.hasKey("biomeName"))
		{
			String biomeName = compound.getString("biomeName");

			Biome biome = Biome.REGISTRY.getObject(new ResourceLocation(biomeName));

			return biome;
		}

		return null;
	}
}
