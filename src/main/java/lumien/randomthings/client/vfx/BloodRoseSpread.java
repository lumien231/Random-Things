package lumien.randomthings.client.vfx;

import java.util.function.Function;

import javax.vecmath.Vector3f;

import lumien.randomthings.client.util.RenderUtils;
import lumien.randomthings.util.math.MathUtils;
import net.minecraft.network.PacketBuffer;

public class BloodRoseSpread extends VisualEffect
{
	Vector3f origin;
	Vector3f destination;
	
	Function<Float, Vector3f>[] functions;

	public BloodRoseSpread()
	{
		super(100);
	}
	
	@SuppressWarnings("unchecked")
	public void init() {
		functions = new Function[8];
		
		for (int i = 0; i < 8; i++)
		{
			functions[i] = MathUtils.getSpiralFunction(origin, destination, 0.05f, 3, (float) (i * Math.PI / 4F + Math.random() * 0.5F - 0.25F));
		}
	}

	@Override
	public void readData(PacketBuffer pb)
	{
		this.origin = new Vector3f(pb.readFloat(), pb.readFloat(), pb.readFloat());
		this.destination = new Vector3f(pb.readFloat(), pb.readFloat(), pb.readFloat());
	}

	@Override
	public void render(float time)
	{
		for (int i = 0; i < 8; i++)
		{
			RenderUtils.drawFunctionLinePart(functions[i], 1f, time / 100F);
		}
	}
}
