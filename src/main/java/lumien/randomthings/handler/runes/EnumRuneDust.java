package lumien.randomthings.handler.runes;

import net.minecraft.item.ItemDye;

public enum EnumRuneDust
{
	WHITE(ItemDye.DYE_COLORS[15]), ORANGE(ItemDye.DYE_COLORS[14]), MAGENTA(ItemDye.DYE_COLORS[13]), LIGHT_BLUE(ItemDye.DYE_COLORS[12]), YELLOW(ItemDye.DYE_COLORS[11]), LIME(ItemDye.DYE_COLORS[10]), PINK(ItemDye.DYE_COLORS[9]), GRAY(ItemDye.DYE_COLORS[8]), SILVER(ItemDye.DYE_COLORS[7]), CYAN(ItemDye.DYE_COLORS[6]), PURPLE(ItemDye.DYE_COLORS[5]), BLUE(ItemDye.DYE_COLORS[4]), BROWN(ItemDye.DYE_COLORS[3]), GREEN(ItemDye.DYE_COLORS[2]), RED(ItemDye.DYE_COLORS[1]), BLACK(ItemDye.DYE_COLORS[0]);
	;

	int color;

	EnumRuneDust(int color)
	{
		this.color = color;
	}

	public String getName()
	{
		return this.name().toLowerCase();
	}

	public static int getColor(int index)
	{
		return values()[index].color;
	}
}
