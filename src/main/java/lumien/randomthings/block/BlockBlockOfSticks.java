package lumien.randomthings.block;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import lumien.randomthings.item.block.ItemBlockOfSticks;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockBlockOfSticks extends BlockBase
{
	public static PropertyBool RETURNING = PropertyBool.create("returning");

	protected BlockBlockOfSticks()
	{
		super("blockOfSticks", Material.WOOD, ItemBlockOfSticks.class);

		this.setHardness(0.2F);
		this.setLightOpacity(1);

		this.setDefaultState(this.blockState.getBaseState().withProperty(RETURNING, false));
	}

	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list)
	{
		for (int i = 0; i < 2; i++)
		{
			list.add(new ItemStack(ModBlocks.blockOfSticks, 1, i));
		}
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(RETURNING) ? 1 : 0;
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(RETURNING, meta == 0 ? false : true);
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] { RETURNING });
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		if (!worldIn.isRemote)
		{
			worldIn.scheduleUpdate(pos, this, 20 * 10);
		}
	}

	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		if (!worldIn.isRemote)
		{
			worldIn.setBlockToAir(pos);

			boolean returning = state.getValue(RETURNING);

			if (returning)
			{
				worldIn.playSound(null, pos, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.BLOCKS, 0.6f, 1.2f);

				List<EntityPlayer> playerList = worldIn.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(pos).grow(50, 50, 50));

				if (!playerList.isEmpty())
				{
					Collections.sort(playerList, new Comparator<EntityPlayer>()
					{
						@Override
						public int compare(EntityPlayer o1, EntityPlayer o2)
						{
							return o1.getPosition().distanceSq(pos) >= o2.getPosition().distanceSq(pos) ? 1 : -1;
						}
					});

					EntityPlayer closes = playerList.get(0);

					if (!closes.capabilities.isCreativeMode)
					{
						closes.inventory.addItemStackToInventory(new ItemStack(this, 1, 1));
					}
				}
			}
			else
			{
				worldIn.playSound(null, pos, SoundEvents.BLOCK_WOOD_BREAK, SoundCategory.BLOCKS, 0.6f, 1.2f);
				worldIn.playEvent(2001, pos, Block.getStateId(state));
			}
		}
	}

	@Override
	public boolean causesSuffocation(IBlockState state)
	{
		return false;
	}

	@Override
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT_MIPPED;
	}
}
