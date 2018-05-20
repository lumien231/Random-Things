package lumien.randomthings.item;

public class ItemRezStone extends ItemBase
{
	public ItemRezStone()
	{
		super("rezStone");

		this.setMaxStackSize(1);

		/*
		 * ChestGenHooks.addItem(ChestGenHooks.DUNGEON_CHEST, new
		 * SingleRandomChestContent(new ItemStack(this), 1, 1, 1));
		 * ChestGenHooks.addItem(ChestGenHooks.MINESHAFT_CORRIDOR, new
		 * SingleRandomChestContent(new ItemStack(this), 1, 1, 2));
		 * ChestGenHooks.addItem(ChestGenHooks.NETHER_FORTRESS, new
		 * SingleRandomChestContent(new ItemStack(this), 1, 1, 5)); TODO
		 */
	}
}
