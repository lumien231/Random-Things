package lumien.randomthings.tileentity;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class TileEntityCustomWorkbench extends TileEntityBase
{
	Block woodMaterial;
	int woodMeta;

	IBlockState woodState;

	public TileEntityCustomWorkbench()
	{
		this.woodMaterial = Blocks.planks;
		this.woodMeta = 0;
		this.woodState = Blocks.planks.getDefaultState();
	}

	@Override
	public void writeDataToNBT(NBTTagCompound compound)
	{
		String woodMaterialName = woodMaterial.getRegistryName();
		compound.setString("woodMaterialName", woodMaterialName);

		compound.setInteger("woodMeta", woodMeta);
	}

	@Override
	public void readDataFromNBT(NBTTagCompound compound)
	{
		String woodMaterialName = compound.getString("woodMaterialName");
		woodMeta = compound.getInteger("woodMeta");

		woodMaterial = Block.getBlockFromName(woodMaterialName);

		if (woodMaterial == null)
		{
			woodMaterial = Blocks.planks;
			woodMeta = 0;
		}

		if (FMLCommonHandler.instance().getEffectiveSide().isClient())
		{
			woodState = woodMaterial.getStateFromMeta(woodMeta);

			if (woodState == null)
			{
				woodState = Blocks.planks.getDefaultState();
			}
		}
	}

	public IBlockState getWoodState()
	{
		return this.woodState;
	}

	@Override
	public boolean renderAfterData()
	{
		return true;
	}

	public void setWood(Block woodBlock, int meta)
	{
		this.woodMaterial = woodBlock;
		this.woodMeta = meta;
		
		if (woodMaterial == null)
		{
			woodMaterial = Blocks.planks;
			woodMeta = 0;
		}

		if (FMLCommonHandler.instance().getEffectiveSide().isClient())
		{
			woodState = woodMaterial.getStateFromMeta(woodMeta);

			if (woodState == null)
			{
				woodState = Blocks.planks.getDefaultState();
			}
		}
	}
}
