package lumien.randomthings.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.google.common.base.Predicate;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ClassInheritanceMultiMap;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class WorldUtil
{
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

		HashMap<Vec3i, ArrayList<AxisAlignedBB>> boxMap = new HashMap<Vec3i, ArrayList<AxisAlignedBB>>();

		for (AxisAlignedBB bb : bbs)
		{
			int minChunkX = MathHelper.floor_double((bb.minX - World.MAX_ENTITY_RADIUS) / 16.0D);
			int maxChunkX = MathHelper.floor_double((bb.maxX + World.MAX_ENTITY_RADIUS) / 16.0D);
			int minChunkZ = MathHelper.floor_double((bb.minZ - World.MAX_ENTITY_RADIUS) / 16.0D);
			int maxChunkZ = MathHelper.floor_double((bb.maxZ + World.MAX_ENTITY_RADIUS) / 16.0D);
			int minChunkY = MathHelper.floor_double((bb.minY - World.MAX_ENTITY_RADIUS) / 16.0D);
			int maxChunkY = MathHelper.floor_double((bb.maxY + World.MAX_ENTITY_RADIUS) / 16.0D);

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
								boxList = new ArrayList<AxisAlignedBB>();
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
					if (entity.getEntityBoundingBox().intersectsWith(bb) && (filter == null || filter.apply(entity)))
					{
						arraylist.add(entity);

						Entity[] entityParts = entity.getParts();

						if (entityParts != null)
						{
							for (int l = 0; l < entityParts.length; ++l)
							{
								entity = entityParts[l];

								if (entity.getEntityBoundingBox().intersectsWith(bb) && (filter == null || filter.apply(entity)))
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

	public static void spawnItemStack(World worldIn, double x, double y, double z, ItemStack stack)
	{
		Random rng = new Random();
		float f = rng.nextFloat() * 0.8F + 0.1F;
		float f1 = rng.nextFloat() * 0.8F + 0.1F;
		float f2 = rng.nextFloat() * 0.8F + 0.1F;

		while (stack.stackSize > 0)
		{
			int i = rng.nextInt(21) + 10;

			if (i > stack.stackSize)
			{
				i = stack.stackSize;
			}

			stack.stackSize -= i;
			EntityItem entityitem = new EntityItem(worldIn, x + f, y + f1, z + f2, new ItemStack(stack.getItem(), i, stack.getMetadata()));

			if (stack.hasTagCompound())
			{
				entityitem.getEntityItem().setTagCompound(stack.getTagCompound().copy());
			}

			float f3 = 0.05F;
			entityitem.motionX = rng.nextGaussian() * f3;
			entityitem.motionY = rng.nextGaussian() * f3 + 0.20000000298023224D;
			entityitem.motionZ = rng.nextGaussian() * f3;
			worldIn.spawnEntityInWorld(entityitem);
		}
	}
}
