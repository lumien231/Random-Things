package lumien.randomthings.entitys;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityReviveCircle extends Entity
{
	public int age;
	EntitySoul toRevive;
	EntityPlayer reviver;

	public EntityReviveCircle(World world)
	{
		super(world);

		age = 0;
		this.noClip = true;
		this.ignoreFrustumCheck = true;
	}

	public EntityReviveCircle(World world, EntityPlayer reviver, double posX, double posY, double posZ, EntitySoul toRevive)
	{
		super(world);

		this.setPosition(posX, posY, posZ);
		this.toRevive = toRevive;
		this.noClip = true;
		this.ignoreFrustumCheck = true;
		this.reviver = reviver;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isInRangeToRenderDist(double distance)
	{
		double d0 = this.getEntityBoundingBox().getAverageEdgeLength();

		if (Double.isNaN(d0))
		{
			d0 = 1.0D;
		}

		d0 = d0 * 64.0D * 5;
		return distance < d0 * d0;
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();

		age++;

		if (!world.isRemote)
		{
			if (this.reviver == null || this.reviver.isDead || this.reviver.getDistanceSq(this.getPosition()) > 25 || this.reviver.dimension != this.dimension || toRevive == null || toRevive.isDead)
			{
				this.setDead();
			}

			if (this.world.getTotalWorldTime() % 20 == 0)
			{
				EntityPlayerMP player = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUsername(toRevive.playerName);
				if (player == null)
				{
					this.setDead();
				}
			}

			if (this.age >= 220 && this.world.getTotalWorldTime() % 10 == 0)
			{
				reviver.attackEntityFrom(DamageSource.MAGIC, 1f);
			}

			if (this.age >= 400)
			{
				toRevive.setDead();
				this.setDead();

				EntityPlayerMP player = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUsername(toRevive.playerName);
				if (player != null)
				{
					if (player.getHealth() <= 0)
					{
						EntityPlayerMP revived = player.connection.player = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().recreatePlayerEntity(player, 0, false);
						if (revived.world.provider.getDimension() != this.dimension)
						{
							FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().transferPlayerToDimension(revived, this.world.provider.getDimension(), new Teleporter((WorldServer) this.world));
						}
						revived.connection.setPlayerLocation(posX, posY, posZ, revived.rotationYaw, revived.rotationPitch);
						revived.setPositionAndUpdate(posX, posY, posZ);
					}
				}
			}
		}
		else
		{
			if (this.age >= 200)
			{
				world.spawnParticle(EnumParticleTypes.REDSTONE, this.posX + Math.random() - 0.5f, this.posY + Math.random(), this.posZ + Math.random() - 0.5f, 0, 0, 0, 1);
			}
		}
	}

	@Override
	public void setDead()
	{
		super.setDead();

		if (this.world.isRemote)
		{
			for (int i = 0; i < 100; i++)
			{
				world.spawnParticle(EnumParticleTypes.REDSTONE, this.posX + Math.random() * 3 - 1.5f, this.posY + Math.random(), this.posZ + Math.random() * 3 - 1.5f, 0, 0, 0, 1);
			}
		}
	}

	@Override
	protected void entityInit()
	{
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbt)
	{
		int soulID = nbt.getInteger("soul");

		Entity entity = world.getEntityByID(soulID);

		if (entity != null && entity instanceof EntitySoul)
		{
			this.toRevive = (EntitySoul) entity;
		}
		else
		{
			this.setDead();
		}
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbt)
	{
		nbt.setInteger("soul", toRevive.getEntityId());
	}

}
