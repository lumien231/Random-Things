package lumien.randomthings.block;

import java.util.List;

import lumien.randomthings.item.block.ItemBlockSpecialChest;
import lumien.randomthings.tileentity.TileEntitySpecialChest;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockSpecialChest extends BlockContainerBase
{
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

	protected static final AxisAlignedBB CHEST_AABB = new AxisAlignedBB(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);

	public BlockSpecialChest()
	{
		super("specialChest", Material.WOOD, ItemBlockSpecialChest.class);

		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
		this.setHardness(2.5F);
		this.setSoundType(SoundType.WOOD);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return CHEST_AABB;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(CreativeTabs tab, NonNullList list)
	{
		for (int i = 0; i < 2; i++)
		{
			list.add(new ItemStack(ModBlocks.specialChest, 1, i));
		}
	}

	@Override
	public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest)
	{
		if (!player.capabilities.isCreativeMode)
		{
			Block.spawnAsEntity(world, pos, new ItemStack(this, 1, ((TileEntitySpecialChest) world.getTileEntity(pos)).getChestType()));
		}
		return world.setBlockToAir(pos);
	}

	@Override
	public void onBlockExploded(World world, BlockPos pos, Explosion explosion)
	{
		Block.spawnAsEntity(world, pos, new ItemStack(this, 1, ((TileEntitySpecialChest) world.getTileEntity(pos)).getChestType()));
	}

	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
		List<ItemStack> ret = new java.util.ArrayList<>();

		return ret;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new TileEntitySpecialChest();
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
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
	{
		return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing());
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		EnumFacing enumfacing = EnumFacing.getHorizontal(MathHelper.floor(placer.rotationYaw * 4.0F / 360.0F + 0.5D) & 3).getOpposite();
		state = state.withProperty(FACING, enumfacing);
		worldIn.setBlockState(pos, state, 3);

		TileEntity tileentity = worldIn.getTileEntity(pos);

		if (tileentity instanceof TileEntitySpecialChest)
		{
			if (stack.hasDisplayName())
			{
				((TileEntitySpecialChest) tileentity).setCustomName(stack.getDisplayName());
			}

			((TileEntitySpecialChest) tileentity).setChestType(stack.getItemDamage());
		}
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		TileEntity tileentity = worldIn.getTileEntity(pos);

		if (tileentity instanceof IInventory)
		{
			InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory) tileentity);
			worldIn.updateComparatorOutputLevel(pos, this);
		}

		super.breakBlock(worldIn, pos, state);
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (worldIn.isRemote)
		{
			return true;
		}
		else
		{
			ILockableContainer ilockablecontainer = this.getLockableContainer(worldIn, pos);

			if (ilockablecontainer != null)
			{
				playerIn.displayGUIChest(ilockablecontainer);
			}

			return true;
		}
	}

	public ILockableContainer getLockableContainer(World worldIn, BlockPos pos)
	{
		TileEntity tileentity = worldIn.getTileEntity(pos);

		if (!(tileentity instanceof TileEntitySpecialChest))
		{
			return null;
		}
		else
		{
			TileEntitySpecialChest object = (TileEntitySpecialChest) tileentity;

			return object;
		}
	}

	@Override
	public boolean hasComparatorInputOverride(IBlockState state)
	{
		return true;
	}

	@Override
	public int getComparatorInputOverride(IBlockState state, World worldIn, BlockPos pos)
	{
		return Container.calcRedstoneFromInventory(this.getLockableContainer(worldIn, pos));
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		EnumFacing enumfacing = EnumFacing.getFront(meta);

		if (enumfacing.getAxis() == EnumFacing.Axis.Y)
		{
			enumfacing = EnumFacing.NORTH;
		}

		return this.getDefaultState().withProperty(FACING, enumfacing);
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(FACING).getIndex();
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] { FACING });
	}
}
