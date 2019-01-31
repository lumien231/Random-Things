package lumien.randomthings.entitys;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import akka.io.Tcp.Message;
import io.netty.buffer.ByteBuf;
import lumien.randomthings.client.ModSounds;
import lumien.randomthings.item.ItemTimeInABottle;
import lumien.randomthings.item.ModItems;
import lumien.randomthings.network.MessageUtil;
import lumien.randomthings.network.PacketHandler;
import lumien.randomthings.network.messages.MessageEclipsedClock;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityEclipsedClock extends EntityHanging implements IEntityAdditionalSpawnData
{
	ItemStack clock;

	private static DataParameter<Integer> targetTime = EntityDataManager.createKey(EntityEclipsedClock.class, DataSerializers.VARINT);

	private int timeDisplayCounter;

	private int animationCounter;
	
	private int cooldownCounter;

	public EntityEclipsedClock(World worldIn)
	{
		super(worldIn);
	}

	public EntityEclipsedClock(World worldIn, BlockPos hangingPositionIn, EnumFacing facing)
	{
		super(worldIn, hangingPositionIn);

		this.updateFacingWithBoundingBox(facing);
	}

	@Override
	protected void entityInit()
	{
		this.getDataManager().register(targetTime, 0);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound)
	{
		super.writeEntityToNBT(compound);

		compound.setInteger("targetTime", getTargetTime());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		super.readEntityFromNBT(compound);

		setTargetTime(compound.getInteger("targetTime"));
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();

		if (this.world.isRemote)
		{
			if (this.timeDisplayCounter > 0)
			{
				this.timeDisplayCounter--;
			}

			if (this.animationCounter > 0)
			{
				this.animationCounter--;
			}
		}
		else
		{
			if (cooldownCounter > 0)
			{
				cooldownCounter--;
			}
		}
	}

	public void triggerAnimation()
	{
		if (this.animationCounter == 0)
			this.animationCounter = 100;
	}

	public boolean shouldDisplayTime()
	{
		return this.timeDisplayCounter > 0;
	}

	@Override
	public boolean processInitialInteract(EntityPlayer player, EnumHand hand)
	{
		ItemStack bottle;

		if ((bottle = player.getHeldItem(hand)).getItem() instanceof ItemTimeInABottle)
		{
			if (!this.world.isRemote && cooldownCounter == 0)
			{
				int timeStored = ItemTimeInABottle.getStoredTime(bottle);
				int dif = (getTargetTime() - (int) this.world.getWorldTime()) % 24000;

				if (dif < 0)
				{
					dif += 24000;
				}

				if (timeStored >= dif || player.capabilities.isCreativeMode)
				{
					world.setWorldTime(world.getWorldTime() + dif);

					if (!player.capabilities.isCreativeMode)
						ItemTimeInABottle.setStoredTime(bottle, timeStored - dif);

					cooldownCounter = 110;
					MessageEclipsedClock msg = new MessageEclipsedClock(this.getEntityId());
					MessageUtil.sendToAllWatchingPos(this.world, this.getPosition(), msg);

					world.playSound(null, this.getPosition(), ModSounds.TIME, SoundCategory.BLOCKS, 0.3F, 1.2F);
				}
			}
		}
		else
		{
			if (!this.world.isRemote)
			{
				int targetTime = getTargetTime();
				if (player.isSneaking())
				{
					targetTime -= 20 * 30;
				}
				else
				{
					targetTime += 20 * 30;
				}

				targetTime = targetTime % 24000;

				setTargetTime(targetTime);
			}
			else
			{
				this.timeDisplayCounter = 60;
			}
		}
		return true;
	}

	public int getTargetTime()
	{
		return this.dataManager.get(targetTime);
	}

	public void setTargetTime(int newTime)
	{
		this.dataManager.set(targetTime, newTime);
	}

	@Override
	public boolean canBeCollidedWith()
	{
		return true;
	}

	public String getStringTargetTime()
	{
		String myTime = "06:00";
		SimpleDateFormat df = new SimpleDateFormat("HH:mm");
		Date d;
		try
		{
			d = df.parse(myTime);
			Calendar cal = Calendar.getInstance();
			cal.setTime(d);
			cal.add(Calendar.MINUTE, (int) (1440 / 24000D * getTargetTime()));
			return df.format(cal.getTime());
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}

		return "XX:XX";
	}

	public float getCollisionBorderSize()
	{
		return 0.0F;
	}

	@Override
	public int getWidthPixels()
	{
		return 12;
	}

	@Override
	public int getHeightPixels()
	{
		return 12;
	}

	@SideOnly(Side.CLIENT)
	public boolean isInRangeToRenderDist(double distance)
	{
		double d0 = 16.0D;
		d0 = d0 * 64.0D * getRenderDistanceWeight();
		return distance < d0 * d0;
	}

	@Override
	public void onBroken(Entity brokenEntity)
	{
		if (this.world.getGameRules().getBoolean("doEntityDrops"))
		{
			this.playSound(SoundEvents.ENTITY_PAINTING_BREAK, 1.0F, 1.0F);

			if (brokenEntity instanceof EntityPlayer)
			{
				EntityPlayer entityplayer = (EntityPlayer) brokenEntity;

				if (entityplayer.capabilities.isCreativeMode)
				{
					return;
				}
			}

			this.entityDropItem(new ItemStack(ModItems.eclipsedClock), 0.0F);
		}
	}

	@Override
	public void playPlaceSound()
	{
	}

	@Override
	public void writeSpawnData(ByteBuf buffer)
	{
		buffer.writeLong(this.getHangingPosition().toLong());

		buffer.writeShort(this.facingDirection.ordinal());
	}

	@Override
	public void readSpawnData(ByteBuf additionalData)
	{
		this.hangingPosition = BlockPos.fromLong(additionalData.readLong());
		this.facingDirection = EnumFacing.VALUES[additionalData.readShort()];

		this.updateFacingWithBoundingBox(facingDirection);
	}

	public int getAnimationCounter()
	{
		return animationCounter;
	}

}
