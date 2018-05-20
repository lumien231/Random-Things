package lumien.randomthings.tileentity;

import lumien.randomthings.block.BlockSpecialChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntitySpecialChest extends TileEntityLockableLoot implements ITickable
{
	private NonNullList<ItemStack> chestContents = NonNullList.<ItemStack>withSize(27, ItemStack.EMPTY);

	/** The current angle of the lid (between 0 and 1) */
	public float lidAngle;
	/** The angle of the lid last tick */
	public float prevLidAngle;
	/** The number of players currently using this chest */
	public int numPlayersUsing;
	/** Server sync counter (once per 20 ticks) */
	private int ticksSinceSync;
	private int chestType;

	public TileEntitySpecialChest()
	{
	}

	public TileEntitySpecialChest(int typeIn)
	{
		this.chestType = typeIn;
	}

	public void setChestType(int chestType)
	{
		if (this.chestType != chestType)
		{
			this.chestType = chestType;
			this.markDirty();
		}
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
	{
		return (oldState.getBlock() != newState.getBlock());
	}

	/**
	 * Returns the number of slots in the inventory.
	 */
	@Override
	public int getSizeInventory()
	{
		return 27;
	}

	@Override
	public boolean isEmpty()
	{
		for (ItemStack itemstack : this.chestContents)
		{
			if (!itemstack.isEmpty())
			{
				return false;
			}
		}

		return true;
	}

	/**
	 * Get the name of this object. For players this returns their username
	 */
	@Override
	public String getName()
	{
		return this.hasCustomName() ? this.customName : "container.chest";
	}

	public static void registerFixesChest(DataFixer fixer)
	{
		fixer.registerWalker(FixTypes.BLOCK_ENTITY, new ItemStackDataLists(TileEntitySpecialChest.class, new String[] { "Items" }));
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		this.chestContents = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);

		if (!this.checkLootAndRead(compound))
		{
			ItemStackHelper.loadAllItems(compound, this.chestContents);
		}

		if (compound.hasKey("CustomName", 8))
		{
			this.customName = compound.getString("CustomName");
		}

		this.chestType = compound.getInteger("chestType");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);

		if (!this.checkLootAndWrite(compound))
		{
			ItemStackHelper.saveAllItems(compound, this.chestContents);
		}

		if (this.hasCustomName())
		{
			compound.setString("CustomName", this.customName);
		}

		compound.setInteger("chestType", chestType);

		return compound;
	}

	/**
	 * Returns the maximum stack size for a inventory slot. Seems to always be 64,
	 * possibly will be extended.
	 */
	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	/**
	 * Like the old updateEntity(), except more generic.
	 */
	@Override
	public void update()
	{
		int i = this.pos.getX();
		int j = this.pos.getY();
		int k = this.pos.getZ();
		++this.ticksSinceSync;

		if (!this.world.isRemote && this.numPlayersUsing != 0 && (this.ticksSinceSync + i + j + k) % 200 == 0)
		{
			this.numPlayersUsing = 0;
			float f = 5.0F;

			for (EntityPlayer entityplayer : this.world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(i - 5.0F, j - 5.0F, k - 5.0F, i + 1 + 5.0F, j + 1 + 5.0F, k + 1 + 5.0F)))
			{
				if (entityplayer.openContainer instanceof ContainerChest)
				{
					IInventory iinventory = ((ContainerChest) entityplayer.openContainer).getLowerChestInventory();

					if (iinventory == this)
					{
						++this.numPlayersUsing;
					}
				}
			}
		}

		this.prevLidAngle = this.lidAngle;
		float f1 = 0.1F;

		if (this.numPlayersUsing > 0 && this.lidAngle == 0.0F)
		{
			double d1 = i + 0.5D;
			double d2 = k + 0.5D;

			this.world.playSound((EntityPlayer) null, d1, j + 0.5D, d2, SoundEvents.BLOCK_CHEST_OPEN, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
		}

		if (this.numPlayersUsing == 0 && this.lidAngle > 0.0F || this.numPlayersUsing > 0 && this.lidAngle < 1.0F)
		{
			float f2 = this.lidAngle;

			if (this.numPlayersUsing > 0)
			{
				this.lidAngle += 0.1F;
			}
			else
			{
				this.lidAngle -= 0.1F;
			}

			if (this.lidAngle > 1.0F)
			{
				this.lidAngle = 1.0F;
			}

			float f3 = 0.5F;

			if (this.lidAngle < 0.5F && f2 >= 0.5F)
			{
				double d3 = i + 0.5D;
				double d0 = k + 0.5D;

				this.world.playSound((EntityPlayer) null, d3, j + 0.5D, d0, SoundEvents.BLOCK_CHEST_CLOSE, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
			}

			if (this.lidAngle < 0.0F)
			{
				this.lidAngle = 0.0F;
			}
		}
	}

	@Override
	public boolean receiveClientEvent(int id, int type)
	{
		if (id == 1)
		{
			this.numPlayersUsing = type;
			return true;
		}
		else
		{
			return super.receiveClientEvent(id, type);
		}
	}

	@Override
	public void openInventory(EntityPlayer player)
	{
		if (!player.isSpectator())
		{
			if (this.numPlayersUsing < 0)
			{
				this.numPlayersUsing = 0;
			}

			++this.numPlayersUsing;
			this.world.addBlockEvent(this.pos, this.getBlockType(), 1, this.numPlayersUsing);
			this.world.notifyNeighborsOfStateChange(this.pos, this.getBlockType(), false);
		}
	}

	@Override
	public void closeInventory(EntityPlayer player)
	{
		if (!player.isSpectator() && this.getBlockType() instanceof BlockSpecialChest)
		{
			--this.numPlayersUsing;
			this.world.addBlockEvent(this.pos, this.getBlockType(), 1, this.numPlayersUsing);
			this.world.notifyNeighborsOfStateChange(this.pos, this.getBlockType(), false);
		}
	}

	public int getChestType()
	{
		return chestType;
	}

	@Override
	public String getGuiID()
	{
		return "minecraft:chest";
	}

	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
	{
		this.fillWithLoot(playerIn);
		return new ContainerChest(playerInventory, this, playerIn);
	}

	@Override
	protected NonNullList<ItemStack> getItems()
	{
		return this.chestContents;
	}

	@Override
	public NBTTagCompound getUpdateTag()
	{
		NBTTagCompound baseCompound = super.getUpdateTag();

		baseCompound.setInteger("chestType", this.chestType);

		return baseCompound;
	}
}