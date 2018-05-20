package lumien.randomthings.item;

import java.util.HashMap;
import java.util.List;

import org.lwjgl.input.Keyboard;

import lumien.randomthings.lib.IEntityFilterItem;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class ItemEntityFilter extends ItemBase implements IEntityFilterItem
{
	HashMap<String, Class> classCache = new HashMap<String, Class>();

	public ItemEntityFilter()
	{
		super("entityFilter");
	}

	private Class getEntityClass(ItemStack filter)
	{
		NBTTagCompound compound;
		if ((compound = filter.getTagCompound()) != null)
		{
			if (!compound.hasKey("entityKey"))
			{
				return null;
			}
			else
			{
				String entityKeyString = compound.getString("entityKey");
				Class clazz = EntityList.getClass(new ResourceLocation(entityKeyString));

				if (clazz != null)
				{
					return clazz;
				}
			}
		}

		return null;
	}

	@Override
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand)
	{
		if (!playerIn.world.isRemote)
		{
			stack = playerIn.getHeldItemMainhand();
			if (stack.getTagCompound() == null)
			{
				stack.setTagCompound(new NBTTagCompound());
			}
			NBTTagCompound compound = stack.getTagCompound();

			ResourceLocation entityKey = EntityList.getKey(target);

			compound.setString("entityKey", entityKey.toString());
		}

		return true;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		ItemStack stack = playerIn.getHeldItem(handIn);

		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
	}

	@Override
	public void addInformation(ItemStack stack, World world, List tooltip, ITooltipFlag advanced)
	{
		super.addInformation(stack, world, tooltip, advanced);

		NBTTagCompound compound;
		if ((compound = stack.getTagCompound()) != null)
		{
			if (!compound.hasKey("entityKey"))
			{
				tooltip.add(I18n.format("tooltip.entityFilter.invalidEntity"));
			}
			else
			{
				String entityKeyString = compound.getString("entityKey");
				Class clazz = EntityList.getClassFromName(entityKeyString);
				String translationKey = EntityList.getTranslationName(new ResourceLocation(entityKeyString));

				if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
				{
					tooltip.add(entityKeyString);
				}

				tooltip.add(I18n.format("entity." + translationKey + ".name"));
			}
		}
	}

	@Override
	public boolean apply(ItemStack me, Entity entity)
	{
		Class filterClass = getEntityClass(me);

		if (filterClass != null)
		{
			return filterClass.isAssignableFrom(entity.getClass());
		}

		return true;
	}
}
