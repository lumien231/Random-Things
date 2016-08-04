package lumien.randomthings.handler.magicavoxel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.Level;

import lumien.randomthings.RandomThings;
import lumien.randomthings.config.Numbers;
import lumien.randomthings.network.PacketHandler;
import lumien.randomthings.network.messages.magicavoxel.MessageModelData;
import lumien.randomthings.network.messages.magicavoxel.MessageModelRequestUpdate;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class ServerModelLibrary
{
	Map<String, LoadedModelFile> loadedModels;
	Map<NetHandlerPlayServer, ServerModelRequest> modelRequests;

	static ServerModelLibrary INSTANCE;

	public ServerModelLibrary()
	{
		loadedModels = new HashMap<String, LoadedModelFile>();
		modelRequests = new WeakHashMap<NetHandlerPlayServer, ServerModelRequest>();
	}

	public static ServerModelLibrary getInstance()
	{
		if (INSTANCE == null)
		{
			INSTANCE = new ServerModelLibrary();
		}

		return INSTANCE;
	}

	public List<String> getModelList()
	{
		return new ArrayList<String>(loadedModels.keySet());
	}

	public void tick()
	{
		Iterator<ServerModelRequest> iterator = modelRequests.values().iterator();

		int bytesAllowed = Numbers.MODEL_TRANSFER_BANDWIDTH;

		if (!modelRequests.isEmpty())
		{
			int bytesPerRequest = bytesAllowed / modelRequests.size();

			if (bytesPerRequest <= 0)
			{
				RandomThings.instance.logger.log(Level.ERROR, "I have 1000 model requests? Probably a bug so please report! :)");
			}

			while (iterator.hasNext())
			{
				ServerModelRequest request = iterator.next();

				if (request.netHandler.getNetworkManager().isChannelOpen())
				{
					int progress = request.bytesSend;

					LoadedModelFile modelFile = loadedModels.get(request.modelName);

					if (modelFile == null)
					{
						iterator.remove();
						RandomThings.instance.logger.log(Level.ERROR, "Expected Model request for " + request.modelName + " to exist on the server");
					}
					else
					{
						byte[] toSend = null;
						if (progress < modelFile.modelData.length)
						{
							toSend = new byte[Math.min(bytesPerRequest, modelFile.modelData.length - progress)];

							for (int i = 0; i < toSend.length; i++)
							{
								toSend[i] = modelFile.modelData[progress + i];
							}

							progress += toSend.length;
						}
						else if (progress < modelFile.modelData.length + modelFile.paletteData.length)
						{
							toSend = new byte[Math.min(bytesPerRequest, modelFile.paletteData.length - (progress - modelFile.modelData.length))];

							for (int i = 0; i < toSend.length; i++)
							{
								toSend[i] = modelFile.paletteData[progress - modelFile.modelData.length + i];
							}

							progress += toSend.length;
						}
						else
						{
							iterator.remove();
						}

						request.bytesSend = progress;

						if (toSend != null)
						{
							MessageModelData message = new MessageModelData(request.modelName, toSend.length, toSend);
							request.netHandler.sendPacket(PacketHandler.INSTANCE.getPacketFrom(message));
						}
					}
				}
				else
				{
					iterator.remove();
				}
			}
		}
	}

	public void load()
	{
		File modelFolder = new File(FMLCommonHandler.instance().getMinecraftServerInstance().getDataDirectory(), "voxmodels");

		if (modelFolder.isDirectory())
		{
			File[] modelFiles = modelFolder.listFiles(new FilenameFilter()
			{
				@Override
				public boolean accept(File dir, String name)
				{
					return name.endsWith(".vox") && new File(dir, name.substring(0, name.length() - 3) + "act").isFile();
				}
			});

			for (File modelFile : modelFiles)
			{
				String modelName = modelFile.getName().substring(0, modelFile.getName().length() - 4);
				File paletteFile = new File(modelFolder, modelName + ".act");

				if (modelFile.length() <= 2000 * 1000 && paletteFile.length() <= 2000 * 1000)
				{
					try
					{
						FileInputStream modelInputStream = new FileInputStream(modelFile);
						FileInputStream paletteInputStream = new FileInputStream(paletteFile);

						LoadedModelFile loadedFile = new LoadedModelFile(IOUtils.toByteArray(modelInputStream), IOUtils.toByteArray(paletteInputStream));

						loadedModels.put(modelName, loadedFile);
					}
					catch (Exception e)
					{
						RandomThings.instance.logger.log(Level.ERROR, "Error loading magica voxel model " + modelFile.getName());
						e.printStackTrace();
					}
				}
				else
				{
					RandomThings.instance.logger.log(Level.ERROR, "Model file too large (Has to be smaller than 2mb) " + modelFile.getName());
				}
			}
		}
	}

	public void refresh()
	{
		loadedModels = new HashMap<String, LoadedModelFile>();
		modelRequests = new WeakHashMap<NetHandlerPlayServer, ServerModelRequest>();
		load();
	}

	public class LoadedModelFile
	{
		Byte[] modelData;
		Byte[] paletteData;

		public LoadedModelFile(byte[] modelData, byte[] paletteData)
		{
			this.modelData = ArrayUtils.toObject(modelData);
			this.paletteData = ArrayUtils.toObject(paletteData);
		}

		public Byte[] getModelData()
		{
			return this.modelData;
		}

		public Byte[] getPaletteData()
		{
			return this.paletteData;
		}
	}

	public void requestModel(NetHandlerPlayServer serverHandler, String modelName)
	{
		MessageModelRequestUpdate updateMessage = new MessageModelRequestUpdate();
		if (loadedModels.containsKey(modelName))
		{
			LoadedModelFile modelFile = loadedModels.get(modelName);
			updateMessage.setData(modelName, modelFile.modelData.length, modelFile.paletteData.length);

			ServerModelRequest request = new ServerModelRequest(modelName, serverHandler);

			modelRequests.put(serverHandler, request);
		}
		else
		{
			updateMessage.setData(modelName, -1, -1);
		}

		serverHandler.sendPacket(PacketHandler.INSTANCE.getPacketFrom(updateMessage));
	}
}
