package lumien.randomthings.tileentity;

import lumien.randomthings.container.AdvancedRedstoneTorchContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class AdvancedRedstoneTorchTileEntity extends TileEntity implements INamedContainerProvider
{
	int signalStrengthRed = 15;
	int signalStrengthGreen = 0;

	public AdvancedRedstoneTorchTileEntity()
	{
		super(ModTileEntityTypes.ADVANCED_REDSTONE_TORCH);
	}

	@Override
	public CompoundNBT write(CompoundNBT compound)
	{
		super.write(compound);
		compound.putInt("signalStrengthRed", signalStrengthRed);
		compound.putInt("signalStrengthGreen", signalStrengthGreen);

		return compound;
	}

	@Override
	public void read(CompoundNBT compound)
	{
		super.read(compound);
		this.signalStrengthRed = compound.getInt("signalStrengthRed");
		this.signalStrengthGreen = compound.getInt("signalStrengthGreen");
	}

	public int signalStrengthRed()
	{
		return signalStrengthRed;
	}

	public int signalStrengthGreen()
	{
		return signalStrengthGreen;
	}

	@Override
	public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity playerEntity)
	{
		return new AdvancedRedstoneTorchContainer(windowId, IWorldPosCallable.of(this.world, pos));
	}

	@Override
	public ITextComponent getDisplayName()
	{
		return new TranslationTextComponent("randomthings.block.advanced_redstone_torch");
	}

	public void setSignalStrengthGreen(int newValue)
	{
		this.signalStrengthGreen = newValue;
	}

	public void setSignalStrengthRed(int newValue)
	{
		this.signalStrengthRed = newValue;
	}
}