package lumien.randomthings.item.block;

import lumien.randomthings.tileentity.TileEntityBlockDiaphanous;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemBlockBlockDiaphanous extends ItemBlock
{
	public ItemBlockBlockDiaphanous(Block block)
	{
		super(block);
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
	{
		if (this.isInCreativeTab(tab))
		{
			ItemStack stoneDiaphanous = new ItemStack(this);
			stoneDiaphanous.setTagCompound(new NBTTagCompound());

			stoneDiaphanous.getTagCompound().setString("block", "minecraft:stone");
			stoneDiaphanous.getTagCompound().setInteger("meta", 0);
			stoneDiaphanous.getTagCompound().setBoolean("inverted", false);

			items.add(stoneDiaphanous);
		}
	}

	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return super.getUnlocalizedName(stack) + (stack.hasTagCompound() && stack.getTagCompound().getBoolean("inverted") ? "_inverted" : "");
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack)
	{
		String display = super.getItemStackDisplayName(stack);

		if (stack.hasTagCompound() && stack.getTagCompound().hasKey("block"))
		{
			NBTTagCompound compound = stack.getTagCompound();
			IBlockState toDisplay;

			Block b = Block.REGISTRY.getObject(new ResourceLocation(compound.getString("block")));
			int meta = compound.getInteger("meta");

			ItemStack blockStack = new ItemStack(b, 1, meta);

			display += " <" + blockStack.getItem().getItemStackDisplayName(blockStack) + ">";
		}
		else
		{
			ItemStack blockStack = new ItemStack(Blocks.STONE);
			display += " <" + blockStack.getItem().getItemStackDisplayName(blockStack) + ">";
		}

		return display;
	}
}
