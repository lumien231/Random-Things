package lumien.randomthings.item;

import java.util.List;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry.EntityRegistration;

public class ItemEntityFilter extends ItemBase
{

	public ItemEntityFilter()
	{
		super("entityFilter");
	}

	public static Class getEntityClass(ItemStack filter)
	{
		NBTTagCompound compound;
		if ((compound = filter.getTagCompound()) != null)
		{
			if (compound.getBoolean("modded"))
			{
				String modID = compound.getString("modID");
				int entityID = compound.getInteger("entityID");

				ModContainer modContainer = Loader.instance().getIndexedModList().get(modID);

				if (modContainer != null)
				{
					EntityRegistration registration = EntityRegistry.instance().lookupModSpawn(modContainer, entityID);

					if (registration != null)
					{
						return registration.getEntityClass();
					}
				}

			}
			else
			{
				int entityID = compound.getInteger("entityID");

				return EntityList.getClassFromID(entityID);
			}
		}

		return null;
	}

	public static boolean filterAppliesTo(ItemStack filter, EntityLivingBase entity)
	{
		NBTTagCompound compound;
		if ((compound = filter.getTagCompound()) != null)
		{
			if (compound.getBoolean("modded"))
			{
				String modID = compound.getString("modID");
				int entityID = compound.getInteger("entityID");

				ModContainer modContainer = Loader.instance().getIndexedModList().get(modID);

				if (modContainer != null)
				{
					EntityRegistration registration = EntityRegistry.instance().lookupModSpawn(modContainer, entityID);

					if (registration != null)
					{
						return registration.getEntityClass().isAssignableFrom(entity.getClass());
					}
				}

			}
			else
			{
				int entityID = compound.getInteger("entityID");

				return entityID == EntityList.getID(entity.getClass());
			}
		}

		return false;
	}

	@Override
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand)
	{
		if (!playerIn.worldObj.isRemote)
		{
			stack = playerIn.getHeldItemMainhand();
			if (stack.getTagCompound() == null)
			{
				stack.setTagCompound(new NBTTagCompound());
			}
			NBTTagCompound compound = stack.getTagCompound();

			int vanillaEntityID = EntityList.getID(target.getClass());

			if (vanillaEntityID > 0)
			{
				compound.setBoolean("modded", false);
				compound.setInteger("entityID", vanillaEntityID);
			}
			else
			{
				EntityRegistration registration = EntityRegistry.instance().lookupModSpawn(target.getClass(), false);

				if (registration != null)
				{
					compound.setBoolean("modded", true);
					compound.setString("modID", registration.getContainer().getModId());
					compound.setInteger("entityID", registration.getModEntityId());
				}
			}
		}

		return true;
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List tooltip, boolean advanced)
	{
		super.addInformation(stack, playerIn, tooltip, advanced);

		NBTTagCompound compound;
		if ((compound = stack.getTagCompound()) != null)
		{
			String entityName = null;
			if (compound.getBoolean("modded"))
			{
				String modID = compound.getString("modID");
				int entityID = compound.getInteger("entityID");

				ModContainer modContainer = Loader.instance().getIndexedModList().get(modID);

				if (modContainer != null)
				{
					EntityRegistration registration = EntityRegistry.instance().lookupModSpawn(modContainer, entityID);

					if (registration != null)
					{
						entityName = registration.getEntityName();
					}
				}
			}
			else
			{
				int entityID = compound.getInteger("entityID");

				entityName = EntityList.func_191302_a(EntityList.func_191306_a((EntityList.getClassFromID(entityID))));
			}

			if (entityName != null)
			{
				tooltip.add(entityName);
			}
			else
			{
				tooltip.add(I18n.format("tooltip.entityFilter.invalidEntity"));
			}
		}
	}
}
