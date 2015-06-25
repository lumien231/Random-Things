package lumien.randomthings.worldgen.spectre;

import java.util.HashMap;
import java.util.UUID;

import lumien.randomthings.util.Size;
import lumien.randomthings.worldgen.ModDimensions;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

public class SpectreHandler extends WorldSavedData
{
	World worldObj;

	HashMap<UUID, SpectreCube> cubes;

	int positionCounter;

	public SpectreHandler(String name)
	{
		super(name);

		cubes = new HashMap<UUID, SpectreCube>();
		this.worldObj = DimensionManager.getWorld(ModDimensions.SPECTRE_ID);

		positionCounter = 0;
	}

	public SpectreHandler()
	{
		this("SpectreHandler");

		cubes = new HashMap<UUID, SpectreCube>();
		this.worldObj = DimensionManager.getWorld(ModDimensions.SPECTRE_ID);

		positionCounter = 0;
	}

	public void teleportPlayerToSpectreCube(EntityPlayerMP player)
	{
		UUID uuid = player.getGameProfile().getId();
		SpectreCube spectreCube = new SpectreCube();

		if (cubes.containsKey(uuid))
		{
			spectreCube = cubes.get(uuid);
		}
		else
		{
			spectreCube = generateSpectreCube(uuid);
		}

		BlockPos spawn = spectreCube.getSpawnBlock();

		player.playerNetServerHandler.setPlayerLocation(spawn.getX() + 0.5, spawn.getY() + 2, spawn.getZ() + 0.5, player.rotationYaw, player.rotationPitch);
	}

	private SpectreCube generateSpectreCube(UUID uuid)
	{
		SpectreCube cube = new SpectreCube();
		cube.size = new Size(4, 4);
		cube.master = uuid;
		cube.position = positionCounter;
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
			SpectreCube cube = new SpectreCube();
			cube.readFromNBT(cubeCompound);
			this.cubes.put(cube.getOwner(), cube);
		}

		this.positionCounter = nbt.getInteger("positionCounter");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
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
	}

	public static SpectreHandler getInstance()
	{
		WorldServer world = DimensionManager.getWorld(ModDimensions.SPECTRE_ID);
		if (world != null)
		{
			WorldSavedData handler = world.getMapStorage().loadData(SpectreHandler.class, "SpectreHandler");
			if (handler == null)
			{
				handler = new SpectreHandler();
				world.getMapStorage().setData("SpectreHandler", handler);
			}

			return (SpectreHandler) handler;
		}

		return null;
	}

	public static void reset()
	{
		WorldServer world = DimensionManager.getWorld(ModDimensions.SPECTRE_ID);
		if (world != null)
		{
			world.getMapStorage().setData("SpectreHandler", new SpectreHandler());
		}
	}

	public void setSize(EntityPlayerMP player, Size newSize)
	{
		cubes.get(player.getGameProfile().getId()).changeSize(worldObj, newSize);
	}
}
