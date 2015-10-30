package lumien.randomthings.util;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class WorldUtil
{
	public static void setEntityPosition(Entity e, double posX, double posY, double posZ)
	{
		if (e instanceof EntityPlayerMP)
		{
			EntityPlayerMP player = (EntityPlayerMP) e;
			player.playerNetServerHandler.setPlayerLocation(posX, posY, posZ, player.rotationYaw, player.rotationPitch);
		}
		else
		{
			e.setPositionAndUpdate(posX, posY, posZ);
		}
	}
	
	public static void spawnItemStack(World worldIn, double x, double y, double z, ItemStack stack)
    {
		Random rng = new Random();
        float f = rng.nextFloat() * 0.8F + 0.1F;
        float f1 = rng.nextFloat() * 0.8F + 0.1F;
        float f2 = rng.nextFloat() * 0.8F + 0.1F;

        while (stack.stackSize > 0)
        {
            int i = rng.nextInt(21) + 10;

            if (i > stack.stackSize)
            {
                i = stack.stackSize;
            }

            stack.stackSize -= i;
            EntityItem entityitem = new EntityItem(worldIn, x + (double)f, y + (double)f1, z + (double)f2, new ItemStack(stack.getItem(), i, stack.getMetadata()));

            if (stack.hasTagCompound())
            {
                entityitem.getEntityItem().setTagCompound((NBTTagCompound)stack.getTagCompound().copy());
            }

            float f3 = 0.05F;
            entityitem.motionX = rng.nextGaussian() * (double)f3;
            entityitem.motionY = rng.nextGaussian() * (double)f3 + 0.20000000298023224D;
            entityitem.motionZ = rng.nextGaussian() * (double)f3;
            worldIn.spawnEntityInWorld(entityitem);
        }
    }
}
