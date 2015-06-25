package lumien.randomthings.block;

import java.util.List;
import java.util.Random;

import lumien.randomthings.RandomThings;
import lumien.randomthings.item.block.ItemBlockColoredGrass;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockColoredGrass extends Block
{
	public static final PropertyEnum COLOR = PropertyEnum.create("color", EnumDyeColor.class);

	public BlockColoredGrass()
	{
		super(Material.grass);

		this.setHardness(0.6F);
		this.setStepSound(soundTypeGrass);
		this.setCreativeTab(RandomThings.instance.creativeTab);
		this.setDefaultState(this.blockState.getBaseState().withProperty(COLOR, EnumDyeColor.WHITE));
		this.setUnlocalizedName("coloredGrass");
		this.setTickRandomly(true);

		GameRegistry.registerBlock(this, ItemBlockColoredGrass.class, "coloredGrass");
	}

	@Override
	public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player)
	{
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderColor(IBlockState state)
	{
		EnumDyeColor color = (EnumDyeColor) state.getValue(COLOR);
		return ItemDye.dyeColors[color.getDyeDamage()];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass)
	{
		EnumDyeColor color = (EnumDyeColor) worldIn.getBlockState(pos).getValue(COLOR);
		return ItemDye.dyeColors[color.getDyeDamage()];
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return Blocks.dirt.getItemDropped(Blocks.dirt.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT), rand, fortune);
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		if (!worldIn.isRemote)
		{
			if (worldIn.getLightFromNeighbors(pos.up()) < 4 && worldIn.getBlockState(pos.up()).getBlock().getLightOpacity(worldIn, pos.up()) > 2)
			{
				worldIn.setBlockState(pos, Blocks.dirt.getDefaultState());
			}
			else
			{
				if (worldIn.getLightFromNeighbors(pos.up()) >= 9)
				{
					for (int i = 0; i < 4; ++i)
					{
						BlockPos blockpos1 = pos.add(rand.nextInt(3) - 1, rand.nextInt(5) - 3, rand.nextInt(3) - 1);
						Block block = worldIn.getBlockState(blockpos1.up()).getBlock();
						IBlockState iblockstate1 = worldIn.getBlockState(blockpos1);

						if (iblockstate1.getBlock() == Blocks.dirt && iblockstate1.getValue(BlockDirt.VARIANT) == BlockDirt.DirtType.DIRT && worldIn.getLightFromNeighbors(blockpos1.up()) >= 4 && block.getLightOpacity(worldIn, blockpos1.up()) <= 2)
						{
							worldIn.setBlockState(blockpos1, state);
						}
					}
				}
			}
		}
	}

	@Override
	public int damageDropped(IBlockState state)
	{
		return 0;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List list)
	{
		EnumDyeColor[] aenumdyecolor = EnumDyeColor.values();
		int i = aenumdyecolor.length;

		for (int j = 0; j < i; ++j)
		{
			EnumDyeColor enumdyecolor = aenumdyecolor[j];
			list.add(new ItemStack(itemIn, 1, enumdyecolor.getMetadata()));
		}
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(COLOR, EnumDyeColor.byMetadata(meta));
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return ((EnumDyeColor) state.getValue(COLOR)).getMetadata();
	}

	@Override
	protected BlockState createBlockState()
	{
		return new BlockState(this, new IProperty[] { COLOR });
	}

	@Override
	@SideOnly(Side.CLIENT)
	public EnumWorldBlockLayer getBlockLayer()
	{
		return EnumWorldBlockLayer.CUTOUT_MIPPED;
	}
}
