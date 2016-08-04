package lumien.randomthings.block;

import java.util.List;

import lumien.randomthings.RandomThings;
import lumien.randomthings.item.ItemItemFilter.ItemFilterRepresentation;
import lumien.randomthings.lib.GuiIds;
import lumien.randomthings.tileentity.TileEntityFilteredSuperLubricentPlatform;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockFilteredSuperLubricentPlatform extends BlockContainerBase
{
	protected static final AxisAlignedBB PLATFORM_AABB = new AxisAlignedBB(0, 14F / 16F, 0, 1, 1, 1);

	protected BlockFilteredSuperLubricentPlatform()
	{
		super("filteredSuperLubricentPlatform", Material.ICE);

		this.slipperiness = 1F / 0.98F;
		this.setHardness(0.5F).setLightOpacity(3);
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (!worldIn.isRemote)
		{
			playerIn.openGui(RandomThings.instance, GuiIds.FILTERED_SUPERLUBRICENT_PLATFORM, worldIn, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.TRANSLUCENT;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return PLATFORM_AABB;
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
	public boolean shouldSideBeRendered(IBlockState state, IBlockAccess worldIn, BlockPos pos, EnumFacing side)
	{
		IBlockState iblockstate = worldIn.getBlockState(pos.offset(side));
		Block block = iblockstate.getBlock();

		if (state != iblockstate)
		{
			return true;
		}

		if (block == this)
		{
			return false;
		}

		return false;
	}

	@Override
	public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		return side == EnumFacing.UP;
	}

	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB mask, List list, Entity collidingEntity)
	{
		if (collidingEntity != null)
		{
			if (collidingEntity instanceof EntityItem)
			{
				EntityItem ei = (EntityItem) collidingEntity;

				ItemFilterRepresentation repres = ((TileEntityFilteredSuperLubricentPlatform) worldIn.getTileEntity(pos)).getRepres();

				if (repres != null && repres.matchesItemStack(ei.getEntityItem()))
				{
					return;
				}
			}
			
			if (collidingEntity.posY < pos.getY() + 14F / 16F)
			{
				return;
			}

			if (collidingEntity instanceof EntityPlayer)
			{
				EntityPlayer player = (EntityPlayer) collidingEntity;

				if (player.isSneaking() && player.motionY <= 0)
				{
					return;
				}
			}
		}
		else
		{
			return;
		}
		
		super.addCollisionBoxToList(state, worldIn, pos, mask, list, collidingEntity);
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new TileEntityFilteredSuperLubricentPlatform();
	}
}
