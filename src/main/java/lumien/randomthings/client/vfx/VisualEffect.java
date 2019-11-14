package lumien.randomthings.client.vfx;

import net.minecraft.network.PacketBuffer;

public abstract class VisualEffect
{
	private int lifeTime;
	
	protected int tickCount;

	public VisualEffect(int lifeTime)
	{
		this.lifeTime = lifeTime;
	}
	
	public void readData(PacketBuffer pb) {
		
	}

	public boolean tick()
	{
		this.tickCount++;

		if (this.tickCount == this.lifeTime)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public void renderInternal(float partialTick)
	{
		this.render(this.tickCount + partialTick);
	}

	public void render(float time)
	{

	}

	public void init()
	{

	}
}
