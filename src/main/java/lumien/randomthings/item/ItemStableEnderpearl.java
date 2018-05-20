package lumien.randomthings.item;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import com.mojang.authlib.GameProfile;

import lumien.randomthings.util.PlayerUtil;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemStableEnderpearl extends ItemBase
{
	Random rand = new Random();

	public ItemStableEnderpearl()
	{
		super("stableEnderpearl");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World world, List tooltip, ITooltipFlag advanced)
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
			if (!entityItem.world.isRemote)
			{
				entityItem.setDead();
				ItemStack itemStack = entityItem.getItem();
				EntityPlayerMP player = null;
				if (itemStack.getTagCompound() != null)
				{
					String uuidString = itemStack.getTagCompound().getString("player-uuid");
					UUID uuid = UUID.fromString(uuidString);
					player = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(uuid);
				}

				if (player != null)
				{
					player.world.playSound(null, player.getPosition(), SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.PLAYERS, 1, 1);

					if (player.dimension != entityItem.dimension)
					{
						PlayerUtil.teleportPlayerToDimension(player, entityItem.dimension);
					}

					player.connection.setPlayerLocation(entityItem.posX, entityItem.posY, entityItem.posZ, player.rotationYaw, player.rotationPitch);
				}
				else
				{
					// Random Teleport
					List<EntityLivingBase> entityList = entityItem.world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(entityItem.posX - 10, entityItem.posY - 10, entityItem.posZ - 10, entityItem.posX + 10, entityItem.posY + 10, entityItem.posZ + 10));
					if (!entityList.isEmpty())
					{
						EntityLivingBase target = entityList.get(rand.nextInt(entityList.size()));
						if (target instanceof EntityPlayerMP)
						{
							EntityPlayerMP targetPlayer = (EntityPlayerMP) target;
							targetPlayer.world.playSound(null, targetPlayer.getPosition(), SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.PLAYERS, 1, 1);
							targetPlayer.connection.setPlayerLocation(entityItem.posX, entityItem.posY, entityItem.posZ, targetPlayer.rotationYaw, targetPlayer.rotationPitch);
						}
						else
						{
							target.world.playSound(null, target.getPosition(), SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.PLAYERS, 1, 1);
							target.setPositionAndUpdate(entityItem.posX, entityItem.posY, entityItem.posZ);
						}
					}
				}
				entityItem.world.playSound(null, entityItem.getPosition(), SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.PLAYERS, 1, 1);
			}
		}
		else
		{
			if (!entityItem.world.isRemote)
			{
				data.setInteger("counter", counter + 1);
			}
			else
			{
				if (entityItem.getAge() > 50)
				{
					for (int i = 0; i < 2; ++i)
					{
						entityItem.world.spawnParticle(EnumParticleTypes.PORTAL, entityItem.posX + (this.rand.nextDouble() - 0.5D) * 1, entityItem.posY + this.rand.nextDouble() * 2 - 0.25D, entityItem.posZ + (this.rand.nextDouble() - 0.5D) * 1, (this.rand.nextDouble() - 0.5D) * 2.0D, -this.rand.nextDouble(), (this.rand.nextDouble() - 0.5D) * 2.0D, new int[0]);
					}
				}
			}
		}

		return false;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand)
	{
		ItemStack itemStackIn = playerIn.getHeldItem(hand);
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
		return new ActionResult<>(EnumActionResult.SUCCESS, itemStackIn);
	}
}
