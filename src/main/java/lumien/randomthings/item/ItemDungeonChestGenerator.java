package lumien.randomthings.item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTableList;
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
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1ItemStack, World world, List par3List, ITooltipFlag par4)
	{
		super.addInformation(par1ItemStack, world, par3List, par4);

		NBTTagCompound nbt = par1ItemStack.getTagCompound();
		if (nbt != null)
		{
			Set<ResourceLocation> lootTableSet = LootTableList.getAll();
			List<ResourceLocation> sortedList = new ArrayList<>(lootTableSet);

			Collections.sort(sortedList, new Comparator<ResourceLocation>()
			{
				@Override
				public int compare(ResourceLocation rl1, ResourceLocation rl2)
				{
					return rl1.toString().compareTo(rl2.toString());
				}
			});

			int tableIndex = nbt.getInteger("tableIndex");
			if (tableIndex >= 0 && tableIndex < sortedList.size())
			{
				ResourceLocation tableLocation = sortedList.get(tableIndex);
				par3List.add(net.minecraft.client.resources.I18n.format("item.dungeonChestGenerator.category", tableLocation.toString()));
				par3List.add(net.minecraft.client.resources.I18n.format("item.dungeonChestGenerator.shiftCategory"));
			}
		}
	}

	@Override
	public String getItemStackDisplayName(ItemStack par1ItemStack)
	{
		NBTTagCompound nbt = par1ItemStack.getTagCompound();

		if (nbt != null)
		{
			Set<ResourceLocation> lootTableSet = LootTableList.getAll();
			List<ResourceLocation> sortedList = new ArrayList<>(lootTableSet);
			Collections.sort(sortedList, new Comparator<ResourceLocation>()
			{
				@Override
				public int compare(ResourceLocation rl1, ResourceLocation rl2)
				{
					return rl1.toString().compareTo(rl2.toString());
				}
			});

			int tableIndex = nbt.getInteger("tableIndex");
			if (tableIndex >= 0 && tableIndex < sortedList.size())
			{
				ResourceLocation tableLocation = sortedList.get(tableIndex);
				return ("" + I18n.translateToLocal(this.getUnlocalizedNameInefficiently(par1ItemStack) + ".name")).trim() + " (" + tableLocation.toString() + ")";
			}
		}

		return ("" + I18n.translateToLocal(this.getUnlocalizedNameInefficiently(par1ItemStack) + ".name")).trim();

	}

	@Override
	public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack)
	{
		if (!entityLiving.world.isRemote && entityLiving.isSneaking())
		{
			NBTTagCompound nbt = stack.getTagCompound();
			if (nbt == null)
			{
				stack.setTagCompound(new NBTTagCompound());
				stack.getTagCompound().setInteger("tableIndex", 0);
			}

			int currentCategory = stack.getTagCompound().getInteger("tableIndex");
			if (currentCategory - 1 >= 0)
			{
				currentCategory--;
			}
			else
			{
				currentCategory = LootTableList.getAll().size() - 1;
			}

			stack.getTagCompound().setInteger("tableIndex", currentCategory);

			return true;
		}
		return false;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World par2World, EntityPlayer par3EntityPlayer, EnumHand hand)
	{
		ItemStack par1ItemStack = par3EntityPlayer.getHeldItem(hand);
		if (!par2World.isRemote && par3EntityPlayer.isSneaking())
		{
			NBTTagCompound nbt = par1ItemStack.getTagCompound();
			if (nbt == null)
			{
				par1ItemStack.setTagCompound(new NBTTagCompound());
				par1ItemStack.getTagCompound().setInteger("tableIndex", 0);
			}

			int currentCategory = par1ItemStack.getTagCompound().getInteger("tableIndex");
			if (currentCategory + 1 < LootTableList.getAll().size())
			{
				currentCategory++;
			}
			else
			{
				currentCategory = 0;
			}

			par1ItemStack.getTagCompound().setInteger("tableIndex", currentCategory);

			return new ActionResult<>(EnumActionResult.SUCCESS, par1ItemStack);
		}
		return new ActionResult<>(EnumActionResult.FAIL, par1ItemStack);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		ItemStack stack = playerIn.getHeldItem(hand);
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

						if (nbt != null)
						{
							int currentCategory = nbt.getInteger("tableIndex");

							Set<ResourceLocation> lootTableSet = LootTableList.getAll();
							List<ResourceLocation> sortedList = new ArrayList<>(lootTableSet);
							Collections.sort(sortedList, new Comparator<ResourceLocation>()
							{
								@Override
								public int compare(ResourceLocation rl1, ResourceLocation rl2)
								{
									return rl1.toString().compareTo(rl2.toString());
								}
							});

							if (currentCategory >= 0 && currentCategory < sortedList.size())
							{
								ResourceLocation currentTableLocation = sortedList.get(currentCategory);

								LootTable lootTable = worldIn.getLootTableManager().getLootTableFromLocation(currentTableLocation);

								if (lootTable != null)
								{
									worldIn.setBlockState(pos, Blocks.CHEST.getDefaultState());

									IInventory chestInventory = (IInventory) worldIn.getTileEntity(pos);

									LootContext.Builder builder = new LootContext.Builder((WorldServer) worldIn).withPlayer(playerIn);

									lootTable.fillInventory(chestInventory, worldIn.rand, builder.build());
								}

							}
						}
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
