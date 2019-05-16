package lumien.randomthings.block;

import java.util.Random;

import javax.annotation.Nullable;

import lumien.randomthings.item.ItemIngredient;
import lumien.randomthings.item.ModItems;
import lumien.randomthings.lib.INoItem;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockLotus extends BlockBase implements IPlantable, IGrowable
{
	public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 3);

	private static final AxisAlignedBB[] CROPS_AABB = new AxisAlignedBB[] { new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.3125D, 0.9375D), new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.5625D, 0.9375D), new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.6875D, 0.9375D), new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.875D, 0.9375D) };

	public BlockLotus()
	{
		super("lotus", Material.PLANTS);

		this.setSoundType(SoundType.PLANT);
		this.setTickRandomly(true);
		this.setHardness(0.0F);

		this.setCreativeTab(null);
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
	{
		return new ItemStack(ModItems.lotusSeeds);
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return ModItems.lotusSeeds;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return CROPS_AABB[state.getValue(AGE)];
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] { AGE });
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(AGE);
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(AGE, meta);
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (state.getValue(AGE) == 3)
		{
			if (worldIn.isRemote)
			{
				return true;
			}
			else
			{
				worldIn.setBlockState(pos, state.withProperty(AGE, 0));
				playerIn.addItemStackToInventory(new ItemStack(ModItems.ingredients, 1, ItemIngredient.INGREDIENT.LOTUS_BLOSSOM.id));
			}
		}

		return false;
	}

	@Override
	public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos)
	{
		return EnumPlantType.Plains;
	}

	@Override
	public IBlockState getPlant(IBlockAccess world, BlockPos pos)
	{
		IBlockState state = world.getBlockState(pos);
		if (state.getBlock() != this)
			return getDefaultState();
		return state;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
	{
		return BlockFaceShape.UNDEFINED;
	}

	@Override
	@Nullable
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
	{
		return NULL_AABB;
	}

	/**
	 * Used to determine ambient occlusion and culling when rebuilding chunks for
	 * render
	 */
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}

	/**
	 * Checks if this block can be placed exactly at the given position.
	 */
	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
	{
		IBlockState soil = worldIn.getBlockState(pos.down());
		return super.canPlaceBlockAt(worldIn, pos) && soil.getBlock().canSustainPlant(soil, worldIn, pos.down(), net.minecraft.util.EnumFacing.UP, this);
	}

	protected boolean canSustainBush(IBlockState state)
	{
		return state.getBlock() == Blocks.GRASS || state.getBlock() == Blocks.DIRT || state.getBlock() == Blocks.FARMLAND;
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
	{
		super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
		this.checkAndDropBlock(worldIn, pos, state);
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		int i = state.getValue(AGE);

		if (i < 3)
		{
			if (rand.nextInt(10) == 0)
			{
				worldIn.setBlockState(pos, state.withProperty(AGE, i + 1), 2);
			}
		}

		this.checkAndDropBlock(worldIn, pos, state);
	}

	protected void checkAndDropBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		if (!this.canBlockStay(worldIn, pos, state))
		{
			this.dropBlockAsItem(worldIn, pos, state, 0);
			worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
		}
	}

	public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state)
	{
		if (state.getBlock() == this)
		{
			IBlockState soil = worldIn.getBlockState(pos.down());
			return soil.getBlock().canSustainPlant(soil, worldIn, pos.down(), net.minecraft.util.EnumFacing.UP, this);
		}
		return this.canSustainBush(worldIn.getBlockState(pos.down()));
	}

	@Override
	public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient)
	{
		return state.getValue(AGE) < 3;
	}

	@Override
	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state)
	{
		return false;
	}

	@Override
	public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state)
	{
	}
}
