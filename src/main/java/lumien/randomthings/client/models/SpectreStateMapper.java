package lumien.randomthings.client.models;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.DefaultStateMapper;

public class SpectreStateMapper extends DefaultStateMapper
{
    protected ModelResourceLocation getModelResourceLocation(IBlockState state)
    {
        return new ModelResourceLocation("randomthings:spectreCoil", this.getPropertyString(state.getProperties()));
    }
}
