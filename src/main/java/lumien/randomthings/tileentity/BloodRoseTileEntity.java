package lumien.randomthings.tileentity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import lumien.randomthings.block.ModBlocks;
import lumien.randomthings.client.vfx.EFFECT;
import lumien.randomthings.item.ModItems;
import lumien.randomthings.network.RTPacketHandler;
import lumien.randomthings.network.messages.VisualEffectMessage;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.CubeCoordinateIterator;
import net.minecraft.util.math.Vec3d;

public class BloodRoseTileEntity extends TileEntity implements ITickableTileEntity
{
	BlockPos targetSpreadPos;

	int spreadTick;
	int damageTick;

	int progress;

	final static int MAX_PROGRESS = 20;

	public BloodRoseTileEntity()
	{
		super(ModTileEntityTypes.BLOOD_ROSE);

		this.damageTick = 0;
	}

	@Override
	public void tick()
	{
		if (!this.world.isRemote)
		{
			if (progress == MAX_PROGRESS)
			{
				if (spreadTick-- == 0)
				{
					spread();
				}
			}
			else
			{
				if (damageTick-- <= 0)
				{
					List<LivingEntity> nearbyEntities = this.world.getEntitiesWithinAABB(LivingEntity.class, new AxisAlignedBB(this.pos).grow(5)).stream().filter((e) -> {
						return !e.isInvulnerable() && !(e instanceof PlayerEntity && ((PlayerEntity) e).isCreative());
					}).collect(Collectors.toList());

					if (!nearbyEntities.isEmpty())
					{
						LivingEntity target = nearbyEntities.get(this.world.getRandom().nextInt(nearbyEntities.size()));

						if (target.attackEntityFrom(DamageSource.MAGIC, 4F))
						{
							Vec3d visualOffset = this.getBlockState().getOffset(world, pos);

							RTPacketHandler.sendToAllNear(new VisualEffectMessage(EFFECT.BLOOD_ROSE_DAMAGE, (pb) -> {
								pb.writeFloat((float) target.posX);
								pb.writeFloat((float) target.posY + target.getHeight() / 2);
								pb.writeFloat((float) target.posZ);

								pb.writeFloat(this.pos.getX() + 0.5F + (float) visualOffset.x);
								pb.writeFloat(this.pos.getY() + 0.9F);
								pb.writeFloat(this.pos.getZ() + 0.5F + (float) visualOffset.z);
							}), world, pos, 16 * 5F);

							if (this.progress++ == MAX_PROGRESS)
							{
								full();
							}
						}
					}

					damageTick = (this.world.getRandom().nextInt(5) + 15) * 20;
				}
			}
		}
	}

	private void spread()
	{
		this.progress = 0;
		this.spreadTick = 0;

		if (this.targetSpreadPos != null)
		{
			if (this.world.isAirBlock(targetSpreadPos) && this.getBlockState().isValidPosition(world, targetSpreadPos))
			this.world.setBlockState(targetSpreadPos, ModBlocks.BLOOD_ROSE.getDefaultState());

			targetSpreadPos = null;
		}
	}

	private void full()
	{
		this.spreadTick = 60;

		CubeCoordinateIterator iter = new CubeCoordinateIterator(this.pos.getX() - 3, this.pos.getY() - 3, this.pos.getZ() - 3, this.pos.getX() + 3, this.pos.getY() + 3, this.pos.getZ() + 3);

		List<BlockPos> possiblePositions = new ArrayList<BlockPos>(7 * 7 * 7);
		while (iter.hasNext())
		{
			possiblePositions.add(new BlockPos(iter.getX(), iter.getY(), iter.getZ()));
		}

		Collections.shuffle(possiblePositions);

		for (BlockPos targetPos : possiblePositions)
		{
			if (!this.world.isOutsideBuildHeight(targetPos) && this.world.isAirBlock(targetPos) && ModBlocks.BLOOD_ROSE.getDefaultState().isValidPosition(this.world, targetPos))
			{
				this.targetSpreadPos = targetPos;

				Vec3d visualOffsetTarget = this.getBlockState().getOffset(world, targetSpreadPos);
				Vec3d visualOffsetOrigin = this.getBlockState().getOffset(world, pos);

				// Spread Effect
				RTPacketHandler.sendToAllNear(new VisualEffectMessage(EFFECT.BLOOD_ROSE_SPREAD, (pb) -> {
					pb.writeFloat(this.pos.getX() + 0.5F + (float) visualOffsetOrigin.x);
					pb.writeFloat(this.pos.getY() + 0.9F);
					pb.writeFloat(this.pos.getZ() + 0.5F + (float) visualOffsetOrigin.z);

					pb.writeFloat((float) targetSpreadPos.getX() + 0.5F + (float) visualOffsetTarget.x);
					pb.writeFloat((float) targetSpreadPos.getY() + 0.9f);
					pb.writeFloat((float) targetSpreadPos.getZ() + 0.5F + (float) visualOffsetTarget.z);
				}), world, pos, 16 * 5F);
				
				return;
			}
		}

		// Drop Petals instead
		int petalCount = this.world.rand.nextInt(2) + 1;
		ItemStack stack = new ItemStack(ModItems.BLOOD_ROSE_PETAL, petalCount);

		double d0 = (double) (world.rand.nextFloat() * 0.7F) + (double) 0.15F;
		double d1 = (double) (world.rand.nextFloat() * 0.7F) + (double) 0.060000002F + 0.6D;
		double d2 = (double) (world.rand.nextFloat() * 0.7F) + (double) 0.15F;
		ItemEntity itementity = new ItemEntity(world, (double) pos.getX() + d0, (double) pos.getY() + d1, (double) pos.getZ() + d2, stack);
		itementity.setDefaultPickupDelay();
		world.addEntity(itementity);
	}
}
