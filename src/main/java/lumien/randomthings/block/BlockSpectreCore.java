package lumien.randomthings.block;

import lumien.randomthings.config.Internals;
import lumien.randomthings.handler.ModDimensions;
import lumien.randomthings.handler.spectre.SpectreCube;
import lumien.randomthings.handler.spectre.SpectreHandler;
import lumien.randomthings.item.ItemIngredient;
import lumien.randomthings.item.ModItems;
import lumien.randomthings.lib.IExplosionImmune;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockSpectreCore extends BlockBase implements IExplosionImmune
{
	enum ORIENTATION implements IStringSerializable
	{
		NW("nw"), NE("ne"), ES("es"), SW("sw");

		String value;

		private ORIENTATION(String value)
		{
			this.value = value;
		}

		@Override
		public String getName()
		{
			return value;
		}
	}

	public static PropertyEnum<BlockSpectreCore.ORIENTATION> orientation = PropertyEnum.<BlockSpectreCore.ORIENTATION>create("orientation", BlockSpectreCore.ORIENTATION.class);

	protected BlockSpectreCore()
	{
		super("spectreCore", Material.ROCK);

		this.setBlockUnbreakable();
		this.setSoundType(SoundType.GLASS);
		this.setResistance(6000000.0F);
		this.setDefaultState(this.blockState.getBaseState().withProperty(orientation, ORIENTATION.NW));
		this.setCreativeTab(null);
	}

	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list)
	{

	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
	{
		ORIENTATION blockPosition;

		if (worldIn.getBlockState(pos.offset(EnumFacing.NORTH)).getBlock() != this && worldIn.getBlockState(pos.offset(EnumFacing.WEST)).getBlock() != this)
		{
			blockPosition = ORIENTATION.NW;
		}
		else if (worldIn.getBlockState(pos.offset(EnumFacing.NORTH)).getBlock() != this && worldIn.getBlockState(pos.offset(EnumFacing.EAST)).getBlock() != this)
		{
			blockPosition = ORIENTATION.NE;
		}
		else if (worldIn.getBlockState(pos.offset(EnumFacing.EAST)).getBlock() != this && worldIn.getBlockState(pos.offset(EnumFacing.SOUTH)).getBlock() != this)
		{
			blockPosition = ORIENTATION.ES;
		}
		else
		{
			blockPosition = ORIENTATION.SW;
		}

		return state.withProperty(orientation, blockPosition);
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return 0;
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] { orientation });
	}

	@Override
	public boolean canEntityDestroy(IBlockState state, IBlockAccess world, BlockPos pos, Entity entity)
	{
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.TRANSLUCENT;
	}

	@Override
	public boolean isNormalCube(IBlockState state)
	{
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockState state, IBlockAccess worldIn, BlockPos pos, EnumFacing side)
	{
		IBlockState iblockstate = worldIn.getBlockState(pos.offset(side));
		Block block = iblockstate.getBlock();

		if (block == this || iblockstate.getBlock() == ModBlocks.spectreBlock)
		{
			return false;
		}

		if (state != iblockstate)
		{
			return true;
		}

		return false;
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (worldIn.provider.getDimension() == Internals.SPECTRE_ID)
		{
			ItemStack holding = playerIn.getHeldItem(hand);

			if (holding.getItem() == ModItems.ingredients && holding.getItemDamage() == ItemIngredient.INGREDIENT.ECTO_PLASM.id)
			{
				if (!worldIn.isRemote)
				{
					SpectreCube cube = SpectreHandler.getInstance().getSpectreCubeFromPos(worldIn, pos.up());

					if (cube != null)
					{
						holding.shrink(cube.increaseHeight(holding.getCount()));
					}
				}
				return true;
			}
			else if (holding.isEmpty())
			{
				if (!worldIn.isRemote)
				{
					SpectreHandler.getInstance().teleportPlayerBack((EntityPlayerMP) playerIn);
				}
				return true;
			}
		}

		return super.onBlockActivated(worldIn, pos, state, playerIn, hand, side, hitX, hitY, hitZ);
	}
}
