package lumien.randomthings.entitys;

import java.util.ArrayList;
import java.util.List;

import io.netty.buffer.ByteBuf;
import lumien.randomthings.util.NBTUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEnchantmentTable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityEnderConnection extends Entity
{
	BlockPos target;

	public EntityEnderConnection(World worldIn)
	{
		super(worldIn);

		this.setSize(1, 2);
	}

	public EntityEnderConnection(World worldIn, double posX, double posY, double posZ)
	{
		this(worldIn);
		this.setPosition(posX, posY, posZ);
	}

	@Override
	public void onEntityUpdate()
	{
		super.onEntityUpdate();
	}

	@Override
	public void onCollideWithPlayer(EntityPlayer entityIn)
	{
		super.onCollideWithPlayer(entityIn);

		List<Integer> list = new ArrayList<Integer>(EnumFacing.values().length);
	}

	@Override
	protected void entityInit()
	{
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound)
	{
		if (compound.hasKey("target"))
		{
			this.target = NBTUtil.readBlockPosFromNBT(compound, "target");
		}
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound)
	{
		if (target != null)
		{
			NBTUtil.writeBlockPosToNBT(compound, "target", target);
		}
	}
}
