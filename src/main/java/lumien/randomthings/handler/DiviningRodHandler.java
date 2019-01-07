package lumien.randomthings.handler;


import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import lumien.randomthings.item.ModItems;
import lumien.randomthings.item.diviningrod.ItemDiviningRod;
import lumien.randomthings.item.diviningrod.RodType;
import lumien.randomthings.util.client.RenderUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DiviningRodHandler
{
	public static DiviningRodHandler INSTANCE;

	LinkedHashSet<BlockPos> positionsToCheck;

	List<Indicator> indicators;

	HashMap<Block, Color> blockColorMap;

	public DiviningRodHandler()
	{
		blockColorMap = new HashMap<Block, Color>();
		positionsToCheck = new LinkedHashSet<BlockPos>();
		indicators = new ArrayList<Indicator>();

		blockColorMap.put(Blocks.COAL_ORE, new Color(20, 20, 20, 50));
		blockColorMap.put(Blocks.IRON_ORE, new Color(211, 180, 159, 50));
		blockColorMap.put(Blocks.GOLD_ORE, new Color(246, 233, 80, 50));
		blockColorMap.put(Blocks.LAPIS_ORE, new Color(5, 45, 150, 50));
		blockColorMap.put(Blocks.REDSTONE_ORE, new Color(211, 1, 1, 50));
		blockColorMap.put(Blocks.EMERALD_ORE, new Color(0, 220, 0, 50));
		blockColorMap.put(Blocks.DIAMOND_ORE, new Color(87, 221, 229, 50));
	}

	public boolean shouldGlow(RodType rodType)
	{
		return !indicators.isEmpty() && (indicators.stream().anyMatch((i) -> i.type == rodType));
	}

	int modX, modY, modZ;

	public void render()
	{
		float partialTicks = Minecraft.getMinecraft().getRenderPartialTicks();

		EntityPlayer player = Minecraft.getMinecraft().player;

		double playerX = player.prevPosX + (player.posX - player.prevPosX) * partialTicks;
		double playerY = player.prevPosY + (player.posY - player.prevPosY) * partialTicks;
		double playerZ = player.prevPosZ + (player.posZ - player.prevPosZ) * partialTicks;

		GlStateManager.disableDepth();
		RenderUtils.enableDefaultBlending();

		GlStateManager.translate(-playerX, -playerY, -playerZ);
		for (Indicator indicator : indicators)
		{
			float size = (1 - (indicator.duration / 160F)) * 0.2F + 0.1F;
			Color c = indicator.color;
			RenderUtils.drawCube((float) (indicator.target.getX() + 0.5 - size / 2), (float) (indicator.target.getY() + 0.5 - size / 2), (float) (indicator.target.getZ() + 0.5 - size / 2), size, c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
		}
		GlStateManager.translate(playerX, playerY, playerZ);

		GlStateManager.enableDepth();
	}

	public void tick()
	{
		Iterator<Indicator> indicatorIterator = indicators.iterator();

		while (indicatorIterator.hasNext())
		{
			Indicator i = indicatorIterator.next();

			i.duration--;

			if (i.duration == 0)
			{
				indicatorIterator.remove();
			}
		}

		EntityPlayer player = Minecraft.getMinecraft().player;

		if (player != null)
		{
			World world = player.world;

			if (world != null)
			{
				ItemStack main = player.getHeldItemMainhand();
				ItemStack off = player.getHeldItemOffhand();

				ItemStack rod = ItemStack.EMPTY;

				if (!main.isEmpty() && main.getItem() == ModItems.diviningRod)
				{
					rod = main;
				}
				else if (!off.isEmpty() && off.getItem() == ModItems.diviningRod)
				{
					rod = off;
				}

				if (!rod.isEmpty())
				{
					BlockPos playerPos = player.getPosition();
					RodType type = ItemDiviningRod.getRodType(rod);

					for (int i = 0; i < 60; i++)
					{
						modX++;

						if (modX == 6)
						{
							modX = -5;
							modZ++;

							if (modZ == 6)
							{
								modZ = -5;
								modY++;

								if (modY == 6)
								{
									modY = -5;
								}
							}
						}

						BlockPos target = playerPos.add(modX, modY, modZ);
						IBlockState blockState = world.getBlockState(target);
						
						
						if (world.isBlockLoaded(target))
						{
							if (type.matches(world, target, blockState))
							{
								Indicator indicator = new Indicator(target, 160, type.getIndicatorColor(world, target, blockState), type);

								indicators.add(indicator);
							}
						}
					}
				}
			}
		}
	}

	private static class Indicator
	{
		BlockPos target;
		int duration;
		Color color;
		
		RodType type;

		public Indicator(BlockPos target, int duration, Color color, RodType type)
		{
			super();
			this.target = target;
			this.duration = duration;
			this.color = color;
			this.type = type;
		}
	}

	public static DiviningRodHandler get()
	{
		if (INSTANCE == null)
		{
			INSTANCE = new DiviningRodHandler();
		}
		return INSTANCE;
	}
}
