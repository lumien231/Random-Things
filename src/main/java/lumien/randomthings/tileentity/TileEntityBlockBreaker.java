package lumien.randomthings.tileentity;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.UUID;

import org.apache.logging.log4j.Level;

import com.google.common.base.Charsets;
import com.mojang.authlib.GameProfile;

import lumien.randomthings.RandomThings;
import lumien.randomthings.block.BlockBlockBreaker;
import lumien.randomthings.enchantment.ModEnchantments;
import lumien.randomthings.util.ItemUtil;
import lumien.randomthings.util.WorldUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.EnumPacketDirection;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

public class TileEntityBlockBreaker extends TileEntityBase implements ITickable
{
	public static final GameProfile breakerProfile = new GameProfile(UUID.nameUUIDFromBytes("RTBlockBreaker".getBytes(Charsets.UTF_8)), "RTBlockBreaker");

	static
	{
		RandomThings.instance.logger.log(Level.DEBUG, "BlockBreakerUUID: " + breakerProfile.getId().toString());
	}

	UUID uuid;

	boolean mining;
	boolean canMine;

	WeakReference<FakePlayer> fakePlayer;

	float curBlockDamage;

	boolean firstTick = true;

	private void initFakePlayer()
	{
		if (uuid == null)
		{
			uuid = UUID.randomUUID();
			syncTE();
		}

		fakePlayer = new WeakReference<>(FakePlayerFactory.get((WorldServer) world, breakerProfile));

		ItemStack unbreakingIronPickaxe = new ItemStack(Items.IRON_PICKAXE, 1);
		unbreakingIronPickaxe.setTagCompound(new NBTTagCompound());
		unbreakingIronPickaxe.getTagCompound().setBoolean("Unbreakable", true);

		HashMap<Enchantment, Integer> enchantmentMap = new HashMap<>();
		enchantmentMap.put(ModEnchantments.magnetic, 1);
		EnchantmentHelper.setEnchantments(enchantmentMap, unbreakingIronPickaxe);

		fakePlayer.get().setSilent(true);
		
		ItemUtil.setHeldItemSilent(fakePlayer.get(), EnumHand.MAIN_HAND, unbreakingIronPickaxe);
		fakePlayer.get().onGround = true;

		fakePlayer.get().connection = new NetHandlerPlayServer(FMLCommonHandler.instance().getMinecraftServerInstance(), new NetworkManager(EnumPacketDirection.SERVERBOUND), fakePlayer.get())
		{
			@Override
			public void sendPacket(Packet packetIn)
			{

			}
		};
	}

	@Override
	public void update()
	{
		if (!this.world.isRemote)
		{
			if (firstTick)
			{
				firstTick = false;
				initFakePlayer();

				neighborChanged(this.world.getBlockState(pos), world, pos, null, null);
			}

			if (mining)
			{
				EnumFacing facing = world.getBlockState(pos).getValue(BlockBlockBreaker.FACING);
				BlockPos targetPos = pos.offset(facing);

				IBlockState targetState = world.getBlockState(targetPos);

				this.curBlockDamage += targetState.getPlayerRelativeBlockHardness(fakePlayer.get(), world, targetPos);

				if (curBlockDamage >= 1.0f)
				{
					mining = false;

					resetProgress();

					FakePlayer player;
					if ((player = fakePlayer.get()) != null)
					{
						boolean catching = false;

						TileEntity te;
						if ((te = world.getTileEntity(this.pos.offset(facing.getOpposite()))) != null && te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing))
						{
							catching = true;
						}

						player.dimension = this.world.provider.getDimension();
						player.interactionManager.world = player.world = this.world;

						player.interactionManager.tryHarvestBlock(targetPos);

						IItemHandler itemHandler = null;
						if (catching)
						{
							itemHandler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing);
						}

						for (int i = 1; i < player.inventory.getSizeInventory(); i++)
						{
							ItemStack stack = player.inventory.getStackInSlot(i);
							player.inventory.setInventorySlotContents(i, ItemStack.EMPTY);

							if (!stack.isEmpty())
							{
								ItemStack remainder = stack;
								if (catching)
								{
									remainder = ItemHandlerHelper.insertItemStacked(itemHandler, stack, false);
								}

								if (!remainder.isEmpty())
								{
									WorldUtil.spawnItemStack(world, pos.offset(facing), stack);
								}
							}
						}
					}
				}
				else
				{
					world.sendBlockBreakProgress(uuid.hashCode(), targetPos, (int) (this.curBlockDamage * 10.0F) - 1);
				}
			}
		}
	}

	@Override
	public void writeDataToNBT(NBTTagCompound compound, boolean sync)
	{
		if (uuid != null)
		{
			compound.setString("uuid", uuid.toString());
		}

		compound.setBoolean("mining", mining);
		compound.setBoolean("canMine", canMine);
		compound.setFloat("curBlockDamage", curBlockDamage);
	}

	@Override
	public void readDataFromNBT(NBTTagCompound compound, boolean sync)
	{
		if (compound.hasKey("uuid"))
		{
			uuid = UUID.fromString(compound.getString("uuid"));
		}

		mining = compound.getBoolean("mining");
		canMine = compound.getBoolean("canMine");
		curBlockDamage = compound.getFloat("curBlockDamage");
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block neighborBlock, BlockPos changedPos)
	{
		BlockPos targetPos = pos.offset(state.getValue(BlockBlockBreaker.FACING));

		canMine = !(worldIn.isBlockIndirectlyGettingPowered(pos) > 0);

		IBlockState targetState = worldIn.getBlockState(targetPos);

		if (canMine)
		{
			if (!worldIn.isAirBlock(targetPos))
			{
				if (!mining)
				{
					mining = true;
					curBlockDamage = 0;
				}
			}
			else
			{
				mining = false;
				resetProgress();
			}
		}
		else
		{
			if (mining)
			{
				mining = false;
				resetProgress();
			}
		}
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		if (mining && uuid != null)
		{
			resetProgress(state);
		}
	}

	private void resetProgress()
	{
		resetProgress(this.world.getBlockState(this.pos));
	}

	private void resetProgress(IBlockState state)
	{
		if (uuid != null)
		{
			BlockPos targetPos = pos.offset(state.getValue(BlockBlockBreaker.FACING));
			world.sendBlockBreakProgress(uuid.hashCode(), targetPos, -1);

			curBlockDamage = 0;
		}
	}
}
