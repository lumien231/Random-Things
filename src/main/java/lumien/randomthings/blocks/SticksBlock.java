package lumien.randomthings.blocks;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import lumien.randomthings.items.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;

public class SticksBlock extends Block
{
	boolean returning;

	public SticksBlock(boolean returning)
	{
		super(Block.Properties.create(Material.WOOD, MaterialColor.WOOD).hardnessAndResistance(0.5F).sound(SoundType.WOOD));

		this.returning = returning;
	}

	@Override
	public boolean isToolEffective(BlockState state, ToolType tool)
	{
		return tool == ToolType.AXE;
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
	{
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		if (!worldIn.isRemote)
		{
			worldIn.getPendingBlockTicks().scheduleTick(pos, this, 20 * 10);
		}
	}

	@Override
	public BlockRenderLayer getRenderLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}

	@OnlyIn(Dist.CLIENT)
	public float func_220080_a(BlockState state, IBlockReader worldIn, BlockPos pos)
	{
		return 1.0F;
	}

	public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos)
	{
		return true;
	}

	public boolean causesSuffocation(BlockState state, IBlockReader worldIn, BlockPos pos)
	{
		return false;
	}

	public boolean isNormalCube(BlockState state, IBlockReader worldIn, BlockPos pos)
	{
		return false;
	}

	public boolean canEntitySpawn(BlockState state, IBlockReader worldIn, BlockPos pos, EntityType<?> type)
	{
		return false;
	}

	@Override
	public void tick(BlockState state, World worldIn, BlockPos pos, Random random)
	{
		if (!worldIn.isRemote)
		{
			worldIn.removeBlock(pos, false);

			if (returning)
			{
				worldIn.playSound(null, pos, SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.BLOCKS, 0.6f, 1.2f);

				List<PlayerEntity> playerList = worldIn.getEntitiesWithinAABB(PlayerEntity.class, new AxisAlignedBB(pos).grow(50, 50, 50));

				if (!playerList.isEmpty())
				{
					Collections.sort(playerList, new Comparator<PlayerEntity>()
					{
						@Override
						public int compare(PlayerEntity o1, PlayerEntity o2)
						{
							return o1.getPosition().distanceSq(pos) >= o2.getPosition().distanceSq(pos) ? 1 : -1;
						}
					});

					PlayerEntity closes = playerList.get(0);

					if (!closes.isCreative())
					{
						closes.inventory.addItemStackToInventory(new ItemStack(returning ? ModItems.BLOCK_OF_STICKS_RETURNING : ModItems.BLOCK_OF_STICKS));
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
}
