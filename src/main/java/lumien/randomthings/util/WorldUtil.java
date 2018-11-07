package lumien.randomthings.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.google.common.base.Predicate;

import lumien.randomthings.network.MessageUtil;
import lumien.randomthings.network.messages.MessageSetBiome;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ClassInheritanceMultiMap;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fluids.IFluidBlock;

public class WorldUtil
{
	public static ChunkPos getChunkPosFromLong(long lng)
	{
		int x = (int) (lng & ~(4294967295L << 32));
		int z = (int) (lng >> 32);
		
		return new ChunkPos(x, z);
	}

	public static RayTraceResult rayTraceAll(World worldIn, EntityPlayer playerIn, boolean useLiquids)
	{
		float f = playerIn.rotationPitch;
		float f1 = playerIn.rotationYaw;
		double d0 = playerIn.posX;
		double d1 = playerIn.posY + (double) playerIn.getEyeHeight();
		double d2 = playerIn.posZ;
		Vec3d vec3d = new Vec3d(d0, d1, d2);
		float f2 = MathHelper.cos(-f1 * 0.017453292F - (float) Math.PI);
		float f3 = MathHelper.sin(-f1 * 0.017453292F - (float) Math.PI);
		float f4 = -MathHelper.cos(-f * 0.017453292F);
		float f5 = MathHelper.sin(-f * 0.017453292F);
		float f6 = f3 * f4;
		float f7 = f2 * f4;
		double d3 = playerIn.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue();
		Vec3d vec3d1 = vec3d.addVector((double) f6 * d3, (double) f5 * d3, (double) f7 * d3);
		return rayTraceBlocksAll(worldIn, vec3d, vec3d1, useLiquids, !useLiquids, true);
	}

	public static RayTraceResult rayTraceBlocksAll(World world, Vec3d vec31, Vec3d vec32, boolean stopOnLiquid, boolean ignoreBlockWithoutBoundingBox, boolean returnLastUncollidableBlock)
	{
		if (!Double.isNaN(vec31.x) && !Double.isNaN(vec31.y) && !Double.isNaN(vec31.z))
		{
			if (!Double.isNaN(vec32.x) && !Double.isNaN(vec32.y) && !Double.isNaN(vec32.z))
			{
				int i = MathHelper.floor(vec32.x);
				int j = MathHelper.floor(vec32.y);
				int k = MathHelper.floor(vec32.z);
				int l = MathHelper.floor(vec31.x);
				int i1 = MathHelper.floor(vec31.y);
				int j1 = MathHelper.floor(vec31.z);
				BlockPos blockpos = new BlockPos(l, i1, j1);
				IBlockState iblockstate = world.getBlockState(blockpos);
				Block block = iblockstate.getBlock();

				if ((!ignoreBlockWithoutBoundingBox || iblockstate.getCollisionBoundingBox(world, blockpos) != Block.NULL_AABB) && (block.canCollideCheck(iblockstate, stopOnLiquid) || block instanceof IFluidBlock || block instanceof BlockLiquid))
				{
					RayTraceResult raytraceresult = iblockstate.collisionRayTrace(world, blockpos, vec31, vec32);

					if (raytraceresult != null)
					{
						return raytraceresult;
					}
				}

				RayTraceResult raytraceresult2 = null;
				int k1 = 200;

				while (k1-- >= 0)
				{
					if (Double.isNaN(vec31.x) || Double.isNaN(vec31.y) || Double.isNaN(vec31.z))
					{
						return null;
					}

					if (l == i && i1 == j && j1 == k)
					{
						return returnLastUncollidableBlock ? raytraceresult2 : null;
					}

					boolean flag2 = true;
					boolean flag = true;
					boolean flag1 = true;
					double d0 = 999.0D;
					double d1 = 999.0D;
					double d2 = 999.0D;

					if (i > l)
					{
						d0 = (double) l + 1.0D;
					}
					else if (i < l)
					{
						d0 = (double) l + 0.0D;
					}
					else
					{
						flag2 = false;
					}

					if (j > i1)
					{
						d1 = (double) i1 + 1.0D;
					}
					else if (j < i1)
					{
						d1 = (double) i1 + 0.0D;
					}
					else
					{
						flag = false;
					}

					if (k > j1)
					{
						d2 = (double) j1 + 1.0D;
					}
					else if (k < j1)
					{
						d2 = (double) j1 + 0.0D;
					}
					else
					{
						flag1 = false;
					}

					double d3 = 999.0D;
					double d4 = 999.0D;
					double d5 = 999.0D;
					double d6 = vec32.x - vec31.x;
					double d7 = vec32.y - vec31.y;
					double d8 = vec32.z - vec31.z;

					if (flag2)
					{
						d3 = (d0 - vec31.x) / d6;
					}

					if (flag)
					{
						d4 = (d1 - vec31.y) / d7;
					}

					if (flag1)
					{
						d5 = (d2 - vec31.z) / d8;
					}

					if (d3 == -0.0D)
					{
						d3 = -1.0E-4D;
					}

					if (d4 == -0.0D)
					{
						d4 = -1.0E-4D;
					}

					if (d5 == -0.0D)
					{
						d5 = -1.0E-4D;
					}

					EnumFacing enumfacing;

					if (d3 < d4 && d3 < d5)
					{
						enumfacing = i > l ? EnumFacing.WEST : EnumFacing.EAST;
						vec31 = new Vec3d(d0, vec31.y + d7 * d3, vec31.z + d8 * d3);
					}
					else if (d4 < d5)
					{
						enumfacing = j > i1 ? EnumFacing.DOWN : EnumFacing.UP;
						vec31 = new Vec3d(vec31.x + d6 * d4, d1, vec31.z + d8 * d4);
					}
					else
					{
						enumfacing = k > j1 ? EnumFacing.NORTH : EnumFacing.SOUTH;
						vec31 = new Vec3d(vec31.x + d6 * d5, vec31.y + d7 * d5, d2);
					}

					l = MathHelper.floor(vec31.x) - (enumfacing == EnumFacing.EAST ? 1 : 0);
					i1 = MathHelper.floor(vec31.y) - (enumfacing == EnumFacing.UP ? 1 : 0);
					j1 = MathHelper.floor(vec31.z) - (enumfacing == EnumFacing.SOUTH ? 1 : 0);
					blockpos = new BlockPos(l, i1, j1);
					IBlockState iblockstate1 = world.getBlockState(blockpos);
					Block block1 = iblockstate1.getBlock();

					if (!ignoreBlockWithoutBoundingBox || iblockstate1.getMaterial() == Material.PORTAL || iblockstate1.getCollisionBoundingBox(world, blockpos) != Block.NULL_AABB)
					{
						if (block1.canCollideCheck(iblockstate1, stopOnLiquid) || block1 instanceof IFluidBlock || block1 instanceof BlockLiquid)
						{
							RayTraceResult raytraceresult1 = iblockstate1.collisionRayTrace(world, blockpos, vec31, vec32);

							if (raytraceresult1 != null)
							{
								return raytraceresult1;
							}
						}
						else
						{
							raytraceresult2 = new RayTraceResult(RayTraceResult.Type.MISS, vec31, enumfacing, blockpos);
						}
					}
				}

				return returnLastUncollidableBlock ? raytraceresult2 : null;
			}
			else
			{
				return null;
			}
		}
		else
		{
			return null;
		}
	}

	public static void setBiome(World worldObj, BlockPos pos, Biome biome)
	{
		Chunk c = worldObj.getChunkFromBlockCoords(new BlockPos(pos.getX(), 0, pos.getZ()));
		int biomeID = Biome.getIdForBiome(biome);
		byte[] biomeArray = c.getBiomeArray();
		if ((biomeArray[(pos.getZ() & 15) << 4 | (pos.getX() & 15)] & 255) != biomeID)
		{
			biomeArray[(pos.getZ() & 15) << 4 | (pos.getX() & 15)] = (byte) (biomeID & 255);
			c.setBiomeArray(biomeArray);
		}

		if (!worldObj.isRemote)
		{
			c.setModified(true);
			MessageUtil.sendToAllWatchingPos(worldObj, pos, new MessageSetBiome(pos, Biome.getIdForBiome(biome)));
		}
		else
		{
			IBlockState state = worldObj.getBlockState(pos);
			worldObj.notifyBlockUpdate(pos, state, state, 3);
		}
	}

	public static void setEntityPosition(Entity e, double posX, double posY, double posZ)
	{
		if (e instanceof EntityPlayerMP)
		{
			EntityPlayerMP player = (EntityPlayerMP) e;
			player.connection.setPlayerLocation(posX, posY, posZ, player.rotationYaw, player.rotationPitch);
		}
		else
		{
			e.setPositionAndUpdate(posX, posY, posZ);
		}
	}

	public static boolean isValidPosition(BlockPos pos)
	{
		return pos.getX() >= -30000000 && pos.getZ() >= -30000000 && pos.getX() < 30000000 && pos.getZ() < 30000000 && pos.getY() >= 0 && pos.getY() < 256;
	}

	public static BlockPos getHeighestPos(World worldObj, int x, int z)
	{
		int startY = worldObj.getChunkFromBlockCoords(new BlockPos(x, 0, z)).getTopFilledSegment() + 16;

		for (int y = startY; y >= 0; y--)
		{
			BlockPos toCheck = new BlockPos(x, y, z);
			if (!worldObj.isAirBlock(toCheck))
			{
				return toCheck;
			}
		}

		return null;
	}

	public static List getEntitiesWithinAABBs(World worldObj, Class classEntity, AxisAlignedBB... bbs)
	{
		return getEntitiesWithinAABBs(worldObj, classEntity, EntitySelectors.NOT_SPECTATING, bbs);
	}

	public static List getEntitiesWithinAABBs(World worldObj, Class clazz, Predicate filter, AxisAlignedBB... bbs)
	{
		ArrayList arraylist = new ArrayList();

		HashMap<Vec3i, ArrayList<AxisAlignedBB>> boxMap = new HashMap<>();

		for (AxisAlignedBB bb : bbs)
		{
			int minChunkX = MathHelper.floor((bb.minX - World.MAX_ENTITY_RADIUS) / 16.0D);
			int maxChunkX = MathHelper.floor((bb.maxX + World.MAX_ENTITY_RADIUS) / 16.0D);
			int minChunkZ = MathHelper.floor((bb.minZ - World.MAX_ENTITY_RADIUS) / 16.0D);
			int maxChunkZ = MathHelper.floor((bb.maxZ + World.MAX_ENTITY_RADIUS) / 16.0D);
			int minChunkY = MathHelper.floor((bb.minY - World.MAX_ENTITY_RADIUS) / 16.0D);
			int maxChunkY = MathHelper.floor((bb.maxY + World.MAX_ENTITY_RADIUS) / 16.0D);

			for (int x = minChunkX; x <= maxChunkX; x++)
			{
				for (int z = minChunkZ; z <= maxChunkZ; z++)
				{
					for (int y = minChunkY; y <= maxChunkY; y++)
					{
						if (y >= 0 && y < worldObj.getHeight() / 16)
						{
							Vec3i chunkVec = new Vec3i(x, y, z);

							ArrayList<AxisAlignedBB> boxList = boxMap.get(chunkVec);
							if (boxList == null)
							{
								boxList = new ArrayList<>();
								boxMap.put(chunkVec, boxList);
							}

							boxList.add(bb);
						}
					}
				}
			}
		}

		for (Vec3i chunkVec : boxMap.keySet())
		{
			Chunk chunk = worldObj.getChunkFromChunkCoords(chunkVec.getX(), chunkVec.getZ());

			ClassInheritanceMultiMap[] entityMapArray = chunk.getEntityLists();

			Iterator<Entity> iterator = entityMapArray[chunkVec.getY()].getByClass(clazz).iterator();

			while (iterator.hasNext())
			{
				Entity entity = iterator.next();

				for (AxisAlignedBB bb : boxMap.get(chunkVec))
				{
					if (entity.getEntityBoundingBox().intersects(bb) && (filter == null || filter.apply(entity)))
					{
						arraylist.add(entity);

						Entity[] entityParts = entity.getParts();

						if (entityParts != null)
						{
							for (int l = 0; l < entityParts.length; ++l)
							{
								entity = entityParts[l];

								if (entity.getEntityBoundingBox().intersects(bb) && (filter == null || filter.apply(entity)))
								{
									arraylist.add(entity);
								}
							}
						}
					}
				}
			}
		}

		return arraylist;
	}

	public static void spawnItemStack(World worldIn, BlockPos pos, ItemStack stack)
	{
		spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), stack);
	}

	public static void spawnItemStack(World worldIn, double x, double y, double z, ItemStack stack)
	{
		float f = 0.5F;
		double d0 = worldIn.rand.nextFloat() * 0.5F + 0.25D;
		double d1 = worldIn.rand.nextFloat() * 0.5F + 0.25D;
		double d2 = worldIn.rand.nextFloat() * 0.5F + 0.25D;
		EntityItem entityitem = new EntityItem(worldIn, x + d0, y + d1, z + d2, stack);
		entityitem.setDefaultPickupDelay();
		worldIn.spawnEntity(entityitem);
	}
}
