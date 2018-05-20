package lumien.randomthings.tileentity;

import lumien.randomthings.block.BlockOnlineDetector;
import lumien.randomthings.block.ModBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class TileEntityOnlineDetector extends TileEntityBase implements ITickable
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
		if (!world.isRemote)
		{
			if (world.getTotalWorldTime() % 20 == 0)
			{
				boolean playerCheck = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUsername(username) != null;

				if (playerOnline != playerCheck)
				{
					this.markDirty();
					this.world.setBlockState(pos, ModBlocks.onlineDetector.getDefaultState().withProperty(BlockOnlineDetector.POWERED, playerCheck));
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
		this.syncTE();
	}

	public String getPlayerName()
	{
		return username;
	}

	@Override
	public void writeDataToNBT(NBTTagCompound compound, boolean sync)
	{
		compound.setString("username", username);
		compound.setBoolean("playerOnline", playerOnline);
	}

	@Override
	public void readDataFromNBT(NBTTagCompound compound, boolean sync)
	{
		username = compound.getString("username");
		playerOnline = compound.getBoolean("playerOnline");
	}

	public boolean isPowering()
	{
		return playerOnline;
	}
}
