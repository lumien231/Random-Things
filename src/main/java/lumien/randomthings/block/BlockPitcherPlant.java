package lumien.randomthings.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCauldron;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockPitcherPlant extends BlockBase
{
	protected static final AxisAlignedBB PITCHER_AABB = new AxisAlignedBB(0.5F - 0.2F, 0.0F, 0.5F - 0.2F, 0.5F + 0.2F, 0.2F * 4.0F, 0.5F + 0.2F);

	protected BlockPitcherPlant()
	{
		super("pitcherPlant", Material.PLANTS);

		float f = 0.2F;
		this.setSoundType(SoundType.PLANT);

		this.setTickRandomly(true);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return PITCHER_AABB;
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		ItemStack equipped = playerIn.getHeldItemMainhand();

		if (equipped.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null))
		{
			IFluidHandler fluidHandler = equipped.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);

			FluidTank waterTank = new FluidTank(new FluidStack(FluidRegistry.WATER, 1000), 1000);

			FluidActionResult result = FluidUtil.tryFillContainer(equipped, waterTank, 1000, playerIn, true);

			if (result.success)
			{
				if (equipped.getCount() == 1)
				{
					playerIn.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, result.result);
				}
				else
				{
					if (!playerIn.capabilities.isCreativeMode)
					{
						equipped.shrink(1);
					}
					playerIn.inventory.addItemStackToInventory(result.result);
				}
				return true;
			}
		}
		else if (equipped.getItem() == Items.GLASS_BOTTLE)
		{
			if (!playerIn.capabilities.isCreativeMode)
			{
				equipped.shrink(1);
			}

			if (!playerIn.inventory.addItemStackToInventory(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.WATER)))
			{
				equipped.grow(1);
			}

			return true;
		}

		return false;
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
	{
		return super.canPlaceBlockAt(worldIn, pos) && canBlockStay(worldIn, pos, this.getDefaultState());
	}

	public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state)
	{
		Block soil = worldIn.getBlockState(pos.down()).getBlock();
		return this.canPlaceBlockOn(soil);
	}

	protected void checkAndDropBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		if (!this.canBlockStay(worldIn, pos, state))
		{
			this.dropBlockAsItem(worldIn, pos, state, 0);
			worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
		}
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block neighborBlock, BlockPos changedPos)
	{
		super.neighborChanged(state, worldIn, pos, neighborBlock, changedPos);
		this.checkAndDropBlock(worldIn, pos, state);
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		this.checkAndDropBlock(worldIn, pos, state);

		for (EnumFacing facing : EnumFacing.HORIZONTALS)
		{
			BlockPos testPos = pos.offset(facing);

			IBlockState testState = worldIn.getBlockState(testPos);

			if (testState.getBlock() == Blocks.CAULDRON)
			{
				int level = testState.getValue(BlockCauldron.LEVEL);

				if (level < 3)
				{
					worldIn.setBlockState(testPos, testState.withProperty(BlockCauldron.LEVEL, level + 1));
				}
			}
			else
			{
				TileEntity te = worldIn.getTileEntity(testPos);

				if (te != null && te.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing.getOpposite()))
				{
					IFluidHandler fluidHandler = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing.getOpposite());

					if (fluidHandler != null)
					{
						fluidHandler.fill(new FluidStack(FluidRegistry.WATER, 1000), true);
					}
				}
			}
		}
	}

	protected boolean canPlaceBlockOn(Block ground)
	{
		return ground == Blocks.GRASS || ground == Blocks.DIRT;
	}

	@Override
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

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}
}
