package lumien.randomthings.entitys;

import java.util.Random;

import lumien.randomthings.config.Numbers;
import lumien.randomthings.item.ItemIngredient;
import lumien.randomthings.item.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntitySpirit extends EntityFlying
{
	private BlockPos spawnPosition;

	Random rng = new Random();

	int changePositionCounter = 0;

	int spiritAge;

	public EntitySpirit(World worldIn)
	{
		super(worldIn);

		this.setSize(0.25F, 0.25F);

		this.moveHelper = new SpiritMoveHelper(this);
	}

	public EntitySpirit(World worldIn, double posX, double posY, double posZ)
	{
		this(worldIn);
		this.setPosition(posX, posY, posZ);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound tagCompund)
	{
		super.readEntityFromNBT(tagCompund);

		this.spiritAge = tagCompund.getInteger("spiritAge");
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound tagCompound)
	{
		super.writeEntityToNBT(tagCompound);

		tagCompound.setInteger("spiritAge", spiritAge);
	}

	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();

		spiritAge++;

		if (!this.worldObj.isRemote && spiritAge > Numbers.SPIRIT_LIFETIME)
		{
			this.onKillCommand();
		}
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		if (source instanceof EntityDamageSource)
		{
			EntityDamageSource eds = (EntityDamageSource) source;
			if (eds.getDamageType().equals("player") && eds.getEntity() instanceof EntityPlayer)
			{
				EntityPlayer player = (EntityPlayer) eds.getEntity();
				
				ItemStack equipped;
				if ((equipped = player.getHeldItemMainhand()) != null && equipped.getItem() == ModItems.spectreSword)
				{
					return super.attackEntityFrom(source, amount);
				}
			}
		}
		if (!source.isMagicDamage() && source != DamageSource.outOfWorld && !source.isCreativePlayer())
		{
			return false;
		}

		return super.attackEntityFrom(source, amount);
	}

	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(1.0);
	}

	@Override
	public void knockBack(Entity entity, float p_70653_2_, double p_70653_3_, double p_70653_5_)
	{

	}

	@Override
	protected void collideWithEntity(Entity entityIn)
	{
	}

	@Override
	protected void collideWithNearbyEntities()
	{
	}

	@Override
	public boolean canBePushed()
	{
		return false;
	}

	@Override
	protected void updateAITasks()
	{
		super.updateAITasks();

		if (spawnPosition == null)
		{
			this.spawnPosition = this.getPosition();
		}

		changePositionCounter++;

		if (changePositionCounter >= 60)
		{
			changePositionCounter = 0;
			int trys = 0;

			BlockPos newTarget = null;

			while (newTarget == null && trys < 10)
			{
				trys++;

				int modX = rng.nextInt(5) - 2;
				int modY = rng.nextInt(3);
				int modZ = rng.nextInt(5) - 2;

				BlockPos modPos = spawnPosition.add(modX, modY, modZ);

				if (worldObj.isAirBlock(modPos))
				{
					newTarget = modPos;
				}
			}

			if (newTarget != null)
			{
				this.moveHelper.setMoveTo(newTarget.getX(), newTarget.getY(), newTarget.getZ(), 0.02);
			}
		}
	}

	@Override
	protected void dropFewItems(boolean wasRecentlyHit, int lootingModifier)
	{
		if (wasRecentlyHit)
		{
			int i = 1;

			if (rand.nextInt(5) == 0)
			{
				i += 1;
			}

			for (int j = 0; j < i; ++j)
			{
				this.entityDropItem(new ItemStack(ModItems.ingredients, 1, ItemIngredient.INGREDIENT.ECTO_PLASM.id), 0);
			}
		}
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();

		if (this.worldObj.isRemote && (this.lastTickPosX != this.posX || this.lastTickPosY != this.posY || this.lastTickPosZ != this.posZ))
		{
			if (Math.random() < 0.5)
			{
				this.worldObj.spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE, this.posX, this.posY, this.posZ, Math.random() * 0.02 - 0.01, Math.random() * 0.02, Math.random() * 0.02 - 0.01);
			}
		}
	}

	static class SpiritMoveHelper extends EntityMoveHelper
	{
		private EntitySpirit parentEntity;
		private int courseChangeCooldown;

		public SpiritMoveHelper(EntitySpirit parentEntity)
		{
			super(parentEntity);
			this.parentEntity = parentEntity;
		}

		@Override
		public void onUpdateMoveHelper()
		{
			if (this.action == Action.MOVE_TO)
			{
				double d0 = this.posX - this.parentEntity.posX;
				double d1 = this.posY - this.parentEntity.posY;
				double d2 = this.posZ - this.parentEntity.posZ;
				double d3 = d0 * d0 + d1 * d1 + d2 * d2;

				d3 = MathHelper.sqrt_double(d3);

				if (this.isNotColliding(this.posX, this.posY, this.posZ, d3) && d3 > 0.2)
				{
					this.parentEntity.motionX += d0 / d3 * 0.01D;
					this.parentEntity.motionY += d1 / d3 * 0.01D;
					this.parentEntity.motionZ += d2 / d3 * 0.01D;
				}
				else
				{
					this.action = Action.WAIT;
				}
			}
		}

		/**
		 * Checks if entity bounding box is not colliding with terrain
		 */
		private boolean isNotColliding(double p_179926_1_, double p_179926_3_, double p_179926_5_, double p_179926_7_)
		{
			double d0 = (p_179926_1_ - this.parentEntity.posX) / p_179926_7_;
			double d1 = (p_179926_3_ - this.parentEntity.posY) / p_179926_7_;
			double d2 = (p_179926_5_ - this.parentEntity.posZ) / p_179926_7_;
			AxisAlignedBB axisalignedbb = this.parentEntity.getEntityBoundingBox();

			for (int i = 1; i < p_179926_7_; ++i)
			{
				axisalignedbb = axisalignedbb.offset(d0, d1, d2);

				if (!this.parentEntity.worldObj.getCollisionBoxes(this.parentEntity, axisalignedbb).isEmpty())
				{
					return false;
				}
			}

			return true;
		}

		@Override
		public void setMoveTo(double x, double y, double z, double speedIn)
		{
			super.setMoveTo(x, y, z, speedIn);
		}
	}
}
