package lumien.randomthings.block;

import java.awt.Color;
import java.util.Random;

import lumien.randomthings.lib.IRTBlockColor;
import lumien.randomthings.lib.ISuperLubricent;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockCompressedSlimeBlock extends BlockBase implements ISuperLubricent, IRTBlockColor
{
	protected static final AxisAlignedBB AABB_0 = new AxisAlignedBB(0D, 0.0D, 0D, 1D, 0.5D, 1D);
	protected static final AxisAlignedBB AABB_1 = new AxisAlignedBB(0D, 0.0D, 0D, 1D, 0.25D, 1D);
	protected static final AxisAlignedBB AABB_2 = new AxisAlignedBB(0D, 0.0D, 0D, 1D, 0.125D, 1D);

	int[] compressionColors = new int[] { new Color(200, 200, 200).getRGB(), new Color(150, 150, 150).getRGB(), new Color(100, 100, 100).getRGB() };

	public static final PropertyInteger COMPRESSION = PropertyInteger.create("compression", 0, 2);

	protected BlockCompressedSlimeBlock()
	{
		super("compressedSlimeBlock", Material.CLAY);

		this.setSoundType(SoundType.SLIME);
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] { COMPRESSION });
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(COMPRESSION);
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(COMPRESSION, meta);
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return Item.getItemFromBlock(Blocks.SLIME_BLOCK);
	}

	@Override
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
	{
		return new ItemStack(Blocks.SLIME_BLOCK);
	}

	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess worldIn, BlockPos pos)
	{
		int compression = state.getValue(COMPRESSION);

		if (compression == 0)
		{
			return AABB_0;
		}
		else if (compression == 1)
		{
			return AABB_1;
		}
		else
		{
			return AABB_2;
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos)
	{
		int compression = state.getValue(COMPRESSION);

		if (compression == 0)
		{
			return AABB_0.offset(pos);
		}
		else if (compression == 1)
		{
			return AABB_1.offset(pos);
		}
		else
		{
			return AABB_2.offset(pos);
		}
	}

	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
	{
		super.onEntityCollidedWithBlock(worldIn, pos, state, entityIn);

		if (entityIn.motionY < 1)
		{
			int compression = state.getValue(COMPRESSION);

			entityIn.onGround = false;
			entityIn.fallDistance = 0;
			entityIn.motionY = 0.8 + compression * 0.4;
		}
	}

	@Override
	public int colorMultiplier(IBlockState state, IBlockAccess p_186720_2_, BlockPos pos, int tintIndex)
	{
		return compressionColors[state.getValue(COMPRESSION)];
	}
}
