package lumien.randomthings.block;

import java.util.List;

import lumien.randomthings.item.block.ItemBlockBiomeStone;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockBiomeStone extends BlockBase
{
	public static final PropertyEnum VARIANT = PropertyEnum.create("variant", BlockBiomeStone.EnumType.class);

	protected BlockBiomeStone()
	{
		super("biomeStone", Material.rock, ItemBlockBiomeStone.class);

		this.setHardness(1.5F);
		this.setResistance(10.0F);
		this.setStepSound(soundTypePiston);
		this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, BlockBiomeStone.EnumType.COBBLE));
	}

	@Override
	public int damageDropped(IBlockState state)
	{
		return ((BlockBiomeStone.EnumType) state.getValue(VARIANT)).getMetadata();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List list)
	{
		BlockBiomeStone.EnumType[] aenumtype = BlockBiomeStone.EnumType.values();
		int i = aenumtype.length;

		for (int j = 0; j < i; ++j)
		{
			BlockBiomeStone.EnumType enumtype = aenumtype[j];
			list.add(new ItemStack(itemIn, 1, enumtype.getMetadata()));
		}
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(VARIANT, BlockBiomeStone.EnumType.byMetadata(meta));
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return ((BlockBiomeStone.EnumType) state.getValue(VARIANT)).getMetadata();
	}

	@Override
	protected BlockState createBlockState()
	{
		return new BlockState(this, new IProperty[] { VARIANT });
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass)
	{
		int foliageColor = BiomeColorHelper.getFoliageColorAtPos(worldIn, pos);
		int waterColor = BiomeColorHelper.getWaterColorAtPos(worldIn, pos);
		int grassColor = BiomeColorHelper.getGrassColorAtPos(worldIn, pos);

		return foliageColor;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderColor(IBlockState state)
	{
		return getBlockColor();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getBlockColor()
	{
		EntityPlayerSP thePlayer = FMLClientHandler.instance().getClientPlayerEntity();
		WorldClient theWorld = FMLClientHandler.instance().getWorldClient();
		BlockPos pos = new BlockPos(thePlayer.posX, thePlayer.posY, thePlayer.posZ);

		int foliageColor = BiomeColorHelper.getFoliageColorAtPos(theWorld, pos);
		int waterColor = BiomeColorHelper.getWaterColorAtPos(theWorld, pos);
		int grassColor = BiomeColorHelper.getGrassColorAtPos(theWorld, pos);

		return foliageColor;
	}

	public static enum EnumType implements IStringSerializable
	{
		COBBLE(0, "cobble"), STONE(1, "smooth"), BRICK(2, "brick"), CRACKED(3, "cracked"), CHISELED(4, "chiseled");
		private static final BlockBiomeStone.EnumType[] META_LOOKUP = new BlockBiomeStone.EnumType[values().length];
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

		public static BlockBiomeStone.EnumType byMetadata(int meta)
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
			BlockBiomeStone.EnumType[] var0 = values();
			int var1 = var0.length;

			for (int var2 = 0; var2 < var1; ++var2)
			{
				BlockBiomeStone.EnumType var3 = var0[var2];
				META_LOOKUP[var3.getMetadata()] = var3;
			}
		}
	}
}
