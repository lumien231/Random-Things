package lumien.randomthings.handler.magicavoxel;

import java.util.HashMap;
import java.util.Set;

public class ModelLibrary
{
	HashMap<String, MagicaVoxelModel> modelMap;

	public ModelLibrary()
	{
		modelMap = new HashMap<String, MagicaVoxelModel>();
	}

	public void addModel(String name, MagicaVoxelModel model)
	{
		this.modelMap.put(name, model);
	}
	
	public Set<String> getModels()
	{
		return modelMap.keySet();
	}
}
