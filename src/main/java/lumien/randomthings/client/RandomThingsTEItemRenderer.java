package lumien.randomthings.client;

import lumien.randomthings.block.ModBlocks;
import lumien.randomthings.tileentity.TileEntitySpecialChest;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RandomThingsTEItemRenderer
{
	public static RandomThingsTEItemRenderer instance = new RandomThingsTEItemRenderer();

	TileEntitySpecialChest natureChest = new TileEntitySpecialChest(0);
	TileEntitySpecialChest waterChest = new TileEntitySpecialChest(1);

	public boolean renderByItem(ItemStack itemStackIn)
	{
		if (itemStackIn.getItem() instanceof ItemBlock)
		{
			ItemBlock ib = (ItemBlock) itemStackIn.getItem();

			if (ib.getBlock() == ModBlocks.specialChest)
			{
				switch (itemStackIn.getItemDamage())
				{
					case 0:
						TileEntityRendererDispatcher.instance.render(this.natureChest, 0.0D, 0.0D, 0.0D, 0.0F);
						return true;
					case 1:
						TileEntityRendererDispatcher.instance.render(this.waterChest, 0.0D, 0.0D, 0.0D, 0.0F);
						return true;
				}
			}
		}

		return false;
	}
}