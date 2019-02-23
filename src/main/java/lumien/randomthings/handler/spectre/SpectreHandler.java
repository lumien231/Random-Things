package lumien.randomthings.handler.spectre;

import java.util.HashMap;
import java.util.UUID;

import lumien.randomthings.config.Internals;
import lumien.randomthings.handler.ModDimensions;
import lumien.randomthings.util.PlayerUtil;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class SpectreHandler extends WorldSavedData
{
	public static final String ID = "SpectreHandler";

	private World worldObj;

	private HashMap<UUID, SpectreCube> cubes;

	private int positionCounter;

	public SpectreHandler(String name)
	{
		super(name);

		cubes = new HashMap<>();
		this.worldObj = DimensionManager.getWorld(Internals.SPECTRE_ID);

		positionCounter = 0;
	}

	public SpectreHandler()
	{
		this(ID);

		cubes = new HashMap<>();
		this.worldObj = DimensionManager.getWorld(Internals.SPECTRE_ID);

		positionCounter = 0;
	}

	public SpectreCube getSpectreCubeFromPos(World worldObj, BlockPos pos)
	{
		if (worldObj.provider.getDimension() != Internals.SPECTRE_ID)
		{
			return null;
		}
		else
		{
			if (pos.getZ() > 16 || pos.getZ() < 0)
			{
				return null;
			}

			Chunk c = worldObj.getChunkFromBlockCoords(pos);

			int position = c.x / 16;

			for (SpectreCube cube : cubes.values())
			{
				if (cube.position / 16 == position)
				{
					if (pos.getY() <= 0 || pos.getY() > cube.height + 1 || pos.getX() < position * 16 || pos.getX() > cube.position * 16 + 15)
					{
						return null;
					}
					else
					{
						return cube;
					}
				}
			}

			return null;
		}
	}

	public void teleportPlayerToSpectreCube(EntityPlayerMP player)
	{
		// Save Old Position / Dimension
		NBTTagCompound compound = player.getEntityData();
		compound.setDouble("spectrePosX", player.posX);
		compound.setDouble("spectrePosY", player.posY);
		compound.setDouble("spectrePosZ", player.posZ);
		compound.setInteger("spectreDimension", player.dimension);

		UUID uuid = player.getGameProfile().getId();
		SpectreCube spectreCube;

		if (cubes.containsKey(uuid))
		{
			spectreCube = cubes.get(uuid);
		}
		else
		{
			spectreCube = generateSpectreCube(uuid);
		}

		BlockPos spawn = spectreCube.getSpawnBlock();

		if (player.dimension != Internals.SPECTRE_ID)
		{
			PlayerUtil.teleportPlayerToDimension(player, Internals.SPECTRE_ID);
		}
		player.connection.setPlayerLocation(spawn.getX() + 0.5, spawn.getY() + 1, spawn.getZ() + 0.5, player.rotationYaw, player.rotationPitch);
	}

	private SpectreCube generateSpectreCube(UUID uuid)
	{
		SpectreCube cube = new SpectreCube(this, uuid, positionCounter);

		increaseNextPosition();

		cube.generate(worldObj);
		cubes.put(uuid, cube);
		this.markDirty();
		return cube;
	}

	private void increaseNextPosition()
	{
		positionCounter += 16;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		NBTTagList cubeTags = nbt.getTagList("cubes", (byte) 10);

		for (int i = 0; i < cubeTags.tagCount(); i++)
		{
			NBTTagCompound cubeCompound = cubeTags.getCompoundTagAt(i);
			SpectreCube cube = new SpectreCube(this);
			cube.readFromNBT(cubeCompound);
			this.cubes.put(cube.getOwner(), cube);
		}

		this.positionCounter = nbt.getInteger("positionCounter");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		NBTTagList cubeTags = new NBTTagList();

		for (SpectreCube cube : cubes.values())
		{
			NBTTagCompound cubeCompound = new NBTTagCompound();
			cube.writeToNBT(cubeCompound);
			cubeTags.appendTag(cubeCompound);
		}

		nbt.setTag("cubes", cubeTags);

		nbt.setInteger("positionCounter", positionCounter);

		return nbt;
	}

	public static SpectreHandler getInstance()
	{
		WorldServer world = DimensionManager.getWorld(Internals.SPECTRE_ID);
		if (world != null)
		{
			WorldSavedData handler = world.getPerWorldStorage().getOrLoadData(SpectreHandler.class, ID);
			if (handler == null)
			{
				handler = new SpectreHandler();
				world.getPerWorldStorage().setData(ID, handler);
			}

			return (SpectreHandler) handler;
		}

		return null;
	}

	public static void reset()
	{
		WorldServer world = DimensionManager.getWorld(Internals.SPECTRE_ID);
		if (world != null)
		{
			world.getMapStorage().setData(ID, new SpectreHandler());
		}
	}

	public void teleportPlayerBack(EntityPlayerMP player)
	{
		// Save Read Position / Dimension
		NBTTagCompound compound = player.getEntityData();
		if (compound.hasKey("spectrePosX"))
		{
			double spectrePosX = compound.getDouble("spectrePosX");
			double spectrePosY = compound.getDouble("spectrePosY");
			double spectrePosZ = compound.getDouble("spectrePosZ");
			int spectreDimension = compound.getInteger("spectreDimension");

			player.posX = spectrePosX;
			player.posY = spectrePosY;
			player.posZ = spectrePosZ;

			if (player.dimension != spectreDimension)
			{
				PlayerUtil.teleportPlayerToDimension(player, spectreDimension);
			}

			player.connection.setPlayerLocation(spectrePosX, spectrePosY, spectrePosZ, player.rotationYaw, player.rotationPitch);

			while (!player.world.getCollisionBoxes(player, player.getEntityBoundingBox()).isEmpty() && player.posY < 256.0D)
			{
				player.setPosition(player.posX, player.posY + 1.0D, player.posZ);
			}

			PlayerUtil.capturePosition(player.connection);
		}
		else
		{
			FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().recreatePlayerEntity(player, player.dimension, true);
		}
	}

	public void checkPosition(EntityPlayerMP player)
	{
		SpectreCube cube = getSpectreCubeFromPos(player.world, player.getPosition());

		if (!player.capabilities.isCreativeMode && (cube == null || !cube.getOwner().equals(player.getGameProfile().getId())))
		{
			SpectreCube playerCube = cubes.get(player.getGameProfile().getId());

			if (playerCube != null)
			{
				BlockPos spawn = playerCube.getSpawnBlock();
				player.connection.setPlayerLocation(spawn.getX() + 0.5, spawn.getY() + 1, spawn.getZ() + 0.5, player.rotationYaw, player.rotationPitch);
			}
			else
			{
				teleportPlayerBack(player);
			}
		}
	}

	public World getWorld()
	{
		return worldObj;
	}
}
