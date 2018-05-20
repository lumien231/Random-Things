package lumien.randomthings.block;

import java.util.Random;

import lumien.randomthings.handler.runes.EnumRuneDust;
import lumien.randomthings.item.ModItems;
import lumien.randomthings.lib.ILuminousBlock;
import lumien.randomthings.lib.INoItem;
import lumien.randomthings.lib.IRTBlockColor;
import lumien.randomthings.tileentity.TileEntityRuneBase;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockRuneBase extends BlockContainerBase implements IRTBlockColor, ILuminousBlock, INoItem
{
	protected static final AxisAlignedBB AABB = new AxisAlignedBB(0D, 0.0D, 0D, 1D, 0.005D, 1D);

	public static final RuneDataProperty RUNE_DATA = new RuneDataProperty();
	public static final ConnectionDataProperty CONNECTION_DATA = new ConnectionDataProperty();

	protected BlockRuneBase()
	{
		super("runeBase", Material.SAND);

		this.setHardness(0.2f);
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
	{
		return ItemStack.EMPTY;
	}

	@Override
	public boolean addHitEffects(IBlockState state, World worldObj, RayTraceResult target, ParticleManager manager)
	{
		return true;
	}

	@Override
	public boolean addDestroyEffects(World world, BlockPos pos, ParticleManager manager)
	{
		return true;
	}

	@Override
	public boolean addLandingEffects(IBlockState state, WorldServer worldObj, BlockPos blockPosition, IBlockState iblockstate, EntityLivingBase entity, int numberOfParticles)
	{
		return true;
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block neighborBlock, BlockPos changedPos)
	{
		IBlockState downState = worldIn.getBlockState(pos.down());
		if (!downState.isSideSolid(worldIn, pos.down(), EnumFacing.UP))
		{
			worldIn.setBlockToAir(pos);
		}
	}

	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list)
	{

	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		TileEntity te = worldIn.getTileEntity(pos);

		if (te instanceof TileEntityRuneBase)
		{
			TileEntityRuneBase runeTE = (TileEntityRuneBase) te;

			int[][] runeData = runeTE.getRuneData();

			for (int x = 0; x < runeData.length; x++)
			{
				for (int y = 0; y < runeData[0].length; y++)
				{
					int rune = runeData[x][y];

					if (rune != -1)
					{
						ItemStack dustStack = new ItemStack(ModItems.runeDust, 1, rune);

						EntityItem entityitem = new EntityItem(worldIn, (double) pos.getX() + x / 4F, pos.getY() + 0.1, (double) pos.getZ() + y / 4F, dustStack);
						entityitem.setNoPickupDelay();
						worldIn.spawnEntity(entityitem);
					}
				}
			}
		}

		super.breakBlock(worldIn, pos, state);
	}

	@Override
	public int quantityDropped(IBlockState state, int fortune, Random random)
	{
		return 0;
	}

	@Override
	public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn)
	{
		if (!worldIn.isRemote)
		{
			Vec3d start = playerIn.getPositionEyes(0);
			RayTraceResult result = worldIn.rayTraceBlocks(start, start.add(playerIn.getLookVec().scale(6)), false, true, false);

			if (result != null && result.typeOfHit == RayTraceResult.Type.BLOCK)
			{
				BlockPos hitPos = result.getBlockPos();

				if (hitPos.equals(pos))
				{
					Vec3d hitVec = result.hitVec.subtract(new Vec3d(pos));

					TileEntityRuneBase te = (TileEntityRuneBase) worldIn.getTileEntity(pos);

					int[][] runeData = te.getRuneData();

					int x = (int) Math.floor(hitVec.x * 4);
					int y = (int) Math.floor(hitVec.z * 4);

					if (runeData[x][y] != -1)
					{
						EntityItem entityitem = new EntityItem(worldIn, pos.getX() + hitVec.x, pos.getY() + 0.1, pos.getZ() + hitVec.z, new ItemStack(ModItems.runeDust, 1, runeData[x][y]));
						entityitem.setNoPickupDelay();
						worldIn.spawnEntity(entityitem);

						runeData[x][y] = -1;
						te.syncTE();

						worldIn.playSound(null, pos, SoundEvents.BLOCK_STONE_BREAK, SoundCategory.BLOCKS, 1F, 0.8F);

						boolean empty = true;
						for (x = 0; x < runeData.length; x++)
						{
							for (y = 0; y < runeData[0].length; y++)
							{
								int rune = runeData[x][y];

								if (rune != -1)
								{
									empty = false;

									break;
								}
							}

							if (!empty)
							{
								break;
							}
						}

						if (empty)
						{
							worldIn.setBlockToAir(pos);
						}
					}
				}
			}
		}

		super.onBlockClicked(worldIn, pos, playerIn);
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new TileEntityRuneBase();
	}

	@Override
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public boolean shouldGlow(IBlockState state, int tintIndex)
	{
		return true;
	}

	@Override
	public int colorMultiplier(IBlockState state, IBlockAccess p_186720_2_, BlockPos pos, int tintIndex)
	{
		return EnumRuneDust.getColor(tintIndex);
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
	public AxisAlignedBB getBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
	{
		return AABB;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos)
	{
		return AABB.offset(pos);
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new ExtendedBlockState(this, new IProperty[] {}, new IUnlistedProperty[] { RUNE_DATA, CONNECTION_DATA });
	}

	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
	{
		TileEntityRuneBase te = (TileEntityRuneBase) worldIn.getTileEntity(pos);
		IExtendedBlockState actualState = (IExtendedBlockState) state;

		if (te != null)
		{
			boolean[] connectionData = new boolean[16];
			int[][] runeData = te.getRuneData();

			for (EnumFacing facing : EnumFacing.HORIZONTALS)
			{
				BlockPos mod = pos.offset(facing);
				TileEntity otherTE = worldIn.getTileEntity(mod);

				if (otherTE instanceof TileEntityRuneBase)
				{
					TileEntityRuneBase otherRune = (TileEntityRuneBase) otherTE;

					int[][] otherRuneData = otherRune.getRuneData();

					for (int i = 0; i < 4; i++)
					{
						if (facing == EnumFacing.NORTH)
						{
							connectionData[i] = runeData[i][0] == otherRuneData[i][3];
						}
						else if (facing == EnumFacing.EAST)
						{
							connectionData[i + 4] = runeData[3][i] == otherRuneData[0][i];
						}
						else if (facing == EnumFacing.SOUTH)
						{
							connectionData[i + 8] = runeData[i][3] == otherRuneData[i][0];
						}
						else if (facing == EnumFacing.WEST)
						{
							connectionData[i + 12] = runeData[0][i] == otherRuneData[3][i];
						}
					}
				}
			}

			return actualState.withProperty(RUNE_DATA, runeData).withProperty(CONNECTION_DATA, connectionData);
		}
		else
		{
			return actualState.withProperty(RUNE_DATA, new int[4][4]);
		}
	}

	private static class RuneDataProperty implements IUnlistedProperty<int[][]>
	{
		@Override
		public String getName()
		{
			return "runedata";
		}

		@Override
		public boolean isValid(int[][] value)
		{
			return true;
		}

		@Override
		public Class<int[][]> getType()
		{
			return int[][].class;
		}

		@Override
		public String valueToString(int[][] value)
		{
			return value.toString();
		}

	}

	private static class ConnectionDataProperty implements IUnlistedProperty<boolean[]>
	{
		@Override
		public String getName()
		{
			return "runedata";
		}

		@Override
		public boolean isValid(boolean[] value)
		{
			return true;
		}

		@Override
		public Class<boolean[]> getType()
		{
			return boolean[].class;
		}

		@Override
		public String valueToString(boolean[] value)
		{
			return value.toString();
		}

	}
}
