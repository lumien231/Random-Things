package lumien.randomthings.tileentity;


import java.lang.reflect.Field;
import java.util.UUID;

import lumien.randomthings.asm.MCPNames;
import lumien.randomthings.handler.spectrelens.SpectreLensHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class TileEntitySpectreLens extends TileEntityBase implements ITickable
{
	static Field isComplete;

	static
	{
		try
		{
			isComplete = TileEntityBeacon.class.getDeclaredField(MCPNames.field("field_146015_k"));
			isComplete.setAccessible(true);
		}
		catch (NoSuchFieldException | SecurityException e)
		{
			e.printStackTrace();
		}
	}


	UUID owner;

	public TileEntitySpectreLens()
	{
		this.owner = null;
	}

	@Override
	public void writeDataToNBT(NBTTagCompound compound, boolean sync)
	{
		if (this.owner != null)
		{
			compound.setString("owner", owner.toString());
		}
	}

	@Override
	public void readDataFromNBT(NBTTagCompound compound, boolean sync)
	{
		if (compound.hasKey("owner"))
		{
			this.owner = UUID.fromString(compound.getString("owner"));
		}
	}

	public void setOwner(UUID owner)
	{
		this.owner = owner;
	}

	@Override
	public boolean syncAdditionalData()
	{
		return false;
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		if (this.owner != null)
		{
			SpectreLensHandler.get(this.world).removeLens(this.owner);
		}
	}

	@Override
	public void update()
	{
		if (!this.world.isRemote && this.owner != null && this.world.getTotalWorldTime() % 80 == 0L)
		{
			IBlockState belowState = this.world.getBlockState(this.pos.down());

			if (belowState.getBlock() == Blocks.BEACON)
			{
				TileEntity te = world.getTileEntity(this.pos.down());

				if (te instanceof TileEntityBeacon)
				{
					TileEntityBeacon beacon = (TileEntityBeacon) te;

					boolean complete = false;
					try
					{
						complete = isComplete.getBoolean(te);
					}
					catch (IllegalArgumentException | IllegalAccessException e)
					{
						e.printStackTrace();
					}

					if (complete)
					{
						int levels = beacon.getField(0);
						int primary = beacon.getField(1);
						int secondary = beacon.getField(2);

						SpectreLensHandler.get(this.world).addLens(owner, levels, primary, secondary);
						return;
					}
				}
			}

			SpectreLensHandler.get(this.world).removeLens(this.owner);
		}
	}
}
