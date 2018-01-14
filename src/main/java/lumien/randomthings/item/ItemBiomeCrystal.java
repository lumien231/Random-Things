package lumien.randomthings.item;

import java.awt.Color;
import java.lang.reflect.Field;
import java.util.List;

import lumien.randomthings.asm.MCPNames;
import lumien.randomthings.lib.IRTItemColor;
import lumien.randomthings.util.client.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBiomeCrystal extends ItemBase implements IRTItemColor
{
	static Field biomeNameField = null;
	static
	{
		try
		{
			biomeNameField = Biome.class.getDeclaredField(MCPNames.field("field_76791_y"));
		}
		catch (NoSuchFieldException e)
		{
			e.printStackTrace();
		}
		catch (SecurityException e)
		{
			e.printStackTrace();
		}

		biomeNameField.setAccessible(true);
	}

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
			try
			{
				myName = myName + " (" + biomeNameField.get(biome) + ")";
			}
			catch (IllegalArgumentException e)
			{
				e.printStackTrace();
			}
			catch (IllegalAccessException e)
			{
				e.printStackTrace();
			}
		}

		return myName;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag advanced)
	{
		super.addInformation(stack, world, tooltip, advanced);

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
			return RenderUtils.getBiomeColor(null, biome, Minecraft.getMinecraft().player.getPosition());
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
			return BiomeDictionary.hasType(biome, Type.MAGICAL);
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
