package lumien.randomthings.tileentity;

import java.util.HashMap;
import java.util.Map;

import lumien.randomthings.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityBlockDiaphanous extends TileEntityBase
{
	IBlockState toDisplay = Blocks.STONE.getDefaultState();

	HashMap<EnumFacing, Boolean> renderMap;
	
	boolean isItem = false;
	boolean inverted = false;

	public TileEntityBlockDiaphanous()
	{
		renderMap = new HashMap<EnumFacing, Boolean>();

		for (EnumFacing facing : EnumFacing.VALUES)
		{
			renderMap.put(facing, true);
		}
	}
	
	public void setItem()
	{
		this.isItem = true;
	}
	
	public boolean isItem()
	{
		return isItem;
	}
	
	public IBlockState getDisplayState()
	{
		return toDisplay;
	}

	@Override
	public boolean syncAdditionalData()
	{
		return true;
	}
	
	public void setInverted(boolean inverted)
	{
		this.inverted = inverted;
	}
	
	public boolean isInverted()
	{
		return inverted;
	}

	@Override
	public void writeDataToNBT(NBTTagCompound compound, boolean sync)
	{
		for (EnumFacing facing : EnumFacing.VALUES)
		{
			compound.setBoolean(facing.toString().toLowerCase(), renderMap.get(facing));
		}

		compound.setString("block", toDisplay.getBlock().getRegistryName().toString());
		compound.setInteger("meta", toDisplay.getBlock().getMetaFromState(toDisplay));
		
		compound.setBoolean("inverted", inverted);
	}

	@Override
	public void readDataFromNBT(NBTTagCompound compound, boolean sync)
	{
		for (EnumFacing facing : EnumFacing.VALUES)
		{
			renderMap.put(facing, compound.getBoolean(facing.toString().toLowerCase()));
		}

		Block b = Block.REGISTRY.getObject(new ResourceLocation(compound.getString("block")));
		int meta = compound.getInteger("meta");

		try
		{
			toDisplay = b.getStateFromMeta(meta);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			toDisplay = Blocks.STONE.getDefaultState();
		}
		
		this.inverted = compound.getBoolean("inverted");
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block neighborBlock, BlockPos changedPos)
	{
		for (EnumFacing facing : EnumFacing.VALUES)
		{
			IBlockState neighbor = worldIn.getBlockState(pos.offset(facing));
			renderMap.put(facing, !neighbor.isSideSolid(worldIn, pos.offset(facing), facing.getOpposite()) && neighbor.getBlock() != ModBlocks.blockDiaphanous);
		}

		syncTE();
	}
	
	@Override
	public boolean shouldRenderInPass(int pass)
	{
		return pass == 1;
	}

	public boolean shouldRenderSide(EnumFacing facing)
	{
		return renderMap.get(facing);
	}

	public Map<EnumFacing, Boolean> getFacingMap()
	{
		return renderMap;
	}

	public void setDisplayState(IBlockState toDisplay)
	{
		this.toDisplay = toDisplay;
		syncTE();
	}
	
	public void setDisplayStateInternal(IBlockState toDisplay)
	{
		this.toDisplay = toDisplay;
	}
	
}
