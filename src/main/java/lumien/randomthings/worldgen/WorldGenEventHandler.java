package lumien.randomthings.worldgen;

import java.util.Random;

import lumien.randomthings.block.ModBlocks;
import lumien.randomthings.config.Worldgen;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class WorldGenEventHandler
{
	WorldGenPatches glowingMushRoomGen = new WorldGenPatches(ModBlocks.glowingMushroom);
	WorldGenPlants plantGenerator = new WorldGenPlants();

	@SubscribeEvent
	public void decorateBiome(DecorateBiomeEvent.Post event)
	{
		BlockPos chunkPos = event.getPos();
		Random random = event.getRand();

		if (Worldgen.GLOWING_MUSHROOM && event.getRand().nextInt(4) == 0)
		{
			int offX = random.nextInt(16) + 8;
			int offZ = random.nextInt(16) + 8;
			int offY = event.getWorld().getHeight(chunkPos.add(offX, 0, offZ)).getY() - 4;

			if (offY > 0)
			{
				int posY = random.nextInt(offY);
				glowingMushRoomGen.generate(event.getWorld(), random, chunkPos.add(offX, posY, offZ));
			}
		}

		plantGenerator.generate(event.getWorld(), event.getRand(), chunkPos);
	}
}
