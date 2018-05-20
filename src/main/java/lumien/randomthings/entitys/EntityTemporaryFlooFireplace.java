package lumien.randomthings.entitys;

import lumien.randomthings.client.particles.ParticleFlooFlame;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityTemporaryFlooFireplace extends Entity
{
	int age;

	public EntityTemporaryFlooFireplace(World worldIn)
	{
		super(worldIn);

		this.setSize(2, 1);
	}

	public EntityTemporaryFlooFireplace(World worldIn, double posX, double posY, double posZ)
	{
		this(worldIn);

		this.setPosition(posX, posY, posZ);
	}

	@Override
	public void onEntityUpdate()
	{
		super.onEntityUpdate();

		age++;

		if (this.world.isRemote && age >= 7)
		{
			spawnParticles();
		}
		else
		{
			if (age > 260)
			{
				this.setDead();
			}
		}
	}

	@SideOnly(Side.CLIENT)
	private void spawnParticles()
	{
		for (float modX = -1; modX <= 1; modX += 0.2)
		{
			for (float modZ = -1; modZ <= 1; modZ += 0.2)
			{
				ParticleFlooFlame particle = new ParticleFlooFlame(world, posX + modX + (Math.random() * 0.2 - 0.1), posY + 0.05, posZ + modZ + (Math.random() * 0.1 - 0.05), 0, Math.random() * 0.01, 0);

				Minecraft.getMinecraft().effectRenderer.addEffect(particle);
			}
		}
	}

	@Override
	protected void entityInit()
	{
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound)
	{
		this.age = compound.getInteger("age");
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound)
	{
		compound.setInteger("age", age);
	}
}
