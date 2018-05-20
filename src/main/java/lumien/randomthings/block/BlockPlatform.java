package lumien.randomthings.block;

import java.util.List;

import lumien.randomthings.item.block.ItemBlockPlatform;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockPlatform extends BlockBase
{
	public static final PropertyEnum VARIANT = PropertyEnum.create("variant", BlockPlatform.EnumType.class);

	protected static final AxisAlignedBB PLATFORM_AABB = new AxisAlignedBB(0, 14F / 16F, 0, 1, 1, 1);

	public BlockPlatform()
	{
		super("platform", Material.WOOD, ItemBlockPlatform.class);

		this.setHardness(1.5F);
	}

	@Override
	public int damageDropped(IBlockState state)
	{
		return getMetaFromState(state);
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
	public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		return side == EnumFacing.UP;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(CreativeTabs tab, NonNullList list)
	{
		BlockPlatform.EnumType[] aenumtype = BlockPlatform.EnumType.values();
		int i = aenumtype.length;

		for (int j = 0; j < i; ++j)
		{
			BlockPlatform.EnumType enumtype = aenumtype[j];
			list.add(new ItemStack(this, 1, enumtype.getMetadata()));
		}
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(VARIANT, BlockPlatform.EnumType.byMetadata(meta));
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return ((BlockPlatform.EnumType) state.getValue(VARIANT)).getMetadata();
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] { VARIANT });
	}

	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB mask, List list, Entity collidingEntity, boolean p_185477_7_)
	{
		if (collidingEntity != null)
		{
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

		super.addCollisionBoxToList(state, worldIn, pos, mask, list, collidingEntity, p_185477_7_);
	}

	public static enum EnumType implements IStringSerializable
	{
		OAK(0, "oak"), SPRUCE(1, "spruce"), BIRCH(2, "birch"), JUNGLE(3, "jungle"), ACACIA(4, "acacia"), DARK_OAK(5, "darkoak");
		private static final BlockPlatform.EnumType[] META_LOOKUP = new BlockPlatform.EnumType[values().length];
		private final int meta;
		private final String name;
		private final String unlocalizedName;

		private static final String __OBFID = "CL_00002058";

		private EnumType(int meta, String name)
		{
			this(meta, name, name);
		}

		private EnumType(int meta, String name, String unlocalizedName)
		{
			this.meta = meta;
			this.name = name;
			this.unlocalizedName = unlocalizedName;
		}

		public int getMetadata()
		{
			return this.meta;
		}

		@Override
		public String toString()
		{
			return this.name;
		}

		public static BlockPlatform.EnumType byMetadata(int meta)
		{
			if (meta < 0 || meta >= META_LOOKUP.length)
			{
				meta = 0;
			}

			return META_LOOKUP[meta];
		}

		@Override
		public String getName()
		{
			return this.name;
		}

		public String getUnlocalizedName()
		{
			return this.unlocalizedName;
		}

		static
		{
			BlockPlatform.EnumType[] var0 = values();
			int var1 = var0.length;

			for (int var2 = 0; var2 < var1; ++var2)
			{
				BlockPlatform.EnumType var3 = var0[var2];
				META_LOOKUP[var3.getMetadata()] = var3;
			}
		}
	}
}
