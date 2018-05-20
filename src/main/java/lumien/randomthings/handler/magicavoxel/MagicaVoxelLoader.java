package lumien.randomthings.handler.magicavoxel;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.logging.log4j.Level;

import com.google.common.primitives.Ints;

import lumien.randomthings.RandomThings;

public class MagicaVoxelLoader
{
	public static MagicaVoxelModel getModel(InputStream modelInputStream, InputStream paletteInputStream) throws Exception
	{
		Palette palette = getPalette(paletteInputStream);

		for (int i = 0; i < 4; i++)
		{
			int b = modelInputStream.read();

			switch (i)
			{
			case 0:
				if (b != 'V')
				{
					modelInputStream.close();
					throw new Exception("Invalid VOX Header");
				}
				break;
			case 1:
				if (b != 'O')
				{
					modelInputStream.close();
					throw new Exception("Invalid VOX Header");
				}
				break;
			case 2:
				if (b != 'X')
				{
					modelInputStream.close();
					throw new Exception("Invalid VOX Header");
				}
				break;
			case 3:
				if (b != ' ')
				{
					modelInputStream.close();
					throw new Exception("Invalid VOX Header");
				}
				break;
			}
		}

		byte[] versionBytes = new byte[4];
		modelInputStream.read(versionBytes);

		int voxVersion = Ints.fromBytes(versionBytes[3], versionBytes[2], versionBytes[1], versionBytes[0]);

		RandomThings.instance.logger.log(Level.DEBUG, "VOX File Version: " + voxVersion);

		MagicaVoxelModel model = new MagicaVoxelModel(palette);

		byte[] chunkIDBytes = new byte[4];
		byte[] contentSizeBytes = new byte[4];
		byte[] childrenSizeBytes = new byte[4];

		while (modelInputStream.read(chunkIDBytes) > 0)
		{
			String chunkID = "" + (char) chunkIDBytes[0] + (char) chunkIDBytes[1] + (char) chunkIDBytes[2] + (char) chunkIDBytes[3];

			RandomThings.instance.logger.log(Level.DEBUG, " - CHUNK: " + chunkID);
			modelInputStream.read(contentSizeBytes);
			modelInputStream.read(childrenSizeBytes);

			int contentSize = Ints.fromBytes(contentSizeBytes[3], contentSizeBytes[2], contentSizeBytes[1], contentSizeBytes[0]);
			int childrenSize = Ints.fromBytes(childrenSizeBytes[3], childrenSizeBytes[2], childrenSizeBytes[1], childrenSizeBytes[0]);

			if (contentSize > 0)
			{
				byte[] chunkContent = new byte[contentSize];
				modelInputStream.read(chunkContent);
				ByteArrayInputStream contentStream = new ByteArrayInputStream(chunkContent);

				if (chunkID.equals("SIZE"))
				{
					byte[] bX = new byte[4];
					byte[] bY = new byte[4];
					byte[] bZ = new byte[4];

					contentStream.read(bX);
					contentStream.read(bY);
					contentStream.read(bZ);

					int x = Ints.fromBytes(bX[3], bX[2], bX[1], bX[0]);
					int y = Ints.fromBytes(bY[3], bY[2], bY[1], bY[0]);
					int z = Ints.fromBytes(bZ[3], bZ[2], bZ[1], bZ[0]);

					model.setSize(x, z, y);

					RandomThings.instance.logger.log(Level.DEBUG, " - Set Size: " + x + ":" + z + ":" + y);
				}
				else if (chunkID.equals("XYZI"))
				{
					byte[] numVoxelsBytes = new byte[4];
					contentStream.read(numVoxelsBytes);
					int numVoxels = Ints.fromBytes(numVoxelsBytes[3], numVoxelsBytes[2], numVoxelsBytes[1], numVoxelsBytes[0]);

					RandomThings.instance.logger.log(Level.DEBUG, " - Added " + numVoxels + " Voxels");

					for (int i = 0; i < numVoxels; i++)
					{
						int x = contentStream.read();
						int y = contentStream.read();
						int z = contentStream.read();
						int colorIndex = contentStream.read();

						model.addVoxel(x, z, y, colorIndex);
					}
				}
			}
		}

		modelInputStream.close();

		model.build();

		return model;
	}

	public static MagicaVoxelModel getModel(File modelFile, File paletteFile) throws Exception
	{
		return getModel(new FileInputStream(modelFile), new FileInputStream(paletteFile));
	}

	public static Palette getPalette(InputStream paletteInputStream) throws IOException
	{
		Color[] colorTable = new Color[256];

		for (int i = 0; i < colorTable.length; i++)
		{
			colorTable[i] = new Color(paletteInputStream.read(), paletteInputStream.read(), paletteInputStream.read());
		}

		paletteInputStream.close();

		return new Palette(colorTable);
	}
}
