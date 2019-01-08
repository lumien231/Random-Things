package lumien.randomthings.item.diviningrod;

import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import lumien.randomthings.handler.DiviningRodHandler;
import lumien.randomthings.item.ItemBase;
import lumien.randomthings.lib.IRTItemColor;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemDiviningRod extends ItemBase implements IRTItemColor
{
	public static List<RodType> types;
	public static Map<RodType, Boolean> availableTypes;

	static
	{
		types = new ArrayList<RodType>();
		availableTypes = new LinkedHashMap<RodType, Boolean>();

		types.add(new OreRodType("coal", "oreCoal", new Color(20, 20, 20, 50)));
		types.add(new OreRodType("iron", "oreIron", new Color(211, 180, 159, 50)));
		types.add(new OreRodType("gold", "oreGold", new Color(246, 233, 80, 50)));
		types.add(new OreRodType("lapis", "oreLapis", new Color(5, 45, 150, 50)));
		types.add(new OreRodType("redstone", "oreRedstone", new Color(211, 1, 1, 50)));
		types.add(new OreRodType("emerald", "oreEmerald", new Color(0, 220, 0, 50)));
		types.add(new OreRodType("diamond", "oreDiamond", new Color(87, 221, 229, 50)));
		types.add(new CombinedRodType("universal", types.toArray(new RodType[0])));

		// Thermal Foundation
		types.add(new OreRodType("copper", "oreCopper", new Color(252, 113, 21, 50)));
		types.add(new OreRodType("tin", "oreTin", new Color(150, 184, 217, 50)));
		types.add(new OreRodType("silver", "oreSilver", new Color(205, 231, 246, 50)));
		types.add(new OreRodType("lead", "oreLead", new Color(117, 133, 187, 50)));
		types.add(new OreRodType("aluminum", "oreAluminum", new Color(197, 197, 202, 50)));
		types.add(new OreRodType("nickel", "oreNickel", new Color(208, 206, 163, 50)));
		types.add(new OreRodType("platinum", "orePlatinum", new Color(42, 183, 252, 50)));
		types.add(new OreRodType("iridium", "oreIridium", new Color(176, 176, 202, 50)));
		types.add(new OreRodType("mithril", "oreMithril", new Color(97, 207, 252, 50)));

		// Draconic
		types.add(new OreRodType("draconium", "oreDraconium", new Color(75, 38, 107, 50)));

		// Tinkers
		types.add(new OreRodType("cobalt", "oreCobalt", new Color(5, 18, 64, 50)));
		types.add(new OreRodType("ardite", "oreArdite", new Color(138, 104, 38, 50)));
		
		// Actually Additions
		types.add(new OreRodType("blackquartz", "oreQuartzBlack", new Color(10, 10, 10, 50)));
		
		// Applied Energistics
		types.add(new OreRodType("certus", "oreCertusQuartz", new Color(136, 166, 193, 50)));
	}

	public ItemDiviningRod()
	{
		super("diviningRod");

		this.setHasSubtypes(true);
		this.setMaxStackSize(1);
	}

	public static void postInit()
	{
		types.stream().forEach((t) -> availableTypes.put(t, t.shouldBeAvailable()));
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
	{
		if (tab == this.getCreativeTab())
		{
			int i = 0;
			for (Entry<RodType, Boolean> e : availableTypes.entrySet())
			{
				if (e.getValue())
				{
					items.add(new ItemStack(this, 1, i));
				}
				i++;
			}
		}
	}

	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return super.getUnlocalizedName(stack) + "." + types.get(stack.getItemDamage()).name;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack stack)
	{
		EntityPlayer player = Minecraft.getMinecraft().player;

		if (player != null)
		{
			if (player.getHeldItemMainhand() == stack || player.getHeldItemOffhand() == stack)
			{
				RodType type = getRodType(stack);

				return DiviningRodHandler.get().shouldGlow(type);
			}
		}

		return super.hasEffect(stack);
	}

	public static RodType getRodType(ItemStack stack)
	{
		return types.get(stack.getItemDamage());
	}

	@Override
	public int getColorFromItemstack(ItemStack stack, int tintIndex)
	{
		int meta = stack.getItemDamage();
		
		if (tintIndex == 1 && meta < types.size())
		{
			return types.get(meta).getItemColor().getRGB();
		}
		else
		{
			return Color.WHITE.getRGB();
		}
	}
}
