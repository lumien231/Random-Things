package lumien.randomthings.tileentity;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.WeakHashMap;

import lumien.randomthings.item.ItemSoundPattern;
import lumien.randomthings.item.ModItems;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntitySoundDampener extends TileEntityBase
{
	public static Set<TileEntitySoundDampener> dampeners = Collections.newSetFromMap(new WeakHashMap());

	HashSet<ResourceLocation> mutedSounds;

	public TileEntitySoundDampener()
	{
		mutedSounds = new HashSet<ResourceLocation>();

		this.setItemHandler(9);
		this.setInventoryChangeListener(() -> {

			if (!this.world.isRemote)
			{
				mutedSounds.clear();

				for (int i = 0; i < this.getItemHandler().getSlots(); i++)
				{
					ItemStack is = this.getItemHandler().getStackInSlot(i);

					if (!is.isEmpty() && is.getItem() == ModItems.soundPattern)
					{
						ResourceLocation sound = ItemSoundPattern.getSoundLocation(is);

						if (sound != null)
						{
							mutedSounds.add(sound);
						}
					}
				}

				syncTE();
			}
		});

		synchronized (dampeners)
		{
			dampeners.add(this);
		}
	}

	public HashSet<ResourceLocation> getMutedSounds()
	{
		return mutedSounds;
	}

	@Override
	public void markDirty()
	{
		super.markDirty();
	}

	@Override
	public void writeDataToNBT(NBTTagCompound compound, boolean sync)
	{
		NBTTagList soundList = new NBTTagList();

		for (ResourceLocation rl : mutedSounds)
		{
			soundList.appendTag(new NBTTagString(rl.toString()));
		}

		compound.setTag("mutedSounds", soundList);
	}

	@Override
	public void readDataFromNBT(NBTTagCompound compound, boolean sync)
	{
		NBTTagList soundList = compound.getTagList("mutedSounds", 8);

		mutedSounds = new HashSet<ResourceLocation>();

		for (int i = 0; i < soundList.tagCount(); i++)
		{
			String sound = soundList.getStringTagAt(i);

			this.mutedSounds.add(new ResourceLocation(sound));
		}
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		super.breakBlock(worldIn, pos, state);

		this.invalidate();
	}

	@Override
	public void onChunkUnload()
	{
		this.invalidate();
	}
}
