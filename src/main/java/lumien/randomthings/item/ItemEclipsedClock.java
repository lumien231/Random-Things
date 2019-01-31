package lumien.randomthings.item;

import javax.annotation.Nullable;

import lumien.randomthings.entitys.EntityEclipsedClock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemEclipsedClock extends ItemBase
{

	public ItemEclipsedClock()
	{
		super("eclipsedClock");

		this.addPropertyOverride(new ResourceLocation("time"), new IItemPropertyGetter()
		{
			@SideOnly(Side.CLIENT)
			public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn)
			{
				int time = 6000;
				
				if (stack.hasTagCompound() && stack.getTagCompound().hasKey("targetTime"))
				{
					time = stack.getTagCompound().getInteger("targetTime");
				}
				
				int i = (int)(time % 24000L);
		        float f = ((float)i) / 24000.0F - 0.25F;

		        if (f < 0.0F)
		        {
		            ++f;
		        }

		        if (f > 1.0F)
		        {
		            --f;
		        }

		        float f1 = 1.0F - (float)((Math.cos((double)f * Math.PI) + 1.0D) / 2.0D);
		        f = f + (f1 - f) / 3.0F;
		        return f;
			}
		});
	}

	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		ItemStack itemstack = player.getHeldItem(hand);
		BlockPos blockpos = pos.offset(facing);

		if (facing != EnumFacing.DOWN && facing != EnumFacing.UP && player.canPlayerEdit(blockpos, facing, itemstack))
		{
			EntityHanging entityhanging = new EntityEclipsedClock(worldIn, blockpos, facing);

			if (entityhanging != null && entityhanging.onValidSurface())
			{
				if (!worldIn.isRemote)
				{
					entityhanging.playPlaceSound();
					worldIn.spawnEntity(entityhanging);
				}

				itemstack.shrink(1);
			}

			return EnumActionResult.SUCCESS;
		}
		else
		{
			return EnumActionResult.FAIL;
		}
	}
}
