package lumien.randomthings.entitys;

import java.util.List;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEnchantmentTable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityArtificialEndPortal extends Entity implements IEntityAdditionalSpawnData
{
	public int actionTimer;

	public EntityArtificialEndPortal(World worldIn)
	{
		super(worldIn);

		this.setSize(3, 1);
	}

	public EntityArtificialEndPortal(World worldIn, double posX, double posY, double posZ)
	{
		this(worldIn);
		this.setPosition(posX, posY, posZ);
	}

	@Override
	public void onEntityUpdate()
	{
		super.onEntityUpdate();

		if (actionTimer < 200)
		{
			actionTimer++;

			if (this.worldObj.isRemote && this.actionTimer > 40)
			{
				spawnParticles();
			}
		}

		if (!this.worldObj.isRemote)
		{
			if (this.worldObj.getTotalWorldTime() % 40 == 0)
			{
				if (!isValidPosition(worldObj, new BlockPos(this.posX, this.posY, this.posZ), false))
				{
					this.setDead();
				}
			}
		}
		else
		{
			if (this.actionTimer == 85)
			{
				this.worldObj.playSound(this.posX, this.posY, this.posZ, SoundEvents.BLOCK_PORTAL_TRAVEL, SoundCategory.BLOCKS, 0.2F, 1, false);
			}
		}
	}

	@SideOnly(Side.CLIENT)
	private void spawnParticles()
	{
		for (int i = 0; i < 5; i++)
		{
			ParticleEnchantmentTable.EnchantmentTable builder = new ParticleEnchantmentTable.EnchantmentTable();

			double modX = Math.random() * 0.05f - 0.025f;
			double modZ = Math.random() * 0.05f - 0.025f;

			Particle particle = builder.getEntityFX(0, worldObj, this.posX + modX, this.posY + 2, this.posZ + modZ, modX * 2, 1, modZ * 2);
			particle.setRBGColorF(0.2F + (float) Math.random() * 0.1f, 0, 0.3F + (float) Math.random() * 0.1f);

			Minecraft.getMinecraft().effectRenderer.addEffect(particle);
		}
	}

	@Override
	public void onCollideWithPlayer(EntityPlayer entityIn)
	{
		super.onCollideWithPlayer(entityIn);

		if (!this.worldObj.isRemote && this.actionTimer >= 200 && entityIn.getEntityBoundingBox().intersectsWith(this.getEntityBoundingBox()) && !entityIn.isRiding() && !entityIn.isBeingRidden())
		{
			entityIn.changeDimension(1);
		}
	}

	@Override
	protected void entityInit()
	{
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound)
	{
		this.actionTimer = compound.getInteger("actionTimer");
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound)
	{
		compound.setInteger("actionTimer", actionTimer);
	}

	@Override
	public void writeSpawnData(ByteBuf buffer)
	{
		buffer.writeInt(actionTimer);
	}

	@Override
	public void readSpawnData(ByteBuf additionalData)
	{
		this.actionTimer = additionalData.readInt();
	}

	public static boolean isValidPosition(World worldObj, BlockPos center, boolean checkForOtherPortals)
	{
		for (int modX = -1; modX < 2; modX++)
		{
			for (int modZ = -1; modZ < 2; modZ++)
			{
				if (!worldObj.isAirBlock(center.add(modX, 0, modZ)))
				{
					return false;
				}
			}
		}

		for (int modY = 1; modY < 3; modY++)
		{
			if (!worldObj.isAirBlock(center.add(0, modY, 0)))
			{
				return false;
			}
		}

		if (worldObj.getBlockState(center.add(0, 3, 0)).getBlock() != Blocks.END_ROD || worldObj.getBlockState(center.add(0, 4, 0)).getBlock() != Blocks.END_STONE)
		{
			return false;
		}

		for (int modX = -1; modX < 2; modX++)
		{
			for (int modZ = -1; modZ < 2; modZ++)
			{
				if (worldObj.getBlockState(center.add(modX, -1, modZ)).getBlock() != Blocks.END_STONE)
				{
					return false;
				}
			}
		}

		for (int modX = -2; modX < 3; modX++)
		{
			for (int modZ = -2; modZ < 3; modZ++)
			{
				if (modX == -2 || modZ == -2 || modX == 2 || modZ == 2)
				{
					if (worldObj.getBlockState(center.add(modX, 0, modZ)).getBlock() != Blocks.OBSIDIAN)
					{
						return false;
					}
				}
			}
		}

		if (checkForOtherPortals)
		{
			List<EntityArtificialEndPortal> portalList = worldObj.getEntitiesWithinAABB(EntityArtificialEndPortal.class, new AxisAlignedBB(center, center.add(1, 2, 1)));

			if (!portalList.isEmpty())
			{
				return false;
			}
		}

		return true;
	}
}
