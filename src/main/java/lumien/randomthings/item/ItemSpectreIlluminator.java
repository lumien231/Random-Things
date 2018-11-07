package lumien.randomthings.item;

import lumien.randomthings.entitys.EntitySpectreIlluminator;
import lumien.randomthings.handler.spectreilluminator.SpectreIlluminationHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

public class ItemSpectreIlluminator extends ItemBase
{

	public ItemSpectreIlluminator()
	{
		super("spectreIlluminator");
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (!worldIn.isRemote)
		{
			SpectreIlluminationHandler handler = SpectreIlluminationHandler.get(worldIn);

			if (!handler.isIlluminated(pos))
			{
				ChunkPos cp = new ChunkPos(pos);
				if (worldIn.getEntitiesWithinAABB(EntitySpectreIlluminator.class, new AxisAlignedBB(new BlockPos(cp.getXStart() - 2, 0, cp.getZStart() - 2), new BlockPos(cp.getXEnd() + 2, 255, cp.getZEnd() + 2))).size() > 0)
				{
					return EnumActionResult.FAIL;
				}

				EntitySpectreIlluminator illuminator = new EntitySpectreIlluminator(worldIn, pos.getX() + 0.5, pos.getY() + 1.2, pos.getZ() + 0.5);

				worldIn.spawnEntity(illuminator);
				
				if (!player.capabilities.isCreativeMode)
				{
					ItemStack me = player.getHeldItem(hand);
					me.shrink(1);
				}
			}
		}

		return EnumActionResult.SUCCESS;
	}
}
