package lumien.randomthings.handler.spectre;

import java.util.Arrays;
import java.util.List;

import com.google.common.collect.ImmutableList;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkProvider;

public class SpectreChunkProvider implements IChunkProvider
{
	World worldObj;

	public SpectreChunkProvider(World worldObj)
	{
		this.worldObj = worldObj;
	}

	@Override
	public boolean chunkExists(int x, int z)
	{
		return true;
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
	public Chunk provideChunk(BlockPos blockPosIn)
	{
		return this.provideChunk(blockPosIn.getX() >> 4, blockPosIn.getZ() >> 4);
	}

	@Override
	public void populate(IChunkProvider p_73153_1_, int p_73153_2_, int p_73153_3_)
	{
	}

	@Override
	public boolean func_177460_a(IChunkProvider p_177460_1_, Chunk p_177460_2_, int p_177460_3_, int p_177460_4_)
	{
		return false;
	}

	@Override
	public boolean saveChunks(boolean p_73151_1_, IProgressUpdate p_73151_2_)
	{
		return true;
	}

	@Override
	public boolean unloadQueuedChunks()
	{
		return false;
	}

	@Override
	public boolean canSave()
	{
		return true;
	}

	@Override
	public String makeString()
	{
		return "Spectre";
	}

	@Override
	public List getPossibleCreatures(EnumCreatureType p_177458_1_, BlockPos p_177458_2_)
	{
		return ImmutableList.of();
	}

	@Override
	public BlockPos getStrongholdGen(World worldIn, String p_180513_2_, BlockPos p_180513_3_)
	{
		return null;
	}

	@Override
	public int getLoadedChunkCount()
	{
		return 0;
	}

	@Override
	public void recreateStructures(Chunk p_180514_1_, int p_180514_2_, int p_180514_3_)
	{
	}

	@Override
	public void saveExtraData()
	{
	}

}
