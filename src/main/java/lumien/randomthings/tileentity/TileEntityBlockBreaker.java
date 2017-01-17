package lumien.randomthings.tileentity;

import java.lang.ref.WeakReference;
import java.util.UUID;

import org.apache.logging.log4j.Level;

import com.google.common.base.Charsets;
import com.mojang.authlib.GameProfile;

import lumien.randomthings.RandomThings;
import lumien.randomthings.block.BlockBlockBreaker;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.EnumPacketDirection;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class TileEntityBlockBreaker extends TileEntityBase implements ITickable
{
	public static final GameProfile breakerProfile = new GameProfile(UUID.nameUUIDFromBytes("RTBlockBreaker".getBytes(Charsets.UTF_8)), "RTBlockBreaker");
	
	static
	{
		RandomThings.instance.logger.log(Level.DEBUG, "BlockBreakerUUID: "+breakerProfile.getId().toString());
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

		fakePlayer = new WeakReference<FakePlayer>(FakePlayerFactory.get((WorldServer) world, breakerProfile));

		ItemStack unbreakingIronPickaxe = new ItemStack(Items.IRON_PICKAXE, 1);
		unbreakingIronPickaxe.setTagCompound(new NBTTagCompound());
		unbreakingIronPickaxe.getTagCompound().setBoolean("Unbreakable", true);

		fakePlayer.get().setHeldItem(EnumHand.MAIN_HAND, unbreakingIronPickaxe);
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
				
				neighborChanged(this.world.getBlockState(pos), world, pos, null);
			}

			if (mining)
			{
				BlockPos targetPos = pos.offset(world.getBlockState(pos).getValue(BlockBlockBreaker.FACING));

				IBlockState targetState = world.getBlockState(targetPos);

				this.curBlockDamage += targetState.getBlock().getPlayerRelativeBlockHardness(targetState,fakePlayer.get(), world, targetPos);

				if (curBlockDamage >= 1.0f)
				{
					mining = false;

					resetProgress();

					if (fakePlayer.get() != null)
					{
						fakePlayer.get().interactionManager.tryHarvestBlock(targetPos);
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
	public void writeDataToNBT(NBTTagCompound compound)
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
	public void readDataFromNBT(NBTTagCompound compound)
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
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block neighborBlock)
	{
		BlockPos targetPos = pos.offset(state.getValue(BlockBlockBreaker.FACING));

		canMine = !(worldIn.isBlockIndirectlyGettingPowered(pos) > 0);

		IBlockState targetState = worldIn.getBlockState(targetPos);

		if (canMine)
		{
			if (!worldIn.isAirBlock(targetPos))
			{
				mining = true;
				curBlockDamage = 0;
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
