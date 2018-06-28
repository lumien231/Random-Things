package lumien.randomthings.item.block;

import java.util.Random;

import lumien.randomthings.RandomThings;
import lumien.randomthings.block.ModBlocks;
import lumien.randomthings.lib.IRTBlockColor;
import lumien.randomthings.lib.IRTItemColor;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBlockSpectreCoil extends ItemBlock implements IRTItemColor
{
	public ItemBlockSpectreCoil(Block block)
	{
		super(block);

		RandomThings.proxy.scheduleColor(this);
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack)
	{
		if (this.block == ModBlocks.spectreCoilNumber)
		{
			Random rnd = new Random(System.identityHashCode(stack));
			
			int number = rnd.nextInt(499) + 1;
			
			return String.format(super.getItemStackDisplayName(stack), number);
		}

		return super.getItemStackDisplayName(stack);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemstack(ItemStack stack, int tintIndex)
	{
		return ((IRTBlockColor) this.block).colorMultiplier(null, Minecraft.getMinecraft().world, Minecraft.getMinecraft().player.getPosition(), tintIndex);
	}
}
