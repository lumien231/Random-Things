package lumien.randomthings.recipes;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;

public abstract class SimpleRecipe implements IRecipe
{
	ResourceLocation registryName;

	public SimpleRecipe(ResourceLocation registryName)
	{
		this.registryName = registryName;
	}

	@Override
	public ResourceLocation getRegistryName()
	{
		return registryName;
	}

	@Override
	public IRecipe setRegistryName(ResourceLocation name)
	{
		registryName = name;
		return this;
	}

	@Override
	public Class<IRecipe> getRegistryType()
	{
		return IRecipe.class;
	}
}
