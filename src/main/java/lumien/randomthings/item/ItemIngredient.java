package lumien.randomthings.item;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Predicates;

import lumien.randomthings.block.ModBlocks;
import lumien.randomthings.config.Features;
import lumien.randomthings.entitys.EntityArtificialEndPortal;
import lumien.randomthings.entitys.EntityGoldenEgg;
import lumien.randomthings.entitys.EntitySpectreIlluminator;
import lumien.randomthings.handler.spectreilluminator.SpectreIlluminationHandler;
import lumien.randomthings.lib.IRTItemColor;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
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
		SAKANADE_SPORES("sakanadeSpores"), EVIL_TEAR("evilTear"), ECTO_PLASM("ectoPlasm"), SPECTRE_INGOT("spectreIngot"), BIOME_SENSOR("biomeSensor"), LUMINOUS_POWDER("luminousPowder"), SUPERLUBRICENT_TINCTURE("superLubricentTincture"), FLOO_POWDER("flooPowder"), PLATE_BASE("plateBase"), PRECIOUS_EMERALD("preciousEmerald"), LOTUS_BLOSSOM("lotusBlossom"), GOLDEN_EGG("goldenEgg"), SPECTRE_STRING("spectreString"), BLACKOUT_POWDER("blackoutPowder");

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

		BehaviorProjectileDispense pro = new BehaviorProjectileDispense()
		{

			@Override
			protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn)
			{
				return new EntityGoldenEgg(worldIn, position.getX(), position.getY(), position.getZ());
			}
		};

		BehaviorDefaultDispenseItem def = new BehaviorDefaultDispenseItem();

		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, new IBehaviorDispenseItem()
		{

			@Override
			public ItemStack dispense(IBlockSource source, ItemStack stack)
			{
				if (stack.getItemDamage() != INGREDIENT.GOLDEN_EGG.id)
				{
					return def.dispense(source, stack);
				}
				else
				{
					return pro.dispense(source, stack);
				}
			}

		});
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

		INGREDIENT ingredient = getIngredient(stack);

		if (ingredient == INGREDIENT.ECTO_PLASM)
		{
			IBlockState state = worldIn.getBlockState(pos);

			int saplingID = OreDictionary.getOreID("treeSapling");

			if (state.getBlock() != ModBlocks.spectreSapling)
			{
				ItemStack is = new ItemStack(state.getBlock());

				if (!is.isEmpty())
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
		}
		else if (ingredient == INGREDIENT.EVIL_TEAR && Features.ARTIFICIAL_END_PORTAL)
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
		else if (ingredient == INGREDIENT.BLACKOUT_POWDER && !worldIn.isRemote)
		{
			SpectreIlluminationHandler handler = SpectreIlluminationHandler.get(worldIn);

			if (handler.isIlluminated(pos))
			{
				List<EntitySpectreIlluminator> list = new ArrayList<EntitySpectreIlluminator>();
				ChunkPos chunkPos = worldIn.getChunkFromBlockCoords(pos).getPos();
				worldIn.getChunkFromBlockCoords(pos).getEntitiesOfTypeWithinAABB(EntitySpectreIlluminator.class, new AxisAlignedBB(chunkPos.getXStart() - 2, 0, chunkPos.getZStart() - 2, chunkPos.getXEnd() + 2, 255, chunkPos.getZEnd() + 2), list, Predicates.alwaysTrue());

				if (!list.isEmpty())
				{
					EntitySpectreIlluminator first = list.get(0);

					first.setDead();

					BlockPos spawnPos = pos.offset(facing);
					worldIn.spawnEntity(new EntityItem(worldIn, spawnPos.getX() + 0.5, spawnPos.getY() + 0.5, spawnPos.getZ() + 0.5, new ItemStack(ModItems.spectreIlluminator)));
				}

				handler.toggleChunk(worldIn, pos);

				return EnumActionResult.SUCCESS;
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

		INGREDIENT ingredient = getIngredient(itemstack);

		if (ingredient == ItemIngredient.INGREDIENT.LOTUS_BLOSSOM)
		{
			playerIn.setActiveHand(handIn);
			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
		}
		else if (ingredient == ItemIngredient.INGREDIENT.GOLDEN_EGG)
		{
			if (!playerIn.capabilities.isCreativeMode)
			{
				itemstack.shrink(1);
			}

			worldIn.playSound((EntityPlayer) null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_EGG_THROW, SoundCategory.PLAYERS, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 1F));

			if (!worldIn.isRemote)
			{
				EntityGoldenEgg entityegg = new EntityGoldenEgg(worldIn, playerIn);
				entityegg.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 1.5F, 1.0F);
				worldIn.spawnEntity(entityegg);
			}

			playerIn.addStat(StatList.getObjectUseStats(this));
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
