package lumien.randomthings.entitys;

import io.netty.buffer.ByteBuf;

import java.util.List;

import lumien.randomthings.item.ItemRezStone;
import lumien.randomthings.util.PlayerUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
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
		this.noClip = true;
		this.playerName = "";
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getBrightnessForRender(float partial)
	{
		return 255;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean isInRangeToRenderDist(double distance)
	{
		double d0 = this.getEntityBoundingBox().getAverageEdgeLength();

        if (Double.isNaN(d0))
        {
            d0 = 1.0D;
        }

        d0 = d0 * 64.0D * 5;
        return distance < d0 * d0;
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

	@Override
	public void applyEntityCollision(Entity entityIn)
	{

	}

	@Override
	protected boolean pushOutOfBlocks(double x, double y, double z)
	{
		return false;
	}

	@Override
	public boolean canBeCollidedWith()
	{
		return true;
	}

	@Override
	protected void doBlockCollisions()
	{

	}

	@Override
	public final boolean processInitialInteract(EntityPlayer player, ItemStack stack, EnumHand hand)
	{
		ItemStack equipped = player.getHeldItemMainhand();
		if (!worldObj.isRemote && equipped.getItem() instanceof ItemRezStone && this.worldObj.getMinecraftServer().getPlayerList().getPlayerByUsername(playerName) != null)
		{
			List<EntityReviveCircle> circles = this.worldObj.getEntitiesWithinAABB(EntityReviveCircle.class, new AxisAlignedBB(this.posX - 2, this.posY - 2, this.posZ - 2, this.posX + 2, this.posZ + 2, this.posZ + 2));

			for (EntityReviveCircle circle : circles)
			{
				if (circle.toRevive == this)
				{
					return false;
				}
			}

			worldObj.spawnEntityInWorld(new EntityReviveCircle(worldObj, player, posX, posY, posZ, this));

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
	public AxisAlignedBB getCollisionBoundingBox()
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
				EntityPlayerMP player = this.worldObj.getMinecraftServer().getPlayerList().getPlayerByUsername(playerName);
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
