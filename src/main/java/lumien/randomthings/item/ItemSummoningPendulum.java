package lumien.randomthings.item;

import java.util.List;

import lumien.randomthings.worldgen.SingleRandomChestContent;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemSummoningPendulum extends ItemBase
{
	public ItemSummoningPendulum()
	{
		super("summoningPendulum");

		ChestGenHooks.addItem(ChestGenHooks.DUNGEON_CHEST, new SingleRandomChestContent(new ItemStack(this), 1, 1, 1));
		ChestGenHooks.addItem(ChestGenHooks.STRONGHOLD_CORRIDOR, new SingleRandomChestContent(new ItemStack(this), 1, 1, 30));
		
		this.setMaxStackSize(1);
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack)
	{
		int entityCount = 0;

		NBTTagCompound compound = stack.getTagCompound();
		if (compound != null)
		{
			NBTTagList tagList = compound.getTagList("entitys", 10);
			entityCount = tagList.tagCount();
		}

		return 1 - 1F / 5F * entityCount;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack stack)
	{
		int entityCount = 0;

		NBTTagCompound compound = stack.getTagCompound();
		if (compound != null)
		{
			NBTTagList tagList = compound.getTagList("entitys", 10);
			entityCount = tagList.tagCount();
		}

		return entityCount == 5;
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack)
	{
		int entityCount = 0;

		NBTTagCompound compound = stack.getTagCompound();
		if (compound != null)
		{
			NBTTagList tagList = compound.getTagList("entitys", 10);
			entityCount = tagList.tagCount();
		}

		return entityCount != 5;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List tooltip, boolean advanced)
	{
		int entityCount = 0;

		NBTTagCompound compound = stack.getTagCompound();
		if (compound != null)
		{
			NBTTagList tagList = compound.getTagList("entitys", 10);
			entityCount = tagList.tagCount();
		}

		tooltip.add(I18n.format(entityCount == 1 ? "tooltip.summoningPendulum.entityCount.singular" : "tooltip.summoningPendulum.entityCount.plural", entityCount));
	}

	@Override
	public boolean itemInteractionForEntity(ItemStack itemstack, net.minecraft.entity.player.EntityPlayer player, EntityLivingBase entity)
	{
		if (entity.worldObj.isRemote)
		{
			return false;
		}

		if (!(entity instanceof IMob || entity instanceof EntityPlayer))
		{
			itemstack = player.getCurrentEquippedItem();
			NBTTagCompound compound = itemstack.getTagCompound();
			if (compound == null)
			{
				compound = new NBTTagCompound();
			}

			NBTTagList tagList = compound.getTagList("entitys", 10);
			if (tagList.tagCount() < 5)
			{
				NBTTagCompound entityNBT = new NBTTagCompound();
				entity.writeToNBTOptional(entityNBT);
				tagList.appendTag(entityNBT);
				entity.setDead();
				entity.worldObj.playSoundEffect(player.posX, player.posY, player.posZ, "mob.endermen.portal", 0.5f, 1.5F);
			}
			else
			{
				entity.worldObj.playSoundEffect(player.posX, player.posY, player.posZ, "fire.ignite", 0.5f, 0.2F);
			}

			compound.setTag("entitys", tagList);
			itemstack.setTagCompound(compound);
			return true;
		}
		return true;
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		pos = pos.offset(side);
		if (!worldIn.isRemote)
		{
			NBTTagCompound compound = stack.getTagCompound();
			if (compound != null)
			{
				NBTTagList tagList = compound.getTagList("entitys", 10);
				if (tagList.tagCount() > 0)
				{
					NBTTagCompound entityNBT = tagList.getCompoundTagAt(0);
					tagList.removeTag(0);

					Entity entity = EntityList.createEntityFromNBT(entityNBT, worldIn);
					if (entity != null)
					{
						entity.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
						worldIn.spawnEntityInWorld(entity);
						playerIn.worldObj.playSoundEffect(playerIn.posX, playerIn.posY, playerIn.posZ, "mob.endermen.portal", 0.5f, 0.5F);
					}
				}
				else
				{
					playerIn.worldObj.playSoundEffect(playerIn.posX, playerIn.posY, playerIn.posZ, "fire.ignite", 0.5f, 0.2F);
				}
			}
		}
		return true;
	}

	@Override
	public EnumRarity getRarity(ItemStack stack)
	{
		return EnumRarity.RARE;
	}
}
