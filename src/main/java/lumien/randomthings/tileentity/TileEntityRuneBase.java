package lumien.randomthings.tileentity;

import java.util.Random;

import lumien.randomthings.block.ModBlocks;
import lumien.randomthings.handler.runes.EnumRuneDust;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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
	public void writeDataToNBT(NBTTagCompound compound)
	{
		NBTTagList runeList = new NBTTagList();
		for (int i = 0; i < runeData.length; i++)
		{
			runeList.appendTag(new NBTTagIntArray(runeData[i]));
		}

		compound.setTag("runeData", runeList);
	}

	@Override
	public void readDataFromNBT(NBTTagCompound compound)
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
	
	@Override
	public boolean renderAfterData()
	{
		return true;
	}
}
