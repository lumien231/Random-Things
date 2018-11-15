package lumien.randomthings.client.mesh;

import lumien.randomthings.item.ItemSpectreCharger;
import lumien.randomthings.item.ModItems;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class SpectreChargerMesh implements ItemMeshDefinition
{

	@Override
	public ModelResourceLocation getModelLocation(ItemStack stack)
	{
		NBTTagCompound options = stack.getSubCompound("options");
		boolean enabled = options != null && options.getBoolean("enabled");
		return new ModelResourceLocation("randomthings:spectrecharger/" + ItemSpectreCharger.TIER.values()[stack.getItemDamage()].getName() + (enabled ? "_enabled" : ""));
	}

}
