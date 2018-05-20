package lumien.randomthings.tileentity;

import java.util.Random;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;

public class TileEntityRuneBase extends TileEntityBase
{
	int[][] runeData = new int[4][4];

	public TileEntityRuneBase()
	{
		Random rng = new Random();
		for (int a = 0; a < 4; a++)
		{
			for (int b = 0; b < 4; b++)
			{
				runeData[a][b] = -1;
			}
		}
	}

	@Override
	public void writeDataToNBT(NBTTagCompound compound, boolean sync)
	{
		NBTTagList runeList = new NBTTagList();
		for (int i = 0; i < runeData.length; i++)
		{
			runeList.appendTag(new NBTTagIntArray(runeData[i]));
		}

		compound.setTag("runeData", runeList);
	}

	@Override
	public void readDataFromNBT(NBTTagCompound compound, boolean sync)
	{
		NBTTagList runeList = compound.getTagList("runeData", (byte) 11);

		for (int i = 0; i < runeList.tagCount(); i++)
		{
			runeData[i] = runeList.getIntArrayAt(i);
		}
	}

	public int[][] getRuneData()
	{
		return runeData;
	}

	public void setRuneData(int[][] newData)
	{
		this.runeData = newData;
	}

	@Override
	public boolean renderAfterData()
	{
		return true;
	}
}
