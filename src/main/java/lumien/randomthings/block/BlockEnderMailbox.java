package lumien.randomthings.block;

import java.util.Random;
import com.mojang.authlib.GameProfile;

import lumien.randomthings.RandomThings;
import lumien.randomthings.handler.EnderLetterHandler;
import lumien.randomthings.handler.EnderLetterHandler.EnderMailboxInventory;
import lumien.randomthings.item.ModItems;
import lumien.randomthings.lib.GuiIds;
import lumien.randomthings.tileentity.TileEntityEnderMailbox;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockEnderMailbox extends BlockContainerBase
{
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	public static final PropertyBool ACTIVE = PropertyBool.create("active");

	protected BlockEnderMailbox()
	{
		super("enderMailbox", Material.rock);

		this.setHardness(1.5F);
		this.setResistance(10.0F);
		this.setStepSound(soundTypePiston);
		this.setTickRandomly(true);
		this.setBlockBounds(0.3125F, 0F, 0.0625F, 1 - 0.3125F, 1.375F, 1 - 0.0625F);
	}

	@Override
	public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		if ((Boolean) state.getValue(ACTIVE))
		{
			for (int i = 0; i < 4; ++i)
			{
				double d0 = pos.getX() + rand.nextFloat();
				double d1 = pos.getY() + rand.nextFloat();
				double d2 = pos.getZ() + rand.nextFloat();
				double d3 = (rand.nextFloat() - 0.5D) * 0.5D;
				double d4 = (rand.nextFloat() - 0.5D) * 0.5D;
				double d5 = (rand.nextFloat() - 0.5D) * 0.5D;
				int j = rand.nextInt(2) * 2 - 1;

				if (worldIn.getBlockState(pos.west()).getBlock() != this && worldIn.getBlockState(pos.east()).getBlock() != this)
				{
					d0 = pos.getX() + 0.5D + 0.25D * j;
					d3 = rand.nextFloat() * 2.0F * j;
				}
				else
				{
					d2 = pos.getZ() + 0.5D + 0.25D * j;
					d5 = rand.nextFloat() * 2.0F * j;
				}

				worldIn.spawnParticle(EnumParticleTypes.PORTAL, d0, d1, d2, d3, d4, d5, new int[0]);
			}
		}
	}

	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return state.withProperty(ACTIVE, true);
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (!worldIn.isRemote)
		{
			if (playerIn.isSneaking())
			{
				ItemStack equipped = playerIn.getCurrentEquippedItem();

				if (equipped.getItem() == ModItems.enderLetter)
				{
					NBTTagCompound compound;

					if ((compound = equipped.getTagCompound()) != null)
					{
						if (compound.hasKey("receiver") && !compound.getBoolean("received"))
						{
							GameProfile playerProfile = MinecraftServer.getServer().getPlayerProfileCache().getGameProfileForUsername(compound.getString("receiver"));

							if (playerProfile != null && playerProfile.getId() != null)
							{
								EnderMailboxInventory mailboxInventory = EnderLetterHandler.get(worldIn).getOrCreateInventoryForPlayer(playerProfile.getId());

								for (int slot = 0; slot < mailboxInventory.getSizeInventory(); slot++)
								{
									if (mailboxInventory.getStackInSlot(slot) == null)
									{
										ItemStack sendingLetter = equipped.copy();
										equipped.stackSize = 0;
										sendingLetter.getTagCompound().setBoolean("received", true);
										sendingLetter.getTagCompound().setString("sender", playerIn.getGameProfile().getName());

										mailboxInventory.setInventorySlotContents(slot, sendingLetter);

										playerIn.worldObj.playSoundAtEntity(playerIn, "mob.endermen.portal", 1, 1);

										return true;
									}
								}

								playerIn.addChatComponentMessage(new ChatComponentTranslation("item.enderLetter.noSpace").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.DARK_PURPLE)));
							}
							else
							{
								playerIn.addChatComponentMessage(new ChatComponentTranslation("item.enderLetter.noPlayer", compound.getString("receiver")).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.DARK_PURPLE)));
							}
						}
					}

					return true;
				}
			}

			TileEntityEnderMailbox te = (TileEntityEnderMailbox) worldIn.getTileEntity(pos);

			if (te.getOwner() != null)
			{
				if (te.getOwner().equals(playerIn.getGameProfile().getId()))
				{
					playerIn.openGui(RandomThings.instance, GuiIds.ENDER_MAILBOX, worldIn, pos.getX(), pos.getY(), pos.getZ());
				}
				else
				{
					playerIn.addChatComponentMessage(new ChatComponentTranslation("block.enderMailbox.owner").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
				}
			}
		}
		return true;
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
	{
		return worldIn.isAirBlock(pos.up()) && super.canPlaceBlockAt(worldIn, pos);
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
	{
		EnumFacing facing = (EnumFacing) worldIn.getBlockState(pos).getValue(FACING);

		if (facing == EnumFacing.SOUTH || facing == EnumFacing.NORTH)
		{
			this.setBlockBounds(0.3125F, 0F, 0.0625F, 1 - 0.3125F, 1.375F, 1 - 0.0625F);
		}
		else
		{
			this.setBlockBounds(0.0625F, 0F, 0.3125F, 1 - 0.0625F, 1.375F, 1 - 0.3125F);
		}
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	public boolean isFullCube()
	{
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IBlockState getStateForEntityRender(IBlockState state)
	{
		return this.getDefaultState().withProperty(FACING, EnumFacing.SOUTH);
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		boolean active;
		if (meta > 5)
		{
			active = true;
			meta -= 6;
		}
		else
		{
			active = false;
		}

		EnumFacing enumfacing = EnumFacing.getFront(meta);

		if (enumfacing.getAxis() == EnumFacing.Axis.Y)
		{
			enumfacing = EnumFacing.NORTH;
		}

		return this.getDefaultState().withProperty(FACING, enumfacing).withProperty(ACTIVE, active);
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return ((EnumFacing) state.getValue(FACING)).getIndex() + ((Boolean) state.getValue(ACTIVE) ? 6 : 0);
	}

	@Override
	protected BlockState createBlockState()
	{
		return new BlockState(this, new IProperty[] { FACING, ACTIVE });
	}

	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
	{
		return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite()).withProperty(ACTIVE, false);
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()).withProperty(ACTIVE, false), 2);

		if (!worldIn.isRemote && placer instanceof EntityPlayer)
		{
			TileEntityEnderMailbox te = (TileEntityEnderMailbox) worldIn.getTileEntity(pos);

			te.setOwner(((EntityPlayer) placer).getGameProfile().getId());
		}
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new TileEntityEnderMailbox();
	}

	@Override
	public int getRenderType()
	{
		return 3;
	}
}
