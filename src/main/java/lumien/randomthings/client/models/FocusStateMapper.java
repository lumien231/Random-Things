package lumien.randomthings.client.models;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.resources.model.ModelResourceLocation;

public class FocusStateMapper extends StateMapperBase
{
	ModelResourceLocation mrl;
	
	public FocusStateMapper(ModelResourceLocation mrl)
	{
		this.mrl = mrl;
	}
	
	@Override
	protected ModelResourceLocation getModelResourceLocation(IBlockState p_178132_1_)
	{
		return mrl;
	}
}
