package lumien.randomthings.handler;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import lumien.randomthings.item.ModItems;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EscapeRopeHandler
{
	static EscapeRopeHandler INSTANCE;

	List<Task> runningTasks = new ArrayList<Task>();

	class Task
	{
		WeakReference<EntityPlayerMP> player;

		ArrayList<BlockPos> toCheck = new ArrayList<BlockPos>();
		HashSet<BlockPos> alreadyChecked = new HashSet<BlockPos>();
	}

	public void addTask(EntityPlayerMP player)
	{
		Task t = new Task();
		t.player = new WeakReference<EntityPlayerMP>(player);
		t.toCheck.add(player.getPosition());

		this.runningTasks.add(t);
	}

	public void tick()
	{
		Iterator<Task> iterator = runningTasks.iterator();

		while (iterator.hasNext())
		{
			Task t = iterator.next();
			EntityPlayerMP actualPlayer = t.player.get();

			if (actualPlayer != null && actualPlayer.world != null)
			{
				ItemStack holding = actualPlayer.getActiveItemStack();

				if (!holding.isEmpty() && holding.getItem() == ModItems.escapeRope)
				{
					ArrayList<BlockPos> toCheck = t.toCheck;
					HashSet<BlockPos> alreadyChecked = t.alreadyChecked;
					World world = actualPlayer.world;

					for (int runs = 0; runs < 4; runs++)
					{
						boolean finished = false;
						if (toCheck.isEmpty() || alreadyChecked.size() > 10000)
						{
							finished = true;

							actualPlayer.dropItem(holding, false);
							actualPlayer.setHeldItem(actualPlayer.getActiveHand(), ItemStack.EMPTY);
						}
						else
						{
							BlockPos nextPos = toCheck.remove(toCheck.size() - 1);

							while (alreadyChecked.contains(nextPos) && toCheck.size() > 0)
							{
								nextPos = toCheck.remove(toCheck.size() - 1);
							}

							if (!alreadyChecked.contains(nextPos))
							{
								if (world.isChunkGeneratedAt(nextPos.getX() >> 4, nextPos.getZ() >> 4) && world.isAirBlock(nextPos) || world.getBlockState(nextPos).getCollisionBoundingBox(world, nextPos) == null)
								{
									if (world.canBlockSeeSky(nextPos))
									{
										world.playSound(actualPlayer, actualPlayer.getPosition(), SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.PLAYERS, 0.5f, 1);

										boolean foundSolid = false;
										for (int y = nextPos.getY(); y >= 0; y--)
										{
											BlockPos target = new BlockPos(nextPos.getX(), y, nextPos.getZ());
											if (world.isSideSolid(target, EnumFacing.UP) || world.getBlockState(target).isFullBlock())
											{
												actualPlayer.connection.setPlayerLocation(target.getX() + 0.5, target.getY() + 1, target.getZ() + 0.5, actualPlayer.rotationYaw, actualPlayer.rotationPitch);

												foundSolid = true;
												break;
											}
										}

										if (!foundSolid)
										{
											actualPlayer.connection.setPlayerLocation(nextPos.getX() + 0.5, nextPos.getY() + 1, nextPos.getZ() + 0.5, actualPlayer.rotationYaw, actualPlayer.rotationPitch);
										}

										actualPlayer.resetActiveHand();

										world.playSound(null, actualPlayer.getPosition(), SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.PLAYERS, 0.5f, 1);

										holding.damageItem(1, actualPlayer);
										finished = true;
									}
									else
									{
										alreadyChecked.add(nextPos);

										if (!alreadyChecked.contains(nextPos.offset(EnumFacing.DOWN)))
										{
											toCheck.add(nextPos.offset(EnumFacing.DOWN));
										}

										for (EnumFacing facing : EnumFacing.HORIZONTALS)
										{
											BlockPos addPos = new BlockPos(nextPos.offset(facing));

											if (!alreadyChecked.contains(addPos))
											{
												toCheck.add(addPos);
											}
										}

										if (!alreadyChecked.contains(nextPos.offset(EnumFacing.UP)))
										{
											toCheck.add(nextPos.offset(EnumFacing.UP));
										}
									}
								}
							}
							else
							{
								finished = true;
							}
						}

						if (finished)
						{
							iterator.remove();
							break;
						}
					}
				}
				else
				{
					iterator.remove();
				}
			}
		}
	}

	public static EscapeRopeHandler getInstance()
	{
		return INSTANCE != null ? INSTANCE : (INSTANCE = new EscapeRopeHandler());
	}
}
