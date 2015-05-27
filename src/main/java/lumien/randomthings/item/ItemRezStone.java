package lumien.randomthings.item;

import java.awt.Color;

import lumien.randomthings.worldgen.SingleRandomChestContent;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemRezStone extends ItemBase
{
	public ItemRezStone()
	{
		super("rezStone");

		this.setMaxStackSize(1);
		
		ChestGenHooks.addItem(ChestGenHooks.DUNGEON_CHEST, new SingleRandomChestContent(new ItemStack(this), 1, 1, 1));
		ChestGenHooks.addItem(ChestGenHooks.MINESHAFT_CORRIDOR, new SingleRandomChestContent(new ItemStack(this), 1, 1, 2));
		ChestGenHooks.addItem(ChestGenHooks.NETHER_FORTRESS, new SingleRandomChestContent(new ItemStack(this), 1, 1, 5));
	}
}
