package lumien.randomthings.handler.floo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import info.debatty.java.stringsimilarity.Levenshtein;
import lumien.randomthings.network.MessageUtil;
import lumien.randomthings.network.PacketHandler;
import lumien.randomthings.network.messages.MessageFlooParticles;
import lumien.randomthings.tileentity.TileEntityFlooBrick;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;

public class FlooNetworkHandler extends WorldSavedData
{
	static final String ID = "rtFlooHandler";

	List<FlooFireplace> firePlaces;

	static final Levenshtein stringSim = new Levenshtein();

	public FlooNetworkHandler()
	{
		this(ID);
	}

	public FlooNetworkHandler(String id)
	{
		super(id);

		firePlaces = new ArrayList<FlooFireplace>();
	}

	public static FlooNetworkHandler get(World worldObj)
	{
		FlooNetworkHandler instance = (FlooNetworkHandler) worldObj.getPerWorldStorage().getOrLoadData(FlooNetworkHandler.class, ID);
		if (instance == null)
		{
			instance = new FlooNetworkHandler();
			worldObj.getPerWorldStorage().setData(ID, instance);
		}

		return instance;
	}

	public String getNameFromUUID(UUID uuid)
	{
		for (FlooFireplace ff : firePlaces)
		{
			if (ff.masterUUID.equals(uuid))
			{
				return ff.name;
			}
		}

		return null;
	}

	public void addFirePlace(UUID creatorPlayerUUID, UUID masterUUID, String name, BlockPos currentPos)
	{
		FlooFireplace firePlace = new FlooFireplace(creatorPlayerUUID, masterUUID, name, currentPos);

		this.firePlaces.add(firePlace);
		this.markDirty();
	}

	public boolean teleport(World world, BlockPos originPos, TileEntityFlooBrick originTE, EntityPlayerMP player, String enteredDestination)
	{
		FlooFireplace targetFirePlace = null;

		double closest = Double.MAX_VALUE;

		for (FlooFireplace firePlace : firePlaces)
		{
			String firePlaceName = firePlace.getName();

			if (firePlaceName != null)
			{
				double sim = stringSim.distance(firePlaceName, enteredDestination);

				if (sim < closest)
				{
					closest = sim;
					targetFirePlace = firePlace;
				}
			}
		}

		if (targetFirePlace != null)
		{
			if (originPos != null && targetFirePlace.lastKnownPosition.equals(originPos))
			{
				player.sendMessage(new TextComponentTranslation("floo.info.same"));

				return false;
			}
			else
			{
				BlockPos targetPos = targetFirePlace.getLastKnownPosition();
				TileEntity te = world.getTileEntity(targetPos);

				if (te instanceof TileEntityFlooBrick)
				{
					TileEntityFlooBrick targetTE = (TileEntityFlooBrick) te;

					if (targetTE.isMaster())
					{
						EnumFacing tpFacing = targetTE.getFacing();

						BlockPos teleportTarget = targetPos.up();
						player.connection.setPlayerLocation(teleportTarget.getX() + 0.5, teleportTarget.getY(), teleportTarget.getZ() + 0.5, tpFacing.getHorizontalAngle(), 0);

						player.sendMessage(new TextComponentTranslation("floo.info.teleport", targetFirePlace.getName()));

						// Particles
						if (originPos != null && originTE != null)
						{
							List<BlockPos> originPositions = new ArrayList<BlockPos>();
							originPositions.add(originPos);
							originPositions.addAll(originTE.getChildren());

							MessageFlooParticles p1 = new MessageFlooParticles(originPositions);

							MessageUtil.sendToAllWatchingPos(player.world, originPos, p1);
						}

						List<BlockPos> destinationPositions = new ArrayList<BlockPos>();
						destinationPositions.add(targetPos);
						destinationPositions.addAll(targetTE.getChildren());

						MessageFlooParticles p2 = new MessageFlooParticles(destinationPositions);

						MessageUtil.sendToAllWatchingPos(player.world, targetPos, p2);

						PacketHandler.INSTANCE.sendTo(p2, player);

						return true;
					}
				}

				firePlaces.remove(targetFirePlace);
				this.markDirty();

				return teleport(world, originPos, originTE, player, enteredDestination);
			}
		}

		return false;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		NBTTagList firePlaceTags = nbt.getTagList("firePlaces", 10);

		for (int i = 0; i < firePlaceTags.tagCount(); i++)
		{
			NBTTagCompound compound = firePlaceTags.getCompoundTagAt(i);
			FlooFireplace firePlace = new FlooFireplace();
			firePlace.readFromNBT(compound);
			this.firePlaces.add(firePlace);
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		NBTTagList firePlaceTags = new NBTTagList();

		for (FlooFireplace firePlace : firePlaces)
		{
			NBTTagCompound firePlaceCompound = new NBTTagCompound();
			firePlace.writeToNBT(firePlaceCompound);
			firePlaceTags.appendTag(firePlaceCompound);
		}

		compound.setTag("firePlaces", firePlaceTags);

		return compound;
	}

	public boolean createFireplace(World worldIn, UUID uuid, String name, EntityPlayer player, BlockPos pos, List<BlockPos> brickList)
	{
		for (FlooFireplace firePlace : firePlaces)
		{
			if (brickList.contains(firePlace.getLastKnownPosition()) || (name != null && firePlace.name != null && firePlace.name.equalsIgnoreCase(name)))
			{
				return false;
			}
		}

		addFirePlace(player.getGameProfile().getId(), uuid, name, pos);

		return true;
	}

	public void updatePosition(UUID uuid, BlockPos pos)
	{
		for (FlooFireplace firePlace : firePlaces)
		{
			if (firePlace.getMasterUUID().equals(uuid))
			{
				firePlace.setPos(pos);
				break;
			}
		}
	}

	@Override
	public boolean isDirty()
	{
		return true;
	}

	public void brokenMaster(World worldIn, BlockPos pos, TileEntityFlooBrick tileEntityFlooBrick)
	{
		if (tileEntityFlooBrick.getUid() != null)
		{
			Iterator<FlooFireplace> iterator = firePlaces.iterator();

			while (iterator.hasNext())
			{
				FlooFireplace firePlace = iterator.next();

				if (firePlace.getMasterUUID().equals(tileEntityFlooBrick.getUid()))
				{
					iterator.remove();
				}
			}
		}
	}

	public List<FlooFireplace> getFirePlaces()
	{
		return firePlaces;
	}

	public TileEntity getFirePlaceTE(World worldObj, UUID uuid)
	{
		for (FlooFireplace firePlace : firePlaces)
		{
			if (firePlace.masterUUID.equals(uuid))
			{
				return worldObj.getTileEntity(firePlace.lastKnownPosition);
			}
		}

		return null;
	}
}
