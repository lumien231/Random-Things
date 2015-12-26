package lumien.randomthings.block;

import lumien.randomthings.tileentity.TileEntityRod;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class BlockRod extends BlockContainerBase
{
	public static PropertyBool ACTIVE = PropertyBool.create("active");

	protected BlockRod(String type)
	{
		super(type + "Rod", Material.rock);

		float f = 1F / 16F * 2 / 2;
		this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 1F, 0.5F + f);
		this.setHardness(50.0F).setResistance(2000.0F);
		this.setHarvestLevel("pickaxe", 3);
		this.setDefaultState(this.getDefaultState().withProperty(ACTIVE, false));
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		if (!worldIn.isRemote && placer instanceof EntityPlayer)
		{
			TileEntityRod rod = (TileEntityRod) worldIn.getTileEntity(pos);
			
			rod.setOwner((EntityPlayer)placer);
		}
	}

	@Override
	protected BlockState createBlockState()
	{
		return new BlockState(this, new IProperty[] { ACTIVE });
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(ACTIVE) ? 1 : 0;
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		if (meta == 0)
		{
			return getDefaultState().withProperty(ACTIVE, false);
		}
		else
		{
			return getDefaultState().withProperty(ACTIVE, true);
		}
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
}
