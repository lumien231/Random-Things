package lumien.randomthings.block;

import java.util.List;

import lumien.randomthings.container.ContainerCustomWorkbench;
import lumien.randomthings.tileentity.TileEntityCustomWorkbench;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockCustomWorkbench extends BlockContainerBase
{
	public static final WoodStateProperty WOOD_STATE = new WoodStateProperty();

	public BlockCustomWorkbench()
	{
		super("customWorkbench", Material.wood);

		this.setHardness(2.5F).setStepSound(soundTypeWood);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List list)
	{
		for (BlockPlanks.EnumType type : BlockPlanks.EnumType.values())
		{
			ItemStack workBench = new ItemStack(this);

			NBTTagCompound compound = new NBTTagCompound();

			workBench.setTagCompound(compound);

			compound.setString("woodName", "minecraft:planks");
			compound.setInteger("woodMeta", type.ordinal());

			list.add(workBench);
		}
	}

	@Override
	public boolean canRenderInLayer(EnumWorldBlockLayer layer)
	{
		return layer == EnumWorldBlockLayer.SOLID || layer == EnumWorldBlockLayer.TRANSLUCENT;
	}

	@Override
	public TileEntity createTileEntity(World world,IBlockState state)
	{
		return new TileEntityCustomWorkbench();
	}

	@Override
	public int getRenderType()
	{
		return 3;
	}

	@Override
	protected BlockState createBlockState()
	{
		return new ExtendedBlockState(this, new IProperty[] {}, new IUnlistedProperty[] { WOOD_STATE });
	}

	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
	{
		TileEntityCustomWorkbench te = (TileEntityCustomWorkbench) worldIn.getTileEntity(pos);
		IExtendedBlockState actualState = (IExtendedBlockState) state;

		if (te.getWoodState() != null)
		{
			return actualState.withProperty(WOOD_STATE, te.getWoodState());
		}
		else
		{
			return actualState.withProperty(WOOD_STATE, Blocks.planks.getDefaultState());
		}
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		NBTTagCompound compound;

		String woodName;
		int meta;

		if ((compound = stack.getTagCompound()) != null)
		{
			woodName = compound.getString("woodName");
			meta = compound.getInteger("woodMeta");
		}
		else
		{
			woodName = "minecraft:planks";
			meta = 0;
		}

		TileEntityCustomWorkbench te = (TileEntityCustomWorkbench) worldIn.getTileEntity(pos);

		Block woodBlock = Block.getBlockFromName(woodName);

		if (woodBlock == null)
		{
			woodBlock = Blocks.planks;
			meta = 0;
		}

		te.setWood(woodBlock, meta);
	}

	private static class WoodStateProperty implements IUnlistedProperty<IBlockState>
	{
		@Override
		public String getName()
		{
			return "woodState";
		}

		@Override
		public boolean isValid(IBlockState value)
		{
			return true;
		}

		@Override
		public Class<IBlockState> getType()
		{
			return IBlockState.class;
		}

		@Override
		public String valueToString(IBlockState value)
		{
			return value.toString();
		}
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (worldIn.isRemote)
		{
			return true;
		}
		else
		{
			playerIn.displayGui(new BlockCustomWorkbench.InterfaceCraftingTable(worldIn, pos));
			return true;
		}
	}

	public static class InterfaceCraftingTable implements IInteractionObject
	{
		private final World world;
		private final BlockPos position;
		private static final String __OBFID = "CL_00002127";

		public InterfaceCraftingTable(World worldIn, BlockPos pos)
		{
			this.world = worldIn;
			this.position = pos;
		}

		/**
		 * Gets the name of this command sender (usually username, but possibly "Rcon")
		 */
		@Override
		public String getCommandSenderName()
		{
			return null;
		}

		/**
		 * Returns true if this thing is named
		 */
		@Override
		public boolean hasCustomName()
		{
			return false;
		}

		/**
		 * Get the formatted ChatComponent that will be used for the sender's username in chat
		 */
		@Override
		public IChatComponent getDisplayName()
		{
			return new ChatComponentTranslation(ModBlocks.customWorkbench.getUnlocalizedName() + ".name", new Object[0]);
		}

		@Override
		public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
		{
			return new ContainerCustomWorkbench(playerInventory, this.world, this.position);
		}

		@Override
		public String getGuiID()
		{
			return "minecraft:crafting_table";
		}
	}
}
