package lumien.randomthings.entitys;

import javax.annotation.Nullable;

import com.google.common.base.Optional;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityTracker;
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
	private static final DataParameter<Optional<ItemStack>> ITEM = EntityDataManager.<Optional<ItemStack>> createKey(EntityProjectedItem.class, DataSerializers.OPTIONAL_ITEM_STACK);
	/**
	 * The age of this EntityItem (used to animate it up and down as well as
	 * expire it)
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
	 * The maximum age of this EntityItem. The item is expired once this is
	 * reached.
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
	 * returns if this entity triggers Block.onEntityWalking on the blocks they
	 * walk on. used for spiders and wolves to prevent them from trampling crops
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
		this.getDataManager().register(ITEM, Optional.<ItemStack> absent());
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void onUpdate()
	{
		if (this.getEntityItem() == null)
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

			this.moveEntity(motionX, motionY, motionZ);

			if (this.motionX == 0 && this.motionY == 0 && this.motionZ == 0 && !this.worldObj.isRemote)
			{
				if (this.enterInventories)
				{
					TileEntity nextTileEntity = this.worldObj.getTileEntity(new BlockPos(this.posX, this.posY, this.posZ).offset(this.direction));
					if (this.getEntityItem() != null && this.getEntityItem().stackSize > 0 && nextTileEntity != null && nextTileEntity.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, direction.getOpposite()))
					{
						IItemHandler itemHandler = nextTileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, direction.getOpposite());

						ItemStack remaining = ItemHandlerHelper.insertItemStacked(itemHandler, this.getEntityItem(), false);

						if (remaining != null && remaining.stackSize > 0)
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
				f = this.worldObj.getBlockState(new BlockPos(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.getEntityBoundingBox().minY) - 1, MathHelper.floor_double(this.posZ))).getBlock().slipperiness * 0.98F;
			}

			if (this.age != -32768)
			{
				++this.age;
			}

			ItemStack item = this.getDataManager().get(ITEM).orNull();

			if (!this.worldObj.isRemote && this.age >= lifespan)
			{
				this.setDead();
				this.dropAsItem();
			}

			if (item != null && item.stackSize <= 0)
			{
				this.setDead();
			}
		}
	}

	private void dropAsItem()
	{
		if (this.getEntityItem() != null && this.getEntityItem().stackSize > 0)
		{
			EntityItem ei = new EntityItem(this.worldObj, this.posX, this.posY, this.posZ, this.getEntityItem());
			this.worldObj.spawnEntityInWorld(ei);

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
		this.attackEntityFrom(DamageSource.inFire, amount);
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
			this.setBeenAttacked();
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

		if (this.getEntityItem() != null)
		{
			compound.setTag("Item", this.getEntityItem().writeToNBT(new NBTTagCompound()));
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
		this.setEntityItemStack(ItemStack.loadItemStackFromNBT(nbttagcompound));

		ItemStack item = this.getDataManager().get(ITEM).orNull();
		if (item == null || item.stackSize <= 0)
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
		if (!this.worldObj.isRemote && this.canBePickedUp)
		{
			ItemStack itemstack = this.getEntityItem();
			int i = itemstack.stackSize;

			if (i <= 0 || entityIn.inventory.addItemStackToInventory(itemstack))
			{
				if (!this.isSilent())
				{
					this.worldObj.playSound((EntityPlayer) null, entityIn.posX, entityIn.posY, entityIn.posZ, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
				}

				entityIn.onItemPickup(this, i);

				if (!this.worldObj.isRemote)
				{
					EntityTracker entitytracker = ((WorldServer) this.worldObj).getEntityTracker();

					entitytracker.sendToAllTrackingEntity(this, new SPacketCollectItem(this.getEntityId(), entityIn.getEntityId()));
				}
				if (itemstack.stackSize <= 0)
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
		return this.hasCustomName() ? this.getCustomNameTag() : I18n.translateToLocal("item." + this.getEntityItem().getUnlocalizedName());
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
	 * Returns the ItemStack corresponding to the Entity (Note: if no item
	 * exists, will log an error but still return an ItemStack containing
	 * Block.stone)
	 */
	public ItemStack getEntityItem()
	{
		ItemStack itemstack = (ItemStack) ((Optional) this.getDataManager().get(ITEM)).orNull();

		if (itemstack == null)
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
		this.getDataManager().set(ITEM, Optional.fromNullable(stack));
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