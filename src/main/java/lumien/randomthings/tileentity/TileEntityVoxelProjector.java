package lumien.randomthings.tileentity;

import lumien.randomthings.client.gui.GuiVoxelProjector;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ITickable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityVoxelProjector extends TileEntityBase implements ITickable
{
	String model;

	int modelRotation;
	int scale;
	int rotationSpeed;

	int rotationMod;
	
	boolean ambientLight;
	boolean randomize;

	public TileEntityVoxelProjector()
	{
		this.model = "Test";
		this.scale = 1;
		this.rotationSpeed = 0;
	}

	@Override
	public void update()
	{
		if (this.worldObj.isRemote)
		{
			if (this.rotationSpeed != 0)
			{
				this.rotationMod += rotationSpeed;
				
				if (this.rotationMod > 360)
				{
					this.rotationMod -= 360;
				}
			}
		}
	}

	@Override
	public void writeDataToNBT(NBTTagCompound compound)
	{
		compound.setString("model", model);
		compound.setInteger("modelRotation", modelRotation);
		compound.setInteger("scale", scale);
		compound.setInteger("rotationSpeed", rotationSpeed);
		compound.setBoolean("ambientLight", ambientLight);
		compound.setBoolean("randomize", randomize);
	}

	@Override
	public void readDataFromNBT(NBTTagCompound compound)
	{
		this.model = compound.getString("model");
		this.modelRotation = compound.getInteger("modelRotation");
		this.scale = compound.getInteger("scale");
		this.rotationSpeed = compound.getInteger("rotationSpeed");
		this.ambientLight = compound.getBoolean("ambientLight");
		this.randomize = compound.getBoolean("randomize");
	}

	@Override
	public AxisAlignedBB getRenderBoundingBox()
	{
		return INFINITE_EXTENT_AABB;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public double getMaxRenderDistanceSquared()
	{
		return 512D * 512.0D;
	}

	public float getRenderModelRotation(float partialTicks)
	{
		if (this.rotationSpeed != 0)
		{
			return this.modelRotation + this.rotationMod + partialTicks * this.rotationSpeed;
		}
		else
		{
			return this.modelRotation;
		}
	}

	public int getModelRotation()
	{
		return modelRotation;
	}

	public void setModelRotation(int newModelRotation)
	{
		this.modelRotation = newModelRotation;

		if (!this.worldObj.isRemote)
		{
			syncTE();
		}
	}

	public String getModel()
	{
		return model;
	}

	public void setModel(String model)
	{
		this.model = model;

		if (!this.worldObj.isRemote)
		{
			syncTE();
		}
	}

	public void setScale(int newScale)
	{
		this.scale = newScale;
	}

	public int getScale()
	{
		return scale;
	}
	
	public int getRotationSpeed()
	{
		return rotationSpeed;
	}

	public void setRotationSpeed(int rotationSpeed)
	{
		this.rotationSpeed = rotationSpeed;
		
		if (!this.worldObj.isRemote)
		{
			syncTE();
		}
	}
	
	public boolean ambientLight()
	{
		return ambientLight;
	}

	public boolean randomize()
	{
		return randomize;
	}

	public void setAmbientLight(boolean newAmbientLight)
	{
		this.ambientLight = newAmbientLight;
		
		if (!this.worldObj.isRemote)
		{
			syncTE();
		}
	}

	public void setRandomize(boolean newRandomize)
	{
		this.randomize = newRandomize;
		
		if (!this.worldObj.isRemote)
		{
			syncTE();
		}
	}
}
