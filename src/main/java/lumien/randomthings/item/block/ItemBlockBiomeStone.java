package lumien.randomthings.item.block;

import lumien.randomthings.RandomThings;
import lumien.randomthings.block.BlockBiomeStone;
import lumien.randomthings.block.ModBlocks;
import lumien.randomthings.lib.IRTItemColor;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBlockBiomeStone extends ItemBlock implements IRTItemColor
{
	public ItemBlockBiomeStone(Block block)
	{
		super(block);

		RandomThings.proxy.scheduleColor(this);
		this.setHasSubtypes(true);
	}

	@Override
	public int getMetadata(int damage)
	{
		return damage;
	}

	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return super.getUnlocalizedName() + "." + BlockBiomeStone.EnumType.byMetadata(stack.getItemDamage()).getUnlocalizedName();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemstack(ItemStack stack, int tintIndex)
	{
		return ModBlocks.biomeStone.colorMultiplier(null, Minecraft.getMinecraft().world, Minecraft.getMinecraft().player.getPosition(), 0);
	}
}
