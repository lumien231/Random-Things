package lumien.randomthings.util.math;

import java.util.function.Function;

import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

public class MathUtils
{
	public static Function<Float, Vector3f> getSpiralFunction(Vector3f from, Vector3f to, float maxRadius, int rotations, float offset)
	{
		Vector3f dif = new Vector3f(to);
		dif.sub(from);

		Vector3f axis1 = new Vector3f();
		axis1.cross(dif, new Vector3f(0, 1, 0));
		axis1.normalize();

		Vector3f axis2 = new Vector3f();
		axis2.cross(dif, axis1);
		axis2.normalize();

		return (progress) -> {
			Vector3f between = new Vector3f();
			between.interpolate(from, to, progress);

			double theta = progress * Math.PI * 2 * rotations + offset;

			float radius = (float) (Math.sin(progress * Math.PI) * maxRadius);

			float sin = (float) Math.sin(theta) * radius;
			float cos = (float) Math.cos(theta) * radius;

			Vector3f sS = new Vector3f(axis1);
			sS.scale(sin);

			Vector3f sC = new Vector3f(axis2);
			sC.scale(cos);

			Vector3f circlePoint = new Vector3f(sS);
			circlePoint.add(sC);

			between.add(circlePoint);

			return between;
		};
	}
}
