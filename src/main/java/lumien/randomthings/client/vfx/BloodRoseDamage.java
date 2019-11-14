package lumien.randomthings.client.vfx;

import java.util.function.Function;

import javax.vecmath.Vector3f;

import lumien.randomthings.client.util.RenderUtils;
import lumien.randomthings.util.math.MathUtils;
import net.minecraft.network.PacketBuffer;

public class BloodRoseDamage extends VisualEffect
{
	Vector3f origin;
	Vector3f destination;

	public BloodRoseDamage()
	{
		super(60);
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
		Function<Float, Vector3f> function = MathUtils.getSpiralFunction(origin, destination, 0.1f, 2, 0);

		RenderUtils.drawFunctionLinePart(function, 0.3f, time / 60F);
	}
}
