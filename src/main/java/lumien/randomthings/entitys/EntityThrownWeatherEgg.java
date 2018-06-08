package lumien.randomthings.entitys;

import io.netty.buffer.ByteBuf;
import lumien.randomthings.item.ItemIngredient;
import lumien.randomthings.item.ItemWeatherEgg;
import lumien.randomthings.item.ItemWeatherEgg.TYPE;
import lumien.randomthings.item.ModItems;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityThrownWeatherEgg extends EntityThrowable implements IEntityAdditionalSpawnData
{
	TYPE eggType = TYPE.SUN;

	public EntityThrownWeatherEgg(World worldIn)
	{
		super(worldIn);
	}

	public EntityThrownWeatherEgg(World worldIn, EntityLivingBase throwerIn, TYPE eggType)
	{
		super(worldIn, throwerIn);
		
		this.eggType = eggType;
	}

	public EntityThrownWeatherEgg(World worldIn, double x, double y, double z, TYPE eggType)
	{
		super(worldIn, x, y, z);

		this.eggType = eggType;
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound)
	{
		super.writeEntityToNBT(compound);

		compound.setInteger("eggType", eggType.ordinal());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		super.readEntityFromNBT(compound);

		this.eggType = ItemWeatherEgg.TYPE.values()[compound.getInteger("eggType")];
	}

	/**
	 * Handler for {@link World#setEntityState}
	 */
	@SideOnly(Side.CLIENT)
	public void handleStatusUpdate(byte id)
	{
		if (id == 3)
		{
			double d0 = 0.08D;

			for (int i = 0; i < 8; ++i)
			{
				this.world.spawnParticle(EnumParticleTypes.ITEM_CRACK, this.posX, this.posY, this.posZ, ((double) this.rand.nextFloat() - 0.5D) * 0.08D, ((double) this.rand.nextFloat() - 0.5D) * 0.08D, ((double) this.rand.nextFloat() - 0.5D) * 0.08D, Item.getIdFromItem(ModItems.weatherEgg), eggType.ordinal());
			}
		}
	}

	/**
	 * Called when this EntityThrowable hits a block or entity.
	 */
	protected void onImpact(RayTraceResult result)
	{
		if (result.entityHit != null)
		{
			result.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 1.0F);
		}

		if (!this.world.isRemote)
		{
			EntityWeatherCloud egg = new EntityWeatherCloud(this.world, result.hitVec.x, result.hitVec.y + 0.5, result.hitVec.z, this.eggType);
			this.world.spawnEntity(egg);

			this.world.setEntityState(this, (byte) 3);
			this.setDead();
		}
	}

	@Override
	public void writeSpawnData(ByteBuf buffer)
	{
		buffer.writeInt(eggType.ordinal());
	}

	@Override
	public void readSpawnData(ByteBuf additionalData)
	{
		this.eggType = ItemWeatherEgg.TYPE.values()[additionalData.readInt()];
	}

	public TYPE getEggType()
	{
		return eggType;
	}
}