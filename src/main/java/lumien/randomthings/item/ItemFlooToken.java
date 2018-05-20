package lumien.randomthings.item;

import java.util.List;

import lumien.randomthings.client.particles.ParticleFlooFlame;
import lumien.randomthings.entitys.EntityTemporaryFlooFireplace;
import lumien.randomthings.network.MessageUtil;
import lumien.randomthings.network.messages.MessageFlooToken;
import lumien.randomthings.util.ReflectionUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityItem;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemFlooToken extends ItemBase
{

	public ItemFlooToken()
	{
		super("flootoken");
	}

	@Override
	public boolean onEntityItemUpdate(EntityItem entityItem)
	{
		int age = ReflectionUtil.getEntityItemAge(entityItem);

		if (!entityItem.world.isRemote)
		{
			if (age > 100 && entityItem.onGround)
			{
				List<EntityTemporaryFlooFireplace> otherFireplaces = entityItem.world.getEntitiesWithinAABB(EntityTemporaryFlooFireplace.class, entityItem.getEntityBoundingBox().grow(5));

				if (otherFireplaces.isEmpty())
				{
					EntityTemporaryFlooFireplace toSpawn = new EntityTemporaryFlooFireplace(entityItem.world, entityItem.posX, entityItem.posY, entityItem.posZ);

					MessageUtil.sendToAllWatchingPos(entityItem.world, entityItem.getPosition(), new MessageFlooToken(entityItem.world.provider.getDimension(), entityItem.posX, entityItem.posY, entityItem.posZ));

					entityItem.world.spawnEntity(toSpawn);

					entityItem.setDead();
				}
			}
		}
		else if (age > 30)
		{
			client(entityItem);
		}
		return super.onEntityItemUpdate(entityItem);
	}

	@SideOnly(Side.CLIENT)
	private void client(EntityItem entityItem)
	{
		Minecraft.getMinecraft().effectRenderer.addEffect(new ParticleFlooFlame(entityItem.world, entityItem.posX, entityItem.posY, entityItem.posZ, 0, Math.random() * 0.05 + 0.1, 0));
	}
}
