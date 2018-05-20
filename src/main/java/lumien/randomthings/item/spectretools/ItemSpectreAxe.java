package lumien.randomthings.item.spectretools;

import java.util.Set;

import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import lumien.randomthings.item.ItemBase;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;

public class ItemSpectreAxe extends ItemTool
{
	private static final Set<Block> EFFECTIVE_ON = Sets.newHashSet(new Block[] { Blocks.PLANKS, Blocks.BOOKSHELF, Blocks.LOG, Blocks.LOG2, Blocks.CHEST, Blocks.PUMPKIN, Blocks.LIT_PUMPKIN, Blocks.MELON_BLOCK, Blocks.LADDER, Blocks.WOODEN_BUTTON, Blocks.WOODEN_PRESSURE_PLATE });

	public ItemSpectreAxe()
	{
		super(ItemSpectreSword.spectreToolMaterial, EFFECTIVE_ON);
		this.attackDamage = 8.0f;
		this.attackSpeed = -3.0f;

		ItemBase.registerItem("spectreAxe", this);
	}

	@Override
	public float getDestroySpeed(ItemStack stack, IBlockState state)
	{
		Material material = state.getMaterial();
		return material != Material.WOOD && material != Material.PLANTS && material != Material.VINE ? super.getDestroySpeed(stack, state) : this.efficiency;
	}

	@Override
	public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot)
	{
		Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(equipmentSlot);

		if (equipmentSlot == EntityEquipmentSlot.MAINHAND)
		{
			multimap.put(EntityPlayer.REACH_DISTANCE.getName(), new AttributeModifier(ItemSpectrePickaxe.MOD_UUID, "Spectre Range Modifier", 3, 0));
		}

		return multimap;
	}
}