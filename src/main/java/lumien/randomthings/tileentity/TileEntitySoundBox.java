package lumien.randomthings.tileentity;

import lumien.randomthings.block.BlockSoundBox;
import lumien.randomthings.block.ModBlocks;
import lumien.randomthings.item.ItemSoundPattern;
import lumien.randomthings.lib.IRedstoneSensitive;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

public class TileEntitySoundBox extends TileEntityBase implements IRedstoneSensitive
{
	ItemStack pattern = ItemStack.EMPTY;

	float volume = 1.0F;
	float pitch = 1.0F;

	public boolean hasPattern()
	{
		return !this.pattern.isEmpty();
	}

	public ItemStack getPattern()
	{
		return this.pattern;
	}

	public void insertPattern(ItemStack newPattern)
	{
		this.pattern = newPattern;

		if (!this.pattern.isEmpty() != this.world.getBlockState(this.pos).getValue(BlockSoundBox.HAS_PATTERN))
		{
			this.world.setBlockState(this.pos, ModBlocks.soundBox.getDefaultState().withProperty(BlockSoundBox.HAS_PATTERN, !this.pattern.isEmpty()));
		}

		this.markDirty();
	}

	@Override
	public boolean syncAdditionalData()
	{
		return false;
	}

	@Override
	public void writeDataToNBT(NBTTagCompound compound, boolean sync)
	{
		if (!pattern.isEmpty())
		{
			NBTTagCompound patternCompound = new NBTTagCompound();
			pattern.writeToNBT(patternCompound);
			compound.setTag("pattern", patternCompound);
		}
	}

	@Override
	public void readDataFromNBT(NBTTagCompound compound, boolean sync)
	{
		if (compound.hasKey("pattern"))
		{
			NBTTagCompound patternCompound = compound.getCompoundTag("pattern");

			this.pattern = new ItemStack(patternCompound);
		}
		else
		{
			this.pattern = ItemStack.EMPTY;
		}
	}

	@Override
	public void redstoneChange(boolean oldState, boolean newState)
	{
		if (!oldState && newState && !this.pattern.isEmpty())
		{
			ResourceLocation soundLocation = ItemSoundPattern.getSoundLocation(this.pattern);

			if (soundLocation != null)
			{
				SoundEvent soundEvent = SoundEvent.REGISTRY.getObject(soundLocation);

				if (soundEvent != null)
				{
					this.world.playSound(null, this.pos, soundEvent, SoundCategory.BLOCKS, volume, pitch);
				}
			}
		}
	}

}
