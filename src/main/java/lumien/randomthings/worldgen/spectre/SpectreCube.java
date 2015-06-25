package lumien.randomthings.worldgen.spectre;

import java.util.ArrayList;
import java.util.UUID;

import lumien.randomthings.block.ModBlocks;
import lumien.randomthings.util.Size;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class SpectreCube
{
	UUID master;
	ArrayList<UUID> guests;

	Size size;
	int position;

	public SpectreCube()
	{
		guests = new ArrayList<UUID>();
	}

	public void writeToNBT(NBTTagCompound compound)
	{
		compound.setString("master", master.toString());
		NBTTagList guestTagList = new NBTTagList();
		for (UUID uuid : guests)
		{
			guestTagList.appendTag(new NBTTagString(uuid.toString()));
		}
		compound.setTag("guests", guestTagList);

		compound.setInteger("width", size.getWidth());
		compound.setInteger("height", size.getHeight());
	}

	public void readFromNBT(NBTTagCompound compound)
	{
		this.master = UUID.fromString(compound.getString("master"));
		NBTTagList guestTagList = compound.getTagList("guestTagList", (byte) 8);
		for (int i = 0; i < guestTagList.tagCount(); i++)
		{
			NBTTagString uuidString = (NBTTagString) guestTagList.get(i);
			this.guests.add(UUID.fromString(uuidString.getString()));
		}

		this.size = new Size(compound.getInteger("width"), compound.getInteger("height"));
	}

	public UUID getOwner()
	{
		return master;
	}

	public void generate(World worldObj)
	{
		BlockPos center = new BlockPos(position * 16 + 8, 50, 8);

		BlockPos pos1 = center.add(-(size.getWidth() / 2), 0, -(size.getWidth() / 2));
		BlockPos pos2 = center.add((size.getWidth() / 2) - 1, size.getHeight(), (size.getWidth() / 2) - 1);

		generateCube(worldObj, pos1, pos2, ModBlocks.spectreBlock.getDefaultState(), 3);
	}

	public void changeSize(World worldObj, Size newSize)
	{
		BlockPos center = new BlockPos(position * 16 + 8, 50, 8);

		BlockPos pos1 = center.add(-(size.getWidth() / 2), 0, -(size.getWidth() / 2));
		BlockPos pos2 = center.add((size.getWidth() / 2) - 1, size.getHeight(), (size.getWidth() / 2) - 1);

		generateCube(worldObj, pos1, pos2, Blocks.air.getDefaultState(), 3);
		this.size = newSize;
		generate(worldObj);
	}

	public BlockPos getSpawnBlock()
	{
		return new BlockPos(position * 16 + 8, 50, 8);
	}

	private static void generateCube(World worldObj, BlockPos pos1, BlockPos pos2, IBlockState state, int flag)
	{
		int minX = Math.min(pos1.getX(), pos2.getX());
		int minY = Math.min(pos1.getY(), pos2.getY());
		int minZ = Math.min(pos1.getZ(), pos2.getZ());

		int maxX = Math.max(pos1.getX(), pos2.getX());
		int maxY = Math.max(pos1.getY(), pos2.getY());
		int maxZ = Math.max(pos1.getZ(), pos2.getZ());

		for (int x = minX; x <= maxX; x++)
		{
			for (int y = minY; y <= maxY; y++)
			{
				for (int z = minZ; z <= maxZ; z++)
				{
					if (x == minX || y == minY || z == minZ || x == maxX || y == maxY || z == maxZ)
					{
						worldObj.setBlockState(new BlockPos(x, y, z), state, flag);
					}
				}
			}
		}
	}
}
