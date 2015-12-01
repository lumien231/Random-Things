package lumien.randomthings.tileentity;

import java.util.Arrays;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.SimpleComponent;
import lumien.randomthings.block.BlockOnlineDetector;
import lumien.randomthings.block.ModBlocks;
import lumien.randomthings.util.PlayerUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;

@Optional.Interface(iface = "li.cil.oc.api.network.SimpleComponent", modid = "OpenComputers")
public class TileEntityOnlineDetector extends TileEntityBase implements SimpleComponent, ITickable
{
	String username;
	boolean playerOnline;

	public TileEntityOnlineDetector()
	{
		username = "";
		playerOnline = false;
	}

	@Override
	public void update()
	{
		if (!worldObj.isRemote)
		{
			if (worldObj.getTotalWorldTime() % 20 == 0)
			{
				boolean playerCheck = PlayerUtil.isPlayerOnline(username);

				if (playerOnline != playerCheck)
				{
					this.markDirty();
					this.worldObj.setBlockState(pos, ModBlocks.onlineDetector.getDefaultState().withProperty(BlockOnlineDetector.POWERED, playerCheck));
					this.playerOnline = playerCheck;
				}
			}
		}
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
	{
		return (oldState.getBlock() != newState.getBlock());
	}

	public void setUsername(String username)
	{
		this.username = username;
		this.markDirty();
		this.worldObj.markBlockForUpdate(pos);
	}

	public String getPlayerName()
	{
		return username;
	}

	@Override
	public String getComponentName()
	{
		return "onlineDetector";
	}

	@Callback
	@Optional.Method(modid = "OpenComputers")
	public Object[] isPlayerOnline(Context context, Arguments args)
	{
		return new Object[] { PlayerUtil.isPlayerOnline(args.checkString(0)) };
	}

	@Callback
	@Optional.Method(modid = "OpenComputers")
	public Object[] getPlayerList(Context context, Arguments args)
	{
		return new Object[] { Arrays.asList(MinecraftServer.getServer().getAllUsernames()) };
	}

	@Override
	public void writeDataToNBT(NBTTagCompound compound)
	{
		compound.setString("username", username);
		compound.setBoolean("playerOnline", playerOnline);
	}

	@Override
	public void readDataFromNBT(NBTTagCompound compound)
	{
		username = compound.getString("username");
		playerOnline = compound.getBoolean("playerOnline");
	}

	public boolean isPowering()
	{
		return playerOnline;
	}
}
