package lumien.randomthings.handler.spectre;

import java.util.ArrayList;
import java.util.UUID;

import lumien.randomthings.block.ModBlocks;
import lumien.randomthings.item.ItemPositionFilter;
import lumien.randomthings.item.ModItems;
import lumien.randomthings.util.NBTUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IInventoryChangedListener;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SpectreCube implements IInventoryChangedListener
{
	UUID owner;
	ArrayList<UUID> guests;

	int height;
	int position;

	SpectreHandler handler;

	InventoryBasic cubeInventory = new InventoryBasic("SpectreCube", false, 2 + 9);

	BlockPos spawnBlock;

	public SpectreCube(SpectreHandler handler)
	{
		guests = new ArrayList<>();
		this.handler = handler;
		this.height = 2;

		cubeInventory.addInventoryChangeListener(this);
	}

	public SpectreCube(SpectreHandler handler, UUID owner, int position)
	{
		this(handler);

		this.owner = owner;
		this.position = position;

		spawnBlock = new BlockPos(position * 16 + 8, 0, 8);
	}

	public InventoryBasic getInventory()
	{
		return cubeInventory;
	}

	public void writeToNBT(NBTTagCompound compound)
	{
		compound.setString("owner", owner.toString());
		compound.setInteger("position", position);

		NBTTagList guestTagList = new NBTTagList();
		for (UUID uuid : guests)
		{
			guestTagList.appendTag(new NBTTagString(uuid.toString()));
		}
		compound.setTag("guests", guestTagList);

		compound.setInteger("height", height);

		NBTUtil.writeBlockPosToNBT(compound, "spawnBlock", spawnBlock);
	}

	public void readFromNBT(NBTTagCompound compound)
	{
		this.owner = UUID.fromString(compound.getString("owner"));
		this.position = compound.getInteger("position");
		NBTTagList guestTagList = compound.getTagList("guestTagList", (byte) 8);
		for (int i = 0; i < guestTagList.tagCount(); i++)
		{
			NBTTagString uuidString = (NBTTagString) guestTagList.get(i);
			this.guests.add(UUID.fromString(uuidString.getString()));
		}

		this.height = compound.getInteger("height");
		this.spawnBlock = NBTUtil.readBlockPosFromNBT(compound, "spawnBlock");
	}

	public UUID getOwner()
	{
		return owner;
	}

	public void generate(World worldObj)
	{
		BlockPos corner = new BlockPos(position * 16, 0, 0);

		BlockPos pos1 = corner;
		BlockPos pos2 = corner.add(15, height + 1, 15);

		generateCube(worldObj, pos1, pos2, ModBlocks.spectreBlock.getDefaultState(), 3);
		generateCube(worldObj, pos1.add(7, 0, 7), pos1.add(8, 0, 8), ModBlocks.spectreCore.getDefaultState(), 3);
	}

	public int increaseHeight(int amount)
	{
		int heightLeft = 255 - (height + 1);

		int difference = heightLeft - amount;

		int newHeight = height;

		if (difference > 0)
		{
			newHeight = height + amount;
		}
		else if (difference <= 0)
		{
			newHeight = height + heightLeft;
		}

		int change = newHeight - height;

		if (newHeight != height)
		{
			changeHeight(handler.getWorld(), newHeight);
		}

		return change;
	}

	private void changeHeight(World worldObj, int newHeight)
	{
		BlockPos corner = new BlockPos(position * 16, 0, 0);

		BlockPos pos1 = corner;
		BlockPos pos2 = corner.add(15, height + 1, 15);

		generateCube(worldObj, pos1, pos2, Blocks.AIR.getDefaultState(), 2);
		this.height = newHeight;
		generate(worldObj);

		handler.markDirty();
	}

	public BlockPos getSpawnBlock()
	{
		return spawnBlock;
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

	@Override
	public void onInventoryChanged(IInventory inventory)
	{
		ItemStack spawnPosStack = inventory.getStackInSlot(0);

		if (spawnPosStack == null || spawnPosStack.getItem() != ModItems.positionFilter)
		{
			spawnBlock = new BlockPos(position * 16 + 8, 0, 8);
		}
		else
		{
			spawnBlock = ItemPositionFilter.getPosition(spawnPosStack);
		}

		this.handler.markDirty();
	}
}
