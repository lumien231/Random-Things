package lumien.randomthings.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockBeanStalk extends BlockBase
{
	boolean strongMagic;
	
	protected static final AxisAlignedBB STALK_AABB = new AxisAlignedBB(0.4f, 0, 0.4f, 0.6f, 1, 0.6f);

	protected BlockBeanStalk(boolean strongMagic)
	{
		super(strongMagic ? "beanStalk" : "lesserBeanStalk", Material.PLANTS);

		this.setSoundType(SoundType.PLANT);

		this.strongMagic = strongMagic;
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return STALK_AABB;
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

			IBlockState upState = worldIn.getBlockState(pos.up());
			if (upState.getBlock().getBlockHardness(upState,worldIn, pos.up()) != -1)
			{
				if (!worldIn.isAirBlock(pos.up()))
				{
					worldIn.playSound(null,pos, worldIn.getBlockState(pos.up()).getBlock().getSoundType().getBreakSound(),SoundCategory.BLOCKS, 1, 1f);
				}
				else
				{
					worldIn.playSound(null,pos, this.getSoundType().getBreakSound(),SoundCategory.BLOCKS, 1, 2);
				}
				
				worldIn.playAuxSFX(2005, pos.up(), 0);
				worldIn.setBlockState(pos.up(), this.getDefaultState());
				worldIn.scheduleUpdate(pos.up(), this, strongMagic ? 1 : 5);
			}
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

	@Override
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

	@Override
	public boolean isLadder(IBlockState state, IBlockAccess world, BlockPos pos, EntityLivingBase entity)
	{
		return true;
	}

	@Override
	public int quantityDropped(Random random)
	{
		return 0;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return null;
	}
}
