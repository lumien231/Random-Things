package lumien.randomthings.client;

import com.mojang.authlib.GameProfile;
import java.util.UUID;

import lumien.randomthings.block.ModBlocks;
import lumien.randomthings.tileentity.TileEntitySpecialChest;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RandomThingsTEItemRenderer
{
	public static RandomThingsTEItemRenderer instance = new RandomThingsTEItemRenderer();

	TileEntitySpecialChest natureChest = new TileEntitySpecialChest(0);
	TileEntitySpecialChest waterChest = new TileEntitySpecialChest(1);
	
	public boolean renderByItem(ItemStack itemStackIn)
	{
		if (itemStackIn.getItem() instanceof ItemBlock)
		{
			ItemBlock ib = (ItemBlock) itemStackIn.getItem();
			
			if (ib.getBlock() == ModBlocks.specialChest)
			{
				switch (itemStackIn.getItemDamage())
				{
					case 0:
						TileEntityRendererDispatcher.instance.renderTileEntityAt(this.natureChest, 0.0D, 0.0D, 0.0D, 0.0F);
						return true;
					case 1:
						TileEntityRendererDispatcher.instance.renderTileEntityAt(this.waterChest, 0.0D, 0.0D, 0.0D, 0.0F);
						return true;
				}
			}
		}
		
		return false;
	}
}