package lumien.randomthings.handler.spectre;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkGenerator;

public class SpectreChunkProvider implements IChunkGenerator
{
	World worldObj;

	public SpectreChunkProvider(World worldObj)
	{
		this.worldObj = worldObj;
	}

	@Override
	public Chunk provideChunk(int x, int z)
	{
		ChunkPrimer chunkprimer = new ChunkPrimer();

		Chunk chunk = new Chunk(this.worldObj, chunkprimer, x, z);
		chunk.resetRelightChecks();

		return chunk;
	}

	@Override
	public void populate(int x, int z)
	{

	}

	@Override
	public boolean generateStructures(Chunk chunkIn, int x, int z)
	{
		return false;
	}

	@Override
	public List<SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos)
	{
		return new ArrayList<>();
	}


	@Override
	public BlockPos getStrongholdGen(World worldIn, String structureName, BlockPos position, boolean p_180513_4_)
	{
		return null;
	}

	@Override
	public void recreateStructures(Chunk chunkIn, int x, int z)
	{
	}
}
