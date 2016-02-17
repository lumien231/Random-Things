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
	boolean canMine;

	WeakReference<FakePlayer> fakePlayer;

	float curBlockDamage;

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

			ItemStack unbreakingIronPickaxe = new ItemStack(Items.iron_pickaxe, 1);
			unbreakingIronPickaxe.setTagCompound(new NBTTagCompound());
			unbreakingIronPickaxe.getTagCompound().setBoolean("Unbreakable", true);

			fakePlayer.get().setCurrentItemOrArmor(0, unbreakingIronPickaxe);
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

				this.curBlockDamage += targetState.getBlock().getPlayerRelativeBlockHardness(fakePlayer.get(), worldObj, targetPos);

				if (curBlockDamage >= 1.0f)
				{
					mining = false;
					
					resetProgress();
					
					if (fakePlayer.get() != null)
					{
						fakePlayer.get().theItemInWorldManager.tryHarvestBlock(targetPos);
					}
				}
				else
				{
					worldObj.sendBlockBreakProgress(uuid.hashCode(), targetPos, (int) (this.curBlockDamage * 10.0F) - 1);
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

	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
	{
		BlockPos targetPos = pos.offset(state.getValue(BlockBlockBreaker.FACING));
		
		canMine = !(worldIn.isBlockIndirectlyGettingPowered(pos)>0);

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
			resetProgress();
		}
	}

	private void resetProgress()
	{
		BlockPos targetPos = pos.offset(worldObj.getBlockState(pos).getValue(BlockBlockBreaker.FACING));
		worldObj.sendBlockBreakProgress(uuid.hashCode(), targetPos, -1);
		
		curBlockDamage = 0;
	}
}
