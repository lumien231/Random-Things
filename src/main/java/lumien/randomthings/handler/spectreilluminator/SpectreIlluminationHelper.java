package lumien.randomthings.handler.spectreilluminator;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

public class SpectreIlluminationHelper
{
	public static void lightUpdateChunk(World world, ChunkPos cp)
	{
		for (int x = cp.getXStart() - 1; x <= cp.getXEnd() + 1; x++)
		{
			for (int z = cp.getZStart() - 1; z <= cp.getZEnd() + 1; z++)
			{
				for (int y = 0; y < 255; y++)
				{
					world.checkLightFor(EnumSkyBlock.BLOCK, new BlockPos(x, y, z));
				}
			}
		}
	}
}
