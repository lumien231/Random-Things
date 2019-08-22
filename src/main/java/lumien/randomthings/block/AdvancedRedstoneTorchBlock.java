package lumien.randomthings.block;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.WeakHashMap;

import com.google.common.collect.Lists;

import lumien.randomthings.tileentity.AdvancedRedstoneTorchTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.TorchBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

public class AdvancedRedstoneTorchBlock extends TorchBlock
{
	public static enum COLOR implements IStringSerializable
	{
		GREEN, RED;

		@Override
		public String getName()
		{
			return this == GREEN ? "green" : "red";
		}
	}

	public static final EnumProperty<COLOR> COLOR_PROPERTY = EnumProperty.create("color", COLOR.class);
	private static final Map<IBlockReader, List<AdvancedRedstoneTorchBlock.Toggle>> BURNED_TORCHES = new WeakHashMap<>();

	protected AdvancedRedstoneTorchBlock()
	{
		super(Block.Properties.create(Material.MISCELLANEOUS).doesNotBlockMovement().hardnessAndResistance(0.0F).lightValue(7).sound(SoundType.WOOD));
		this.setDefaultState(this.stateContainer.getBaseState().with(COLOR_PROPERTY, COLOR.RED));
	}

	protected AdvancedRedstoneTorchBlock(Block.Properties properties)
	{
		super(properties);
	}

	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new AdvancedRedstoneTorchTileEntity();
	}

	public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
	{
		if (worldIn.isRemote)
		{
			return true;
		}
		else
		{
			TileEntity tileentity = worldIn.getTileEntity(pos);
			if (tileentity instanceof AdvancedRedstoneTorchTileEntity)
			{
				AdvancedRedstoneTorchTileEntity art = (AdvancedRedstoneTorchTileEntity) tileentity;
				NetworkHooks.openGui((ServerPlayerEntity) player, art);
			}

			return true;
		}
	}

	/**
	 * How many world ticks before ticking
	 */
	public int tickRate(IWorldReader worldIn)
	{
		return 2;
	}

	public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving)
	{
		for (Direction direction : Direction.values())
		{
			worldIn.notifyNeighborsOfStateChange(pos.offset(direction), this);
		}

	}

	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
	{
		if (!isMoving)
		{
			for (Direction direction : Direction.values())
			{
				worldIn.notifyNeighborsOfStateChange(pos.offset(direction), this);
			}

		}
	}

	/**
	 * @deprecated call via {@link IBlockState#getWeakPower(IBlockAccess,BlockPos,EnumFacing)} whenever possible. Implementing/overriding is fine.
	 */
	public int getWeakPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side)
	{
		AdvancedRedstoneTorchTileEntity te = (AdvancedRedstoneTorchTileEntity) blockAccess.getTileEntity(pos);

		int strength = blockState.get(COLOR_PROPERTY) == COLOR.RED ? te.signalStrengthRed() : te.signalStrengthGreen();

		return Direction.UP != side ? strength : 0;
	}

	protected boolean shouldBeGreen(World worldIn, BlockPos pos, BlockState state)
	{
		return worldIn.isSidePowered(pos.down(), Direction.DOWN);
	}

	public void tick(BlockState state, World worldIn, BlockPos pos, Random random)
	{
		update(state, worldIn, pos, random, this.shouldBeGreen(worldIn, pos, state));
	}

	public static void update(BlockState state, World worldIn, BlockPos pos, Random p_196527_3_, boolean p_196527_4_)
	{
		List<AdvancedRedstoneTorchBlock.Toggle> list = BURNED_TORCHES.get(worldIn);

		while (list != null && !list.isEmpty() && worldIn.getGameTime() - (list.get(0)).time > 60L)
		{
			list.remove(0);
		}

		if (state.get(COLOR_PROPERTY) == COLOR.RED)
		{
			if (p_196527_4_)
			{
				worldIn.setBlockState(pos, state.with(COLOR_PROPERTY, COLOR.GREEN), 3);
				if (isBurnedOut(worldIn, pos, true))
				{
					worldIn.playEvent(1502, pos, 0);
					worldIn.getPendingBlockTicks().scheduleTick(pos, worldIn.getBlockState(pos).getBlock(), 160);
				}
			}
		}
		else if (!p_196527_4_ && !isBurnedOut(worldIn, pos, false))
		{
			worldIn.setBlockState(pos, state.with(COLOR_PROPERTY, COLOR.RED), 3);
		}

	}

	public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
	{
		if (state.get(COLOR_PROPERTY) == COLOR.RED == this.shouldBeGreen(worldIn, pos, state) && !worldIn.getPendingBlockTicks().isTickPending(pos, this))
		{
			worldIn.getPendingBlockTicks().scheduleTick(pos, this, this.tickRate(worldIn));
		}

	}

	/**
	 * @deprecated call via {@link IBlockState#getStrongPower(IBlockAccess,BlockPos,EnumFacing)} whenever possible. Implementing/overriding is fine.
	 */
	public int getStrongPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side)
	{
		return side == Direction.DOWN ? blockState.getWeakPower(blockAccess, pos, side) : 0;
	}

	/**
	 * Can this block provide power. Only wire currently seems to have this change based on its state.
	 * 
	 * @deprecated call via {@link IBlockState#canProvidePower()} whenever possible. Implementing/overriding is fine.
	 */
	public boolean canProvidePower(BlockState state)
	{
		return true;
	}

	protected RedstoneParticleData GREEN_DUST = new RedstoneParticleData(0.0F, 1.0F, 0.0F, 1.0F);

	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		if (stateIn.get(COLOR_PROPERTY) == COLOR.RED)
		{
			double d0 = (double) pos.getX() + 0.5D + (rand.nextDouble() - 0.5D) * 0.2D;
			double d1 = (double) pos.getY() + 0.7D + (rand.nextDouble() - 0.5D) * 0.2D;
			double d2 = (double) pos.getZ() + 0.5D + (rand.nextDouble() - 0.5D) * 0.2D;
			worldIn.addParticle(RedstoneParticleData.REDSTONE_DUST, d0, d1, d2, 0.0D, 0.0D, 0.0D);
		}
		else
		{
			double d0 = (double) pos.getX() + 0.5D + (rand.nextDouble() - 0.5D) * 0.2D;
			double d1 = (double) pos.getY() + 0.7D + (rand.nextDouble() - 0.5D) * 0.2D;
			double d2 = (double) pos.getZ() + 0.5D + (rand.nextDouble() - 0.5D) * 0.2D;
			worldIn.addParticle(GREEN_DUST, d0, d1, d2, 0.0D, 0.0D, 0.0D);
		}
	}

	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(COLOR_PROPERTY);
	}

	private static boolean isBurnedOut(World p_176598_0_, BlockPos worldIn, boolean pos)
	{
		List<AdvancedRedstoneTorchBlock.Toggle> list = BURNED_TORCHES.computeIfAbsent(p_176598_0_, (p_220288_0_) -> {
			return Lists.newArrayList();
		});
		if (pos)
		{
			list.add(new AdvancedRedstoneTorchBlock.Toggle(worldIn.toImmutable(), p_176598_0_.getGameTime()));
		}

		int i = 0;

		for (int j = 0; j < list.size(); ++j)
		{
			AdvancedRedstoneTorchBlock.Toggle redstonetorchblock$toggle = list.get(j);
			if (redstonetorchblock$toggle.pos.equals(worldIn))
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

	public static class Toggle
	{
		private final BlockPos pos;
		private final long time;

		public Toggle(BlockPos pos, long time)
		{
			this.pos = pos;
			this.time = time;
		}
	}
}