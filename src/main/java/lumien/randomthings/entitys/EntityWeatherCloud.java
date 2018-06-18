package lumien.randomthings.entitys;

import java.util.Random;

import io.netty.buffer.ByteBuf;
import lumien.randomthings.client.particles.EntityColoredSmokeFX;
import lumien.randomthings.item.ItemWeatherEgg;
import lumien.randomthings.item.ItemWeatherEgg.TYPE;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityWeatherCloud extends Entity implements IEntityAdditionalSpawnData
{
	TYPE eggType = TYPE.SUN;
	int age = 0;

	public EntityWeatherCloud(World worldIn)
	{
		super(worldIn);

		this.noClip = true;
	}

	public EntityWeatherCloud(World worldIn, double x, double y, double z, TYPE eggType)
	{
		super(worldIn);

		this.noClip = true;

		this.eggType = eggType;

		this.setPosition(x, y, z);
	}

	@Override
	public void onEntityUpdate()
	{
		super.onEntityUpdate();

		if (this.age < 200)
		{
			this.motionY = 0.007;
		}
		else
		{
			if (this.posY < this.world.getHeight())
			{
				this.motionY += 0.001;
				this.motionY *= 1.02;
			}
			else if (!this.world.isRemote)
			{
				int i = (300 + (new Random()).nextInt(600)) * 20;

				WorldInfo info = this.world.getWorldInfo();

				switch (this.eggType)
				{
					case RAIN:
						info.setCleanWeatherTime(0);
						info.setRainTime(i);
						info.setThunderTime(i);
						info.setRaining(true);
						info.setThundering(false);

						break;
					case STORM:
						info.setCleanWeatherTime(0);
						info.setRainTime(i);
						info.setThunderTime(i);
						info.setRaining(true);
						info.setThundering(true);

						break;
					case SUN:
						info.setCleanWeatherTime(i);
						info.setRainTime(0);
						info.setThunderTime(0);
						info.setRaining(false);
						info.setThundering(false);

						break;
					default:
						break;

				}

				this.setDead();
			}
		}

		this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);

		if (this.world.isRemote)
		{
			spawnParticles();
		}

		age++;
	}
	
	@SideOnly(Side.CLIENT)
	private void spawnParticles()
	{
		switch (eggType)
		{
			case RAIN:
				spawnDefaultCloud();
				for (int i = 0; i < 2; i++)
				{
					double t = Math.PI * 2 * Math.random();

					double a = 0.25;
					double b = 0.35;

					a /= 1.5 + Math.random();
					b /= 1.5 + Math.random();

					double elX = a * Math.cos(t);
					double elZ = b * Math.sin(t);
					this.world.spawnParticle(EnumParticleTypes.WATER_WAKE, true, this.posX + elX, this.posY - 0.2, this.posZ + elZ, 0, -0.05, 0);
				}

				break;
			case STORM:
				spawnDefaultCloud();

				double t = Math.PI * 2 * Math.random();

				double a = 0.25;
				double b = 0.35;

				a /= 1.5 + Math.random();
				b /= 1.5 + Math.random();

				double elX = a * Math.cos(t);
				double elZ = b * Math.sin(t);

				EntityColoredSmokeFX particle = new EntityColoredSmokeFX(this.world, this.posX + elX, this.posY, this.posZ + elZ, Math.random() * 0.1 - 0.05, Math.random() * 0.2 - 0.1, Math.random() * 0.1 - 0.05);
				particle.setRBGColorF(1, 1, 0);
				Minecraft.getMinecraft().effectRenderer.addEffect(particle);


				break;
			case SUN:
				spawnNiceCloud();

				break;
			default:
				break;
		}
	}

	private void spawnDefaultCloud()
	{
		for (double y = -1; y <= 1; y += 1)
		{
			for (double t = 0; t < Math.PI * 2; t += Math.PI / 5)
			{
				double a = 0.25;
				double b = 0.35;

				a /= Math.abs(y) * 0.5 + 1;
				b /= Math.abs(y) * 0.5 + 1;

				double elX = a * Math.cos(t);
				double elZ = b * Math.sin(t);
				this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, true, this.posX + elX, this.posY + y / 8, this.posZ + elZ, 0, -0.03, 0);
			}
		}
	}

	@SideOnly(Side.CLIENT)
	private void spawnNiceCloud()
	{
		for (double y = -1; y <= 1; y += 1)
		{
			for (double t = 0; t < Math.PI * 2; t += Math.PI / 3)
			{
				double a = 0.25;
				double b = 0.35;

				a /= Math.abs(y) * 0.5 + 1;
				b /= Math.abs(y) * 0.5 + 1;

				double elX = a * Math.cos(t);
				double elZ = b * Math.sin(t);

				EntityColoredSmokeFX particle = new EntityColoredSmokeFX(this.world, this.posX + elX, this.posY + y / 8, this.posZ + elZ, 0, -0.03, 0);

				float shade = (float) (Math.random() * 0.05 - 0.025);

				particle.setRBGColorF(0.95F + shade, 0.95F + shade, 0.95F + shade);
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
		this.eggType = ItemWeatherEgg.TYPE.values()[compound.getInteger("eggType")];
		this.age = compound.getInteger("age");
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound)
	{
		compound.setInteger("eggType", this.eggType.ordinal());
		compound.setInteger("age", age);
	}

	@Override
	public void writeSpawnData(ByteBuf buffer)
	{
		buffer.writeInt(eggType.ordinal());
		buffer.writeInt(age);
	}

	@Override
	public void readSpawnData(ByteBuf additionalData)
	{
		this.eggType = TYPE.values()[additionalData.readInt()];
		this.age = additionalData.readInt();
	}

	public TYPE getEggType()
	{
		return eggType;
	}

}
