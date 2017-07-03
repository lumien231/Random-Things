package lumien.randomthings.client.particles;

import lumien.randomthings.lib.AtlasSprite;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class ParticlePeaceCandle extends ParticleBase
{
	/** the scale of the flame FX */
	private final float flameScale;

	public ParticlePeaceCandle(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn)
	{
		super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
		this.motionX = this.motionX * 0.009999999776482582D + xSpeedIn;
		this.motionY = this.motionY * 0.009999999776482582D + ySpeedIn;
		this.motionZ = this.motionZ * 0.009999999776482582D + zSpeedIn;
		this.posX += (double) ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F);
		this.posY += (double) ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F);
		this.posZ += (double) ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F);
		this.flameScale = this.particleScale;
		this.particleRed = 1.0F;
		this.particleGreen = 1.0F;
		this.particleBlue = 1.0F;
		this.particleMaxAge = (int) (8.0D / (Math.random() * 0.8D + 0.2D)) + 4;
		this.setParticleTextureIndex(0);
	}
	
	@Override
    public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
    {
		float w = ((float) this.particleAge + partialTicks) / (float) this.particleMaxAge;
		this.particleScale = this.flameScale * (1.0F - w * w * 0.5F);
		
		super.renderParticle(buffer, entityIn, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
    }

	public void move(double x, double y, double z)
	{
		this.setBoundingBox(this.getBoundingBox().offset(x, y, z));
		this.resetPositionToBB();
	}

	public int getBrightnessForRender(float p_189214_1_)
	{
		float f = ((float) this.particleAge + p_189214_1_) / (float) this.particleMaxAge;
		f = MathHelper.clamp(f, 0.0F, 1.0F);
		int i = super.getBrightnessForRender(p_189214_1_);
		int j = i & 255;
		int k = i >> 16 & 255;
		j = j + (int) (f * 15.0F * 16.0F);

		if (j > 240)
		{
			j = 240;
		}

		return j | k << 16;
	}

	public void onUpdate()
	{
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;

		if (this.particleAge++ >= this.particleMaxAge)
		{
			this.setExpired();
		}

		this.move(this.motionX, this.motionY, this.motionZ);
		this.motionX *= 0.9599999785423279D;
		this.motionY *= 0.9599999785423279D;
		this.motionZ *= 0.9599999785423279D;

		if (this.onGround)
		{
			this.motionX *= 0.699999988079071D;
			this.motionZ *= 0.699999988079071D;
		}
	}
}