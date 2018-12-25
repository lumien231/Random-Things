package lumien.randomthings.entitys;

import lumien.randomthings.handler.spectreilluminator.SpectreIlluminationHandler;
import lumien.randomthings.item.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class EntitySpectreIlluminator extends Entity
{
	int actionTimer;
	boolean illuminated;

	public EntitySpectreIlluminator(World worldIn)
	{
		super(worldIn);

		this.noClip = true;

		this.illuminated = false;

		this.setSize(0.5F, 0.5F);
	}
	
	@Override
	public void onKillCommand()
	{
		// TODO Auto-generated method stub
		super.onKillCommand();
		
		if (!world.isRemote)
		{
			SpectreIlluminationHandler handler = SpectreIlluminationHandler.get(this.world);

			if (handler.isIlluminated(this.getPosition()))
				handler.toggleChunk(this.world, this.getPosition());
		}
	}

	@Override
	public boolean isInRangeToRenderDist(double distance)
	{
		return true;
	}

	public EntitySpectreIlluminator(World worldIn, double x, double y, double z)
	{
		this(worldIn);

		this.setPosition(x, y, z);
	}

	@Override
	public boolean canBeCollidedWith()
	{
		return true;
	}

	@Override
	public EnumActionResult applyPlayerInteraction(EntityPlayer player, Vec3d vec, EnumHand hand)
	{
		if (!player.world.isRemote)
		{
			this.setDead();

			player.world.spawnEntity(new EntityItem(player.world, this.posX, this.posY, this.posZ, new ItemStack(ModItems.spectreIlluminator)));

			SpectreIlluminationHandler handler = SpectreIlluminationHandler.get(this.world);

			if (handler.isIlluminated(this.getPosition()))
				handler.toggleChunk(this.world, this.getPosition());
		}
		return EnumActionResult.SUCCESS;
	}

	@Override
	public void onEntityUpdate()
	{
		super.onEntityUpdate();

		if (!this.world.isRemote)
		{
			int heighestBlockInChunk = 0;

			Chunk c = world.getChunkFromBlockCoords(this.getPosition());

			for (int x = 0; x < 16; x++)
			{
				for (int z = 0; z < 16; z++)
				{
					int height = c.getHeightValue(x, z);

					if (height > heighestBlockInChunk)
					{
						heighestBlockInChunk = height;
					}
				}
			}

			heighestBlockInChunk++;

			if (Math.abs(heighestBlockInChunk - this.posY) > 0.05)
			{
				if (heighestBlockInChunk < this.posY)
				{
					this.motionY = -0.05;
				}
				else if (heighestBlockInChunk > this.posY)
				{
					this.motionY = 0.05;
				}
			}
			else
			{
				this.posY = heighestBlockInChunk;
				this.motionY = 0;
			}

			BlockPos myPosition = this.getPosition();

			ChunkPos pos = new ChunkPos(myPosition);

			double chunkX = (pos.getXStart() + pos.getXEnd()) / 2D;
			double chunkZ = (pos.getZStart() + pos.getZEnd()) / 2D;

			if (Math.abs(posX - chunkX) > 0.03)
			{
				if (chunkX > posX)
				{
					this.motionX = 0.03;
				}
				else
				{
					this.motionX = -0.03;
				}
			}
			else
			{
				this.posX = chunkX;
				this.motionX = 0;
			}

			if (Math.abs(posZ - chunkZ) > 0.03)
			{
				if (chunkZ > posZ)
				{
					this.motionZ = 0.03;
				}
				else
				{
					this.motionZ = -0.03;
				}
			}
			else
			{
				this.posZ = chunkZ;
				this.motionZ = 0;
			}

			if (!illuminated && this.posX == chunkX && this.posZ == chunkZ && !SpectreIlluminationHandler.get(this.world).isIlluminated(this.getPosition()))
			{
				SpectreIlluminationHandler.get(this.world).toggleChunk(this.world, this.getPosition());

				this.illuminated = true;
			}
		}

		this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
	}

	@Override
	protected void entityInit()
	{
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound)
	{
		this.illuminated = compound.getBoolean("illuminated");
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound)
	{
		compound.setBoolean("illuminated", illuminated);
	}

}
