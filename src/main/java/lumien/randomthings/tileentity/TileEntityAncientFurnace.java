package lumien.randomthings.tileentity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import lumien.randomthings.config.Numbers;
import lumien.randomthings.lib.AncientFurnaceConversion;
import lumien.randomthings.util.NBTUtil;
import lumien.randomthings.util.WorldUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.biome.Biome;

public class TileEntityAncientFurnace extends TileEntityBase implements ITickable
{
	public enum STATE
	{
		IDLE, STARTING, RUNNING;
	}

	STATE state;

	// Starting
	int startingCounter;
	List<BlockPos> toBreak;

	// Running
	LinkedHashMap<BlockPos, Boolean[]> nextCheckEntries;
	long transformCount;

	public TileEntityAncientFurnace()
	{
		this.state = STATE.IDLE;
	}

	@Override
	public void writeDataToNBT(NBTTagCompound compound, boolean sync)
	{
		compound.setInteger("state", state.ordinal());

		if (!sync && state == STATE.RUNNING)
		{
			NBTTagList tagList = new NBTTagList();

			Iterator<Entry<BlockPos, Boolean[]>> iterator = nextCheckEntries.entrySet().iterator();

			while (iterator.hasNext())
			{
				Entry<BlockPos, Boolean[]> entry = iterator.next();

				NBTTagCompound entryCompound = new NBTTagCompound();

				NBTUtil.writeBlockPosToNBT(entryCompound, "pos", entry.getKey());

				for (int i = 0; i < entry.getValue().length; i++)
				{
					entryCompound.setBoolean("facing" + i, entry.getValue()[i]);
				}

				tagList.appendTag(entryCompound);
			}

			compound.setTag("nextCheckEntries", tagList);
			compound.setLong("transformCount", transformCount);
		}

		if (state == STATE.STARTING)
		{
			if (!sync)
			{
				NBTTagList toBreakTagList = new NBTTagList();
				for (BlockPos p : toBreak)
				{
					NBTTagCompound posCompound = new NBTTagCompound();
					NBTUtil.writeBlockPosToNBT(posCompound, "data", p);

					toBreakTagList.appendTag(posCompound);
				}

				compound.setTag("toBreak", toBreakTagList);
			}

			compound.setInteger("startingCounter", startingCounter);
		}
	}

	@Override
	public void readDataFromNBT(NBTTagCompound compound, boolean sync)
	{
		this.state = STATE.values()[compound.getInteger("state")];

		if (!sync && state == STATE.RUNNING)
		{
			nextCheckEntries = new LinkedHashMap<BlockPos, Boolean[]>();
			NBTTagList tagList = compound.getTagList("nextCheckEntries", 10);

			for (int i = 0; i < tagList.tagCount(); i++)
			{
				NBTTagCompound entryCompound = tagList.getCompoundTagAt(i);

				BlockPos pos = NBTUtil.readBlockPosFromNBT(entryCompound, "pos");

				Boolean[] facingArray = new Boolean[EnumFacing.HORIZONTALS.length];

				for (int b = 0; b < facingArray.length; b++)
				{
					facingArray[b] = entryCompound.getBoolean("facing" + b);
				}

				nextCheckEntries.put(pos, facingArray);
			}

			this.transformCount = compound.getLong("transformCount");
		}

		if (state == STATE.STARTING)
		{
			if (!sync)
			{
				toBreak = new ArrayList<BlockPos>();

				NBTTagList toBreakTagList = compound.getTagList("toBreak", 10);

				for (int i = 0; i < toBreakTagList.tagCount(); i++)
				{
					toBreak.add(NBTUtil.readBlockPosFromNBT(toBreakTagList.getCompoundTagAt(i), "data"));
				}
			}

			this.startingCounter = compound.getInteger("startingCounter");
		}
	}

	@Override
	public void update()
	{
		if (this.state == STATE.RUNNING)
		{
			if (!this.world.isRemote)
			{
				if (!nextCheckEntries.isEmpty() && transformCount <= Numbers.ANCIENT_FURNACE_LIMIT)
				{
					Iterator<Entry<BlockPos, Boolean[]>> iterator = nextCheckEntries.entrySet().iterator();
					Entry<BlockPos, Boolean[]> nextEntry = iterator.next();
					iterator.remove();

					BlockPos nextPos = nextEntry.getKey();

					Biome b = this.world.getBiome(nextPos);

					Biome conversion = AncientFurnaceConversion.getHeatingConversion(b);

					if (conversion != null)
					{
						BlockPos topPos = WorldUtil.getHeighestPos(this.world, nextPos.getX(), nextPos.getZ());

						WorldUtil.setBiome(this.world, nextPos, conversion);
						transformCount++;

						IBlockState topState = this.world.getBlockState(topPos);

						if (topState.getBlock() == Blocks.SNOW_LAYER)
						{
							this.world.setBlockToAir(topPos);
						}
						else if (topState.getBlock() == Blocks.ICE)
						{
							this.world.setBlockState(topPos, Blocks.WATER.getDefaultState());
						}

						for (EnumFacing facing : EnumFacing.HORIZONTALS)
						{
							if (nextEntry.getValue()[facing.getHorizontalIndex()])
							{
								BlockPos addingPos = nextPos.offset(facing);

								if (nextCheckEntries.containsKey(addingPos))
								{
									Boolean[] existingArray = nextCheckEntries.get(addingPos);
									existingArray[facing.getOpposite().getHorizontalIndex()] = false;
								}
								else
								{
									Boolean[] newArray = new Boolean[EnumFacing.HORIZONTALS.length];

									for (int i = 0; i < newArray.length; i++)
									{
										newArray[i] = true;
									}

									newArray[facing.getOpposite().getHorizontalIndex()] = false;

									nextCheckEntries.put(addingPos, newArray);
								}
							}
						}
					}
				}
				else
				{
					for (int modX = -1; modX < 2; modX++)
					{
						for (int modY = -1; modY < 2; modY++)
						{
							for (int modZ = -1; modZ < 2; modZ++)
							{
								world.setBlockToAir(this.pos.add(modX, modY, modZ));
							}
						}
					}

					Explosion explosion = new Explosion(this.world, null, this.pos.getX() + 0.5, this.pos.getY() + 0.5, this.pos.getZ() + 0.5, 4, true, true);
					explosion.doExplosionA();
					explosion.doExplosionB(true);
				}
			}
			else
			{
				for (int i = 0; i < 3; i++)
				{
					world.spawnParticle(EnumParticleTypes.FLAME, this.pos.getX() + 0.2 + Math.random() * 0.6, this.pos.getY() - 1, this.pos.getZ() + 0.2 + Math.random() * 0.6, 0.0D, -(Math.random() * 0.2), 0.0D);
				}
			}
		}
		else if (state == STATE.STARTING)
		{
			startingCounter = Math.min(20 * 20, startingCounter + 1);

			if (!this.world.isRemote)
			{
				if (!toBreak.isEmpty() && startingCounter % 4 == 0)
				{
					BlockPos nextBreak = toBreak.remove(toBreak.size() - 1);

					if (!world.isAirBlock(nextBreak))
					{
						world.setBlockToAir(nextBreak);

						world.playSound((EntityPlayer) null, nextBreak, SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
						world.playEvent(2000, nextBreak, 4);
					}
				}
				else if (startingCounter == 20 * 20)
				{
					run();
				}
			}

		}
	}

	public void start()
	{
		if (this.state == STATE.IDLE)
		{
			this.state = STATE.STARTING;

			startingCounter = 0;

			toBreak = new ArrayList<BlockPos>();

			for (int modX = -2; modX <= 2; modX++)
			{
				for (int modZ = -2; modZ <= 2; modZ++)
				{
					for (int modY = -2; modY <= 2; modY++)
					{
						if (Math.abs(modX) == 2 || Math.abs(modZ) == 2 || Math.abs(modY) == 2)
						{
							toBreak.add(this.pos.add(modX, modY, modZ));
						}
					}
				}
			}

			Collections.shuffle(toBreak);

			syncTE();
		}
	}

	private void run()
	{
		this.state = STATE.RUNNING;
		nextCheckEntries = new LinkedHashMap<BlockPos, Boolean[]>();

		Boolean[] newArray = new Boolean[EnumFacing.HORIZONTALS.length];

		for (int i = 0; i < newArray.length; i++)
		{
			newArray[i] = true;
		}

		nextCheckEntries.put(this.pos, newArray);

		transformCount = 0;

		syncTE();
	}

	public STATE getState()
	{
		return state;
	}

	public int getStartingCounter()
	{
		return startingCounter;
	}
}
