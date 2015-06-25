package lumien.randomthings.item;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.mojang.authlib.GameProfile;

public class ItemStableEnderpearl extends ItemBase
{
	Random rand = new Random();

	public ItemStableEnderpearl()
	{
		super("stableEnderpearl");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List tooltip, boolean advanced)
	{
		NBTTagCompound compound;
		if ((compound = stack.getTagCompound()) != null)
		{
			String playerName = compound.getString("player-name");
			tooltip.add(I18n.format("tooltip.stableEnderpearl.boundto", playerName));
		}
	}

	@Override
	public boolean onEntityItemUpdate(EntityItem entityItem)
	{
		NBTTagCompound data = entityItem.getEntityData();
		int counter = data.getInteger("counter");
		if (counter == 140)
		{
			if (!entityItem.worldObj.isRemote)
			{
				entityItem.setDead();
				ItemStack itemStack = entityItem.getEntityItem();
				EntityPlayerMP player = null;
				if (itemStack.getTagCompound() != null)
				{
					String uuidString = itemStack.getTagCompound().getString("player-uuid");
					UUID uuid = UUID.fromString(uuidString);
					player = MinecraftServer.getServer().getConfigurationManager().getPlayerByUUID(uuid);
				}

				if (player != null)
				{
					player.worldObj.playSoundEffect(player.posX, player.posY, player.posZ, "mob.endermen.portal", 1, 1);
					player.playerNetServerHandler.setPlayerLocation(entityItem.posX, entityItem.posY, entityItem.posZ, player.rotationYaw, player.rotationPitch);
				}
				else
				{
					// Random Teleport
					List<EntityLivingBase> entityList = entityItem.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(entityItem.posX - 10, entityItem.posY - 10, entityItem.posZ - 10, entityItem.posX + 10, entityItem.posY + 10, entityItem.posZ + 10));
					if (!entityList.isEmpty())
					{
						EntityLivingBase target = entityList.get(rand.nextInt(entityList.size()));
						if (target instanceof EntityPlayerMP)
						{
							EntityPlayerMP targetPlayer = (EntityPlayerMP) target;
							targetPlayer.worldObj.playSoundEffect(targetPlayer.posX, targetPlayer.posY, targetPlayer.posZ, "mob.endermen.portal", 1, 1);
							targetPlayer.playerNetServerHandler.setPlayerLocation(entityItem.posX, entityItem.posY, entityItem.posZ, targetPlayer.rotationYaw, targetPlayer.rotationPitch);
						}
						else
						{
							target.worldObj.playSoundEffect(target.posX, target.posY, target.posZ, "mob.endermen.portal", 1, 1);
							target.setPositionAndUpdate(entityItem.posX, entityItem.posY, entityItem.posZ);
						}
					}
				}
				entityItem.worldObj.playSoundEffect(entityItem.posX, entityItem.posY, entityItem.posZ, "mob.endermen.portal", 1, 1);
			}
		}
		else
		{
			if (!entityItem.worldObj.isRemote)
			{
				data.setInteger("counter", counter + 1);
			}
			else
			{
				if (entityItem.getAge() > 50)
				{
					for (int i = 0; i < 2; ++i)
					{
						entityItem.worldObj.spawnParticle(EnumParticleTypes.PORTAL, entityItem.posX + (this.rand.nextDouble() - 0.5D) * 1, entityItem.posY + this.rand.nextDouble() * 2 - 0.25D, entityItem.posZ + (this.rand.nextDouble() - 0.5D) * 1, (this.rand.nextDouble() - 0.5D) * 2.0D, -this.rand.nextDouble(), (this.rand.nextDouble() - 0.5D) * 2.0D, new int[0]);
					}
				}
			}
		}

		return false;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn)
	{
		if (!worldIn.isRemote)
		{
			if (itemStackIn.getTagCompound() == null)
			{
				itemStackIn.setTagCompound(new NBTTagCompound());
			}

			NBTTagCompound compound = itemStackIn.getTagCompound();

			GameProfile gameProfile = playerIn.getGameProfile();

			compound.setString("player-uuid", gameProfile.getId().toString());
			compound.setString("player-name", gameProfile.getName());
		}
		return itemStackIn;
	}
}
