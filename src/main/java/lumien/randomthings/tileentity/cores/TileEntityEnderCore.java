package lumien.randomthings.tileentity.cores;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import lumien.randomthings.item.ModItems;
import lumien.randomthings.tileentity.TileEntityBase;
import lumien.randomthings.util.WorldUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityEnchantmentTableParticleFX;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;

import org.apache.commons.lang3.tuple.Pair;

public class TileEntityEnderCore extends TileEntityBase implements IUpdatePlayerListBox
{
	Random rand;
	boolean active;

	HashMap<EntityItem, Integer> progressMap;

	static ArrayList<Pair<ItemStack, ItemStack>> recipes = new ArrayList<Pair<ItemStack, ItemStack>>();
	{
		recipes.add(Pair.of(new ItemStack(ModItems.inertLinkingOrb, 1, 0), new ItemStack(ModItems.linkingOrb, 1, 0)));
	}

	public TileEntityEnderCore()
	{
		rand = new Random();
		active = false;
		progressMap = new HashMap<EntityItem, Integer>();
	}

	@Override
	public void update()
	{
		if (!worldObj.isRemote)
		{
			AxisAlignedBB myBoundingBox = AxisAlignedBB.fromBounds(this.pos.getX(), this.pos.getY(), this.pos.getZ(), this.pos.getX(), this.pos.getY(), this.pos.getZ());

			List<EntityItem> itemEntitys = worldObj.getEntitiesWithinAABB(EntityItem.class, myBoundingBox.expand(7, 7, 7));
			for (EntityItem ei : itemEntitys)
			{
				ItemStack itemStack = ei.getEntityItem();
				if (!progressMap.containsKey(ei))
				{
					for (Pair<ItemStack, ItemStack> recipePair : recipes)
					{
						if (ItemStack.areItemStacksEqual(itemStack, recipePair.getLeft()))
						{
							progressMap.put(ei, 0);
						}
					}
				}
			}

			if (!progressMap.isEmpty())
			{
				ArrayList<EntityItem> toRemove = new ArrayList<EntityItem>();
				for (Map.Entry<EntityItem, Integer> entry : progressMap.entrySet())
				{
					EntityItem ei = entry.getKey();
					Integer progress = entry.getValue();

					if (!ei.isEntityAlive())
					{
						toRemove.add(ei);
					}
					else
					{
						Integer newProgress = progress + 1;
						if (newProgress >= 15 * 20)
						{
							for (Pair<ItemStack, ItemStack> recipePair : recipes)
							{
								if (ItemStack.areItemStacksEqual(ei.getEntityItem(), recipePair.getLeft()))
								{
									ei.setEntityItemStack(recipePair.getRight().copy());
								}
							}

							toRemove.add(ei);
						}
						else
						{
							progressMap.put(entry.getKey(), newProgress);
						}
					}
				}

				for (EntityItem ei : toRemove)
				{
					progressMap.remove(ei);
				}
			}

			// Switch
			if (rand.nextInt(200) == 0)
			{
				List<EntityLivingBase> livingEntitys = worldObj.getEntitiesWithinAABB(EntityLivingBase.class, myBoundingBox.expand(20, 20, 20));
				if (livingEntitys.size() >= 2)
				{
					int random1 = rand.nextInt(livingEntitys.size());
					EntityLivingBase entity1 = livingEntitys.get(random1);
					int random2 = (random1 + rand.nextInt(livingEntitys.size() - 1) + 1) % livingEntitys.size();
					EntityLivingBase entity2 = livingEntitys.get(random2);

					Vec3 pos1 = entity1.getPositionVector();
					Vec3 pos2 = entity2.getPositionVector();

					WorldUtil.setEntityPosition(entity1, pos2.xCoord, pos2.yCoord, pos2.zCoord);
					WorldUtil.setEntityPosition(entity2, pos1.xCoord, pos1.yCoord, pos1.zCoord);

					entity1.worldObj.playSoundEffect(entity1.posX, entity1.posY, entity1.posZ, "mob.endermen.portal", 0.5f, 1.5F);
					entity1.worldObj.playSoundEffect(entity2.posX, entity2.posY, entity2.posZ, "mob.endermen.portal", 0.5f, 1.5F);
				}
			}
		}
		else
		{
			spawnParticles();
		}
	}

	private void spawnParticles()
	{
		AxisAlignedBB myBoundingBox = AxisAlignedBB.fromBounds(this.pos.getX(), this.pos.getY(), this.pos.getZ(), this.pos.getX(), this.pos.getY(), this.pos.getZ());

		List<EntityItem> itemEntitys = worldObj.getEntitiesWithinAABB(EntityItem.class, myBoundingBox.expand(7, 7, 7));
		for (EntityItem ei : itemEntitys)
		{
			if (ei.getAge() > 60)
			{
				ItemStack itemStack = ei.getEntityItem();
				for (Pair<ItemStack, ItemStack> recipePair : recipes)
				{
					if (ItemStack.areItemStacksEqual(itemStack, recipePair.getLeft()))
					{
						double difX = this.pos.getX() - ei.posX + rand.nextFloat() * 2 - 1;
						double difY = this.pos.getY() - ei.posY + 0.5 + rand.nextFloat() * 2 - 1;
						double difZ = this.pos.getZ() - ei.posZ + rand.nextFloat() * 2 - 1;

						// Spawn Particles
						EntityFX particle = new EntityEnchantmentTableParticleFX.EnchantmentTable().getEntityFX(0, worldObj, ei.posX, ei.posY + 1.5, ei.posZ, difX + 0.5, difY - 1.5, difZ + 0.5, new int[0]);
						particle.setRBGColorF(1F / 255F * 110, 0, 1F / 255F * 183);
						Minecraft.getMinecraft().effectRenderer.addEffect(particle);
					}
				}
			}
		}
	}

	@Override
	public void writeDataToNBT(NBTTagCompound compound)
	{
		compound.setBoolean("active", active);
	}

	@Override
	public void readDataFromNBT(NBTTagCompound compound)
	{
		active = compound.getBoolean("active");
	}

	public void activate()
	{
		this.active = true;
	}
}
