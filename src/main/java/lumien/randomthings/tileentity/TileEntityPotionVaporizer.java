package lumien.randomthings.tileentity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import lumien.randomthings.block.BlockPotionVaporizer;
import lumien.randomthings.network.PacketHandler;
import lumien.randomthings.network.messages.MessagePotionVaporizerParticles;
import lumien.randomthings.util.InventoryUtil;
import lumien.randomthings.util.WorldUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ITickable;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

public class TileEntityPotionVaporizer extends TileEntityBase implements ITickable, ISidedInventory
{
	HashSet<BlockPos> affectedBlocks;

	InventoryBasic inventory;

	final int MAX_BLOCKS = 100;

	PotionEffect currentPotionEffect;

	int durationLeft;

	int fuelBurn;
	int fuelBurnTime;

	public TileEntityPotionVaporizer()
	{
		affectedBlocks = new HashSet<BlockPos>();

		validBlocks = new HashSet<BlockPos>();
		checkedBlocks = new HashSet<BlockPos>();
		toBeChecked = new ArrayList<BlockPos>();

		durationLeft = 1;
		fuelBurnTime = 0;

		inventory = new InventoryBasic("tile.potionVaporizer", false, 3);
	}

	@Override
	public void writeDataToNBT(NBTTagCompound compound)
	{
		InventoryUtil.saveInventoryInCompound(compound, inventory);

		compound.setInteger("durationLeft", durationLeft);
		compound.setInteger("fuelBurn", fuelBurn);
		compound.setInteger("fuelBurnTime", fuelBurnTime);

		NBTTagCompound potionEffectCompound = new NBTTagCompound();

		if (currentPotionEffect != null)
		{
			currentPotionEffect.writeCustomPotionEffectToNBT(potionEffectCompound);

			compound.setTag("currentPotionEffect", potionEffectCompound);
		}

		NBTTagList affectedBlocksList = new NBTTagList();

		for (BlockPos pos : affectedBlocks)
		{
			NBTTagCompound posCompound = new NBTTagCompound();

			posCompound.setInteger("posX", pos.getX());
			posCompound.setInteger("posY", pos.getY());
			posCompound.setInteger("posZ", pos.getZ());

			affectedBlocksList.appendTag(posCompound);
		}

		compound.setTag("affectedBlocks", affectedBlocksList);
	}

	@Override
	public void readDataFromNBT(NBTTagCompound compound)
	{
		InventoryUtil.readInventoryFromCompound(compound, inventory);

		durationLeft = compound.getInteger("durationLeft");
		fuelBurn = compound.getInteger("fuelBurn");
		fuelBurnTime = compound.getInteger("fuelBurnTime");

		if (compound.hasKey("currentPotionEffect"))
		{
			NBTTagCompound potionEffectCompound = compound.getCompoundTag("currentPotionEffect");

			this.currentPotionEffect = PotionEffect.readCustomPotionEffectFromNBT(potionEffectCompound);
		}

		NBTTagList affectedBlocksList = compound.getTagList("affectedBlocks", 10);

		for (int i = 0; i < affectedBlocksList.tagCount(); i++)
		{
			NBTTagCompound posCompound = affectedBlocksList.getCompoundTagAt(i);

			this.affectedBlocks.add(new BlockPos(posCompound.getInteger("posX"), posCompound.getInteger("posY"), posCompound.getInteger("posZ")));
		}
	}

	@Override
	public void update()
	{
		if (!worldObj.isRemote)
		{
			int roomSteps = affectedBlocks.size() > 0 ? 2 : 5;
			for (int i = 0; i < roomSteps; i++)
			{
				stepRoomDetection();
			}

			stepPotionTank();
			stepFuel();

			if (fuelBurnTime > 0 && affectedBlocks.size() > 0)
			{
				stepPotionEffect();
				spawnParticles();
			}
		}
	}

	public IInventory getInventory()
	{
		return inventory;
	}

	public int getDurationLeft()
	{
		return durationLeft;
	}

	public int getDuration()
	{
		if (this.currentPotionEffect != null)
		{
			return currentPotionEffect.getDuration();
		}
		else
		{
			return 0;
		}
	}

	public int getColor()
	{
		if (this.currentPotionEffect != null)
		{
			return Potion.potionTypes[currentPotionEffect.getPotionID()].getLiquidColor();
		}
		else
		{
			return 0;
		}
	}

	private void stepFuel()
	{
		if (fuelBurnTime > 0)
		{
			fuelBurnTime--;
		}
		else if (currentPotionEffect != null && affectedBlocks.size() > 0)
		{
			if (getStackInSlot(0) != null && durationLeft > 0)
			{
				fuelBurnTime = fuelBurn = TileEntityFurnace.getItemBurnTime(getStackInSlot(0));

				decrStackSize(0, 1);
			}
		}
	}

	private void stepPotionTank()
	{
		if (currentPotionEffect == null)
		{
			ItemStack newPotion = inventory.getStackInSlot(1);

			if (newPotion != null)
			{
				ItemStack output = getStackInSlot(2);

				if (output == null || output.stackSize < 64)
				{
					List<PotionEffect> effects = Items.potionitem.getEffects(newPotion);

					if (effects != null && !effects.isEmpty())
					{
						currentPotionEffect = new PotionEffect(effects.get(0));
						durationLeft = currentPotionEffect.getDuration();

						inventory.setInventorySlotContents(1, null);

						if (output != null)
						{
							output.stackSize++;
						}
						else
						{
							setInventorySlotContents(2, new ItemStack(Items.glass_bottle,1,0));
						}
					}
				}
			}
		}
	}

	private void stepPotionEffect()
	{
		if (!worldObj.isRemote)
		{
			if (currentPotionEffect != null)
			{
				durationLeft--;
				AxisAlignedBB[] bbs = new AxisAlignedBB[affectedBlocks.size()];

				int counter = 0;

				for (BlockPos pos : affectedBlocks)
				{
					bbs[counter] = new AxisAlignedBB(pos, pos.add(1, 1, 1));

					counter++;
				}

				for (EntityLivingBase entity : (List<EntityLivingBase>) WorldUtil.getEntitiesWithinAABBs(worldObj, EntityLivingBase.class, bbs))
				{
					PotionEffect activeEffect = entity.getActivePotionEffect(Potion.potionTypes[currentPotionEffect.getPotionID()]);
					boolean isNightVision = currentPotionEffect.getPotionID() == Potion.nightVision.id;
					if (activeEffect == null || activeEffect.getDuration() < (isNightVision ? 205 : 3))
					{
						PotionEffect applyEffect = new PotionEffect(new PotionEffect(currentPotionEffect.getPotionID(), isNightVision ? 205 : 80, currentPotionEffect.getAmplifier(), currentPotionEffect.getIsAmbient(), currentPotionEffect.getIsShowParticles()));
						entity.addPotionEffect(applyEffect);
					}
				}
			}
		}

		if (durationLeft == 0)
		{
			currentPotionEffect = null;
		}
	}

	private void spawnParticles()
	{
		if (!worldObj.isRemote && currentPotionEffect != null && worldObj.getTotalWorldTime() % 5 == 0)
		{
			MessagePotionVaporizerParticles message = new MessagePotionVaporizerParticles(new ArrayList<BlockPos>(affectedBlocks), Potion.potionTypes[currentPotionEffect.getPotionID()].getLiquidColor());
			PacketHandler.INSTANCE.sendToAllAround(message, new TargetPoint(this.worldObj.provider.getDimensionId(), this.pos.getX(), this.pos.getY(), this.pos.getZ(), 32));
		}
	}

	HashSet<BlockPos> validBlocks;
	HashSet<BlockPos> checkedBlocks;
	ArrayList<BlockPos> toBeChecked;

	int checkCounter;
	boolean firstCheck = true;

	private void stepRoomDetection()
	{
		if (firstCheck)
		{
			EnumFacing facing = (EnumFacing) worldObj.getBlockState(pos).getValue(BlockPotionVaporizer.FACING);
			toBeChecked.add(this.pos.offset(facing));
			firstCheck = false;
		}
		if (checkCounter > MAX_BLOCKS)
		{
			affectedBlocks.clear();
			validBlocks.clear();
			reset();
		}
		else
		{
			if (toBeChecked.size() > 0)
			{
				BlockPos toCheck = toBeChecked.remove(0);
				if (!checkedBlocks.contains(toCheck))
				{
					checkedBlocks.add(toCheck);
					if (worldObj.isBlockLoaded(toCheck) && worldObj.isAirBlock(toCheck))
					{
						validBlocks.add(toCheck);
						checkCounter++;

						for (EnumFacing facing : EnumFacing.VALUES)
						{
							BlockPos nextPos = new BlockPos(toCheck.offset(facing));

							if (!checkedBlocks.contains(nextPos))
							{
								toBeChecked.add(nextPos);
							}
						}
					}
				}
			}
			else
			{
				reset();
			}
		}
	}

	private void reset()
	{
		affectedBlocks.clear();
		affectedBlocks.addAll(validBlocks);

		checkCounter = 0;
		toBeChecked.clear();
		validBlocks.clear();
		checkedBlocks.clear();

		EnumFacing facing = (EnumFacing) worldObj.getBlockState(pos).getValue(BlockPotionVaporizer.FACING);

		toBeChecked.add(this.pos.offset(facing));
	}

	@Override
	public int getSizeInventory()
	{
		return inventory.getSizeInventory();
	}

	@Override
	public ItemStack getStackInSlot(int index)
	{
		return inventory.getStackInSlot(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int count)
	{
		return inventory.decrStackSize(index, count);
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int index)
	{
		return inventory.getStackInSlotOnClosing(index);
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack)
	{
		inventory.setInventorySlotContents(index, stack);
	}

	@Override
	public int getInventoryStackLimit()
	{
		return inventory.getInventoryStackLimit();
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		return inventory.isUseableByPlayer(player);
	}

	@Override
	public void openInventory(EntityPlayer player)
	{
		inventory.openInventory(player);
	}

	@Override
	public void closeInventory(EntityPlayer player)
	{
		inventory.closeInventory(player);
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack)
	{
		return inventory.isItemValidForSlot(index, stack);
	}

	@Override
	public int getField(int id)
	{
		return inventory.getField(id);
	}

	@Override
	public void setField(int id, int value)
	{
		inventory.setField(id, value);
	}

	@Override
	public int getFieldCount()
	{
		return inventory.getFieldCount();
	}

	@Override
	public void clear()
	{
		inventory.clear();
	}

	@Override
	public String getCommandSenderName()
	{
		return inventory.getCommandSenderName();
	}

	@Override
	public boolean hasCustomName()
	{
		return inventory.hasCustomName();
	}

	@Override
	public IChatComponent getDisplayName()
	{
		return inventory.getDisplayName();
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side)
	{
		return new int[] { 0, 1, 2 };
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction)
	{
		if (index == 2)
		{
			return false;
		}

		switch (index)
		{
			case 0:
				return TileEntityFurnace.isItemFuel(itemStackIn);
			case 1:
				return itemStackIn.getItem() == Items.potionitem;
		}

		return false;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction)
	{
		return index == 2;
	}

	public int getPotionID()
	{
		if (currentPotionEffect == null)
		{
			return 0;
		}
		else
		{
			return currentPotionEffect.getPotionID();
		}
	}

	public int getAmplifier()
	{
		if (currentPotionEffect == null)
		{
			return 0;
		}
		else
		{
			return currentPotionEffect.getAmplifier();
		}
	}

	public int getFuelBurnTime()
	{
		return fuelBurnTime;
	}

	public int getFuelBurn()
	{
		return fuelBurn;
	}

	@Override
	public boolean writeNBTToDescriptionPacket()
	{
		return false;
	}
}
