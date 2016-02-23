package lumien.randomthings.item;

import lumien.randomthings.handler.ModDimensions;
import lumien.randomthings.handler.spectre.SpectreHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.EntitySmokeFX;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
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
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	{
		par3EntityPlayer.setItemInUse(par1ItemStack, this.getMaxItemUseDuration(par1ItemStack));
		return par1ItemStack;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack stack)
	{
		return Minecraft.getMinecraft().thePlayer.worldObj.provider.getDimensionId() == ModDimensions.SPECTRE_ID;
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	{
		if (!par2World.isRemote)
		{
			SpectreHandler spectreHandler;

			if ((spectreHandler = SpectreHandler.getInstance()) != null)
			{
				if (par2World.provider.getDimensionId() != ModDimensions.SPECTRE_ID)
				{
					spectreHandler.teleportPlayerToSpectreCube((EntityPlayerMP) par3EntityPlayer);
				}
				else
				{
					spectreHandler.teleportPlayerBack((EntityPlayerMP) par3EntityPlayer);
				}
			}
		}
		return par1ItemStack;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onUsingTick(ItemStack stack, EntityPlayer player, int count)
	{
		if (player.worldObj.isRemote && count < 60)
		{
			EntityFX particle;
			float t = 1F / 255F;

			EntitySmokeFX.Factory factory = new EntitySmokeFX.Factory();

			for (int i = 0; i < (60 - count) * 2; i++)
			{
				particle = factory.getEntityFX(0, player.worldObj, player.posX + Math.random() * 1.8 - 0.9, player.posY + Math.random() * 1.8f, player.posZ + Math.random() * 1.8 - 0.9, 0, 0, 0);
				particle.setRBGColorF(t * 122F, t * 197F, t * 205F);
				Minecraft.getMinecraft().effectRenderer.addEffect(particle);
			}
		}
	}
}
