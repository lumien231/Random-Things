package lumien.randomthings.handler.magicavoxel;

import java.awt.Color;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.OpenGLException;
import org.lwjgl.opengl.Util;

import lumien.randomthings.handler.magicavoxel.MagicaVoxelModel.Voxel;
import lumien.randomthings.util.client.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;

public class MagicaVoxelRenderModel
{
	MagicaVoxelModel model;
	Palette palette;

	int vertexBufferID;
	int colourBufferID;
	int normalBufferID;
	int numberIndices;

	boolean build;

	boolean randomized;

	public MagicaVoxelRenderModel(MagicaVoxelModel model, boolean randomized)
	{
		this.model = model;
		this.palette = model.getPalette();
		this.build = false;
		this.randomized = randomized;
	}

	public void build()
	{
		if (!build)
		{
			int arrayBuffer = GL11.glGetInteger(GL15.GL_ARRAY_BUFFER_BINDING);

			vertexBufferID = GL15.glGenBuffers();
			colourBufferID = GL15.glGenBuffers();
			normalBufferID = GL15.glGenBuffers();
			numberIndices = model.getVoxels().size();

			FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(numberIndices * 6 * 4 * 3);

			float size = 1F / 20f;

			for (Voxel voxel : model.getVoxels())
			{
				float x = voxel.x * size;
				float y = voxel.y * size;
				float z = voxel.z * size;

				vertexBuffer.put(new float[] { x + 0F, y + 0F, z + 0F });
				vertexBuffer.put(new float[] { x + 0F, y + size, z + 0F });
				vertexBuffer.put(new float[] { x + size, y + size, z + 0F });
				vertexBuffer.put(new float[] { x + size, y + 0F, z + 0F });

				vertexBuffer.put(new float[] { x + size, y + size, z + 0F });
				vertexBuffer.put(new float[] { x + size, y + size, z + size });
				vertexBuffer.put(new float[] { x + size, y + 0F, z + size });
				vertexBuffer.put(new float[] { x + size, y + 0F, z + 0F });

				vertexBuffer.put(new float[] { x + size, y + size, z + size });
				vertexBuffer.put(new float[] { x + 0F, y + size, z + size });
				vertexBuffer.put(new float[] { x + 0F, y + 0F, z + size });
				vertexBuffer.put(new float[] { x + size, y + 0F, z + size });

				vertexBuffer.put(new float[] { x + 0F, y + size, z + size });
				vertexBuffer.put(new float[] { x + 0F, y + size, z + 0F });
				vertexBuffer.put(new float[] { x + 0F, y + 0F, z + 0F });
				vertexBuffer.put(new float[] { x + 0F, y + 0F, z + size });

				vertexBuffer.put(new float[] { x + 0F, y + 0F, z + 0F });
				vertexBuffer.put(new float[] { x + size, y + 0F, z + 0F });
				vertexBuffer.put(new float[] { x + size, y + 0F, z + size });
				vertexBuffer.put(new float[] { x + 0F, y + 0F, z + size });

				vertexBuffer.put(new float[] { x + 0F, y + size, z + 0F });
				vertexBuffer.put(new float[] { x + 0F, y + size, z + size });
				vertexBuffer.put(new float[] { x + size, y + size, z + size });
				vertexBuffer.put(new float[] { x + size, y + size, z + 0F });
			}

			vertexBuffer.rewind();

			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexBufferID);
			GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexBuffer, GL15.GL_STATIC_DRAW);

			FloatBuffer colourBuffer = BufferUtils.createFloatBuffer(numberIndices * 6 * 4 * 4);

			for (Voxel voxel : model.getVoxels())
			{
				Color colour = palette.getColor(voxel.colorIndex);

				float colorRandom = 0;

				if (randomized)
				{
					colorRandom = (float) (Math.random() * 0.2f - 0.1f);
				}

				float mod = 1F / 255F;

				for (int i = 0; i < (6 * 4); i++)
				{
					colourBuffer.put(new float[] { mod * colour.getRed() + colorRandom, mod * colour.getGreen() + colorRandom, mod * colour.getBlue() + colorRandom, 1f });
				}
			}

			colourBuffer.rewind();

			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, colourBufferID);
			GL15.glBufferData(GL15.GL_ARRAY_BUFFER, colourBuffer, GL15.GL_STATIC_DRAW);

			FloatBuffer normalBuffer = BufferUtils.createFloatBuffer(numberIndices * 6 * 3 * 4);

			for (Voxel voxel : model.getVoxels())
			{
				for (int i = 0; i < 4; i++)
					normalBuffer.put(new float[] { 0, 0, -1f });
				for (int i = 0; i < 4; i++)
					normalBuffer.put(new float[] { 1f, 0, 0 });
				for (int i = 0; i < 4; i++)
					normalBuffer.put(new float[] { 0, 0, 1f });
				for (int i = 0; i < 4; i++)
					normalBuffer.put(new float[] { -1f, 0, 0 });
				for (int i = 0; i < 4; i++)
					normalBuffer.put(new float[] { 0, -1f, 0 });
				for (int i = 0; i < 4; i++)
					normalBuffer.put(new float[] { 0, 1f, 0 });
			}

			normalBuffer.rewind();

			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, normalBufferID);
			GL15.glBufferData(GL15.GL_ARRAY_BUFFER, normalBuffer, GL15.GL_STATIC_DRAW);

			build = true;

			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, arrayBuffer);
			Util.checkGLError();
		}
	}

	public boolean isBuild()
	{
		return build;
	}

	public void draw(boolean ambientLight)
	{
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		Minecraft.getMinecraft().entityRenderer.disableLightmap();
		
		if (ambientLight)
		{
			GlStateManager.enableLighting();
		}
		else
		{
			GlStateManager.disableLighting();
		}

		FloatBuffer globalAmbient = BufferUtils.createFloatBuffer(4);
		globalAmbient.put(new float[] { 0.2f, 0.2f, 0.2f, 1f });
		globalAmbient.rewind();

		GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, globalAmbient);

		GlStateManager.shadeModel(GL11.GL_SMOOTH);

		GlStateManager.enableColorMaterial();
		GlStateManager.colorMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_AMBIENT_AND_DIFFUSE);

		FloatBuffer materialSpecular = BufferUtils.createFloatBuffer(4);
		materialSpecular.put(new float[] { 1, 1, 1, 1 });
		materialSpecular.rewind();
		GL11.glMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_SPECULAR, materialSpecular);

		FloatBuffer materialEmission = BufferUtils.createFloatBuffer(4);
		materialEmission.put(new float[] { 0, 0, 0, 1 });
		materialEmission.rewind();
		GL11.glMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_EMISSION, materialEmission);



		int arrayBuffer = GL11.glGetInteger(GL15.GL_ARRAY_BUFFER_BINDING);

		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexBufferID);
		GL11.glVertexPointer(3, GL11.GL_FLOAT, 0, 0);

		GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, colourBufferID);
		GL11.glColorPointer(4, GL11.GL_FLOAT, 0, 0);

		GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, normalBufferID);
		GL11.glNormalPointer(GL11.GL_FLOAT, 0, 0);

		GL11.glDrawArrays(GL11.GL_QUADS, 0, numberIndices * 6 * 4);

		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, arrayBuffer);

		GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
		GL11.glDisableClientState(GL11.GL_NORMAL_ARRAY);

		GL11.glEnable(GL11.GL_TEXTURE_2D);

		Minecraft.getMinecraft().entityRenderer.enableLightmap();
	}
}
