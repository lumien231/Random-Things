package lumien.randomthings.tileentity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;

import lumien.randomthings.block.BlockBlockDestabilizer;
import lumien.randomthings.config.Numbers;
import lumien.randomthings.entitys.EntityFallingBlockSpecial;
import lumien.randomthings.lib.ContainerSynced;
import lumien.randomthings.lib.IRedstoneSensitive;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class TileEntityBlockDestabilizer extends TileEntityBase implements ITickable, IRedstoneSensitive
{
	enum STATE
	{
		IDLE, SEARCHING, DROPPING;
	}

	STATE state = STATE.IDLE;

	HashSet<BlockPos> alreadyChecked;
	ArrayList<BlockPos> toCheck;

	HashSet<BlockPos> targetBlocks;
	IBlockState targetState;

	ArrayList<BlockPos> targetBlocksSorted;
	int dropCounter;

	@ContainerSynced
	boolean lazy;

	@ContainerSynced
	boolean fuzzy;

	HashSet<BlockPos> invalidBlocks;

	public void resetLazy()
	{
		if (this.state == STATE.IDLE)
		{
			this.invalidBlocks = null;
		}
	}

	@Override
	public void writeDataToNBT(NBTTagCompound compound, boolean sync)
	{
		compound.setInteger("state", state.ordinal());
		compound.setBoolean("lazy", lazy);
		compound.setBoolean("fuzzy", fuzzy);

		if (lazy && invalidBlocks != null)
		{
			NBTTagList invalidBlocksNBT = new NBTTagList();

			for (BlockPos p : invalidBlocks)
			{
				invalidBlocksNBT.appendTag(new NBTTagInt(p.getX()));
				invalidBlocksNBT.appendTag(new NBTTagInt(p.getY()));
				invalidBlocksNBT.appendTag(new NBTTagInt(p.getZ()));
			}

			compound.setTag("invalidBlocks", invalidBlocksNBT);
		}

		if (state == STATE.SEARCHING)
		{
			NBTTagList alreadyCheckedNBT = new NBTTagList();

			for (BlockPos p : alreadyChecked)
			{
				alreadyCheckedNBT.appendTag(new NBTTagInt(p.getX()));
				alreadyCheckedNBT.appendTag(new NBTTagInt(p.getY()));
				alreadyCheckedNBT.appendTag(new NBTTagInt(p.getZ()));
			}

			NBTTagList toCheckNBT = new NBTTagList();

			for (BlockPos p : toCheck)
			{
				toCheckNBT.appendTag(new NBTTagInt(p.getX()));
				toCheckNBT.appendTag(new NBTTagInt(p.getY()));
				toCheckNBT.appendTag(new NBTTagInt(p.getZ()));
			}

			NBTTagList targetBlocksNBT = new NBTTagList();

			for (BlockPos p : targetBlocks)
			{
				targetBlocksNBT.appendTag(new NBTTagInt(p.getX()));
				targetBlocksNBT.appendTag(new NBTTagInt(p.getY()));
				targetBlocksNBT.appendTag(new NBTTagInt(p.getZ()));
			}

			compound.setString("targetStateBlock", targetState.getBlock().getRegistryName().toString());
			compound.setInteger("targetStateMeta", targetState.getBlock().getMetaFromState(targetState));

			compound.setTag("alreadyChecked", alreadyCheckedNBT);
			compound.setTag("toCheck", toCheckNBT);
			compound.setTag("targetBlocks", targetBlocksNBT);
		}
		else if (state == STATE.DROPPING)
		{
			compound.setInteger("dropCounter", dropCounter);

			NBTTagList targetBlocksSortedNBT = new NBTTagList();

			for (BlockPos p : targetBlocksSorted)
			{
				targetBlocksSortedNBT.appendTag(new NBTTagInt(p.getX()));
				targetBlocksSortedNBT.appendTag(new NBTTagInt(p.getY()));
				targetBlocksSortedNBT.appendTag(new NBTTagInt(p.getZ()));
			}

			compound.setTag("targetBlocksSorted", targetBlocksSortedNBT);
			compound.setString("targetStateBlock", targetState.getBlock().getRegistryName().toString());
			compound.setInteger("targetStateMeta", targetState.getBlock().getMetaFromState(targetState));
		}
	}

	@Override
	public void readDataFromNBT(NBTTagCompound compound, boolean sync)
	{
		this.state = STATE.values()[compound.getInteger("state")];
		this.lazy = compound.getBoolean("lazy");
		this.fuzzy = compound.getBoolean("fuzzy");

		if (lazy && compound.hasKey("invalidBlocks"))
		{
			invalidBlocks = new HashSet<BlockPos>();

			NBTTagList invalidBlocksNBT = compound.getTagList("invalidBlocks", 3);

			for (int i = 0; i < invalidBlocksNBT.tagCount(); i += 3)
			{
				BlockPos pos = new BlockPos(invalidBlocksNBT.getIntAt(i), invalidBlocksNBT.getIntAt(i + 1), invalidBlocksNBT.getIntAt(i + 2));

				invalidBlocks.add(pos);
			}
		}

		if (state == STATE.SEARCHING)
		{
			toCheck = new ArrayList<BlockPos>();
			targetBlocks = new HashSet<BlockPos>();
			alreadyChecked = new HashSet<BlockPos>();

			NBTTagList alreadyCheckedNBT = compound.getTagList("alreadyChecked", 3);

			for (int i = 0; i < alreadyCheckedNBT.tagCount(); i += 3)
			{
				BlockPos pos = new BlockPos(alreadyCheckedNBT.getIntAt(i), alreadyCheckedNBT.getIntAt(i + 1), alreadyCheckedNBT.getIntAt(i + 2));

				alreadyChecked.add(pos);
			}

			NBTTagList toCheckNBT = compound.getTagList("toCheck", 3);

			for (int i = 0; i < toCheckNBT.tagCount(); i += 3)
			{
				BlockPos pos = new BlockPos(toCheckNBT.getIntAt(i), toCheckNBT.getIntAt(i + 1), toCheckNBT.getIntAt(i + 2));

				toCheck.add(pos);
			}

			NBTTagList targetBlocksNBT = compound.getTagList("targetBlocks", 3);

			for (int i = 0; i < targetBlocksNBT.tagCount(); i += 3)
			{
				BlockPos pos = new BlockPos(targetBlocksNBT.getIntAt(i), targetBlocksNBT.getIntAt(i + 1), targetBlocksNBT.getIntAt(i + 2));

				targetBlocks.add(pos);
			}

			String targetBlockName = compound.getString("targetStateBlock");
			int targetBlockMeta = compound.getInteger("targetStateMeta");

			Block b = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(targetBlockName));

			if (b == null)
			{
				this.targetState = Blocks.STONE.getDefaultState();
			}
			else
			{
				this.targetState = b.getStateFromMeta(targetBlockMeta);
			}
		}
		else if (state == STATE.DROPPING)
		{
			targetBlocksSorted = new ArrayList<BlockPos>();

			this.dropCounter = compound.getInteger("dropCounter");

			NBTTagList targetBlocksSortedNBT = compound.getTagList("targetBlocksSorted", 3);

			for (int i = 0; i < targetBlocksSortedNBT.tagCount(); i += 3)
			{
				BlockPos pos = new BlockPos(targetBlocksSortedNBT.getIntAt(i), targetBlocksSortedNBT.getIntAt(i + 1), targetBlocksSortedNBT.getIntAt(i + 2));

				targetBlocksSorted.add(pos);
			}

			String targetBlockName = compound.getString("targetStateBlock");
			int targetBlockMeta = compound.getInteger("targetStateMeta");

			Block b = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(targetBlockName));

			if (b == null)
			{
				this.targetState = Blocks.STONE.getDefaultState();
			}
			else
			{
				this.targetState = b.getStateFromMeta(targetBlockMeta);
			}
		}
	}

	@Override
	public boolean syncAdditionalData()
	{
		return false;
	}

	@Override
	public void update()
	{
		if (!this.world.isRemote)
		{
			if (state == STATE.SEARCHING)
			{
				stepSearch();
			}
			else if (state == STATE.DROPPING)
			{
				dropNextBlock();
			}
		}
	}

	private void dropNextBlock()
	{
		if (dropCounter < targetBlocksSorted.size())
		{
			BlockPos pos = targetBlocksSorted.get(dropCounter);

			IBlockState target = world.getBlockState(pos);

			if ((fuzzy && target.getBlock() == targetState.getBlock() || target == targetState) && world.getTileEntity(pos) == null)
			{
				EntityFallingBlockSpecial fallingEntity = new EntityFallingBlockSpecial(this.world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, target);

				this.world.spawnEntity(fallingEntity);

				fallingEntity.shouldDropItem = false;
			}

			dropCounter++;
		}
		else
		{
			this.state = STATE.IDLE;

			targetBlocksSorted = null;
			targetState = null;
		}
	}

	private void initDrop()
	{
		targetBlocksSorted = new ArrayList<BlockPos>(targetBlocks);

		long time = System.currentTimeMillis();

		targetBlocksSorted.sort(new Comparator<BlockPos>()
		{
			@Override
			public int compare(BlockPos o1, BlockPos o2)
			{
				int difY = o1.getY() - o2.getY();

				if (difY != 0)
				{
					return difY;
				}

				BlockPos me = TileEntityBlockDestabilizer.this.pos;
				return (int) (o1.distanceSq(me) - o2.distanceSq(me));
			}
		});

		state = STATE.DROPPING;
		dropCounter = 0;

		targetBlocks = null;
		toCheck = null;
		alreadyChecked = null;
	}

	private void stepSearch()
	{
		if (toCheck.isEmpty() || (Numbers.BLOCK_DESTABILIZER_LIMIT != 0 && targetBlocks.size() >= Numbers.BLOCK_DESTABILIZER_LIMIT))
		{
			initDrop();
		}
		else
		{
			BlockPos nextPos = toCheck.remove(0);

			if (!alreadyChecked.contains(nextPos))
			{
				IBlockState state = world.getBlockState(nextPos);

				alreadyChecked.add(nextPos);

				if ((fuzzy && state.getBlock() == targetState.getBlock() || state == targetState))
				{
					targetBlocks.add(nextPos);

					for (EnumFacing facing : EnumFacing.VALUES)
					{
						BlockPos addPos = new BlockPos(nextPos.offset(facing));

						if (!alreadyChecked.contains(addPos))
						{
							toCheck.add(addPos);
						}
					}
				}
				else if (lazy)
				{
					invalidBlocks.add(nextPos);
				}
			}
		}
	}

	private void initStart()
	{
		EnumFacing facing = world.getBlockState(pos).getValue(BlockBlockDestabilizer.FACING);
		if (!this.world.isAirBlock(pos.offset(facing)))
		{
			targetState = world.getBlockState(pos.offset(facing));

			if (targetState.getBlockHardness(this.world, pos.offset(facing)) >= 0)
			{

				this.state = STATE.SEARCHING;
				toCheck = new ArrayList<BlockPos>();
				toCheck.add(pos.offset(facing));

				targetBlocks = new HashSet<BlockPos>();

				alreadyChecked = new HashSet<BlockPos>();

				if (lazy)
				{
					if (invalidBlocks != null)
					{
						alreadyChecked.addAll(invalidBlocks);
					}
					else
					{
						invalidBlocks = new HashSet<BlockPos>();
					}
				}
				else
				{
					this.invalidBlocks = null;
				}
			}
			else
			{
				targetState = null;
			}
		}
	}

	@Override
	public void redstoneChange(boolean oldState, boolean newState)
	{
		if (!oldState && newState && this.state == STATE.IDLE)
		{
			initStart();
		}
	}

	public void toggleLazy()
	{
		if (state == STATE.IDLE)
		{
			lazy = !lazy;

			if (!lazy)
			{
				invalidBlocks = null;
			}
		}
	}

	public void toggleFuzzy()
	{
		fuzzy = !fuzzy;
	}
}
