package lumien.randomthings.block;

import java.awt.Color;

import lumien.randomthings.item.block.ItemBlockColored;
import lumien.randomthings.lib.IRTBlockColor;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockBiomeGlass extends BlockBase implements IRTBlockColor
{
	protected BlockBiomeGlass()
	{
		super("biomeGlass", Material.GROUND, ItemBlockColored.class);

		this.setSoundType(SoundType.GLASS);
		this.setHardness(0.3f);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.TRANSLUCENT;
	}

	@Override
	public boolean isFullBlock(IBlockState state)
	{
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int colorMultiplier(IBlockState state, IBlockAccess access, BlockPos pos, int tintIndex)
	{
		if (pos == null)
		{
			return Color.WHITE.getRGB();
		}
		int foliageColor = BiomeColorHelper.getFoliageColorAtPos(access, pos);
		int waterColor = BiomeColorHelper.getWaterColorAtPos(access, pos);
		int grassColor = BiomeColorHelper.getGrassColorAtPos(access, pos);

		return foliageColor;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockState state, IBlockAccess worldIn, BlockPos pos, EnumFacing side)
	{
		IBlockState iblockstate = worldIn.getBlockState(pos.offset(side));
		Block block = iblockstate.getBlock();

		if (state != iblockstate)
		{
			return true;
		}

		if (block == this)
		{
			return false;
		}

		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
}
