package lumien.randomthings.block;

import java.util.Random;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockLapisLamp extends BlockBase
{
	protected BlockLapisLamp()
	{
		super("lapisLamp", Material.GROUND);

		this.setSoundType(SoundType.GLASS);
		this.setHardness(0.3F);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState state, World worldIn, BlockPos pos, Random rand)
	{
		if (worldIn.isRemote && worldIn.getLight(pos) == 0)
		{
			worldIn.checkLightFor(EnumSkyBlock.BLOCK, pos);
		}
	}

	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		if (state.getBlock() != this)
		{
			return state.getBlock().getLightValue(state, world, pos);
		}

		if (FMLCommonHandler.instance().getEffectiveSide().isClient())
		{
			return 15;
		}
		else
		{
			return 0;
		}
	}
}
