package lumien.randomthings.block;

import java.util.List;
import java.util.Random;

import lumien.randomthings.item.ItemLinkingOrb;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.EntityReddustFX;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockLifeAnchor extends BlockBase
{

	protected BlockLifeAnchor()
	{
		super("lifeAnchor", Material.rock);
		
		this.setHardness(1.5F);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		ItemStack equipped = Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem();
		if (equipped != null && equipped.getItem() instanceof ItemLinkingOrb)
		{
			AxisAlignedBB boundingBox = AxisAlignedBB.fromBounds(pos.getX(), pos.getY(), pos.getZ(), pos.getX(), pos.getY(), pos.getZ()).expand(5, 5, 5);

			List<EntityLivingBase> entityList = worldIn.getEntitiesWithinAABB(EntityLivingBase.class, boundingBox);
			entityList.remove(Minecraft.getMinecraft().thePlayer);

			Vec3 thisBlock = new Vec3(pos.getX(), pos.getY(), pos.getZ());

			for (EntityLivingBase e : entityList)
			{
				Vec3 dif = e.getPositionVector().subtract(thisBlock);

				for (double d = 0; d <= 1; d += 0.02d)
				{
					EntityFX particle = new EntityReddustFX.Factory().getEntityFX(0, worldIn, thisBlock.xCoord + 0.5 + dif.xCoord * d, thisBlock.yCoord + 0.5 + dif.yCoord * d, thisBlock.zCoord + 0.5 + dif.zCoord * d, 0, 0, 0);
					Minecraft.getMinecraft().effectRenderer.addEffect(particle);
					particle.setRBGColorF(0.7f, 0f, 0.3f);
				}
			}
		}
	}
}
