package lumien.randomthings.item.block;

import lumien.randomthings.tileentity.TileEntityBlockDiaphanous;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemBlockBlockDiaphanous extends ItemBlock
{
	public ItemBlockBlockDiaphanous(Block block)
	{
		super(block);

		this.setHasSubtypes(true);
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack)
	{
		String display = super.getItemStackDisplayName(stack);

		if (stack.hasTagCompound())
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
