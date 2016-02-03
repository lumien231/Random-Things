package lumien.randomthings.entitys;

import java.awt.Color;
import java.util.Random;

import lumien.randomthings.item.ItemIngredient;
import lumien.randomthings.item.ModItems;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntitySpirit extends EntityFlying
{
	private BlockPos spawnPosition;

	Random rng = new Random();

	int changePositionCounter = 0;

	public EntitySpirit(World worldIn)
	{
		super(worldIn);

		this.setSize(0.25F, 0.25F);

		this.moveHelper = new SpiritMoveHelper(this);
	}
	
	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(1.0);
	}

	public void fall(float distance, float damageMultiplier)
	{
	}

	protected void updateFallState(double y, boolean onGroundIn, Block blockIn, BlockPos pos)
	{
	}

	@Override
	public void knockBack(Entity entity, float p_70653_2_, double p_70653_3_, double p_70653_5_)
	{

	}

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
		int i = this.rand.nextInt(3);

		if (lootingModifier > 0)
		{
			i += this.rand.nextInt(lootingModifier + 1);
		}

		for (int j = 0; j < i; ++j)
		{
			this.entityDropItem(new ItemStack(ModItems.ingredients, 1, ItemIngredient.INGREDIENT.ECTO_PLASM.id), 0);
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
				this.worldObj.spawnParticle(EnumParticleTypes.FIREWORKS_SPARK, this.posX, this.posY, this.posZ, Math.random() * 0.02 - 0.01, Math.random() * 0.02, Math.random() * 0.02 - 0.01);
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

		public void onUpdateMoveHelper()
		{
			parentEntity.getLookHelper().setLookPosition(parentEntity.posX, parentEntity.posY, parentEntity.posZ-1, 10f, parentEntity.getVerticalFaceSpeed());
			if (this.update)
			{
				double d0 = this.posX - this.parentEntity.posX;
				double d1 = this.posY - this.parentEntity.posY;
				double d2 = this.posZ - this.parentEntity.posZ;
				double d3 = d0 * d0 + d1 * d1 + d2 * d2;

				d3 = (double) MathHelper.sqrt_double(d3);

				if (this.isNotColliding(this.posX, this.posY, this.posZ, d3) && d3 > 0.2)
				{
					this.parentEntity.motionX += d0 / d3 * 0.01D;
					this.parentEntity.motionY += d1 / d3 * 0.01D;
					this.parentEntity.motionZ += d2 / d3 * 0.01D;
				}
				else
				{
					this.update = false;
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

			for (int i = 1; (double) i < p_179926_7_; ++i)
			{
				axisalignedbb = axisalignedbb.offset(d0, d1, d2);

				if (!this.parentEntity.worldObj.getCollidingBoundingBoxes(this.parentEntity, axisalignedbb).isEmpty())
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
