package lumien.randomthings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import com.mojang.authlib.GameProfile;

import lumien.randomthings.handler.festival.FestivalHandler;
import lumien.randomthings.handler.floo.FlooFireplace;
import lumien.randomthings.handler.floo.FlooNetworkHandler;
import lumien.randomthings.handler.spectreilluminator.SpectreIlluminationHandler;
import lumien.randomthings.item.ItemBiomeCrystal;
import lumien.randomthings.item.ItemPositionFilter;
import lumien.randomthings.item.ModItems;
import lumien.randomthings.lib.IOpable;
import lumien.randomthings.network.PacketHandler;
import lumien.randomthings.network.messages.MessageNotification;
import lumien.randomthings.tileentity.TileEntityBase;
import lumien.randomthings.worldgen.WorldGenAncientFurnace;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class RTCommand extends CommandBase
{

	@Override
	public String getName()
	{
		return "rt";
	}

	@Override
	public String getUsage(ICommandSender sender)
	{
		return "/rt";
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos)
	{
		if (args.length == 1)
		{
			return getListOfStringsMatchingLastWord(args, new String[] { "generateBiomeCrystalChests", "setBiomeCrystal", "tpFilter", "testSlimeSpawn", "notify", "fireplaces", "festival", "op" });
		}
		else
		{
			if (args[0].equals("setBiomeCrystal") && args.length == 2)
			{
				return getListOfStringsMatchingLastWord(args, ForgeRegistries.BIOMES.getKeys());
			}
			else if (args[0].equals("notify"))
			{
				if (args.length == 4)
				{
					return getListOfStringsMatchingLastWord(args, Item.REGISTRY.getKeys());
				}
				else if (args.length == 5)
				{
					return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
				}
			}
			else if (args[0].equals("op"))
			{
				if (args.length > 1 && args.length <= 4)
				{
					return getTabCompletionCoordinate(args, 1, pos);
				}
			}
		}
		return Collections.<String>emptyList();
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		if (args.length == 0)
		{
			return;
		}

		if (args[0].equals("setBiomeCrystal"))
		{
			if (args.length == 2 && sender instanceof EntityPlayer)
			{
				String biomeName = args[1];

				EntityPlayer player = (EntityPlayer) sender;

				ItemStack equipped = player.getHeldItemMainhand();

				if (equipped != null && equipped.getItem() instanceof ItemBiomeCrystal)
				{
					if (equipped.getTagCompound() == null)
					{
						equipped.setTagCompound(new NBTTagCompound());
					}

					equipped.getTagCompound().setString("biomeName", args[1]);
				}
			}
		}
		else if (args[0].equals("generateBiomeCrystalChests"))
		{
			if (sender instanceof EntityPlayer)
			{
				List<ResourceLocation> biomeIds = new ArrayList<>(Biome.REGISTRY.getKeys());

				int modX = 0;

				while (!biomeIds.isEmpty())
				{
					sender.getEntityWorld().setBlockState(sender.getPosition().add(modX, 0, 0), Blocks.CHEST.getDefaultState());

					IInventory inventory = (IInventory) sender.getEntityWorld().getTileEntity(sender.getPosition().add(modX, 0, 0));
					for (int i = 0; i < 27; i++)
					{
						if (!biomeIds.isEmpty())
						{
							ResourceLocation next = biomeIds.remove(biomeIds.size() - 1);
							ItemStack crystal = new ItemStack(ModItems.biomeCrystal);
							crystal.setTagCompound(new NBTTagCompound());
							crystal.getTagCompound().setString("biomeName", next.toString());
							inventory.setInventorySlotContents(i, crystal);
						}
					}

					modX += 2;
				}
			}
		}
		else if (args[0].equals("tpFilter"))
		{
			if (sender instanceof EntityPlayerMP)
			{
				EntityPlayerMP player = (EntityPlayerMP) sender;

				ItemStack held;
				if ((held = player.getHeldItemMainhand()) != null && held.getItem() == ModItems.positionFilter)
				{
					BlockPos pos = ItemPositionFilter.getPosition(held);

					player.connection.setPlayerLocation(pos.getX(), pos.getY() + 150, pos.getZ(), player.rotationYaw, player.rotationPitch);
				}
			}
		}
		else if (args[0].equals("testSlimeSpawn"))
		{
			BlockPos pos = sender.getPosition();
			World world = sender.getEntityWorld();

			if (pos != null && world != null)
			{
				EntitySlime slime = new EntitySlime(world);
				slime.setLocationAndAngles(pos.getX(), pos.getY(), pos.getZ(), 0F, 0F);
				sender.sendMessage(new TextComponentString(slime.getCanSpawnHere() + ""));
			}
		}
		else if (args[0].equals("notify") && args.length == 5)
		{
			String title = args[1];
			String body = args[2];
			String itemName = args[3];
			String player = args[4];

			EntityPlayerMP playerEntity = server.getPlayerList().getPlayerByUsername(player);

			ItemStack itemStack = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName)));

			MessageNotification message = new MessageNotification(title, body, itemStack);
			PacketHandler.INSTANCE.sendTo(message, playerEntity);
		}
		else if (args[0].equals("fireplaces"))
		{
			FlooNetworkHandler handler = FlooNetworkHandler.get(sender.getEntityWorld());

			List<FlooFireplace> firePlaces = handler.getFirePlaces();

			sender.sendMessage(new TextComponentString("Floo Fireplaces in Dimension " + sender.getEntityWorld().provider.getDimension()).setStyle(new Style().setUnderlined(true)));
			sender.sendMessage(new TextComponentString(""));

			for (FlooFireplace firePlace : firePlaces)
			{
				String name = firePlace.getName();
				UUID creator = firePlace.getCreatorUUID();
				String ownerName = null;

				if (creator != null)
				{
					GameProfile profile = server.getPlayerProfileCache().getProfileByUUID(creator);

					if (profile != null)
					{
						ownerName = profile.getName();
					}
				}

				BlockPos pos = firePlace.getLastKnownPosition();

				sender.sendMessage(new TextComponentString((name == null ? "<Unnamed>" : name) + " | " + String.format("%d %d %d", pos.getX(), pos.getY(), pos.getZ()) + (ownerName != null ? " | " + ownerName : "")));
			}
		}
		else if (args[0].equals("festival"))
		{
			FestivalHandler handler = FestivalHandler.get(sender.getEntityWorld());
			List<EntityVillager> villagerList = sender.getEntityWorld().getEntitiesWithinAABB(EntityVillager.class, new AxisAlignedBB(sender.getPosition()).grow(50));

			if (!villagerList.isEmpty())
			{
				EntityVillager villager = villagerList.get(0);

				int success = handler.addFestival(villager);

				if (success == 2)
				{
					sender.sendMessage(new TextComponentTranslation("command.festival.scheduled"));
				}
				else
				{
					sender.sendMessage(new TextComponentTranslation("command.festival.failed"));
				}
			}
			else
			{
				sender.sendMessage(new TextComponentTranslation("command.festival.novillager"));
			}
		}
		else if (args[0].equals("ancientFurnace"))
		{
			WorldGenAncientFurnace.pattern.place(sender.getEntityWorld(), sender.getPosition(), 3);
		}
		else if (args[0].equals("op") && args.length == 4)
		{
			if (sender instanceof EntityPlayerMP && sender.canUseCommand(2, "op"))
			{
				EntityPlayerMP player = (EntityPlayerMP) sender;

				BlockPos target = parseBlockPos(sender, args, 1, false);

				TileEntity teTarget = player.world.getTileEntity(target);

				if (teTarget instanceof TileEntityBase && teTarget instanceof IOpable)
				{
					boolean newValue = ((TileEntityBase) teTarget).toggleOp();

					sender.sendMessage(new TextComponentTranslation("rt.command.op.feedback", newValue + ""));
				}
				else
				{
					sender.sendMessage(new TextComponentTranslation("rt.command.op.error"));
				}
			}
		}
		else if (args[0].equals("ti"))
		{
			SpectreIlluminationHandler.get(sender.getEntityWorld()).toggleChunk(sender.getEntityWorld(), sender.getPosition());
		}
	}

}
