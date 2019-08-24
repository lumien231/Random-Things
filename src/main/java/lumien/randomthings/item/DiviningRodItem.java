package lumien.randomthings.item;

import java.awt.Color;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

/**
 * DiviningRodItem
 */
public class DiviningRodItem extends Item
{
	String[] detectingTags;
	Color[] colors;

	public DiviningRodItem(Properties properties, Color[] colors, String[] detectingTags)
	{
		super(properties.maxStackSize(1));

		this.detectingTags = detectingTags;
		this.colors = colors;
	}

	public Color getColor(int index)
	{
		return colors[index];
	}

	public String[] getDetectedTags()
	{
		return detectingTags;
	}

	public int matches(BlockState blockState)
	{
		for (int i = 0; i < detectingTags.length; i++)
		{
			Tag<Block> t = BlockTags.getCollection().get(new ResourceLocation(detectingTags[i]));

			if (t != null && t.contains(blockState.getBlock()))
			{
				return i;
			}
		}
		return -1;
	}
}