package lumien.randomthings.client.vfx;

public enum EFFECT
{
	BLOOD_ROSE_DAMAGE(BloodRoseDamage.class),
	BLOOD_ROSE_SPREAD(BloodRoseSpread.class);
	
	Class<? extends VisualEffect> effectClass;
	
	private EFFECT(Class<? extends VisualEffect> effectClass)
	{
		this.effectClass = effectClass;
	}
	
	public Class<? extends VisualEffect> getEffectClass() {
		return effectClass;
	}
}
