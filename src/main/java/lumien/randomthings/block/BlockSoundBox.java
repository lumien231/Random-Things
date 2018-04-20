package lumien.randomthings.block;

import lumien.randomthings.item.ModItems;
import lumien.randomthings.tileentity.TileEntitySoundBox;
import lumien.randomthings.util.WorldUtil;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockSoundBox extends BlockContainerBase
{
	public static final PropertyBool HAS_PATTERN = PropertyBool.create("has_pattern");

	protected BlockSoundBox()
	{
		super("soundBox", Material.WOOD);
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		boolean hasPattern = state.getValue(HAS_PATTERN);

		TileEntitySoundBox te = (TileEntitySoundBox) worldIn.getTileEntity(pos);

		if (te.hasPattern())
		{
			ItemStack pattern = te.getPattern();

			if (!worldIn.isRemote)
			{
				te.insertPattern(ItemStack.EMPTY);

				WorldUtil.spawnItemStack(worldIn, pos.up(), pattern);
			}

			return true;
		}
		else
		{
			ItemStack heldItem = playerIn.getHeldItem(hand);

			if (heldItem.getItem() == ModItems.soundPattern)
			{
				if (!worldIn.isRemote)
				{
					te.insertPattern(heldItem);
				}
				
				return true;
			}
		}

		return false;
	}


	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new TileEntitySoundBox();
	}

	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(HAS_PATTERN, Boolean.valueOf(meta > 0));
	}

	public int getMetaFromState(IBlockState state)
	{
		return ((Boolean) state.getValue(HAS_PATTERN)).booleanValue() ? 1 : 0;
	}

	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] { HAS_PATTERN });
	}
}
