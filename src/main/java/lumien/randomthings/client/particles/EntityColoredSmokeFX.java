package lumien.randomthings.client.particles;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class EntityColoredSmokeFX extends EntityFX
{
	float smokeParticleScale;

	public EntityColoredSmokeFX(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double p_i46347_8_, double p_i46347_10_, double p_i46347_12_)
	{
		this(worldIn, xCoordIn, yCoordIn, zCoordIn, p_i46347_8_, p_i46347_10_, p_i46347_12_, 1.0F);
	}

	public EntityColoredSmokeFX(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double p_i46348_8_, double p_i46348_10_, double p_i46348_12_, float p_i46348_14_)
	{
		super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0D, 0.0D, 0.0D);
		this.xSpeed *= 0.10000000149011612D;
		this.ySpeed *= 0.10000000149011612D;
		this.zSpeed *= 0.10000000149011612D;
		this.xSpeed += p_i46348_8_;
		this.ySpeed += p_i46348_10_;
		this.zSpeed += p_i46348_12_;
		this.particleRed = this.particleGreen = this.particleBlue = (float) (Math.random() * 0.30000001192092896D);
		this.particleScale *= 0.75F;
		this.particleScale *= p_i46348_14_;
		this.smokeParticleScale = this.particleScale;
		this.particleMaxAge = (int) (8.0D / (Math.random() * 0.8D + 0.2D));
		this.particleMaxAge = (int) ((float) this.particleMaxAge * p_i46348_14_);
	}

	/**
	 * Renders the particle
	 */
	public void renderParticle(VertexBuffer worldRendererIn, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
	{
		float f = ((float) this.particleAge + partialTicks) / (float) this.particleMaxAge * 32.0F;
		f = MathHelper.clamp_float(f, 0.0F, 1.0F);
		this.particleScale = this.smokeParticleScale * f;
		super.renderParticle(worldRendererIn, entityIn, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
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

		this.setParticleTextureIndex(7 - this.particleAge * 8 / this.particleMaxAge);
		this.ySpeed += 0.004D;
		this.moveEntity(this.xSpeed, this.ySpeed, this.zSpeed);

		if (this.posY == this.prevPosY)
		{
			this.xSpeed *= 1.1D;
			this.zSpeed *= 1.1D;
		}

		this.xSpeed *= 0.9599999785423279D;
		this.ySpeed *= 0.9599999785423279D;
		this.zSpeed *= 0.9599999785423279D;

		if (this.isCollided)
		{
			this.xSpeed *= 0.699999988079071D;
			this.zSpeed *= 0.699999988079071D;
		}
	}
}