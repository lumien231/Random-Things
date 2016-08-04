package lumien.randomthings.handler.magicavoxel;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.Level;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;

import lumien.randomthings.RandomThings;
import lumien.randomthings.config.Features;
import lumien.randomthings.handler.magicavoxel.ClientModelRequest.STATE;
import lumien.randomthings.network.PacketHandler;
import lumien.randomthings.network.messages.magicavoxel.MessageModelRequest;
import net.minecraft.client.Minecraft;

public class ClientModelLibrary
{
	Timer cleanUpTimer;

	Cache<String, MagicaVoxelModel> modelCache;

	Map<String, ClientModelRequest> modelRequests;

	static ClientModelLibrary INSTANCE;

	public ClientModelLibrary()
	{
		CacheBuilder cacheBuilder = CacheBuilder.newBuilder();
		cacheBuilder.expireAfterAccess(5, TimeUnit.MINUTES);

		cacheBuilder.removalListener(new RemovalListener<String, MagicaVoxelModel>()
		{
			@Override
			public void onRemoval(RemovalNotification<String, MagicaVoxelModel> notification)
			{
				MagicaVoxelModel model = notification.getValue();

				model.cleanUp();
			}
		});

		cleanUpTimer = new Timer("ModelCleanupTimer", true);

		cleanUpTimer.scheduleAtFixedRate(new TimerTask()
		{
			@Override
			public void run()
			{
				Minecraft.getMinecraft().addScheduledTask(new Runnable()
				{
					@Override
					public void run()
					{
						ClientModelLibrary.getInstance().modelCache.cleanUp();
					}
				});
			}
		}, 1000, 10000);

		modelCache = cacheBuilder.build();
		modelRequests = new HashMap<String, ClientModelRequest>();
	}

	private MagicaVoxelModel loadModelFromFile(String modelName)
	{
		File modelFolder = new File(Minecraft.getMinecraft().mcDataDir, "voxmodels");

		if (modelFolder.isDirectory())
		{
			File modelRequested = new File(modelFolder, modelName + ".vox");
			File paletteRequested = new File(modelFolder, modelName + ".act");

			if (modelRequested.isFile() && paletteRequested.isFile())
			{
				try
				{
					return MagicaVoxelLoader.getModel(modelRequested, paletteRequested);
				}
				catch (Exception e)
				{
					RandomThings.instance.logger.log(Level.ERROR, "Error loading model from file: " + modelName);
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public MagicaVoxelModel getModel(String modelName)
	{
		MagicaVoxelModel model;
		if ((model = modelCache.getIfPresent(modelName)) != null)
		{
			return model;
		}
		else
		{
			if (modelRequests.containsKey(modelName))
			{
				ClientModelRequest request = modelRequests.get(modelName);

				if (request.getState() == STATE.FINISHED)
				{
					try
					{
						model = MagicaVoxelLoader.getModel(new ByteArrayInputStream(request.modelData), new ByteArrayInputStream(request.paletteData));
						modelCache.put(modelName, model);
						modelRequests.remove(modelName);
					}
					catch (Exception e)
					{
						RandomThings.instance.logger.log(Level.ERROR, "Error loading model from server: " + modelName);
						e.printStackTrace();

						if (model != null)
						{
							model.cleanUp();
						}

						request.state = STATE.NOT_AVAILABLE;
						request.modelData = null;
						request.paletteData = null;

						return null;
					}

					if (Features.MODEL_CLIENT_SAVING)
					{
						File modelFolder = new File(Minecraft.getMinecraft().mcDataDir, "voxmodels");

						if (!modelFolder.exists())
						{
							modelFolder.mkdir();
						}

						if (modelFolder.isDirectory())
						{
							try
							{
								File modelFile = new File(modelFolder, modelName + ".vox");
								File paletteFile = new File(modelFolder, modelName + ".act");

								if (!modelFile.exists())
								{
									FileOutputStream modelOutputStream = new FileOutputStream(modelFile);
									modelOutputStream.write(request.modelData);
									modelOutputStream.close();
								}

								if (!paletteFile.exists())
								{
									FileOutputStream paletteOutputStream = new FileOutputStream(paletteFile);
									paletteOutputStream.write(request.paletteData);
									paletteOutputStream.close();
								}
							}
							catch (Exception e)
							{
								RandomThings.instance.logger.log(Level.ERROR, "Error saving " + modelName + " to File");
								e.printStackTrace();
							}
						}
					}

					return model;
				}
				else
				{
					return null;
				}
			}
			else
			{
				if ((model = loadModelFromFile(modelName)) != null)
				{
					if (modelRequests.containsKey(modelName))
					{
						modelRequests.remove(modelName);
					}

					RandomThings.instance.logger.log(Level.DEBUG, "Loaded " + modelName + " from File");

					modelCache.put(modelName, model);
					return model;
				}
				else
				{
					RandomThings.instance.logger.log(Level.DEBUG, "Requesting " + modelName + " from Server");

					ClientModelRequest request = new ClientModelRequest();
					request.setState(STATE.SEND_REQUEST);

					MessageModelRequest requestMessage = new MessageModelRequest(modelName);

					PacketHandler.INSTANCE.sendToServer(requestMessage);

					modelRequests.put(modelName, request);
				}
			}

		}
		return null;
	}

	public static ClientModelLibrary getInstance()
	{
		if (INSTANCE == null)
		{
			INSTANCE = new ClientModelLibrary();
		}

		return INSTANCE;
	}

	public void updateRequest(String modelName, int modelSize, int paletteSize)
	{
		if (this.modelRequests.containsKey(modelName))
		{
			ClientModelRequest request = this.modelRequests.get(modelName);

			request.updateRequest(modelSize, paletteSize);
		}
	}

	public void reset()
	{
		this.modelRequests.clear();
		this.modelCache.invalidateAll();
	}

	public void addModelData(String modelName, byte[] data)
	{
		if (modelRequests.containsKey(modelName))
		{
			ClientModelRequest request = modelRequests.get(modelName);

			if (request.getState() != STATE.RECEIVING)
			{
				RandomThings.instance.logger.log(Level.ERROR, "Receiving data for invalid request state: " + request.getState());
			}
			int progress = request.bytesReceived;

			if (progress < request.modelData.length)
			{
				for (int i = 0; i < data.length; i++)
				{
					request.modelData[progress + i] = data[i];
				}
			}
			else if (progress < request.modelData.length + request.paletteData.length)
			{
				for (int i = 0; i < data.length; i++)
				{
					request.paletteData[progress - request.modelSize + i] = data[i];
				}
			}

			progress += data.length;
			request.bytesReceived = progress;

			if (progress >= request.modelSize + request.paletteSize)
			{
				request.setState(STATE.FINISHED);
			}
		}
	}
}
