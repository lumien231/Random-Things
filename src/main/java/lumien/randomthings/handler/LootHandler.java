package lumien.randomthings.handler;

import java.util.Random;

import lumien.randomthings.block.ModBlocks;
import lumien.randomthings.config.Worldgen;
import lumien.randomthings.item.ModItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootEntryEmpty;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.conditions.RandomChance;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.LootTableLoadEvent;

public class LootHandler
{
	static LootCondition onlyFound = new LootCondition()
	{
		
		@Override
		public boolean testCondition(Random rand, LootContext context)
		{
			return context.getKillerPlayer() instanceof EntityPlayer && !(context.getKillerPlayer() instanceof FakePlayer);
		}
	};
	
	public static void addLoot(LootTableLoadEvent event)
	{
		LootTable table = event.getTable();
		if (event.getName().equals(LootTableList.CHESTS_SIMPLE_DUNGEON))
		{
			if (Worldgen.LAVA_CHARM)
				addSingleItemWithChance(table, ModItems.lavaCharm, 5);

			if (Worldgen.SUMMONING_PENDULUM)
				addSingleItemWithChance(table, ModItems.summoningPendulum, 10);

			if (Worldgen.MAGIC_HOOD)
				addSingleItemWithChance(table, ModItems.magicHood, 5);

			if (Worldgen.SLIME_CUBE)
				addSingleItemWithChance(table, Item.getItemFromBlock(ModBlocks.slimeCube), 10);

			if (Worldgen.NUMBERED_COILS)
				addSingleItemWithChance(table, Item.getItemFromBlock(ModBlocks.spectreCoilNumber), 10, onlyFound);
		}
		else if (Worldgen.LAVA_CHARM && event.getName().equals(LootTableList.CHESTS_NETHER_BRIDGE))
		{
			addSingleItemWithChance(table, ModItems.lavaCharm, 30);
		}
		else if (Worldgen.NUMBERED_COILS && event.getName().equals(LootTableList.CHESTS_ABANDONED_MINESHAFT))
		{
			addSingleItemWithChance(table, Item.getItemFromBlock(ModBlocks.spectreCoilNumber), 8, onlyFound);
		}
		else if (Worldgen.MAGIC_HOOD && event.getName().equals(LootTableList.CHESTS_VILLAGE_BLACKSMITH))
		{
			addSingleItemWithChance(table, ModItems.magicHood, 15);
		}
		else if (Worldgen.SUMMONING_PENDULUM && event.getName().equals(LootTableList.CHESTS_STRONGHOLD_CORRIDOR))
		{
			addSingleItemWithChance(table, ModItems.summoningPendulum, 50);
		}
		else if (Worldgen.SLIME_CUBE && event.getName().equals(LootTableList.CHESTS_JUNGLE_TEMPLE))
		{
			addSingleItemWithChance(table, Item.getItemFromBlock(ModBlocks.slimeCube), 80);
		}
		else if (Worldgen.NUMBERED_COILS && event.getName().equals(LootTableList.CHESTS_END_CITY_TREASURE))
		{
			addSingleItemWithChance(table, Item.getItemFromBlock(ModBlocks.spectreCoilNumber), 30, onlyFound);
		}

		if (Worldgen.BIOME_CRYSTAL && event.getName().toString().startsWith("minecraft:chests/"))
		{
			LootEntry crystalEntry = new LootEntryItem(ModItems.biomeCrystal, 1, 0, new LootFunction[] { new LootFunction(new LootCondition[] {})
			{
				@Override
				public ItemStack apply(ItemStack stack, Random rand, LootContext context)
				{
					Object[] locationArray = Biome.REGISTRY.getKeys().toArray();
					ResourceLocation randomLocation = (ResourceLocation) locationArray[rand.nextInt(locationArray.length)];

					stack.setTagCompound(new NBTTagCompound());
					stack.getTagCompound().setString("biomeName", randomLocation.toString());

					return stack;
				}
			} }, new LootCondition[] {}, "randomthings:biomeCrystal");

			LootPool crystalPool = new LootPool(new LootEntry[] { crystalEntry }, new LootCondition[] { new RandomChance(0.2f) }, new RandomValueRange(1, 1), new RandomValueRange(0, 0), "randomthings:biomeCrystal");
			table.addPool(crystalPool);
		}
	}

	private static void addSingleItemWithChance(LootTable table, Item item, int chance)
	{
		String itemName = item.getRegistryName().getResourcePath().toString();

		LootEntry entryItem = new LootEntryItem(item, chance, 0, new LootFunction[0], new LootCondition[0], "item");
		LootEntry entryEmpty = new LootEntryEmpty(100 - chance, 0, new LootCondition[0], "empty");

		LootPool pool = new LootPool(new LootEntry[] { entryItem, entryEmpty }, new LootCondition[0], new RandomValueRange(1), new RandomValueRange(0), itemName);

		table.addPool(pool);
	}

	private static void addSingleItemWithChance(LootTable table, Item item, int chance, LootFunction function)
	{
		String itemName = item.getRegistryName().getResourcePath().toString();

		LootEntry entryItem = new LootEntryItem(item, chance, 0, new LootFunction[] { function }, new LootCondition[0], "item");
		LootEntry entryEmpty = new LootEntryEmpty(100 - chance, 0, new LootCondition[0], "empty");

		LootPool pool = new LootPool(new LootEntry[] { entryItem, entryEmpty }, new LootCondition[0], new RandomValueRange(1), new RandomValueRange(0), itemName);

		table.addPool(pool);
	}
	
	private static void addSingleItemWithChance(LootTable table, Item item, int chance, LootFunction function, LootCondition condition)
	{
		String itemName = item.getRegistryName().getResourcePath().toString();

		LootEntry entryItem = new LootEntryItem(item, chance, 0, new LootFunction[] { function }, new LootCondition[0], "item");
		LootEntry entryEmpty = new LootEntryEmpty(100 - chance, 0, new LootCondition[0], "empty");

		LootPool pool = new LootPool(new LootEntry[] { entryItem, entryEmpty }, new LootCondition[] {condition}, new RandomValueRange(1), new RandomValueRange(0), itemName);

		table.addPool(pool);
	}
	
	private static void addSingleItemWithChance(LootTable table, Item item, int chance, LootCondition condition)
	{
		String itemName = item.getRegistryName().getResourcePath().toString();

		LootEntry entryItem = new LootEntryItem(item, chance, 0, new LootFunction[0], new LootCondition[0], "item");
		LootEntry entryEmpty = new LootEntryEmpty(100 - chance, 0, new LootCondition[0], "empty");

		LootPool pool = new LootPool(new LootEntry[] { entryItem, entryEmpty }, new LootCondition[] {condition}, new RandomValueRange(1), new RandomValueRange(0), itemName);

		table.addPool(pool);
	}
}
