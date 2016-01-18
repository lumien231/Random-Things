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
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.BiomeGenBase;

public class TileEntityNatureCore extends TileEntityBase implements ITickable
{
	static Random rand = new Random();

	@Override
	public void update()
	{
		if (!worldObj.isRemote)
		{
			// Replace Sand
			if (rand.nextInt(40) == 0)
			{
				int rX = this.pos.getX() + rand.nextInt(11) - 5;
				int rY = this.pos.getY() + rand.nextInt(4) - 3;
				int rZ = this.pos.getZ() + rand.nextInt(11) - 5;

				BlockPos target = new BlockPos(rX, rY, rZ);
				IBlockState state = worldObj.getBlockState(target);
				if (state.getBlock() instanceof BlockSand)
				{
					if (this.worldObj.isAirBlock(target.up()))
					{
						this.worldObj.setBlockState(target, Blocks.grass.getDefaultState());
					}
					else
					{
						this.worldObj.setBlockState(target, Blocks.dirt.getDefaultState());
					}
				}
			}

			// Animal Spawning
			if (rand.nextInt(400) == 0)
			{
				List<EntityAnimal> closeAnimals = worldObj.getEntitiesWithinAABB(EntityAnimal.class, new AxisAlignedBB(this.pos, this.pos).expand(5, 5, 5));
				if (closeAnimals.size() < 2)
				{
					int rX = this.pos.getX() + rand.nextInt(11) - 5;
					int rY = this.pos.getY() + rand.nextInt(5) - 2;
					int rZ = this.pos.getZ() + rand.nextInt(11) - 5;

					BiomeGenBase.SpawnListEntry entry = ((WorldServer) worldObj).getSpawnListEntryForTypeAt(EnumCreatureType.CREATURE, new BlockPos(rX, rY, rZ));
					if (entry != null)
					{
						EntityLiving entityliving = null;
						try
						{
							entityliving = entry.entityClass.getConstructor(new Class[] { World.class }).newInstance(new Object[] { worldObj });
						}
						catch (Exception exception)
						{
							exception.printStackTrace();
						}
						if (entityliving != null)
						{
							entityliving.setLocationAndAngles(rX, rY, rZ, rand.nextFloat() * 360.0F, 0.0F);

							if (entityliving.getCanSpawnHere())
							{
								worldObj.spawnEntityInWorld(entityliving);
							}
						}
					}
				}
			}

			// Bonemealing
			if (rand.nextInt(300) == 0)
			{
				int rX = this.pos.getX() + rand.nextInt(11) - 5;
				int rY = this.pos.getY() + rand.nextInt(4) - 3;
				int rZ = this.pos.getZ() + rand.nextInt(11) - 5;

				BlockPos target = new BlockPos(rX, rY, rZ);
				IBlockState state = worldObj.getBlockState(target);
				if (state.getBlock() instanceof IGrowable)
				{
					IGrowable growable = (IGrowable) state.getBlock();
					if (growable.canGrow(worldObj, target, state, worldObj.isRemote))
					{
						worldObj.playAuxSFX(2005, target, 0);
						growable.grow(worldObj, rand, target, state);
					}
				}
			}

			// Trees
			if (rand.nextInt(600) == 0)
			{
				int rX = this.pos.getX() + rand.nextInt(41) - 20;
				int rY = this.pos.getY() + rand.nextInt(6) - 3;
				int rZ = this.pos.getZ() + rand.nextInt(41) - 20;

				BlockPos target = new BlockPos(rX, rY, rZ);
				IBlockState state = worldObj.getBlockState(target);
				if (Blocks.sapling.canPlaceBlockAt(worldObj, target.up()))
				{
					worldObj.playAuxSFX(2005, target, 0);
					worldObj.setBlockState(target.up(), Blocks.sapling.getDefaultState());
				}
			}

			// Rebuild
			if (rand.nextInt(600) == 0)
			{
				ArrayList<BlockInfo> patternInfo = WorldGenCores.natureCore.getBlockInfo();

				BlockInfo randomInfo = patternInfo.get(rand.nextInt(patternInfo.size()));
				if (randomInfo.getState().getBlock() != ModBlocks.natureCore)
				{
					if (worldObj.isAirBlock(this.pos.add(randomInfo.getMod().down())))
					{
						worldObj.setBlockState(this.pos.add(randomInfo.getMod().down()), randomInfo.getState());
					}
				}
			}
		}
	}

	@Override
	public void writeDataToNBT(NBTTagCompound compound)
	{
	}

	@Override
	public void readDataFromNBT(NBTTagCompound compound)
	{
	}
}
