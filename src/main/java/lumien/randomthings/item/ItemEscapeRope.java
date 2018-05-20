package lumien.randomthings.item;

import lumien.randomthings.client.particles.EntityColoredSmokeFX;
import lumien.randomthings.handler.EscapeRopeHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemEscapeRope extends ItemBase
{
	public ItemEscapeRope()
	{
		super("escapeRope");

		this.setMaxStackSize(1);
		this.setMaxDamage(20);
	}

	@Override
	public int getMaxItemUseDuration(ItemStack par1ItemStack)
	{
		return 20 * 60;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack par1ItemStack)
	{
		return EnumAction.BOW;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World par2World, EntityPlayer par3EntityPlayer, EnumHand hand)
	{
		ItemStack par1ItemStack = par3EntityPlayer.getHeldItem(hand);

		if (!par2World.provider.hasSkyLight() || par2World.canBlockSeeSky(par3EntityPlayer.getPosition()) || !par2World.isAirBlock(par3EntityPlayer.getPosition()))
		{
			return new ActionResult<ItemStack>(EnumActionResult.FAIL, par1ItemStack);
		}

		par3EntityPlayer.setActiveHand(hand);

		if (!par2World.isRemote)
		{
			EscapeRopeHandler.getInstance().addTask((EntityPlayerMP) par3EntityPlayer);
		}

		return new ActionResult<>(EnumActionResult.SUCCESS, par1ItemStack);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack stack)
	{
		return Minecraft.getMinecraft().player.getActiveItemStack() == stack;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void onUsingTick(ItemStack stack, EntityLivingBase player, int count)
	{
		if (player.world.isRemote)
		{
			float alpha = Math.min(1, (getMaxItemUseDuration(stack) - count) * (1 / 60f));
			for (int i = 0; i < 7; i += 1)
			{
				for (int c = 0; c < 20; c += 10)
				{
					double x = Math.sin((count + i * 20) / (10F + c));
					double z = Math.cos((count + i * 20) / (10F + c));
					double y = Math.sin((count + i * 20) / (15f + c));

					EntityColoredSmokeFX smoke = new EntityColoredSmokeFX(player.world, player.posX + x, player.posY + 1 + y, player.posZ + z, 0, 0, 0);
					smoke.setRBGColorF(1, 1, 0);
					smoke.setAlphaF(alpha);
					Minecraft.getMinecraft().effectRenderer.addEffect(smoke);
				}
			}
		}
	}
}
