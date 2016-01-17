package lumien.randomthings.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockQuartzLamp extends BlockBase
{
	protected BlockQuartzLamp()
	{
		super("quartzLamp", Material.ground);

		this.setStepSound(soundTypeGlass);
		this.setHardness(0.3F);
	}

	@Override
	public int getLightValue(IBlockAccess world, BlockPos pos)
	{
		IBlockState state = world.getBlockState(pos);
		if (state.getBlock() != this)
		{
			return state.getBlock().getLightValue(world, pos);
		}

		if (FMLCommonHandler.instance().getEffectiveSide().isServer())
		{
			return 15;
		}
		else
		{
			return 0;
		}
	}
	
	@Override
	public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		if (worldIn.isRemote && worldIn.getLight(pos)==15)
		{
			worldIn.checkLightFor(EnumSkyBlock.BLOCK, pos);
		}
	}
}
