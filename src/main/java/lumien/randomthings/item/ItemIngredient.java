package lumien.randomthings.item;

import lumien.randomthings.block.ModBlocks;
import lumien.randomthings.config.Features;
import lumien.randomthings.entitys.EntityArtificialEndPortal;
import lumien.randomthings.lib.IRTItemColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

public class ItemIngredient extends ItemBase implements IRTItemColor
{
	static int counter = 0;

	public enum INGREDIENT
	{
		SAKANADE_SPORES("sakanadeSpores"), EVIL_TEAR("evilTear"), ECTO_PLASM("ectoPlasm"), SPECTRE_INGOT("spectreIngot"), BIOME_SENSOR("biomeSensor"), LUMINOUS_POWDER("luminousPowder"), SUPERLUBRICENT_TINCTURE("superLubricentTincture"), FLOO_POWDER("flooPowder"), PLATE_BASE("plateBase"), PRECIOUS_EMERALD("preciousEmerald"), LOTUS_BLOSSOM("lotusBlossom");

		public String name;

		public int id;

		INGREDIENT(String name)
		{
			this.name = name;
			this.id = counter++;
		}
	}

	public ItemIngredient()
	{
		super("ingredient");

		this.setHasSubtypes(true);
	}

	@Override
	public boolean hasEffect(ItemStack stack)
	{
		return stack.getItemDamage() == INGREDIENT.PRECIOUS_EMERALD.id;
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems)
	{
		if (this.isInCreativeTab(tab))
		{
			for (INGREDIENT i : INGREDIENT.values())
			{
				subItems.add(new ItemStack(this, 1, i.id));
			}
		}
	}

	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		int id = stack.getItemDamage();

		if (id >= 0 && id < INGREDIENT.values().length)
		{
			return "item.ingredient." + INGREDIENT.values()[id].name;
		}
		else
		{
			return "item.ingredient.invalid";
		}
	}

	public INGREDIENT getIngredient(ItemStack stack)
	{
		return INGREDIENT.values()[stack.getItemDamage()];
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		ItemStack stack = playerIn.getHeldItem(hand);
		if (getIngredient(stack) == INGREDIENT.ECTO_PLASM)
		{
			IBlockState state = worldIn.getBlockState(pos);

			int saplingID = OreDictionary.getOreID("treeSapling");

			if (state.getBlock() != ModBlocks.spectreSapling)
			{
				for (int id : OreDictionary.getOreIDs(new ItemStack(state.getBlock())))
				{
					if (id == saplingID)
					{
						if (!worldIn.isRemote)
						{
							stack.shrink(1);
							worldIn.setBlockState(pos, ModBlocks.spectreSapling.getDefaultState());
						}

						return EnumActionResult.SUCCESS;
					}
				}
			}
		}
		else if (getIngredient(stack) == INGREDIENT.EVIL_TEAR && Features.ARTIFICIAL_END_PORTAL)
		{
			IBlockState state = worldIn.getBlockState(pos);

			if (state.getBlock() == Blocks.END_ROD)
			{
				if (EntityArtificialEndPortal.isValidPosition(worldIn, pos.down(3), true))
				{
					if (!worldIn.isRemote)
					{
						BlockPos portalCenter = pos.down(3);
						worldIn.spawnEntity(new EntityArtificialEndPortal(worldIn, portalCenter.getX() + 0.5, portalCenter.getY(), portalCenter.getZ() + 0.5));
						stack.shrink(1);
					}

					return EnumActionResult.SUCCESS;
				}
			}
		}

		return EnumActionResult.PASS;
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
	{
		if (getIngredient(stack) == ItemIngredient.INGREDIENT.LOTUS_BLOSSOM)
		{
			if (entityLiving instanceof EntityPlayer)
			{
				EntityPlayer entityplayer = (EntityPlayer) entityLiving;

				if (!worldIn.isRemote)
				{
					int i = 3 + worldIn.rand.nextInt(5) + worldIn.rand.nextInt(5);

					while (i > 0)
					{
						int j = EntityXPOrb.getXPSplit(i);
						i -= j;
						worldIn.spawnEntity(new EntityXPOrb(worldIn, entityplayer.posX, entityplayer.posY, entityplayer.posZ, j));
					}
				}
			}

			stack.shrink(1);

			return stack;
		}

		return super.onItemUseFinish(stack, worldIn, entityLiving);
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack)
	{
		if (getIngredient(stack) == ItemIngredient.INGREDIENT.LOTUS_BLOSSOM)
		{
			return 10;
		}

		return super.getMaxItemUseDuration(stack);
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack)
	{

		if (getIngredient(stack) == ItemIngredient.INGREDIENT.LOTUS_BLOSSOM)
		{
			return EnumAction.EAT;
		}

		return super.getItemUseAction(stack);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		ItemStack itemstack = playerIn.getHeldItem(handIn);

		if (getIngredient(itemstack) == ItemIngredient.INGREDIENT.LOTUS_BLOSSOM)
		{
			playerIn.setActiveHand(handIn);
			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
		}

		return super.onItemRightClick(worldIn, playerIn, handIn);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemstack(ItemStack stack, int tintIndex)
	{
		if (stack.getItemDamage() == INGREDIENT.BIOME_SENSOR.id && tintIndex == 1)
		{
			EntityPlayer player = FMLClientHandler.instance().getClientPlayerEntity();

			if (player != null)
			{
				return ModBlocks.biomeStone.colorMultiplier(null, player.world, player.getPosition(), 0);
			}
		}

		return -1;
	}
}
