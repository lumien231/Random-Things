package lumien.randomthings.client.particles;

import lumien.randomthings.lib.AtlasSprite;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public abstract class ParticleBase extends Particle
{
	public ParticleBase(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn)
	{
		super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);

		this.setParticleTexture(particleSprite);
	}

	@AtlasSprite(resource = "randomthings:entitys/particle")
	static TextureAtlasSprite particleSprite;

	@Override
	public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
	{
		float texSizeU = this.particleTexture.getMaxU() - this.particleTexture.getMinU();
		float texSizeV = this.particleTexture.getMaxV() - this.particleTexture.getMinV();

		float partSizeU = texSizeU / 16;
		float partSizeV = texSizeV / 16;

		float particleU = this.particleTexture.getMinU() + this.particleTextureIndexX * partSizeU;
		float particleU2 = particleU + partSizeU;
		float particleV = this.particleTexture.getMinV() + this.particleTextureIndexY * partSizeV;
		float particleV2 = particleV + partSizeV;

		float f4 = 0.1F * this.particleScale;

		float f5 = (float) (this.prevPosX + (this.posX - this.prevPosX) * partialTicks - interpPosX);
		float f6 = (float) (this.prevPosY + (this.posY - this.prevPosY) * partialTicks - interpPosY);
		float f7 = (float) (this.prevPosZ + (this.posZ - this.prevPosZ) * partialTicks - interpPosZ);
		int i = this.getBrightnessForRender(partialTicks);
		int j = i >> 16 & 65535;
		int k = i & 65535;
		Vec3d[] avec3d = new Vec3d[] { new Vec3d(-rotationX * f4 - rotationXY * f4, -rotationZ * f4, -rotationYZ * f4 - rotationXZ * f4), new Vec3d(-rotationX * f4 + rotationXY * f4, rotationZ * f4, -rotationYZ * f4 + rotationXZ * f4), new Vec3d(rotationX * f4 + rotationXY * f4, rotationZ * f4, rotationYZ * f4 + rotationXZ * f4), new Vec3d(rotationX * f4 - rotationXY * f4, -rotationZ * f4, rotationYZ * f4 - rotationXZ * f4) };

		if (this.particleAngle != 0.0F)
		{
			float f8 = this.particleAngle + (this.particleAngle - this.prevParticleAngle) * partialTicks;
			float f9 = MathHelper.cos(f8 * 0.5F);
			float f10 = MathHelper.sin(f8 * 0.5F) * (float) cameraViewDir.x;
			float f11 = MathHelper.sin(f8 * 0.5F) * (float) cameraViewDir.y;
			float f12 = MathHelper.sin(f8 * 0.5F) * (float) cameraViewDir.z;
			Vec3d vec3d = new Vec3d(f10, f11, f12);

			for (int l = 0; l < 4; ++l)
			{
				avec3d[l] = vec3d.scale(2.0D * avec3d[l].dotProduct(vec3d)).add(avec3d[l].scale(f9 * f9 - vec3d.dotProduct(vec3d))).add(vec3d.crossProduct(avec3d[l]).scale(2.0F * f9));
			}
		}

		buffer.pos(f5 + avec3d[0].x, f6 + avec3d[0].y, f7 + avec3d[0].z).tex(particleU2, particleV2).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
		buffer.pos(f5 + avec3d[1].x, f6 + avec3d[1].y, f7 + avec3d[1].z).tex(particleU2, particleV).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
		buffer.pos(f5 + avec3d[2].x, f6 + avec3d[2].y, f7 + avec3d[2].z).tex(particleU, particleV).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
		buffer.pos(f5 + avec3d[3].x, f6 + avec3d[3].y, f7 + avec3d[3].z).tex(particleU, particleV2).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
	}

	@Override
	public void setParticleTextureIndex(int particleTextureIndex)
	{
		this.particleTextureIndexX = particleTextureIndex % 16;
		this.particleTextureIndexY = particleTextureIndex / 16;
	}

	@Override
	public int getFXLayer()
	{
		return 1;
	}
}