package lumien.randomthings.block;

import java.util.Random;

import lumien.randomthings.item.block.ItemBlockBlockDiaphanous;
import lumien.randomthings.tileentity.TileEntityBlockDiaphanous;
import lumien.randomthings.util.WorldUtil;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.particle.ParticleDigging;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockBlockDiaphanous extends BlockContainerBase
{
	static final AxisAlignedBB EMPTY = new AxisAlignedBB(0, 0, 0, 0, 0, 0);

	protected BlockBlockDiaphanous()
	{
		super("diaphanousBlock", Material.GLASS, ItemBlockBlockDiaphanous.class);

		this.setSoundType(SoundType.GLASS);
		this.setHardness(0.3F);
	}
	
	@Override
	public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items)
	{
		
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		TileEntityBlockDiaphanous te = (TileEntityBlockDiaphanous) worldIn.getTileEntity(pos);

		ItemStack drop = new ItemStack(this);

		IBlockState displayState = te.getDisplayState();

		drop.setTagCompound(new NBTTagCompound());

		drop.getTagCompound().setString("block", displayState.getBlock().getRegistryName().toString());
		drop.getTagCompound().setInteger("meta", displayState.getBlock().getMetaFromState(displayState));
		drop.getTagCompound().setBoolean("inverted", te.isInverted());

		WorldUtil.spawnItemStack(worldIn, pos, drop);
	}

	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{

	}
	
	@Override
	public boolean addLandingEffects(IBlockState state, WorldServer worldObj, BlockPos blockPosition, IBlockState iblockstate, EntityLivingBase entity, int numberOfParticles)
	{
		TileEntityBlockDiaphanous te = (TileEntityBlockDiaphanous) worldObj.getTileEntity(blockPosition);

		IBlockState display = te.getDisplayState();
		Block b = display.getBlock();

		((WorldServer)worldObj).spawnParticle(EnumParticleTypes.BLOCK_DUST, blockPosition.getX(), blockPosition.getY(), blockPosition.getZ(), numberOfParticles, 0.0D, 0.0D, 0.0D, 0.15000000596046448D, Block.getStateId(display));

		return true;
	}

	@Override
	public boolean addDestroyEffects(World world, BlockPos pos, ParticleManager manager)
	{
		TileEntityBlockDiaphanous te = (TileEntityBlockDiaphanous) world.getTileEntity(pos);

		IBlockState display = te.getDisplayState();
		Block b = display.getBlock();

		try
		{
			manager.addBlockDestroyEffects(pos, display);
		}
		catch (Exception e)
		{

		}

		return true;
	}

	static final Random rand = new Random();

	@Override
	public boolean addHitEffects(IBlockState state, World worldObj, RayTraceResult target, ParticleManager manager)
	{
		if (target.typeOfHit == RayTraceResult.Type.BLOCK)
		{
			TileEntityBlockDiaphanous te = (TileEntityBlockDiaphanous) worldObj.getTileEntity(target.getBlockPos());

			if (te instanceof TileEntityBlockDiaphanous)
			{
				IBlockState display = te.getDisplayState();
				Block b = display.getBlock();

				BlockPos pos = target.getBlockPos();

				try
				{
					int i = pos.getX();
					int j = pos.getY();
					int k = pos.getZ();
					float f = 0.1F;
					AxisAlignedBB axisalignedbb = display.getBoundingBox(worldObj, pos);
					double d0 = (double) i + rand.nextDouble() * (axisalignedbb.maxX - axisalignedbb.minX - 0.20000000298023224D) + 0.10000000149011612D + axisalignedbb.minX;
					double d1 = (double) j + rand.nextDouble() * (axisalignedbb.maxY - axisalignedbb.minY - 0.20000000298023224D) + 0.10000000149011612D + axisalignedbb.minY;
					double d2 = (double) k + rand.nextDouble() * (axisalignedbb.maxZ - axisalignedbb.minZ - 0.20000000298023224D) + 0.10000000149011612D + axisalignedbb.minZ;

					EnumFacing side = target.sideHit;

					if (side == EnumFacing.DOWN)
					{
						d1 = (double) j + axisalignedbb.minY - 0.10000000149011612D;
					}

					if (side == EnumFacing.UP)
					{
						d1 = (double) j + axisalignedbb.maxY + 0.10000000149011612D;
					}

					if (side == EnumFacing.NORTH)
					{
						d2 = (double) k + axisalignedbb.minZ - 0.10000000149011612D;
					}

					if (side == EnumFacing.SOUTH)
					{
						d2 = (double) k + axisalignedbb.maxZ + 0.10000000149011612D;
					}

					if (side == EnumFacing.WEST)
					{
						d0 = (double) i + axisalignedbb.minX - 0.10000000149011612D;
					}

					if (side == EnumFacing.EAST)
					{
						d0 = (double) i + axisalignedbb.maxX + 0.10000000149011612D;
					}

					manager.addEffect(((ParticleDigging) new ParticleDigging.Factory().createParticle(0, worldObj, d0, d1, d2, 0.0D, 0.0D, 0.0D, Block.getStateId(display))).setBlockPos(pos).multiplyVelocity(0.2F).multipleParticleScaleBy(0.6F));
				}
				catch (Exception e)
				{

				}
			}
		}
		return true;
	}

	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}

	@Override
	public boolean isNormalCube(IBlockState state)
	{
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
	{
		TileEntity te =  worldIn.getTileEntity(pos);

		return (te instanceof TileEntityBlockDiaphanous && ((TileEntityBlockDiaphanous)te).isInverted()) ? Block.FULL_BLOCK_AABB : Block.NULL_AABB;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos)
	{
		EntityPlayerSP thePlayer = Minecraft.getMinecraft().player;

		if (thePlayer != null)
		{
			for (EnumHand hand : EnumHand.values())
			{
				ItemStack held = thePlayer.getHeldItem(hand);

				if (!held.isEmpty() && held.getItem() == Item.getItemFromBlock(ModBlocks.blockDiaphanous))
				{
					return Block.FULL_BLOCK_AABB.offset(pos);
				}
			}
		}

		return EMPTY;
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);

		if (stack.hasTagCompound() && stack.getTagCompound().hasKey("block"))
		{
			NBTTagCompound compound = stack.getTagCompound();
			IBlockState toDisplay;

			Block b = Block.REGISTRY.getObject(new ResourceLocation(compound.getString("block")));
			int meta = compound.getInteger("meta");

			try
			{
				toDisplay = b.getStateFromMeta(meta);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				toDisplay = Blocks.STONE.getDefaultState();
			}

			TileEntityBlockDiaphanous te = (TileEntityBlockDiaphanous) worldIn.getTileEntity(pos);
			te.setDisplayState(toDisplay);
			te.setInverted(compound.getBoolean("inverted"));
		}

		if (!worldIn.isRemote)
		{
			neighborChanged(state, worldIn, pos, this, pos);
		}
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new TileEntityBlockDiaphanous();
	}
}
