package lumien.randomthings.item;

import java.util.ArrayList;
import java.util.List;

import lumien.randomthings.handler.DiviningRodHandler;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemDiviningRod extends ItemBase
{
	public static List<Rod> types;

	static
	{
		types = new ArrayList<Rod>();
		types.add(new Rod("coal", new Block[] { Blocks.COAL_ORE }));
		types.add(new Rod("iron", new Block[] { Blocks.IRON_ORE }));
		types.add(new Rod("gold", new Block[] { Blocks.GOLD_ORE }));
		types.add(new Rod("lapis", new Block[] { Blocks.LAPIS_ORE }));
		types.add(new Rod("redstone", new Block[] { Blocks.REDSTONE_ORE }));
		types.add(new Rod("emerald", new Block[] { Blocks.EMERALD_ORE }));
		types.add(new Rod("diamond", new Block[] { Blocks.DIAMOND_ORE }));
		types.add(new Rod("universal", new Block[] { Blocks.COAL_ORE, Blocks.IRON_ORE, Blocks.GOLD_ORE, Blocks.LAPIS_ORE, Blocks.REDSTONE_ORE, Blocks.EMERALD_ORE, Blocks.DIAMOND_ORE }));
	}

	public ItemDiviningRod()
	{
		super("diviningRod");
		
		this.setHasSubtypes(true);
		this.setMaxStackSize(1);
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
	{
		if (tab == this.getCreativeTab())
		{
			for (int i = 0; i < types.size(); i++)
			{
				items.add(new ItemStack(this, 1, i));
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
				Block[] detectedBlocks = getDetectedBlocks(stack);

				if (detectedBlocks.length == 1)
				{
					return DiviningRodHandler.get().shouldGlow(detectedBlocks[0]);
				}
				else
				{
					return DiviningRodHandler.get().shouldGlow(null);
				}
			}
		}
		return super.hasEffect(stack);
	}

	public static Block[] getDetectedBlocks(ItemStack stack)
	{
		return types.get(stack.getItemDamage()).detectedBlocks;
	}

	public static class Rod
	{
		public String name;
		Block[] detectedBlocks;

		public Rod(String name, Block[] detectedBlocks)
		{
			this.name = name;
			this.detectedBlocks = detectedBlocks;
		}
	}
}
