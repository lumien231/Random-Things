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

public class ItemBlockAncientBrick extends ItemBlock
{
	public ItemBlockAncientBrick(Block block)
	{
		super(block);

		this.setHasSubtypes(true);
	}

	@Override
	public int getMetadata(int damage)
	{
		return damage;
	}
}
