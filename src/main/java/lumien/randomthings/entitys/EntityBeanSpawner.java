package lumien.randomthings.entitys;

import java.util.Random;

import lumien.randomthings.block.ModBlocks;
import lumien.randomthings.util.NBTUtil;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityBeanSpawner extends Entity
{
	BlockPos center;

	double counter;

	public EntityBeanSpawner(World worldIn)
	{
		super(worldIn);


	}

	@Override
	protected void entityInit()
	{
	}

	int maxRadius = 5;

	@Override
	public void onUpdate()
	{
		super.onUpdate();

		if (!worldObj.isRemote)
		{
			for (int run = 0; run < 10 && !isDead; run++)
			{
				for (double radius = maxRadius; radius > maxRadius - 5; radius--)
				{
					Random rng = new Random();

					double modX = (int) Math.floor(10 * Math.cos(counter / 5D));
					double modZ = (int) Math.floor(10 * Math.sin(counter / 5D));

					int nextX = (int) Math.floor(radius * Math.cos(6 * counter) + modX);
					double nextY = counter * 4;

					int nextZ = (int) Math.floor(radius * Math.sin(6 * counter) + modZ);

					if (nextY + posY > 200)
					{
						this.setDead();
						return;
					}

					int dif = (int) (nextY + posY - 180);
					if (dif <= 0)
					{
						dif = 1;
					}
					else
					{
						maxRadius = 5 + dif / 4;
					}

					BlockPos pos = new BlockPos(nextX + this.posX, nextY + this.posY, nextZ + this.posZ);

					if (radius == maxRadius - 5 + 1)
					{
						worldObj.setBlockState(pos, Blocks.GLOWSTONE.getDefaultState(), 2);
					}
					else
					{
						worldObj.setBlockState(pos, ModBlocks.specialBeanStalk.getDefaultState(), 2);
					}

					if (rng.nextBoolean())
					{
						counter += 2 * Math.PI / (400) / dif;
					}
				}
			}
		}
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound)
	{
		if (compound.hasKey("center"))
		{
			this.center = NBTUtil.readBlockPosFromNBT(compound, "center");
		}
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound)
	{
		if (center != null)
		{
			NBTUtil.writeBlockPosToNBT(compound, "center", center);
		}
	}

}
