package lumien.randomthings.handler.magicavoxel;

import java.awt.Color;

public class Palette
{
	Color[] colorTable;

	public Palette(Color[] colorTable)
	{
		this.colorTable = colorTable;
	}

	public Color getColor(int index)
	{
		return colorTable[index];
	}
}
