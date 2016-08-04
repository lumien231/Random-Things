package lumien.randomthings.item.block;

import lumien.randomthings.RandomThings;
import lumien.randomthings.lib.IRTBlockColor;
import lumien.randomthings.lib.IRTItemColor;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBlockColored extends ItemBlock implements IRTItemColor
{
	public ItemBlockColored(Block block)
	{
		super(block);
		
		RandomThings.proxy.scheduleColor(this);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemstack(ItemStack stack, int tintIndex)
	{
		return ((IRTBlockColor)this.block).colorMultiplier(null, Minecraft.getMinecraft().theWorld, Minecraft.getMinecraft().thePlayer.getPosition(), tintIndex);
	}
}
