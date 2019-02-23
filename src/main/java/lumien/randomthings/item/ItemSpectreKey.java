package lumien.randomthings.item;

import lumien.randomthings.config.Internals;
import lumien.randomthings.handler.ModDimensions;
import lumien.randomthings.handler.spectre.SpectreHandler;
import lumien.randomthings.handler.spectre.SpectreWorldProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleSmokeNormal;
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

public class ItemSpectreKey extends ItemBase
{

	public ItemSpectreKey()
	{
		super("spectreKey");

		this.setMaxStackSize(1);
	}

	@Override
	public int getMaxItemUseDuration(ItemStack par1ItemStack)
	{
		return 100;
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
		par3EntityPlayer.setActiveHand(hand);
		return new ActionResult<>(EnumActionResult.SUCCESS, par1ItemStack);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack stack)
	{
		return Minecraft.getMinecraft().player.world.provider instanceof SpectreWorldProvider;
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack par1ItemStack, World par2World, EntityLivingBase livingEntity)
	{
		if (!par2World.isRemote)
		{
			SpectreHandler spectreHandler;

			if ((spectreHandler = SpectreHandler.getInstance()) != null)
			{
				if (par2World.provider.getDimension() != Internals.SPECTRE_ID)
				{
					spectreHandler.teleportPlayerToSpectreCube((EntityPlayerMP) livingEntity);
				}
				else
				{
					spectreHandler.teleportPlayerBack((EntityPlayerMP) livingEntity);
				}
			}
		}
		return par1ItemStack;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onUsingTick(ItemStack stack, EntityLivingBase livingEntity, int count)
	{
		if (livingEntity.world.isRemote && count < 60)
		{
			Particle particle;
			float t = 1F / 255F;

			ParticleSmokeNormal.Factory factory = new ParticleSmokeNormal.Factory();

			for (int i = 0; i < (60 - count) * 2; i++)
			{
				particle = factory.createParticle(0, livingEntity.world, livingEntity.posX + Math.random() * 1.8 - 0.9, livingEntity.posY + Math.random() * 1.8f, livingEntity.posZ + Math.random() * 1.8 - 0.9, 0, 0, 0);
				particle.setRBGColorF(t * 122F, t * 197F, t * 205F);
				Minecraft.getMinecraft().effectRenderer.addEffect(particle);
			}
		}
	}
}
