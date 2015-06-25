package lumien.randomthings.block;

import lumien.randomthings.item.block.ItemBlockColored;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockBiomeGlass extends BlockBase
{
	protected BlockBiomeGlass()
	{
		super("biomeGlass", Material.ground, ItemBlockColored.class);

		this.setStepSound(soundTypeGlass);
		this.setHardness(0.3f);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public EnumWorldBlockLayer getBlockLayer()
	{
		return EnumWorldBlockLayer.TRANSLUCENT;
	}

	@Override
	public boolean isFullCube()
	{
		return false;
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

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
	{
		IBlockState iblockstate = worldIn.getBlockState(pos);
		Block block = iblockstate.getBlock();

		if (worldIn.getBlockState(pos.offset(side.getOpposite())) != iblockstate)
		{
			return true;
		}

		if (block == this)
		{
			return false;
		}

		return block == this ? false : super.shouldSideBeRendered(worldIn, pos, side);
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}
}
