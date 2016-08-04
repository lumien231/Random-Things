package lumien.randomthings.block;

import java.awt.Color;

import lumien.randomthings.item.ModItems;
import lumien.randomthings.lib.IRTBlockColor;
import lumien.randomthings.tileentity.TileEntityBiomeRadar;
import lumien.randomthings.tileentity.TileEntityBiomeRadar.STATE;
import lumien.randomthings.util.WorldUtil;
import lumien.randomthings.util.client.RenderUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockBiomeRadar extends BlockContainerBase implements IRTBlockColor
{
	protected BlockBiomeRadar()
	{
		super("biomeRadar", Material.IRON);
		
		this.setHardness(5.0F);
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new TileEntityBiomeRadar();
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		TileEntityBiomeRadar radar = (TileEntityBiomeRadar) worldIn.getTileEntity(pos);
		
		if (radar.getCurrentCrystal()!=null)
		{
			WorldUtil.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), radar.getCurrentCrystal());
		}
		
		super.breakBlock(worldIn, pos, state);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int colorMultiplier(IBlockState state, IBlockAccess worldIn, BlockPos pos, int renderPass)
	{
		if (pos == null)
		{
			return Color.WHITE.getRGB();
		}

		return new Color(RenderUtils.getBiomeColor(worldIn, worldIn.getBiome(pos), pos)).brighter().getRGB();
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block neighborBlock)
	{
		((TileEntityBiomeRadar) worldIn.getTileEntity(pos)).neighborChanged(neighborBlock);
	}

	@Override
	public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		return true;
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		ItemStack equipped = playerIn.getHeldItemMainhand();
		TileEntityBiomeRadar biomeRadar = (TileEntityBiomeRadar) worldIn.getTileEntity(pos);

		if (biomeRadar.getState() == STATE.IDLE)
		{
			if (biomeRadar.getCurrentCrystal() == null)
			{
				if (equipped != null && equipped.getItem() == ModItems.biomeCrystal)
				{
					if (!worldIn.isRemote)
					{
						biomeRadar.setCrystal(equipped.copy());

						equipped.stackSize--;
						worldIn.playEvent(null, 1037, pos, 0);
					}

					return true;
				}
			}
			else
			{
				ItemStack currentCrystal;
				if (equipped == null && (currentCrystal = biomeRadar.getCurrentCrystal()) != null)
				{
					if (!worldIn.isRemote)
					{
						playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, currentCrystal);
						biomeRadar.setCrystal(null);

						worldIn.playEvent(null, 1036, pos, 0);
					}
					return true;
				}
			}
		}
		else if (biomeRadar.getState() == STATE.FINISHED)
		{
			if (equipped != null && equipped.getItem() == Items.PAPER)
			{
				if (!worldIn.isRemote)
				{
					ItemStack positionFilter = biomeRadar.generatePositionFilter();
					
					equipped.stackSize--;
					playerIn.inventory.addItemStackToInventory(positionFilter);
				}
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}
}
