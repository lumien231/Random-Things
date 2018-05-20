package lumien.randomthings.entitys;

import javax.annotation.Nullable;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityTracker;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SPacketCollectItem;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

public class EntityProjectedItem extends Entity implements IEntityAdditionalSpawnData
{
	private static final DataParameter<ItemStack> ITEM = EntityDataManager.<ItemStack>createKey(EntityProjectedItem.class, DataSerializers.ITEM_STACK);
	/**
	 * The age of this EntityItem (used to animate it up and down as well as expire
	 * it)
	 */
	private int age;

	/** The health of this EntityItem. (For example, damage for tools) */
	private int health;

	/** The EntityItem's random initial float height. */
	public float hoverStart;

	EnumFacing direction = EnumFacing.NORTH;
	boolean canBePickedUp = false;
	boolean enterInventories = true;

	/**
	 * The maximum age of this EntityItem. The item is expired once this is reached.
	 */
	public int lifespan = 20 * 60;

	public EntityProjectedItem(World worldIn, double x, double y, double z)
	{
		super(worldIn);
		this.health = 5;
		this.hoverStart = (float) (Math.random() * Math.PI * 2.0D);
		this.setSize(0.25F, 0.25F);
		this.setPosition(x, y, z);
		this.rotationYaw = (float) (Math.random() * 360.0D);
	}

	public EntityProjectedItem(World worldIn, double x, double y, double z, ItemStack stack, EnumFacing facing)
	{
		this(worldIn, x, y, z);
		this.setEntityItemStack(stack);
		this.lifespan = 20 * 60;
		this.direction = facing;
	}

	/**
	 * returns if this entity triggers Block.onEntityWalking on the blocks they walk
	 * on. used for spiders and wolves to prevent them from trampling crops
	 */
	@Override
	protected boolean canTriggerWalking()
	{
		return false;
	}

	public EntityProjectedItem(World worldIn)
	{
		super(worldIn);
		this.health = 5;
		this.hoverStart = (float) (Math.random() * Math.PI * 2.0D);
		this.setSize(0.25F, 0.25F);
		this.setEntityItemStack(new ItemStack(Blocks.AIR, 0));
	}

	public void setPickup(boolean canBePickedUp)
	{
		this.canBePickedUp = canBePickedUp;
	}

	public void setEnterInventories(boolean enterInventories)
	{
		this.enterInventories = enterInventories;
	}

	@Override
	protected void entityInit()
	{
		this.getDataManager().register(ITEM, ItemStack.EMPTY);
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void onUpdate()
	{
		if (this.getItem() == null)
		{
			this.setDead();
		}
		else
		{
			super.onUpdate();

			this.prevPosX = this.posX;
			this.prevPosY = this.posY;
			this.prevPosZ = this.posZ;

			this.noClip = this.pushOutOfBlocks(this.posX, (this.getEntityBoundingBox().minY + this.getEntityBoundingBox().maxY) / 2.0D, this.posZ);

			if (this.noClip)
			{
				dropAsItem();
			}

			this.motionX = direction.getFrontOffsetX() / 10D;
			this.motionY = direction.getFrontOffsetY() / 10D;
			this.motionZ = direction.getFrontOffsetZ() / 10D;

			this.move(MoverType.SELF, motionX, motionY, motionZ);

			if (this.motionX == 0 && this.motionY == 0 && this.motionZ == 0 && !this.world.isRemote)
			{
				if (this.enterInventories)
				{
					TileEntity nextTileEntity = this.world.getTileEntity(new BlockPos(this.posX, this.posY, this.posZ).offset(this.direction));
					if (this.getItem() != null && this.getItem().getCount() > 0 && nextTileEntity != null && nextTileEntity.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, direction.getOpposite()))
					{
						IItemHandler itemHandler = nextTileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, direction.getOpposite());

						ItemStack remaining = ItemHandlerHelper.insertItemStacked(itemHandler, this.getItem(), false);

						if (remaining != null && remaining.getCount() > 0)
						{
							this.dropAsItem();
						}
						else
						{
							this.setDead();
						}
					}
				}
				else
				{
					this.dropAsItem();
				}
			}

			float f = 0.98F;

			if (this.onGround)
			{
				f = this.world.getBlockState(new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.getEntityBoundingBox().minY) - 1, MathHelper.floor(this.posZ))).getBlock().slipperiness * 0.98F;
			}

			if (this.age != -32768)
			{
				++this.age;
			}

			ItemStack item = this.getDataManager().get(ITEM);

			if (!this.world.isRemote && this.age >= lifespan)
			{
				this.setDead();
				this.dropAsItem();
			}

			if (item != ItemStack.EMPTY && item.getCount() <= 0)
			{
				this.setDead();
			}
		}
	}

	private void dropAsItem()
	{
		if (this.getItem() != null && this.getItem().getCount() > 0)
		{
			EntityItem ei = new EntityItem(this.world, this.posX, this.posY, this.posZ, this.getItem());
			this.world.spawnEntity(ei);

			ei.lifespan = 20 * 60;
		}

		this.setDead();
	}

	public void setLifeSpan(int newLifespawn)
	{
		this.lifespan = newLifespawn;
	}

	/**
	 * Will deal the specified amount of fire damage to the entity if the entity
	 * isn't immune to fire damage.
	 */
	@Override
	protected void dealFireDamage(int amount)
	{
		this.attackEntityFrom(DamageSource.IN_FIRE, amount);
	}

	/**
	 * Called when the entity is attacked.
	 */
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		if (this.isEntityInvulnerable(source))
		{
			return false;
		}
		else
		{
			this.markVelocityChanged();
			this.health = (int) (this.health - amount);

			if (this.health <= 0)
			{
				this.setDead();
			}

			return false;
		}
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	public void writeEntityToNBT(NBTTagCompound compound)
	{
		compound.setShort("Health", (short) this.health);
		compound.setShort("Age", (short) this.age);
		compound.setInteger("Lifespan", lifespan);
		compound.setInteger("direction", direction.ordinal());
		compound.setBoolean("canBePickedUp", canBePickedUp);

		if (this.getItem() != null)
		{
			compound.setTag("Item", this.getItem().writeToNBT(new NBTTagCompound()));
		}
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		this.health = compound.getShort("Health");
		this.age = compound.getShort("Age");
		this.direction = EnumFacing.values()[compound.getInteger("direction")];
		this.canBePickedUp = compound.getBoolean("canBePickedUp");

		NBTTagCompound nbttagcompound = compound.getCompoundTag("Item");
		this.setEntityItemStack(new ItemStack(nbttagcompound));

		ItemStack item = this.getDataManager().get(ITEM);
		if (item.isEmpty() || item.getCount() <= 0)
			this.setDead();
		if (compound.hasKey("Lifespan"))
			lifespan = compound.getInteger("Lifespan");
	}

	/**
	 * Called by a player entity when they collide with an entity
	 */
	@Override
	public void onCollideWithPlayer(EntityPlayer entityIn)
	{
		if (!this.world.isRemote && this.canBePickedUp)
		{
			ItemStack itemstack = this.getItem();
			int i = itemstack.getCount();

			if (i <= 0 || entityIn.inventory.addItemStackToInventory(itemstack))
			{
				if (!this.isSilent())
				{
					this.world.playSound((EntityPlayer) null, entityIn.posX, entityIn.posY, entityIn.posZ, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
				}

				entityIn.onItemPickup(this, i);

				if (!this.world.isRemote)
				{
					EntityTracker entitytracker = ((WorldServer) this.world).getEntityTracker();

					entitytracker.sendToTracking(this, new SPacketCollectItem(this.getEntityId(), entityIn.getEntityId(), this.getItem().getCount()));
				}
				if (itemstack.getCount() <= 0)
				{
					this.setDead();
				}

				entityIn.addStat(StatList.getObjectsPickedUpStats(itemstack.getItem()), i);
			}
		}
	}

	/**
	 * Get the name of this object. For players this returns their username
	 */
	@Override
	public String getName()
	{
		return this.hasCustomName() ? this.getCustomNameTag() : I18n.translateToLocal("item." + this.getItem().getUnlocalizedName());
	}

	/**
	 * Returns true if it's possible to attack this entity with an item.
	 */
	@Override
	public boolean canBeAttackedWithItem()
	{
		return false;
	}

	@Override
	@Nullable
	public Entity changeDimension(int dimensionIn)
	{
		Entity entity = super.changeDimension(dimensionIn);

		return entity;
	}

	/**
	 * Returns the ItemStack corresponding to the Entity (Note: if no item exists,
	 * will log an error but still return an ItemStack containing Block.stone)
	 */
	public ItemStack getItem()
	{
		ItemStack itemstack = this.getDataManager().get(ITEM);

		if (itemstack.isEmpty())
		{
			return new ItemStack(Blocks.STONE);
		}
		else
		{
			return itemstack;
		}
	}

	/**
	 * Sets the ItemStack for this entity
	 */
	public void setEntityItemStack(@Nullable ItemStack stack)
	{
		this.getDataManager().set(ITEM, stack);
		this.getDataManager().setDirty(ITEM);
	}

	@SideOnly(Side.CLIENT)
	public int getAge()
	{
		return this.age;
	}

	public void setNoDespawn()
	{
		this.age = -6000;
	}

	@Override
	public void writeSpawnData(ByteBuf buffer)
	{
		buffer.writeInt(direction.ordinal());
	}

	@Override
	public void readSpawnData(ByteBuf additionalData)
	{
		this.direction = EnumFacing.values()[additionalData.readInt()];
	}
}