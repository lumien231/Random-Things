package lumien.randomthings.handler.magicavoxel;

import java.util.ArrayList;
import java.util.List;

public class MagicaVoxelModel
{
	public class Voxel
	{
		int x, y, z, colorIndex;

		public Voxel(int x, int y, int z, int colorIndex)
		{
			this.x = x;
			this.y = y;
			this.z = z;
			this.colorIndex = colorIndex - 1;
		}
	}

	ArrayList<Voxel> voxels;
	Palette palette;

	int sizeX, sizeY, sizeZ;

	boolean build;

	MagicaVoxelRenderModel renderModel;
	MagicaVoxelRenderModel randomizedRenderModel;

	public MagicaVoxelModel(Palette palette)
	{
		voxels = new ArrayList<>();
		this.palette = palette;
		this.build = false;
	}

	public void build()
	{
		this.randomizedRenderModel = new MagicaVoxelRenderModel(this, true);
		this.randomizedRenderModel.build();

		this.renderModel = new MagicaVoxelRenderModel(this, false);
		this.renderModel.build();

		this.build = true;
	}

	public void addVoxel(int x, int y, int z, int colorIndex)
	{
		voxels.add(new Voxel(x, y, z, colorIndex));
	}

	public void setSize(int sizeX, int sizeY, int sizeZ)
	{
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.sizeZ = sizeZ;
	}

	public List<Voxel> getVoxels()
	{
		return voxels;
	}

	public Palette getPalette()
	{
		return palette;
	}

	public MagicaVoxelRenderModel getRenderModel(boolean randomized)
	{
		if (randomized)
		{
			return randomizedRenderModel;
		}
		else
		{
			return renderModel;
		}
	}

	public int getSizeX()
	{
		return sizeX;
	}

	public int getSizeY()
	{
		return sizeY;
	}

	public int getSizeZ()
	{
		return sizeZ;
	}

	public void cleanUp()
	{
		renderModel.cleanUp();
		randomizedRenderModel.cleanUp();
	}
}
