package lumien.randomthings.util;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public class NBTUtil
{
	public static void writeBlockPosToNBT(NBTTagCompound compound, String tagName, BlockPos pos)
	{
		NBTTagCompound posPounds = new NBTTagCompound();

		posPounds.setInteger("posX", pos.getX());
		posPounds.setInteger("posY", pos.getY());
		posPounds.setInteger("posZ", pos.getZ());

		compound.setTag(tagName, posPounds);
	}

	public static BlockPos readBlockPosFromNBT(NBTTagCompound compound, String tagName)
	{
		NBTTagCompound posPounds = compound.getCompoundTag(tagName);

		return new BlockPos(posPounds.getInteger("posX"), posPounds.getInteger("posY"), posPounds.getInteger("posZ"));
	}
}
