package lumien.randomthings.tileentity.cores;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import lumien.randomthings.block.ModBlocks;
import lumien.randomthings.tileentity.TileEntityBase;
import lumien.randomthings.util.BlockPattern.BlockInfo;
import lumien.randomthings.worldgen.WorldGenCores;
import net.minecraft.block.BlockSand;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;

public class TileEntityNatureCore extends TileEntityBase implements ITickable
{
	static Random rand = new Random();

	@Override
	public void update()
	{
		if (!world.isRemote)
		{
			// Replace Sand
			if (rand.nextInt(40) == 0)
			{
				int rX = this.pos.getX() + rand.nextInt(11) - 5;
				int rY = this.pos.getY() + rand.nextInt(4) - 3;
				int rZ = this.pos.getZ() + rand.nextInt(11) - 5;

				BlockPos target = new BlockPos(rX, rY, rZ);
				IBlockState state = world.getBlockState(target);
				if (state.getBlock() instanceof BlockSand)
				{
					if (this.world.isAirBlock(target.up()))
					{
						this.world.setBlockState(target, Blocks.GRASS.getDefaultState());
					}
					else
					{
						this.world.setBlockState(target, Blocks.DIRT.getDefaultState());
					}
				}
			}

			// Animal Spawning
			if (rand.nextInt(400) == 0)
			{
				List<EntityAnimal> closeAnimals = world.getEntitiesWithinAABB(EntityAnimal.class, new AxisAlignedBB(this.pos, this.pos).grow(5, 5, 5));
				if (closeAnimals.size() < 2)
				{
					int rX = this.pos.getX() + rand.nextInt(11) - 5;
					int rY = this.pos.getY() + rand.nextInt(5) - 2;
					int rZ = this.pos.getZ() + rand.nextInt(11) - 5;

					Biome.SpawnListEntry entry = ((WorldServer) world).getSpawnListEntryForTypeAt(EnumCreatureType.CREATURE, new BlockPos(rX, rY, rZ));
					if (entry != null)
					{
						EntityLiving entityliving = null;
						try
						{
							entityliving = entry.entityClass.getConstructor(new Class[] { World.class }).newInstance(new Object[] { world });
						}
						catch (Exception exception)
						{
							exception.printStackTrace();
						}
						if (entityliving != null)
						{
							entityliving.setLocationAndAngles(rX, rY, rZ, rand.nextFloat() * 360.0F, 0.0F);

							if (entityliving.getCanSpawnHere() && entityliving.isNotColliding())
							{
								world.spawnEntity(entityliving);
							}
						}
					}
				}
			}

			// Bonemealing
			if (rand.nextInt(100) == 0)
			{
				int rX = this.pos.getX() + rand.nextInt(11) - 5;
				int rY = this.pos.getY() + rand.nextInt(4) - 3;
				int rZ = this.pos.getZ() + rand.nextInt(11) - 5;

				BlockPos target = new BlockPos(rX, rY, rZ);
				IBlockState state = world.getBlockState(target);
				if (state.getBlock() instanceof IGrowable)
				{
					IGrowable growable = (IGrowable) state.getBlock();
					if (growable.canGrow(world, target, state, world.isRemote))
					{
						world.playEvent(2005, target, 0);
						growable.grow(world, rand, target, state);
					}
				}
			}

			// Trees
			if (rand.nextInt(600) == 0)
			{
				double radius = rand.nextInt(20) + 10;
				double angle = Math.random() * Math.PI * 2;

				int x = (int) Math.floor(this.pos.getX() + radius * Math.cos(angle));
				int z = (int) Math.floor(this.pos.getZ() + radius * Math.sin(angle));
				int y = this.pos.getY() + rand.nextInt(4) - 3;

				BlockPos target = new BlockPos(x, y, z);
				IBlockState state = world.getBlockState(target);

				boolean space = true;
				for (EnumFacing facing : EnumFacing.HORIZONTALS)
				{
					BlockPos log = target.up().offset(facing);
					IBlockState there = world.getBlockState(log);

					if (!(world.isAirBlock(log) || there.getBlock().isReplaceable(world, log)))
					{
						space = false;
						break;
					}
				}

				if (space && Blocks.SAPLING.canPlaceBlockAt(world, target.up()))
				{
					world.playEvent(2005, target, 0);
					world.setBlockState(target.up(), Blocks.SAPLING.getDefaultState());
				}
			}

			// Rebuild
			if (rand.nextInt(600) == 0)
			{
				ArrayList<BlockInfo> patternInfo = WorldGenCores.natureCore.getBlockInfo();

				BlockInfo randomInfo = patternInfo.get(rand.nextInt(patternInfo.size()));
				if (randomInfo.getState().getBlock() != ModBlocks.natureCore)
				{
					BlockPos targetPlace = this.pos.add(randomInfo.getMod().down());
					IBlockState original = world.getBlockState(targetPlace);
					if (world.isAirBlock(this.pos.add(randomInfo.getMod().down())) || original.getBlock().isReplaceable(world, targetPlace))
					{
						world.setBlockState(this.pos.add(randomInfo.getMod().down()), randomInfo.getState());
					}
				}
			}
		}
	}

	@Override
	public void writeDataToNBT(NBTTagCompound compound, boolean sync)
	{
	}

	@Override
	public void readDataFromNBT(NBTTagCompound compound, boolean sync)
	{
	}
}
