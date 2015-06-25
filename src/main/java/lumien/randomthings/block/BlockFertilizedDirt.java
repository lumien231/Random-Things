package lumien.randomthings.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

public class BlockFertilizedDirt extends BlockBase
{
	boolean tilled;

	protected BlockFertilizedDirt(boolean tilled)
	{
		super("fertilizedDirt" + (tilled ? "Tilled" : ""), Material.ground);

		this.tilled = tilled;
		this.setTickRandomly(true);
		this.setHardness(0.6F);
		this.setStepSound(soundTypeGravel);

		if (tilled)
		{
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.9375F, 1.0F);
			this.setLightOpacity(255);
			this.setCreativeTab(null);
			this.useNeighborBrightness = true;
		}
	}

	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos pos)
	{
		return new ItemStack(ModBlocks.fertilizedDirt, 1, 0);
	}

	@Override
	public String getUnlocalizedName()
	{
		return "tile.fertilizedDirt";
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
	{
		return new AxisAlignedBB(pos, pos.add(1, 1, 1));
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return Item.getItemFromBlock(ModBlocks.fertilizedDirt);
	}

	@Override
	public boolean isOpaqueCube()
	{
		return !tilled;
	}

	@Override
	public boolean isFertile(World world, BlockPos pos)
	{
		return true;
	}

	@Override
	public boolean canSustainPlant(IBlockAccess world, BlockPos pos, EnumFacing direction, IPlantable plantable)
	{
		EnumPlantType plantType = plantable.getPlantType(world, pos.up());

		switch (plantType)
		{
			case Desert:
				return !tilled;
			case Nether:
				return false;
			case Crop:
				return tilled;
			case Cave:
				return !tilled;
			case Plains:
				return !tilled;
			case Water:
				return false;
			case Beach:
				return !tilled;
		}

		return false;
	}

	@Override
	public void updateTick(World worldObj, BlockPos pos, IBlockState state, Random rand)
	{
		if (!worldObj.isRemote)
		{
			IBlockState blockState = worldObj.getBlockState(pos.up());
			Block toBoost = blockState.getBlock();
			if (blockState != null && toBoost != null && toBoost != Blocks.air && toBoost instanceof IPlantable)
			{
				worldObj.playAuxSFX(2005, pos.up(), 0);
			}
			for (int i = 0; i < 3; i++)
			{
				blockState = worldObj.getBlockState(pos.up());
				toBoost = blockState.getBlock();
				if (blockState != null && toBoost != null && toBoost != Blocks.air && toBoost instanceof IPlantable)
				{
					toBoost.updateTick(worldObj, pos.up(), blockState, rand);
				}
			}
		}
	}
}
