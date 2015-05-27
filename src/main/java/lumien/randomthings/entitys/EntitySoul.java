package lumien.randomthings.entitys;

import java.util.List;

import io.netty.buffer.ByteBuf;
import lumien.randomthings.RandomThings;
import lumien.randomthings.item.ItemRezStone;
import lumien.randomthings.item.ModItems;
import lumien.randomthings.util.PlayerUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntitySoul extends Entity implements IEntityAdditionalSpawnData
{
	String playerName;
	int counter = 0;

	public boolean render = false;

	public int type;

	public EntitySoul(World worldObj)
	{
		super(worldObj);

		this.setSize(0.3F, 0.3F);
		this.renderDistanceWeight = 5;
		this.noClip = true;
		this.playerName = "";
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getBrightnessForRender(float partial)
	{
		return 255;
	}

	public EntitySoul(World worldObj, double posX, double posY, double posZ, String playerName)
	{
		super(worldObj);
		this.setSize(0.3F, 0.3F);
		this.setPosition(posX, posY, posZ);
		this.motionX = 0;
		this.motionY = 0;
		this.motionZ = 0;

		this.noClip = true;
		this.playerName = playerName;
	}

	public void applyEntityCollision(Entity entityIn)
	{

	}

	protected boolean pushOutOfBlocks(double x, double y, double z)
	{
		return false;
	}

	@Override
	public boolean canBeCollidedWith()
	{
		return true;
	}

	protected void doBlockCollisions()
	{

	}

	@Override
	public boolean interactFirst(EntityPlayer user)
	{
		ItemStack equipped = user.getCurrentEquippedItem();
		if (!worldObj.isRemote && equipped.getItem() instanceof ItemRezStone && MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(playerName) != null)
		{
			List<EntityReviveCircle> circles = this.worldObj.getEntitiesWithinAABB(EntityReviveCircle.class, AxisAlignedBB.fromBounds(this.posX - 2, this.posY - 2, this.posZ - 2, this.posX + 2, this.posZ + 2, this.posZ + 2));
			
			for (EntityReviveCircle circle:circles)
			{
				if (circle.toRevive==this)
				{
					return false;
				}
			}
			
			worldObj.spawnEntityInWorld(new EntityReviveCircle(worldObj, user, posX, posY, posZ, this));
			
			return true;
		}
		return false;
	}

	@Override
	protected boolean canTriggerWalking()
	{
		return false;
	}

	@Override
	public AxisAlignedBB getCollisionBox(Entity p_70114_1_)
	{
		return null;
	}

	@Override
	public AxisAlignedBB getBoundingBox()
	{
		return null;
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();
		counter++;
		if (worldObj.getTotalWorldTime() % 20 == 0)
		{
			if (!worldObj.isRemote)
			{
				EntityPlayerMP player = MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(playerName);
				if (player != null)
				{
					if (player.getHealth() > 0)
					{
						this.setDead();
					}
				}
			}
			else
			{
				render = PlayerUtil.isPlayerOnline(this.playerName);
			}
		}
	}

	@Override
	protected void entityInit()
	{
		type = this.rand.nextInt(2);
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbt)
	{
		this.playerName = nbt.getString("playerName");
		this.type = nbt.getInteger("type");
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbt)
	{
		nbt.setString("playerName", playerName);
		nbt.setInteger("type", type);
	}

	@Override
	public void writeSpawnData(ByteBuf buffer)
	{
		ByteBufUtils.writeUTF8String(buffer, playerName);
		buffer.writeInt(type);
	}

	@Override
	public void readSpawnData(ByteBuf additionalData)
	{
		this.playerName = ByteBufUtils.readUTF8String(additionalData);
		this.type = additionalData.readInt();
	}

}
