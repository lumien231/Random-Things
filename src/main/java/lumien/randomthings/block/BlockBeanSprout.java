package lumien.randomthings.block;

import java.util.Random;

import lumien.randomthings.item.ModItems;
import lumien.randomthings.util.WorldUtil;
import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockBeanSprout extends BlockBush implements IGrowable, IPlantable
{
	protected static final AxisAlignedBB AABB_1 = new AxisAlignedBB(0.30000001192092896D, 0.0D, 0.30000001192092896D, 0.699999988079071D, 0.3000000238418579D, 0.699999988079071D);
	protected static final AxisAlignedBB AABB_2 = new AxisAlignedBB(0.30000001192092896D, 0.0D, 0.30000001192092896D, 0.699999988079071D, 0.6000000238418579D, 0.699999988079071D);

	public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 7);

	public BlockBeanSprout()
	{
		this.setUnlocalizedName("beanSprout");

		this.setRegistryName(new ResourceLocation("randomthings", "beanSprout"));
		ForgeRegistries.BLOCKS.register(this);

		this.setSoundType(SoundType.PLANT);
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		int age = state.getValue(AGE);

		if (age == 7)
		{
			if (!worldIn.isRemote)
			{
				worldIn.setBlockState(pos, state.withProperty(AGE, 0));

				Random rand = new Random();
				int beans = rand.nextInt(2) + 1;

				ItemStack stack = new ItemStack(ModItems.beans, beans);

				if (!playerIn.addItemStackToInventory(stack))
				{
					WorldUtil.spawnItemStack(worldIn, pos, stack);
				}
			}

			return true;
		}
		else
		{
			return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
		}
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		if (state.getValue(AGE) == 7)
		{
			return AABB_2;
		}
		else
		{
			return AABB_1;
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(CreativeTabs tab, NonNullList list)
	{

	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(AGE, Integer.valueOf(meta));
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(AGE).intValue();
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] { AGE });
	}

	@Override
	public java.util.List<ItemStack> getDrops(net.minecraft.world.IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
		java.util.List<ItemStack> ret = super.getDrops(world, pos, state, fortune);

		int age = state.getValue(AGE).intValue();
		Random rand = world instanceof World ? ((World) world).rand : new Random();

		if (age >= 7)
		{
			ret.add(new ItemStack(ModItems.beans, rand.nextInt(2) + 2, 0));
		}
		return ret;
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		super.updateTick(worldIn, pos, state, rand);

		if (worldIn.getLightFromNeighbors(pos.up()) >= 9)
		{
			int i = state.getValue(AGE).intValue();

			if (i < 7)
			{
				float f = 25;

				if (rand.nextInt((int) (25.0F / f) + 1) == 0)
				{
					worldIn.setBlockState(pos, state.withProperty(AGE, Integer.valueOf(i + 1)), 2);
				}
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return ModItems.beans;
	}

	@Override
	public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state)
	{
		return (worldIn.getLight(pos) >= 8 || worldIn.canSeeSky(pos)) && worldIn.getBlockState(pos.down()).getBlock().canSustainPlant(worldIn.getBlockState(pos.down()), worldIn, pos.down(), net.minecraft.util.EnumFacing.UP, this);
	}

	@Override
	public net.minecraftforge.common.EnumPlantType getPlantType(net.minecraft.world.IBlockAccess world, BlockPos pos)
	{
		return EnumPlantType.Plains;
	}

	@Override
	public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient)
	{
		return state.getValue(AGE).intValue() < 7;
	}

	@Override
	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state)
	{
		return true;
	}

	@Override
	public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state)
	{
		int i = state.getValue(AGE).intValue() + MathHelper.getInt(worldIn.rand, 2, 5);

		if (i > 7)
		{
			i = 7;
		}

		worldIn.setBlockState(pos, state.withProperty(AGE, Integer.valueOf(i)), 2);
	}
}
