package lumien.randomthings.block;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockBeanStalk extends BlockBase
{
	boolean strongMagic;

	protected BlockBeanStalk(boolean strongMagic)
	{
		super(strongMagic ? "beanStalk" : "lesserBeanStalk", Material.plants);

		this.setStepSound(soundTypeGrass);
		this.setBlockBounds(0.4f, 0, 0.4f, 0.6f, 1, 0.6f);

		this.strongMagic = strongMagic;
	}

	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
	{
		if (entityIn.onGround || entityIn.isCollidedVertically)
		{
			return;
		}

		if (entityIn.motionY >= 0.1)
		{
			Block top = entityIn.worldObj.getBlockState(new BlockPos(MathHelper.floor_double(entityIn.posX), MathHelper.floor_double(entityIn.posY) + 3, MathHelper.floor_double(entityIn.posZ))).getBlock();
			if (top == this)
			{
				entityIn.setPosition(entityIn.posX, entityIn.posY + 0.5, entityIn.posZ);
			}
		}
		else if (entityIn.motionY <= -0.1)
		{
			Block bottom = entityIn.worldObj.getBlockState(new BlockPos(MathHelper.floor_double(entityIn.posX), MathHelper.floor_double(entityIn.posY) - 3, MathHelper.floor_double(entityIn.posZ))).getBlock();
			if (bottom == null || bottom == this)
			{ // prevent clipping into block
				entityIn.setPosition(entityIn.posX, entityIn.posY - 0.5, entityIn.posZ);
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List list)
	{
		return;
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		if (!worldIn.isRemote)
		{
			if (strongMagic)
			{
				if (pos.getY() == worldIn.getHeight() - 2)
				{
					worldIn.setBlockState(pos.up(), ModBlocks.beanPod.getDefaultState());
					return;
				}
			}
			else
			{
				if (pos.getY() == worldIn.getHeight() || !worldIn.isAirBlock(pos.up()))
				{
					return;
				}
			}
			if (!worldIn.isAirBlock(pos.up()))
			{
				worldIn.playSoundEffect(pos.getX(), pos.up().getY(), pos.getZ(), worldIn.getBlockState(pos.up()).getBlock().stepSound.getBreakSound(), 1, 1f);
			}
			else
			{
				worldIn.playSoundEffect(pos.getX(), pos.up().getY(), pos.getZ(), this.stepSound.getBreakSound(), 1, 2);
			}
			worldIn.playAuxSFX(2005, pos.up(), 0);
			worldIn.setBlockState(pos.up(), this.getDefaultState());
			worldIn.scheduleUpdate(pos.up(), this, strongMagic ? 1 : 5);
		}
	}

	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
	{
		if (!worldIn.isRemote)
		{
			worldIn.scheduleUpdate(pos, this, 5);
		}
		return super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer);
	}

	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
	{
		this.checkForDrop(worldIn, pos, state);
	}

	protected final boolean checkForDrop(World worldIn, BlockPos p_176353_2_, IBlockState state)
	{
		if (this.canBlockStay(worldIn, p_176353_2_))
		{
			return true;
		}
		else
		{
			this.dropBlockAsItem(worldIn, p_176353_2_, state, 0);
			worldIn.setBlockToAir(p_176353_2_);
			return false;
		}
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
	{
		if (worldIn.isAirBlock(pos.down()))
		{
			return false;
		}
		IBlockState down = worldIn.getBlockState(pos.down());
		if (!(down.getBlock() instanceof BlockGrass || down.getBlock() instanceof BlockDirt || down.getBlock() instanceof BlockBeanStalk))
		{
			return false;
		}

		return true;
	}

	public boolean canBlockStay(World worldIn, BlockPos pos)
	{
		return this.canPlaceBlockAt(worldIn, pos);
	}

	public boolean isOpaqueCube()
	{
		return false;
	}

	public boolean isFullCube()
	{
		return false;
	}

	@SideOnly(Side.CLIENT)
	public EnumWorldBlockLayer getBlockLayer()
	{
		return EnumWorldBlockLayer.CUTOUT;
	}

	@Override
	public boolean isLadder(IBlockAccess world, BlockPos pos, EntityLivingBase entity)
	{
		return true;
	}

	public int quantityDropped(Random random)
	{
		return 0;
	}

	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return null;
	}
}
