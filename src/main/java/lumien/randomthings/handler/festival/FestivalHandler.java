package lumien.randomthings.handler.festival;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import lumien.randomthings.handler.festival.Festival.STATE;
import lumien.randomthings.network.MessageUtil;
import lumien.randomthings.util.ReflectionUtil;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.village.Village;
import net.minecraft.village.VillageDoorInfo;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;

public class FestivalHandler extends WorldSavedData
{
	static final String ID = "RTFestivalHandler";
	List<Festival> currentFestivals;

	static Random rng = new Random();

	public FestivalHandler(String id)
	{
		super(id);

		currentFestivals = new ArrayList<Festival>();
	}

	public FestivalHandler()
	{
		this(ID);
	}

	public int addFestival(EntityVillager villager)
	{
		Village v = ReflectionUtil.getVillage(villager);

		if (v != null)
		{
			List<VillageDoorInfo> doorInfos = v.getVillageDoorInfoList();

			if (doorInfos != null && !doorInfos.isEmpty())
			{
				Festival f = new Festival();

				for (VillageDoorInfo doorInfo : doorInfos)
				{
					f.addDoorPos(doorInfo.getInsideBlockPos());
				}

				HashSet<BlockPos> hashTest = new HashSet<>(f.getDoorPositions());

				for (Festival currentFestival : currentFestivals)
				{
					for (BlockPos p : currentFestival.doorPositions)
					{
						if (hashTest.contains(p))
						{
							return 1;
						}
					}
				}

				addFestival(f);

				return 2;
			}
		}

		return 0;
	}

	private void addFestival(Festival f)
	{
		this.currentFestivals.add(f);
		this.markDirty();
	}

	public static FestivalHandler get(World worldObj)
	{
		FestivalHandler instance = (FestivalHandler) worldObj.getPerWorldStorage().getOrLoadData(FestivalHandler.class, ID);

		if (instance == null)
		{
			instance = new FestivalHandler();
			worldObj.getPerWorldStorage().setData(ID, instance);
		}

		return instance;
	}

	public void tick(World worldObj)
	{
		long time = worldObj.getWorldTime();
		boolean celebrationTime = time > 14000 & time < 20000;

		Iterator<Festival> iterator = currentFestivals.iterator();
		while (iterator.hasNext())
		{
			boolean firework = false;

			Festival festival = iterator.next();

			if (festival.getState() == STATE.SCHEDULED && celebrationTime)
			{
				festival.setActive();
				firework = true;

				this.markDirty();
			}
			else if (festival.getState() == STATE.ACTIVE && !celebrationTime)
			{
				iterator.remove();
				this.markDirty();
			}
			else if (festival.getState() == STATE.ACTIVE && celebrationTime)
			{
				firework = true;
			}

			if (firework)
			{
				List<BlockPos> doorPositions = festival.getDoorPositions();

				if (time == 14001)
				{
					MessageUtil.sendToAllWatchingPos(worldObj, doorPositions.get(0), new SPacketChat(new TextComponentTranslation("festival.celebrating"), ChatType.SYSTEM));
				}

				boolean midnight = Math.abs(time - 18000) < 20 * 15;

				if (time == 18000)
				{
					for (BlockPos pos : doorPositions)
					{
						if (worldObj.isBlockLoaded(pos) && worldObj.isAnyPlayerWithinRangeAt(pos.getX(), pos.getY(), pos.getZ(), 256))
						{
							ItemStack rocket = getRandomFirework();

							if (!rocket.isEmpty())
							{
								BlockPos topPos = worldObj.getTopSolidOrLiquidBlock(pos);

								EntityFireworkRocket entity = new EntityFireworkRocket(worldObj, topPos.getX() + rng.nextFloat(), topPos.getY() + 1, topPos.getZ() + rng.nextFloat(), rocket);

								worldObj.spawnEntity(entity);
							}
						}
					}
				}
				else
				{
					if (!doorPositions.isEmpty() && rng.nextFloat() < (midnight ? 0.1 : 0.05))
					{
						BlockPos randomPosition = doorPositions.get(rng.nextInt(doorPositions.size()));

						if (worldObj.isBlockLoaded(randomPosition) && worldObj.isAnyPlayerWithinRangeAt(randomPosition.getX(), randomPosition.getY(), randomPosition.getZ(), 256))
						{
							ItemStack rocket = getRandomFirework();

							if (!rocket.isEmpty())
							{
								BlockPos pos = worldObj.getTopSolidOrLiquidBlock(randomPosition);

								EntityFireworkRocket entity = new EntityFireworkRocket(worldObj, pos.getX() + rng.nextFloat(), pos.getY() + 1, pos.getZ() + rng.nextFloat(), rocket);

								worldObj.spawnEntity(entity);
							}
						}
					}
				}
			}
		}
	}

	private ItemStack getRandomFirework()
	{
		String[] possibleCareers = new String[] { "farmer", "fisherman", "shepherd", "fletcher", "librarian", "cartographer", "cleric", "armor", "weapon", "tool", "butcher", "leather", "nitwit", "purple" };
		String name = possibleCareers[rng.nextInt(possibleCareers.length)];

		ItemStack rocket = new ItemStack(Items.FIREWORKS);
		String json = null;

		if (name.equals("farmer"))
		{
			json = "{Fireworks:{Flight:2,Explosions:[{Type:0,Flicker:0,Trail:1,Colors:[I;5320730,14602026],FadeColors:[I;3887386,4312372]}]}}";
		}
		else if (name.equals("fisherman"))
		{
			json = "{Fireworks:{Flight:2,Explosions:[{Type:1,Flicker:0,Trail:1,Colors:[I;2437522,2651799],FadeColors:[I;2437522,2651799,6719955]}]}}";
		}
		else if (name.equals("shepherd"))
		{
			json = "{Fireworks:{Flight:1,Explosions:[{Type:3,Flicker:0,Trail:0,Colors:[I;11250603],FadeColors:[I;15790320]}]}}";
		}
		else if (name.equals("fletcher"))
		{
			json = "{Fireworks:{Flight:3,Explosions:[{Type:0,Flicker:1,Trail:0,Colors:[I;1973019,15790320],FadeColors:[I;1973019,15790320]}]}}";
		}
		else if (name.equals("librarian"))
		{
			json = "{Fireworks:{Flight:2,Explosions:[{Type:2,Flicker:0,Trail:1,Colors:[I;5320730,15435844],FadeColors:[I;11250603,15790320]}]}}";
		}
		else if (name.equals("cartographer"))
		{
			json = "{Fireworks:{Flight:3,Explosions:[{Type:1,Flicker:1,Trail:1,Colors:[I;15435844],FadeColors:[I;11743532,3887386,5320730,2437522]}]}}";
		}
		else if (name.equals("cleric"))
		{
			json = "{Fireworks:{Flight:2,Explosions:[{Type:4,Flicker:0,Trail:1,Colors:[I;15790320],FadeColors:[I;15790320]}]}}";
		}
		else if (name.equals("armor"))
		{
			json = "{Fireworks:{Flight:2,Explosions:[{Type:0,Flicker:1,Trail:0,Colors:[I;5320730,14602026],FadeColors:[]}]}}";
		}
		else if (name.equals("weapon"))
		{
			json = "{Fireworks:{Flight:2,Explosions:[{Type:4,Flicker:1,Trail:0,Colors:[I;11743532,15790320],FadeColors:[]}]}}";
		}
		else if (name.equals("tool"))
		{
			json = "{Fireworks:{Flight:2,Explosions:[{Type:0,Flicker:1,Trail:0,Colors:[I;3887386,15790320],FadeColors:[]}]}}";
		}
		else if (name.equals("butcher"))
		{
			json = "{Fireworks:{Flight:3,Explosions:[{Type:3,Flicker:0,Trail:0,Colors:[I;11743532,14188952],FadeColors:[]}]}}";
		}
		else if (name.equals("leather"))
		{
			json = "{Fireworks:{Flight:1,Explosions:[{Type:4,Flicker:0,Trail:1,Colors:[I;5320730],FadeColors:[]}]}}";
		}
		else if (name.equals("nitwit"))
		{
			json = "{Fireworks:{Flight:2,Explosions:[{Type:1,Flicker:0,Trail:1,Colors:[I;11743532,3887386,2437522,4312372,14602026,15790320],FadeColors:[]}]}}";
		}
		else if (name.equals("purple"))
		{
			json = "{Fireworks:{Flight:3,Explosions:[{Type:4,Flicker:1,Trail:1,Colors:[I;8073150,12801229],FadeColors:[I;11743532]}]}}";
		}

		if (json != null)
		{
			try
			{
				rocket.setTagCompound(JsonToNBT.getTagFromJson(json));
			}
			catch (NBTException e)
			{
				e.printStackTrace();
				return ItemStack.EMPTY;
			}

			return rocket;
		}
		return ItemStack.EMPTY;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		NBTTagList list = nbt.getTagList("festivals", 10);

		for (int i = 0; i < list.tagCount(); i++)
		{
			NBTTagCompound compound = list.getCompoundTagAt(i);

			Festival f = new Festival();

			f.deserializeNBT(compound);

			currentFestivals.add(f);
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		NBTTagList list = new NBTTagList();

		for (Festival f : currentFestivals)
		{
			list.appendTag(f.serializeNBT());
		}

		compound.setTag("festivals", list);

		return compound;
	}
}
