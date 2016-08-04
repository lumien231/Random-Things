package lumien.randomthings.tileentity;

import static lumien.randomthings.tileentity.TileEntityEnderBridge.BRIDGESTATE.IDLE;
import static lumien.randomthings.tileentity.TileEntityEnderBridge.BRIDGESTATE.SCANNING;
import static lumien.randomthings.tileentity.TileEntityEnderBridge.BRIDGESTATE.WAITING;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lumien.randomthings.block.BlockEnderBridge;
import lumien.randomthings.block.ModBlocks;
import lumien.randomthings.util.WorldUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityEnderBridge extends TileEntityBase implements ITickable
{
	enum BRIDGESTATE
	{
		IDLE, SCANNING, WAITING
	}

	BRIDGESTATE state;

	boolean redstonePowered;
	int scanningCounter;

	public static Set<Class<? extends Entity>> entityWhitelist;

	static
	{
		entityWhitelist = new HashSet<Class<? extends Entity>>();
		entityWhitelist.add(EntityPlayerMP.class);
		entityWhitelist.add(EntityItem.class);
	}

	public TileEntityEnderBridge()
	{
		redstonePowered = false;
		scanningCounter = 2;
		state = IDLE;
	}

	@Override
	public void update()
	{
		if (!worldObj.isRemote)
		{
			if (state == SCANNING)
			{
				IBlockState blockState = worldObj.getBlockState(pos);
				EnumFacing facing = blockState.getValue(BlockEnderBridge.FACING);

				BlockPos nextPos = new BlockPos(pos.offset(facing, scanningCounter));

				if (worldObj.isBlockLoaded(nextPos))
				{
					IBlockState nextState = worldObj.getBlockState(nextPos);
					if (nextState.getBlock() == ModBlocks.enderAnchor)
					{
						List<Entity> entityList = worldObj.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos.getX() - 2, pos.getY() - 2, pos.getZ() - 2, pos.getX() + 2, pos.getY() + 2, pos.getZ() + 2), null);

						if (!entityList.isEmpty())
						{
							BlockPos target = nextPos.up();

							for (Entity e : entityList)
							{
								if (TileEntityEnderBridge.entityWhitelist.contains(e.getClass()))
								{
									WorldUtil.setEntityPosition(e, target.getX() + 0.5, target.getY(), target.getZ() + 0.5);
								}
							}
						}

						state = WAITING;
					}
					else if (!nextState.getBlock().isAir(nextState, worldObj, nextPos))
					{
						state = WAITING;
					}
				}

				scanningCounter++;
			}
		}
	}

	@Override
	public void neighborChanged(IBlockState blockState, World worldIn, BlockPos pos, Block neighborBlock)
	{
		if (!worldIn.isRemote)
		{
			boolean powered = worldIn.isBlockIndirectlyGettingPowered(pos) != 0;

			if (powered != redstonePowered)
			{
				if (state == IDLE && powered)
				{
					scanningCounter = 2;
					state = SCANNING;
				}
				else if (state == SCANNING && !powered)
				{
					state = IDLE;
				}
				else if (state == WAITING && !powered)
				{
					state = IDLE;
				}
				redstonePowered = powered;
			}
		}
	}

	@Override
	public void writeDataToNBT(NBTTagCompound compound)
	{
		compound.setInteger("state", state.ordinal());
		compound.setBoolean("redstonePowered", redstonePowered);
		compound.setInteger("scanningCounter", scanningCounter);
	}

	@Override
	public void readDataFromNBT(NBTTagCompound compound)
	{
		state = BRIDGESTATE.values()[compound.getInteger("state")];
		redstonePowered = compound.getBoolean("redstonePowered");
		scanningCounter = compound.getInteger("scanningCounter");
	}
}
