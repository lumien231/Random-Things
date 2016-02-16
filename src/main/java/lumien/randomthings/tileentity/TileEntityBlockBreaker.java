package lumien.randomthings.tileentity;

import java.lang.ref.WeakReference;
import java.util.UUID;

import com.mojang.authlib.GameProfile;

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
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;

public class TileEntityBlockBreaker extends TileEntityBase implements ITickable
{
	UUID uuid;

	boolean mining;

	WeakReference<FakePlayer> fakePlayer;

	float curBlockDamageMP;

	@Override
	public void onLoad()
	{
		super.onLoad();

		if (!this.worldObj.isRemote)
		{
			if (uuid == null)
			{
				uuid = UUID.randomUUID();
				this.worldObj.markBlockForUpdate(this.pos);
			}

			fakePlayer = new WeakReference<FakePlayer>(FakePlayerFactory.get((WorldServer) worldObj, new GameProfile(null, "RTBlockBreaker")));
			fakePlayer.get().setCurrentItemOrArmor(0, new ItemStack(Items.iron_pickaxe, 1, -1));
			fakePlayer.get().onGround = true;

			fakePlayer.get().playerNetServerHandler = new NetHandlerPlayServer(MinecraftServer.getServer(), new NetworkManager(EnumPacketDirection.SERVERBOUND), fakePlayer.get())
			{
				@Override
				public void sendPacket(Packet packetIn)
				{

				}
			};
		}
	}

	@Override
	public void update()
	{
		if (!this.worldObj.isRemote)
		{
			if (mining)
			{
				BlockPos targetPos = pos.offset(worldObj.getBlockState(pos).getValue(BlockBlockBreaker.FACING));

				IBlockState targetState = worldObj.getBlockState(targetPos);

				this.curBlockDamageMP += targetState.getBlock().getPlayerRelativeBlockHardness(fakePlayer.get(), worldObj, targetPos);

				if (curBlockDamageMP >= 1.0f)
				{
					mining = false;
					curBlockDamageMP = 0;

					worldObj.sendBlockBreakProgress(uuid.hashCode(), targetPos, -1);
					if (fakePlayer.get() != null)
					{
						fakePlayer.get().theItemInWorldManager.tryHarvestBlock(targetPos);
					}
				}
				else
				{
					worldObj.sendBlockBreakProgress(uuid.hashCode(), targetPos, (int) (this.curBlockDamageMP * 10.0F) - 1);
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
	}

	@Override
	public void readDataFromNBT(NBTTagCompound compound)
	{
		if (compound.hasKey("uuid"))
		{
			uuid = UUID.fromString(compound.getString("uuid"));
		}
	}

	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
	{
		BlockPos targetPos = pos.offset(state.getValue(BlockBlockBreaker.FACING));

		IBlockState targetState = worldIn.getBlockState(targetPos);

		if (!worldIn.isAirBlock(targetPos))
		{
			mining = true;
			curBlockDamageMP = 0;
		}
		else
		{
			mining = false;
		}
	}
}
