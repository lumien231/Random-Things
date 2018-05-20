package lumien.randomthings.block;

import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;

import lumien.randomthings.RandomThings;
import lumien.randomthings.lib.GuiIds;
import lumien.randomthings.lib.INoItem;
import lumien.randomthings.tileentity.TileEntityAdvancedRedstoneTorch;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockAdvancedRedstoneTorch extends BlockContainerBase implements INoItem
{
	protected static final AxisAlignedBB STANDING_AABB = new AxisAlignedBB(0.4000000059604645D, 0.0D, 0.4000000059604645D, 0.6000000238418579D, 0.6000000238418579D, 0.6000000238418579D);
	protected static final AxisAlignedBB TORCH_NORTH_AABB = new AxisAlignedBB(0.3499999940395355D, 0.20000000298023224D, 0.699999988079071D, 0.6499999761581421D, 0.800000011920929D, 1.0D);
	protected static final AxisAlignedBB TORCH_SOUTH_AABB = new AxisAlignedBB(0.3499999940395355D, 0.20000000298023224D, 0.0D, 0.6499999761581421D, 0.800000011920929D, 0.30000001192092896D);
	protected static final AxisAlignedBB TORCH_WEST_AABB = new AxisAlignedBB(0.699999988079071D, 0.20000000298023224D, 0.3499999940395355D, 1.0D, 0.800000011920929D, 0.6499999761581421D);
	protected static final AxisAlignedBB TORCH_EAST_AABB = new AxisAlignedBB(0.0D, 0.20000000298023224D, 0.3499999940395355D, 0.30000001192092896D, 0.800000011920929D, 0.6499999761581421D);

	private static final Map<World, List<BlockAdvancedRedstoneTorch.Toggle>> toggles = new java.util.WeakHashMap<World, List<Toggle>>(); // FORGE - fix vanilla MC-101233
	private final boolean isOn;

	public static final PropertyDirection FACING = PropertyDirection.create("facing", new Predicate<EnumFacing>()
	{
		@Override
		public boolean apply(@Nullable EnumFacing p_apply_1_)
		{
			return p_apply_1_ != EnumFacing.DOWN;
		}
	});

	protected BlockAdvancedRedstoneTorch(boolean isOn)
	{
		super("advancedRedstoneTorch" + (isOn ? "_on" : ""), Material.CIRCUITS);

		this.isOn = isOn;
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.UP));
		this.setTickRandomly(true);
	}

	@Override
	public boolean hasNoItem()
	{
		return !this.getUnlocalizedName().endsWith("_on");
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (!worldIn.isRemote)
		{
			playerIn.openGui(RandomThings.instance, GuiIds.ADVANCED_REDSTONE_TORCH, worldIn, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
	}

	@Override
	public int tickRate(World worldIn)
	{
		return 2;
	}

	private boolean isBurnedOut(World worldIn, BlockPos pos, boolean turnOff)
	{
		if (!toggles.containsKey(worldIn))
		{
			toggles.put(worldIn, Lists.newArrayList());
		}

		List<BlockAdvancedRedstoneTorch.Toggle> list = toggles.get(worldIn);

		if (turnOff)
		{
			list.add(new BlockAdvancedRedstoneTorch.Toggle(pos, worldIn.getTotalWorldTime()));
		}

		int i = 0;

		for (int j = 0; j < list.size(); ++j)
		{
			BlockAdvancedRedstoneTorch.Toggle blockredstonetorch$toggle = list.get(j);

			if (blockredstonetorch$toggle.pos.equals(pos))
			{
				++i;

				if (i >= 8)
				{
					return true;
				}
			}
		}

		return false;
	}

	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
	{
		if (this.isOn)
		{
			for (EnumFacing enumfacing : EnumFacing.values())
			{
				worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this, false);
			}
		}
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		if (this.isOn)
		{
			for (EnumFacing enumfacing : EnumFacing.values())
			{
				worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this, false);
			}
		}
	}

	@Override
	public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
	{
		int strengthOn = ((TileEntityAdvancedRedstoneTorch) blockAccess.getTileEntity(pos)).getSignalStrengthOn();
		int strengthOff = ((TileEntityAdvancedRedstoneTorch) blockAccess.getTileEntity(pos)).getSignalStrengthOff();

		return blockState.getValue(FACING) != side ? (isOn ? strengthOn : strengthOff) : 0;
	}

	private boolean shouldBeOff(World worldIn, BlockPos pos, IBlockState state)
	{
		EnumFacing enumfacing = state.getValue(FACING).getOpposite();
		return worldIn.isSidePowered(pos.offset(enumfacing), enumfacing);
	}

	@Override
	public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random)
	{
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		boolean flag = this.shouldBeOff(worldIn, pos, state);
		List<BlockAdvancedRedstoneTorch.Toggle> list = toggles.get(worldIn);

		while (list != null && !list.isEmpty() && worldIn.getTotalWorldTime() - (list.get(0)).time > 60L)
		{
			list.remove(0);
		}

		if (this.isOn)
		{
			if (flag)
			{
				worldIn.setBlockState(pos, ModBlocks.advancedRedstoneTorchOff.getDefaultState().withProperty(FACING, state.getValue(FACING)), 3);

				if (this.isBurnedOut(worldIn, pos, true))
				{
					worldIn.playSound((EntityPlayer) null, pos, SoundEvents.BLOCK_REDSTONE_TORCH_BURNOUT, SoundCategory.BLOCKS, 0.5F, 2.6F + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.8F);

					for (int i = 0; i < 5; ++i)
					{
						double d0 = pos.getX() + rand.nextDouble() * 0.6D + 0.2D;
						double d1 = pos.getY() + rand.nextDouble() * 0.6D + 0.2D;
						double d2 = pos.getZ() + rand.nextDouble() * 0.6D + 0.2D;
						worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, 0.0D, 0.0D, 0.0D);
					}

					worldIn.scheduleUpdate(pos, worldIn.getBlockState(pos).getBlock(), 160);
				}
			}
		}
		else if (!flag && !this.isBurnedOut(worldIn, pos, false))
		{
			worldIn.setBlockState(pos, ModBlocks.advancedRedstoneTorchOn.getDefaultState().withProperty(FACING, state.getValue(FACING)), 3);
		}
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
	{
		if (!this.onNeighborChangeInternal(worldIn, pos, state))
		{
			if (this.isOn == this.shouldBeOff(worldIn, pos, state))
			{
				worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
			}
		}
	}

	@Override
	public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
	{
		return side == EnumFacing.DOWN ? blockState.getWeakPower(blockAccess, pos, side) : 0;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return Item.getItemFromBlock(ModBlocks.advancedRedstoneTorchOn);
	}

	@Override
	public boolean canProvidePower(IBlockState state)
	{
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		double d0 = pos.getX() + 0.5D + (rand.nextDouble() - 0.5D) * 0.2D;
		double d1 = pos.getY() + 0.7D + (rand.nextDouble() - 0.5D) * 0.2D;
		double d2 = pos.getZ() + 0.5D + (rand.nextDouble() - 0.5D) * 0.2D;
		EnumFacing enumfacing = stateIn.getValue(FACING);

		if (enumfacing.getAxis().isHorizontal())
		{
			EnumFacing enumfacing1 = enumfacing.getOpposite();
			double d3 = 0.27D;
			d0 += 0.27D * enumfacing1.getFrontOffsetX();
			d1 += 0.22D;
			d2 += 0.27D * enumfacing1.getFrontOffsetZ();
		}

		if (this.isOn)
		{
			worldIn.spawnParticle(EnumParticleTypes.REDSTONE, d0, d1, d2, 0.0D, 0.0D, 0.0D);
		}
		else
		{
			worldIn.spawnParticle(EnumParticleTypes.REDSTONE, d0, d1, d2, 0.1D, 1.0D, 0.0D);
		}
	}

	@Override
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
	{
		return new ItemStack(ModBlocks.advancedRedstoneTorchOn);
	}

	@Override
	public boolean isAssociatedBlock(Block other)
	{
		return other == ModBlocks.advancedRedstoneTorchOn || other == ModBlocks.advancedRedstoneTorchOff;
	}

	static class Toggle
	{
		BlockPos pos;
		long time;

		public Toggle(BlockPos pos, long time)
		{
			this.pos = pos;
			this.time = time;
		}
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new TileEntityAdvancedRedstoneTorch();
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		switch (state.getValue(FACING))
		{
		case EAST:
			return TORCH_EAST_AABB;
		case WEST:
			return TORCH_WEST_AABB;
		case SOUTH:
			return TORCH_SOUTH_AABB;
		case NORTH:
			return TORCH_NORTH_AABB;
		default:
			return STANDING_AABB;
		}
	}

	@Override
	@Nullable
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
	{
		return NULL_AABB;
	}

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

	private boolean canPlaceOn(World worldIn, BlockPos pos)
	{
		IBlockState state = worldIn.getBlockState(pos);
		return state.getBlock().canPlaceTorchOnTop(state, worldIn, pos);
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
	{
		for (EnumFacing enumfacing : FACING.getAllowedValues())
		{
			if (this.canPlaceAt(worldIn, pos, enumfacing))
			{
				return true;
			}
		}

		return false;
	}

	private boolean canPlaceAt(World worldIn, BlockPos pos, EnumFacing facing)
	{
		BlockPos blockpos = pos.offset(facing.getOpposite());
		IBlockState iblockstate = worldIn.getBlockState(blockpos);
		Block block = iblockstate.getBlock();
		BlockFaceShape blockfaceshape = iblockstate.getBlockFaceShape(worldIn, blockpos, facing);

		if (facing.equals(EnumFacing.UP) && this.canPlaceOn(worldIn, blockpos))
		{
			return true;
		}
		else if (facing != EnumFacing.UP && facing != EnumFacing.DOWN)
		{
			return !isExceptBlockForAttachWithPiston(block) && blockfaceshape == BlockFaceShape.SOLID;
		}
		else
		{
			return false;
		}
	}

	@Override
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
	{
		if (this.canPlaceAt(worldIn, pos, facing))
		{
			return this.getDefaultState().withProperty(FACING, facing);
		}
		else
		{
			for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
			{
				if (this.canPlaceAt(worldIn, pos, enumfacing))
				{
					return this.getDefaultState().withProperty(FACING, enumfacing);
				}
			}

			return this.getDefaultState();
		}
	}

	protected boolean onNeighborChangeInternal(World worldIn, BlockPos pos, IBlockState state)
	{
		if (!this.checkForDrop(worldIn, pos, state))
		{
			return true;
		}
		else
		{
			EnumFacing enumfacing = state.getValue(FACING);
			EnumFacing.Axis enumfacing$axis = enumfacing.getAxis();
			EnumFacing enumfacing1 = enumfacing.getOpposite();
			BlockPos blockpos = pos.offset(enumfacing1);
			boolean flag = false;

			if (enumfacing$axis.isHorizontal() && worldIn.getBlockState(blockpos).getBlockFaceShape(worldIn, blockpos, enumfacing) != BlockFaceShape.SOLID)
			{
				flag = true;
			}
			else if (enumfacing$axis.isVertical() && !this.canPlaceOn(worldIn, blockpos))
			{
				flag = true;
			}

			if (flag)
			{
				this.dropBlockAsItem(worldIn, pos, state, 0);
				worldIn.setBlockToAir(pos);
				return true;
			}
			else
			{
				return false;
			}
		}
	}

	protected boolean checkForDrop(World worldIn, BlockPos pos, IBlockState state)
	{
		if (state.getBlock() == this && this.canPlaceAt(worldIn, pos, state.getValue(FACING)))
		{
			return true;
		}
		else
		{
			if (worldIn.getBlockState(pos).getBlock() == this)
			{
				this.dropBlockAsItem(worldIn, pos, state, 0);
				worldIn.setBlockToAir(pos);
			}

			return false;
		}
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		IBlockState iblockstate = this.getDefaultState();

		switch (meta)
		{
		case 1:
			iblockstate = iblockstate.withProperty(FACING, EnumFacing.EAST);
			break;
		case 2:
			iblockstate = iblockstate.withProperty(FACING, EnumFacing.WEST);
			break;
		case 3:
			iblockstate = iblockstate.withProperty(FACING, EnumFacing.SOUTH);
			break;
		case 4:
			iblockstate = iblockstate.withProperty(FACING, EnumFacing.NORTH);
			break;
		case 5:
		default:
			iblockstate = iblockstate.withProperty(FACING, EnumFacing.UP);
		}

		return iblockstate;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}

	/**
	 * Convert the BlockState into the correct metadata value
	 */
	@Override
	public int getMetaFromState(IBlockState state)
	{
		int i = 0;

		switch (state.getValue(FACING))
		{
		case EAST:
			i = i | 1;
			break;
		case WEST:
			i = i | 2;
			break;
		case SOUTH:
			i = i | 3;
			break;
		case NORTH:
			i = i | 4;
			break;
		case DOWN:
		case UP:
		default:
			i = i | 5;
		}

		return i;
	}

	@Override
	public IBlockState withRotation(IBlockState state, Rotation rot)
	{
		return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
	}

	@Override
	public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
	{
		return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] { FACING });
	}

	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
	{
		return BlockFaceShape.UNDEFINED;
	}
}