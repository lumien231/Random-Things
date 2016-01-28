package lumien.randomthings.block;

import java.util.List;

import lumien.randomthings.item.block.ItemBlockPlatform;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockPlatform extends BlockBase
{
	public static final PropertyEnum VARIANT = PropertyEnum.create("variant", BlockPlatform.EnumType.class);

	public BlockPlatform()
	{
		super("platform", Material.wood, ItemBlockPlatform.class);

		this.setBlockBounds(0, 14F / 16F, 0, 1, 1, 1);
		this.setHardness(1.5F);
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	public boolean isFullCube()
	{
		return false;
	}

	@Override
	public boolean isSideSolid(IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		return side == EnumFacing.UP;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List list)
	{
		BlockPlatform.EnumType[] aenumtype = BlockPlatform.EnumType.values();
		int i = aenumtype.length;

		for (int j = 0; j < i; ++j)
		{
			BlockPlatform.EnumType enumtype = aenumtype[j];
			list.add(new ItemStack(itemIn, 1, enumtype.getMetadata()));
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
	protected BlockState createBlockState()
	{
		return new BlockState(this, new IProperty[] { VARIANT });
	}

	@Override
	public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List list, Entity collidingEntity)
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

		super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
	}

	public static enum EnumType implements IStringSerializable
	{
		OAK(0, "oak"), SPRUCE(1, "spruce"), BIRCH(2, "birch"), JUNGLE(3, "jungle"), ACACIA(4, "acacia"), DARK_OAK(5, "darkOak");
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
