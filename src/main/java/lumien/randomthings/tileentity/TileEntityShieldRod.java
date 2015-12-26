package lumien.randomthings.tileentity;

import java.util.List;

import com.google.common.base.Predicates;

import lumien.randomthings.block.BlockRod;
import lumien.randomthings.client.particles.EntityColoredSmokeFX;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.world.WorldServer;

public class TileEntityShieldRod extends TileEntityRod
{
	static int RANGE = 5;

	@Override
	public void writeDataToNBT(NBTTagCompound compound)
	{
		super.writeDataToNBT(compound);
	}

	@Override
	public void readDataFromNBT(NBTTagCompound compound)
	{
		super.readDataFromNBT(compound);
	}

	@Override
	public void update()
	{
		if (this.worldObj.getBlockState(this.pos).getValue(BlockRod.ACTIVE))
		{
			if (!this.worldObj.isRemote)
			{
				List<Entity> projectileEntitys = this.worldObj.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(this.pos.add(-RANGE, -RANGE, -RANGE), this.pos.add(RANGE, RANGE, RANGE)), Predicates.instanceOf(IProjectile.class));

				for (Entity entity : projectileEntitys)
				{
					IProjectile projectile = (IProjectile) entity;
					if (entity.getDistanceSq(this.pos) <= RANGE * RANGE && consumeExperience(3))
					{
						entity.setDead();

						((WorldServer) this.worldObj).spawnParticle(EnumParticleTypes.FIREWORKS_SPARK, true, entity.posX, entity.posY, entity.posZ, 5, 0D, 0D, 0D, 0.2);
					}
				}
			}
			else
			{
				for (int i = 0; i < 10; i++)
				{
					double u = -1 + Math.random() * 2;
					double o = -Math.random() * Math.PI * 2;

					double x = this.pos.getX() + 0.5 + Math.sqrt(1 - u * u) * Math.cos(o) * RANGE + Math.random() * 0.1;
					double y = this.pos.getY() + Math.sqrt(1 - u * u) * Math.sin(o) * RANGE + Math.random() * 0.1;
					double z = this.pos.getZ() + 0.5 + u * RANGE + Math.random() * 0.2;

					EntityColoredSmokeFX fx = new EntityColoredSmokeFX(this.worldObj, x, y, z, 0, 0, 0, 1);
					fx.setRBGColorF(1, 1, 0);

					Minecraft.getMinecraft().effectRenderer.addEffect(fx);
				}
			}
		}
	}

}
