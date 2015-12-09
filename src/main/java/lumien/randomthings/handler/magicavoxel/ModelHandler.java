package lumien.randomthings.handler.magicavoxel;

import java.io.File;
import java.io.FileFilter;
import java.util.Set;

import org.apache.logging.log4j.Level;

import lumien.randomthings.RandomThings;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.relauncher.Side;

public class ModelHandler
{
	Side side;

	ModelLibrary modelLibrary;

	public ModelHandler(Side side)
	{
		this.side = side;
		modelLibrary = new ModelLibrary();
	}
	
	public Set<String> getModels()
	{
		return modelLibrary.getModels();
	}

	public void load()
	{
		File modelFolder = new File("voxmodels/");

		if (modelFolder.exists())
		{
			File[] voxList = modelFolder.listFiles(new FileFilter()
			{
				@Override
				public boolean accept(File arg0)
				{
					return arg0.isFile() && arg0.getName().endsWith(".vox");
				}
			});

			if (voxList != null)
			{
				for (File f : voxList)
				{
					String modelName = f.getName().substring(0, f.getName().length() - 4);
					RandomThings.instance.logger.log(Level.DEBUG, "[VOX] Attempting to load " + f.getName());

					File paletteFile = new File(modelFolder, modelName + ".act");

					if (paletteFile.exists() && paletteFile.isFile())
					{
						try
						{
							MagicaVoxelModel model = MagicaVoxelLoader.getModel(f, paletteFile);
							
							modelLibrary.addModel(modelName, model);
						}
						catch (Exception e)
						{
							RandomThings.instance.logger.log(Level.DEBUG, "[VOX] ERROR Loading Model");
							e.printStackTrace();
						}
					}
					else
					{
						RandomThings.instance.logger.log(Level.DEBUG, "[VOX] No Palette file available (" + paletteFile.getName() + ")");
					}
				}
			}
		}
	}

	public MagicaVoxelModel getModel(String string)
	{
		return modelLibrary.modelMap.get(string);
	}

}
