package lumien.randomthings.recipes;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.Level;

import lumien.randomthings.RandomThings;
import lumien.randomthings.asm.MCPNames;
import lumien.randomthings.block.ModBlocks;
import lumien.randomthings.item.ItemIDCard;
import lumien.randomthings.item.ItemIngredient;
import lumien.randomthings.item.ItemPositionFilter;
import lumien.randomthings.item.ModItems;
import lumien.randomthings.recipes.anvil.AnvilRecipeHandler;
import lumien.randomthings.recipes.imbuing.ImbuingRecipeHandler;
import lumien.randomthings.util.ReflectionUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionHelper;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.RecipeSorter.Category;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.IRegistryDelegate;

public class ModRecipes
{
	public static String[] oreDictDyes = new String[16];
	static String[] dyes = { "Black", "Red", "Green", "Brown", "Blue", "Purple", "Cyan", "LightGray", "Gray", "Pink", "Lime", "Yellow", "LightBlue", "Magenta", "Orange", "White" };

	public static void addGlowingMushroomRecipes()
	{
		try
		{
			Field listField = PotionHelper.class.getDeclaredField(MCPNames.field("field_185213_a"));
			ReflectionUtil.makeModifiable(listField);

			Class mpClass = Class.forName("net.minecraft.potion.PotionHelper$MixPredicate");
			
			Constructor constructor = mpClass.getConstructor(IForgeRegistryEntry.Impl.class, Ingredient.class, IForgeRegistryEntry.Impl.class);
			constructor.setAccessible(true);
			
			Field inputField = mpClass.getDeclaredField(MCPNames.field("field_185198_a"));
			ReflectionUtil.makeModifiable(inputField);

			Field reagentField = mpClass.getDeclaredField(MCPNames.field("field_185199_b"));
			ReflectionUtil.makeModifiable(reagentField);

			Field outputField = mpClass.getDeclaredField(MCPNames.field("field_185200_c"));
			ReflectionUtil.makeModifiable(outputField);

			List<Object> conversionList = (List<Object>) listField.get(null);

			List<Object> toAdd = new ArrayList<>();

			for (Object p : conversionList)
			{
				Ingredient reagent = (Ingredient) reagentField.get(p);

				if (reagent.test(new ItemStack(Items.GLOWSTONE_DUST)))
				{
					PotionType input = (PotionType) ((IRegistryDelegate) inputField.get(p)).get();
					PotionType output = (PotionType) ((IRegistryDelegate) outputField.get(p)).get();

					toAdd.add(constructor.newInstance(input, Ingredient.fromItem(Item.getItemFromBlock(ModBlocks.glowingMushroom)), output));
				}
			}

			RandomThings.instance.logger.log(Level.DEBUG, "Added " + toAdd.size() + " Glowing Mushroom Recipes");
			conversionList.addAll(toAdd);
		}
		catch (Exception e)
		{
			RandomThings.instance.logger.log(Level.ERROR, "Couldn't add glowing mushroom potion recipes, please report this as a bug");
			e.printStackTrace();
		}
	}

	public static void register()
	{
		for (int i = 0; i < oreDictDyes.length; i++)
		{
			oreDictDyes[i] = "dye" + dyes[i];
		}

		ArrayUtils.reverse(oreDictDyes);

		RecipeSorter.register("randomthings:customWorkbenchRecipe", RecipeWorkbench.class, Category.SHAPED, "");

		final ItemStack rottenFlesh = new ItemStack(Items.ROTTEN_FLESH);
		final ItemStack boneMeal = new ItemStack(Items.DYE, 1, 15);
		final ItemStack lapis = new ItemStack(Items.DYE, 1, 4);
		final ItemStack cobblestone = new ItemStack(Blocks.COBBLESTONE);
		final ItemStack ghastTear = new ItemStack(Items.GHAST_TEAR);
		final ItemStack vine = new ItemStack(Blocks.VINE);
		final ItemStack waterBottle = PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.WATER);
		final ItemStack mossyCobblestone = new ItemStack(Blocks.MOSSY_COBBLESTONE);
		final ItemStack netherBrick = new ItemStack(Blocks.NETHER_BRICK);
		final ItemStack coal = new ItemStack(Items.COAL);
		final ItemStack blazePowder = new ItemStack(Items.BLAZE_POWDER);
		final ItemStack flint = new ItemStack(Items.FLINT);
		final ItemStack spiderEye = new ItemStack(Items.SPIDER_EYE);
		final ItemStack redMushroom = new ItemStack(Blocks.RED_MUSHROOM);
		final ItemStack glowStone = new ItemStack(Items.GLOWSTONE_DUST);
		final ItemStack glisteringMelon = new ItemStack(Items.SPECKLED_MELON);
		final ItemStack witherSkull = new ItemStack(Items.SKULL, 1, 1);

		ForgeRegistries.RECIPES.register(new RecipeWorkbench());

		// Imbuing Station
		ImbuingRecipeHandler.addRecipe(waterBottle, vine, boneMeal, cobblestone, mossyCobblestone);

		ImbuingRecipeHandler.addRecipe(coal, flint, blazePowder, waterBottle, new ItemStack(ModItems.imbue, 1, 0));
		ImbuingRecipeHandler.addRecipe(spiderEye, rottenFlesh, redMushroom, waterBottle, new ItemStack(ModItems.imbue, 1, 1));
		ImbuingRecipeHandler.addRecipe(new ItemStack(ModItems.beans, 1, 1), lapis, glowStone, waterBottle, new ItemStack(ModItems.imbue, 1, 2));
		ImbuingRecipeHandler.addRecipe(witherSkull, netherBrick, ghastTear, waterBottle, new ItemStack(ModItems.imbue, 1, 3));

		// Anvil
		if (Loader.isModLoaded("baubles"))
		{
			AnvilRecipeHandler.addAnvilRecipe(new ItemStack(ModItems.obsidianSkull), new ItemStack(Items.FIRE_CHARGE), new ItemStack(ModItems.obsidianSkullRing), 3);
		}

		AnvilRecipeHandler.addAnvilRecipe(new ItemStack(ModItems.waterWalkingBoots), new ItemStack(ModItems.obsidianSkull), new ItemStack(ModItems.obsidianWaterWalkingBoots), 10);
		AnvilRecipeHandler.addAnvilRecipe(new ItemStack(ModItems.waterWalkingBoots), new ItemStack(ModItems.obsidianSkullRing), new ItemStack(ModItems.obsidianWaterWalkingBoots), 10);
		AnvilRecipeHandler.addAnvilRecipe(new ItemStack(ModItems.obsidianWaterWalkingBoots), new ItemStack(ModItems.lavaCharm), new ItemStack(ModItems.lavaWader), 15);

		// Spectre Anchor
		IRecipe anchorRecipe = new SimpleRecipe(new ResourceLocation("randomthings", "spectreAnchorCombine"))
		{
			@Override
			public boolean matches(InventoryCrafting inv, World worldIn)
			{
				ItemStack anchor = null;
				ItemStack target = null;

				for (int i = 0; i < inv.getSizeInventory(); i++)
				{
					ItemStack is = inv.getStackInSlot(i);

					if (!is.isEmpty())
					{
						if (is.getItem() == ModItems.spectreAnchor)
						{
							if (anchor == null)
							{
								anchor = is;
							}
							else
							{
								return false;
							}
						}
						else
						{
							if (target == null)
							{
								if (is.getMaxStackSize() != 1)
								{
									return false;
								}
								else
								{
									target = is;
								}
							}
							else
							{
								return false;
							}
						}
					}
				}
				return anchor != null && target != null && (!target.hasTagCompound() || !target.getTagCompound().hasKey("spectreAnchor"));
			}

			@Override
			public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv)
			{
				return NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
			}

			@Override
			public ItemStack getRecipeOutput()
			{
				return new ItemStack(ModItems.spectreAnchor);
			}

			@Override
			public ItemStack getCraftingResult(InventoryCrafting inv)
			{
				ItemStack anchor = null;
				ItemStack target = null;

				for (int i = 0; i < inv.getSizeInventory(); i++)
				{
					ItemStack is = inv.getStackInSlot(i);

					if (!is.isEmpty())
					{
						if (is.getItem() == ModItems.spectreAnchor)
						{
							anchor = is;
						}
						else
						{
							target = is;
						}
					}
				}

				ItemStack result = target.copy();

				result.setTagInfo("spectreAnchor", new NBTTagByte((byte) 0));

				return result;
			}

			@Override
			public boolean canFit(int width, int height)
			{
				return true;
			}
		};

		RecipeSorter.register("spectreAnchorCombine", anchorRecipe.getClass(), Category.SHAPELESS, "");
		ForgeRegistries.RECIPES.register(anchorRecipe);

		// Golden Compass
		IRecipe goldenCompassRecipe = new SimpleRecipe(new ResourceLocation("randomthings", "goldenCompassSetPosition"))
		{
			@Override
			public boolean matches(InventoryCrafting inv, World worldIn)
			{
				ItemStack compass = null;
				ItemStack target = null;

				for (int i = 0; i < inv.getSizeInventory(); i++)
				{
					ItemStack is = inv.getStackInSlot(i);

					if (!is.isEmpty())
					{
						if (is.getItem() == ModItems.goldenCompass)
						{
							if (compass == null)
							{
								compass = is;
							}
							else
							{
								return false;
							}
						}
						else
						{
							if (target == null)
							{
								if (is.getItem() == ModItems.positionFilter)
								{
									target = is;
								}
								else
								{
									return false;
								}
							}
							else
							{
								return false;
							}
						}
					}
				}
				return compass != null && target != null && (!target.hasTagCompound() || !target.getTagCompound().hasKey("spectreAnchor"));
			}

			@Override
			public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv)
			{
				NonNullList aitemstack = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);

				for (int i = 0; i < inv.getSizeInventory(); i++)
				{
					ItemStack is = inv.getStackInSlot(i);

					if (!is.isEmpty())
					{
						if (is.getItem() == ModItems.positionFilter)
						{
							ItemStack cp = is.copy();
							cp.setCount(1);
							aitemstack.set(i, cp);
						}
					}
				}

				return aitemstack;
			}

			@Override
			public ItemStack getRecipeOutput()
			{
				return new ItemStack(ModItems.goldenCompass);
			}

			@Override
			public ItemStack getCraftingResult(InventoryCrafting inv)
			{
				ItemStack compass = null;
				ItemStack target = null;

				for (int i = 0; i < inv.getSizeInventory(); i++)
				{
					ItemStack is = inv.getStackInSlot(i);

					if (!is.isEmpty())
					{
						if (is.getItem() == ModItems.goldenCompass)
						{
							compass = is;
						}
						else
						{
							target = is;
						}
					}
				}

				ItemStack result = compass.copy();

				BlockPos pos = ItemPositionFilter.getPosition(target);

				if (pos != null)
				{
					if (result.getTagCompound() == null)
					{
						result.setTagCompound(new NBTTagCompound());
					}
					NBTTagCompound compound = result.getTagCompound();

					compound.setInteger("targetX", pos.getX());
					compound.setInteger("targetZ", pos.getZ());
				}
				return result;
			}

			@Override
			public boolean canFit(int width, int height)
			{
				return true;
			}
		};

		RecipeSorter.register("goldenCompass", goldenCompassRecipe.getClass(), Category.SHAPELESS, "");
		ForgeRegistries.RECIPES.register(goldenCompassRecipe);

		// Luminous Powder
		IRecipe luminousPowderRecipe = new SimpleRecipe(new ResourceLocation("randomthings", "luminousPowder"))
		{
			@Override
			public boolean matches(InventoryCrafting inv, World worldIn)
			{
				ItemStack powder = null;
				ItemStack target = null;

				for (int i = 0; i < inv.getSizeInventory(); i++)
				{
					ItemStack is = inv.getStackInSlot(i);

					if (!is.isEmpty())
					{
						if (is.getItem() == ModItems.ingredients && is.getItemDamage() == ItemIngredient.INGREDIENT.LUMINOUS_POWDER.id)
						{
							if (powder == null)
							{
								powder = is;
							}
							else
							{
								return false;
							}
						}
						else
						{
							if (target == null)
							{
								if (!is.isItemEnchanted())
								{
									return false;
								}
								else
								{
									target = is;
								}
							}
							else
							{
								return false;
							}
						}
					}
				}
				return powder != null && target != null && (!target.hasTagCompound() || !target.getTagCompound().hasKey("luminousEnchantment"));
			}

			@Override
			public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv)
			{
				return NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
			}

			@Override
			public ItemStack getRecipeOutput()
			{
				return new ItemStack(ModItems.spectreAnchor);
			}

			@Override
			public ItemStack getCraftingResult(InventoryCrafting inv)
			{
				ItemStack powder = null;
				ItemStack target = null;

				for (int i = 0; i < inv.getSizeInventory(); i++)
				{
					ItemStack is = inv.getStackInSlot(i);

					if (!is.isEmpty())
					{
						if (is.getItem() == ModItems.ingredients)
						{
							powder = is;
						}
						else
						{
							target = is;
						}
					}
				}

				ItemStack result = target.copy();

				result.setTagInfo("luminousEnchantment", new NBTTagByte((byte) 0));

				return result;
			}

			@Override
			public boolean canFit(int width, int height)
			{
				return true;
			}
		};

		RecipeSorter.register("luminousPowder", luminousPowderRecipe.getClass(), Category.SHAPELESS, "");
		ForgeRegistries.RECIPES.register(luminousPowderRecipe);

		// Emerald Compass
		IRecipe emeraldCompassRecipe = new SimpleRecipe(new ResourceLocation("randomthings", "emeraldcompass_settarget"))
		{
			@Override
			public boolean matches(InventoryCrafting inv, World worldIn)
			{
				ItemStack compass = null;
				ItemStack target = null;

				for (int i = 0; i < inv.getSizeInventory(); i++)
				{
					ItemStack is = inv.getStackInSlot(i);

					if (!is.isEmpty())
					{
						if (is.getItem() == ModItems.emeraldCompass)
						{
							if (compass == null)
							{
								compass = is;
							}
							else
							{
								return false;
							}
						}
						else
						{
							if (target == null)
							{
								if (is.getItem() == ModItems.idCard)
								{
									target = is;
								}
								else
								{
									return false;
								}
							}
							else
							{
								return false;
							}
						}
					}
				}
				return compass != null && target != null && (!target.hasTagCompound() || !target.getTagCompound().hasKey("spectreAnchor"));
			}

			@Override
			public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv)
			{
				NonNullList aitemstack = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);

				for (int i = 0; i < inv.getSizeInventory(); i++)
				{
					ItemStack is = inv.getStackInSlot(i);

					if (!is.isEmpty())
					{
						if (is.getItem() == ModItems.idCard)
						{
							ItemStack copy = is.copy();
							copy.setCount(1);
							aitemstack.set(i, copy);
						}
					}
				}

				return aitemstack;
			}

			@Override
			public ItemStack getRecipeOutput()
			{
				return new ItemStack(ModItems.emeraldCompass);
			}

			@Override
			public ItemStack getCraftingResult(InventoryCrafting inv)
			{
				ItemStack compass = null;
				ItemStack target = null;

				for (int i = 0; i < inv.getSizeInventory(); i++)
				{
					ItemStack is = inv.getStackInSlot(i);

					if (!is.isEmpty())
					{
						if (is.getItem() == ModItems.emeraldCompass)
						{
							compass = is;
						}
						else
						{
							target = is;
						}
					}
				}

				ItemStack result = compass.copy();

				UUID uuid = ItemIDCard.getUUID(target);

				if (uuid != null)
				{
					if (result.getTagCompound() == null)
					{
						result.setTagCompound(new NBTTagCompound());
					}
					NBTTagCompound compound = result.getTagCompound();

					compound.setString("uuid", uuid.toString());
				}
				return result;
			}

			@Override
			public boolean canFit(int width, int height)
			{
				return true;
			}
		};

		RecipeSorter.register("emeraldCompass", emeraldCompassRecipe.getClass(), Category.SHAPELESS, "");
		ForgeRegistries.RECIPES.register(emeraldCompassRecipe);

		// Port Key Recipe
		IRecipe portKeyCamoRecipe = new SimpleRecipe(new ResourceLocation("randomthings", "portkey_camo"))
		{
			@Override
			public boolean matches(InventoryCrafting inv, World worldIn)
			{
				ItemStack portkey = null;
				ItemStack camo = null;

				for (int i = 0; i < inv.getSizeInventory(); i++)
				{
					ItemStack is = inv.getStackInSlot(i);

					if (!is.isEmpty())
					{
						if (is.getItem() == ModItems.portKey)
						{
							if (portkey == null)
							{
								portkey = is;
							}
							else
							{
								return false;
							}
						}
						else if (is.getItem() != ModItems.portKey)
						{
							if (camo == null)
							{
								camo = is;
							}
							else
							{
								return false;
							}
						}
					}
				}
				return portkey != null && camo != null;
			}

			@Override
			public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv)
			{
				NonNullList aitemstack = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);

				for (int i = 0; i < inv.getSizeInventory(); i++)
				{
					ItemStack is = inv.getStackInSlot(i);

					if (!is.isEmpty())
					{
						if (is.getItem() != ModItems.portKey)
						{
							ItemStack copy = is.copy();
							copy.setCount(1);
							aitemstack.set(i, copy);
						}
					}
				}

				return aitemstack;
			}

			@Override
			public ItemStack getRecipeOutput()
			{
				return new ItemStack(ModItems.portKey);
			}

			@Override
			public ItemStack getCraftingResult(InventoryCrafting inv)
			{
				ItemStack portKey = null;
				ItemStack camo = null;

				for (int i = 0; i < inv.getSizeInventory(); i++)
				{
					ItemStack is = inv.getStackInSlot(i);

					if (!is.isEmpty())
					{
						if (is.getItem() == ModItems.portKey)
						{
							portKey = is;
						}
						else
						{
							camo = is;
						}
					}
				}

				ItemStack result = portKey.copy();

				UUID uuid = ItemIDCard.getUUID(camo);

				NBTTagCompound camoTag = result.getOrCreateSubCompound("camo");

				NBTTagCompound stackTag = new NBTTagCompound();
				camo.writeToNBT(stackTag);
				camoTag.setTag("stack", stackTag);

				return result;
			}

			@Override
			public boolean canFit(int width, int height)
			{
				return true;
			}
		};

		RecipeSorter.register("portKeyCamoRecipe", portKeyCamoRecipe.getClass(), Category.SHAPELESS, "");
		ForgeRegistries.RECIPES.register(portKeyCamoRecipe);

		// Diaphanous Block
		IRecipe diaphanousRecipe = new SimpleRecipe(new ResourceLocation("randomthings", "diaphanousChange"))
		{
			@Override
			public boolean matches(InventoryCrafting inv, World worldIn)
			{
				ItemStack diaphanous = null;
				IBlockState target = null;

				Item diaphanousItem = Item.getItemFromBlock(ModBlocks.blockDiaphanous);

				for (int i = 0; i < inv.getSizeInventory(); i++)
				{
					ItemStack is = inv.getStackInSlot(i);

					if (!is.isEmpty())
					{
						if (is.getItem() == diaphanousItem)
						{
							if (diaphanous == null)
							{
								diaphanous = is;
							}
							else
							{
								return false;
							}
						}
						else
						{
							if (target == null)
							{
								Item targetItem = is.getItem();
								if (targetItem instanceof ItemBlock)
								{
									ItemBlock targetItemBlock = (ItemBlock) targetItem;
									Block targetBlock = targetItemBlock.getBlock();

									if (!targetBlock.hasTileEntity())
									{
										try
										{
											IBlockState state = targetBlock.getStateFromMeta(targetItemBlock.getMetadata(is));

											if (state != null)
											{
												target = state;
												continue;
											}
										}
										catch (Exception e)
										{

										}
									}
								}

								return false;
							}
							else
							{
								return false;
							}
						}
					}
				}
				return diaphanous != null && target != null;
			}

			@Override
			public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv)
			{
				ItemStack diaphanous = null;
				IBlockState target = Blocks.STONE.getDefaultState();
				int meta = 0;

				NonNullList list = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);

				Item diaphanousItem = Item.getItemFromBlock(ModBlocks.blockDiaphanous);

				for (int i = 0; i < inv.getSizeInventory(); i++)
				{
					ItemStack is = inv.getStackInSlot(i);

					if (!is.isEmpty())
					{
						if (is.getItem() != diaphanousItem)
						{
							ItemStack copy = is.copy();
							copy.setCount(1);
							list.set(i, copy);
						}
					}
				}

				return list;
			}

			@Override
			public ItemStack getRecipeOutput()
			{
				return new ItemStack(ModBlocks.blockDiaphanous);
			}

			@Override
			public ItemStack getCraftingResult(InventoryCrafting inv)
			{
				ItemStack diaphanous = null;
				IBlockState target = Blocks.STONE.getDefaultState();
				int meta = 0;

				Item diaphanousItem = Item.getItemFromBlock(ModBlocks.blockDiaphanous);

				for (int i = 0; i < inv.getSizeInventory(); i++)
				{
					ItemStack is = inv.getStackInSlot(i);

					if (!is.isEmpty())
					{
						if (is.getItem() == diaphanousItem)
						{
							diaphanous = is;
						}
						else
						{
							Item targetItem = is.getItem();
							if (targetItem instanceof ItemBlock)
							{
								ItemBlock targetItemBlock = (ItemBlock) targetItem;
								Block targetBlock = targetItemBlock.getBlock();

								if (!targetBlock.hasTileEntity())
								{
									try
									{
										IBlockState state = targetBlock.getStateFromMeta(targetItemBlock.getMetadata(is));

										if (state != null)
										{
											target = state;
											meta = targetItemBlock.getMetadata(is);
											continue;
										}
									}
									catch (Exception e)
									{

									}
								}
							}
						}
					}
				}

				diaphanous = diaphanous.copy();
				diaphanous.setCount(1);

				if (!diaphanous.hasTagCompound())
				{
					diaphanous.setTagCompound(new NBTTagCompound());
				}

				diaphanous.getTagCompound().setString("block", target.getBlock().getRegistryName().toString());
				diaphanous.getTagCompound().setInteger("meta", meta);

				return diaphanous;
			}

			@Override
			public boolean canFit(int width, int height)
			{
				return true;
			}
		};

		RecipeSorter.register("diaphanousSet", diaphanousRecipe.getClass(), Category.SHAPELESS, "");
		ForgeRegistries.RECIPES.register(diaphanousRecipe);

		// Diaphanous Block Invert
		IRecipe diaphanousInvertRecipe = new SimpleRecipe(new ResourceLocation("randomthings", "diaphanousInvert"))
		{
			@Override
			public boolean matches(InventoryCrafting inv, World worldIn)
			{
				ItemStack diaphanous = null;
				Item diaphanousItem = Item.getItemFromBlock(ModBlocks.blockDiaphanous);

				for (int i = 0; i < inv.getSizeInventory(); i++)
				{
					ItemStack is = inv.getStackInSlot(i);

					if (!is.isEmpty())
					{
						if (is.getItem() == diaphanousItem)
						{
							if (diaphanous == null)
							{
								diaphanous = is;
							}
							else
							{
								return false;
							}
						}
						else
						{
							return false;
						}
					}
				}
				return diaphanous != null;
			}

			@Override
			public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv)
			{
				return NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
			}

			@Override
			public ItemStack getRecipeOutput()
			{
				return new ItemStack(ModBlocks.blockDiaphanous);
			}

			@Override
			public ItemStack getCraftingResult(InventoryCrafting inv)
			{
				ItemStack diaphanous = null;
				
				Item diaphanousItem = Item.getItemFromBlock(ModBlocks.blockDiaphanous);

				for (int i = 0; i < inv.getSizeInventory(); i++)
				{
					ItemStack is = inv.getStackInSlot(i);

					if (!is.isEmpty())
					{
						if (is.getItem() == diaphanousItem)
						{
							diaphanous = is;
						}
					}
				}

				if (diaphanous == null)
				{
					return ItemStack.EMPTY;
				}
				
				diaphanous = diaphanous.copy();
				diaphanous.setCount(1);

				if (!diaphanous.hasTagCompound())
				{
					diaphanous.setTagCompound(new NBTTagCompound());
				}

				diaphanous.getTagCompound().setBoolean("inverted", !diaphanous.getTagCompound().getBoolean("inverted"));

				return diaphanous;
			}

			@Override
			public boolean canFit(int width, int height)
			{
				return true;
			}
		};

		RecipeSorter.register("diaphanousInvert", diaphanousInvertRecipe.getClass(), Category.SHAPELESS, "");
		ForgeRegistries.RECIPES.register(diaphanousInvertRecipe);
	}
}
