package lumien.randomthings.item;

import java.util.List;
import java.util.Random;

import lumien.randomthings.lib.ChestCategory;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemDungeonChestGenerator extends ItemBase
{
	private static Random rng = new Random();

	public ItemDungeonChestGenerator()
	{
		super("dungeonChestGenerator");
		this.setMaxStackSize(1);
		this.setFull3D();
	}

	@Override
	public void getSubItems(Item p_150895_1_, CreativeTabs p_150895_2_, List p_150895_3_)
	{
		ItemStack is = new ItemStack(p_150895_1_, 1, 0);
		is.setTagCompound(new NBTTagCompound());
		p_150895_3_.add(is);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
	{
		super.addInformation(par1ItemStack, par2EntityPlayer, par3List, par4);

		NBTTagCompound nbt = par1ItemStack.getTagCompound();
		if (nbt != null)
		{
			ChestCategory selectedCategory = ChestCategory.values()[nbt.getInteger("category")];
			par3List.add(net.minecraft.client.resources.I18n.format("item.dungeonChestGenerator.category", selectedCategory.getName()));
			par3List.add(net.minecraft.client.resources.I18n.format("item.dungeonChestGenerator.shiftCategory"));
		}
	}

	@Override
	public String getItemStackDisplayName(ItemStack par1ItemStack)
	{
		NBTTagCompound nbt = par1ItemStack.getTagCompound();

		if (nbt != null)
		{
			ChestCategory selectedCategory = ChestCategory.values()[nbt.getInteger("category")];
			return ("" + I18n.translateToLocal(this.getUnlocalizedNameInefficiently(par1ItemStack) + ".name")).trim() + " (" + selectedCategory.getName() + ")";
		}
		else
		{
			return ("" + I18n.translateToLocal(this.getUnlocalizedNameInefficiently(par1ItemStack) + ".name")).trim();
		}
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer, EnumHand hand)
	{
		if (!par2World.isRemote && par3EntityPlayer.isSneaking())
		{
			NBTTagCompound nbt = par1ItemStack.getTagCompound();
			if (nbt == null)
			{
				par1ItemStack.setTagCompound(new NBTTagCompound());
				par1ItemStack.getTagCompound().setInteger("category", 0);
			}
			else
			{
				int currentCategory = par1ItemStack.getTagCompound().getInteger("category");
				if (currentCategory + 1 < ChestCategory.values().length)
				{
					currentCategory++;
				}
				else
				{
					currentCategory = 0;
				}
				par1ItemStack.getTagCompound().setInteger("category", currentCategory);
			}

			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, par1ItemStack);
		}
		return new ActionResult<ItemStack>(EnumActionResult.FAIL, par1ItemStack);
	}

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (!worldIn.isRemote && !playerIn.isSneaking())
		{
			pos = pos.offset(side);

			if (!playerIn.canPlayerEdit(pos, side, stack))
			{
				return EnumActionResult.FAIL;
			}
			else
			{
				if (worldIn.isAirBlock(pos))
				{
					if (Blocks.CHEST.canPlaceBlockAt(worldIn, pos))
					{
						NBTTagCompound nbt = stack.getTagCompound();
						if (nbt == null)
						{
							stack.setTagCompound(new NBTTagCompound());
							stack.getTagCompound().setInteger("category", 0);
						}
						ChestCategory category = ChestCategory.values()[stack.getTagCompound().getInteger("category")];
						worldIn.setBlockState(pos, Blocks.CHEST.getDefaultState());
						//WeightedRandomChestContent.generateChestContents(rng, ChestGenHooks.getItems(category.getName(), rng), (IInventory) worldIn.getTileEntity(pos), ChestGenHooks.getCount(category.getName(), rng));
					}
				}
				return EnumActionResult.SUCCESS;
			}
		}
		return EnumActionResult.FAIL;
	}

	@Override
	public EnumRarity getRarity(ItemStack stack)
	{
		return EnumRarity.EPIC;
	}
}
