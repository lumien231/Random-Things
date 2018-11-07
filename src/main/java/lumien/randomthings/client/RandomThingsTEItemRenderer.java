package lumien.randomthings.client;

import lumien.randomthings.block.ModBlocks;
import lumien.randomthings.client.render.RenderSpectreIlluminator;
import lumien.randomthings.entitys.EntitySpectreIlluminator;
import lumien.randomthings.item.ModItems;
import lumien.randomthings.tileentity.TileEntityBlockDiaphanous;
import lumien.randomthings.tileentity.TileEntitySpecialChest;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RandomThingsTEItemRenderer
{
	public static RandomThingsTEItemRenderer instance = new RandomThingsTEItemRenderer();

	TileEntitySpecialChest natureChest = new TileEntitySpecialChest(0);
	TileEntitySpecialChest waterChest = new TileEntitySpecialChest(1);

	TileEntityBlockDiaphanous diaphanous = new TileEntityBlockDiaphanous();
	
	RenderSpectreIlluminator rsi = new RenderSpectreIlluminator(Minecraft.getMinecraft().getRenderManager());

	public boolean renderByItem(ItemStack itemStackIn)
	{
		if (itemStackIn.getItem() == ModItems.spectreIlluminator)
		{
			GlStateManager.scale(1.8, 1.8, 1.8);
			rsi.doRender(null, 0.3, 0.3, 0.3, 0, Minecraft.getMinecraft().getRenderPartialTicks());
			GlStateManager.scale(10 / 18, 10 / 18, 10 / 18);
			Minecraft.getMinecraft().entityRenderer.disableLightmap();

			return true;
		}

		if (itemStackIn.getItem() instanceof ItemBlock)
		{
			ItemBlock ib = (ItemBlock) itemStackIn.getItem();

			if (ib.getBlock() == ModBlocks.specialChest)
			{
				switch (itemStackIn.getItemDamage())
				{
					case 0:
						TileEntityRendererDispatcher.instance.render(this.natureChest, 0.0D, 0.0D, 0.0D, 0.0F);
						return true;
					case 1:
						TileEntityRendererDispatcher.instance.render(this.waterChest, 0.0D, 0.0D, 0.0D, 0.0F);
						return true;
				}
			}
			else if (ib.getBlock() == ModBlocks.blockDiaphanous)
			{
				if (itemStackIn.hasTagCompound() && itemStackIn.getTagCompound().hasKey("block"))
				{
					NBTTagCompound compound = itemStackIn.getTagCompound();

					IBlockState toDisplay;

					Block b = Block.REGISTRY.getObject(new ResourceLocation(compound.getString("block")));
					int meta = compound.getInteger("meta");

					try
					{
						toDisplay = b.getStateFromMeta(meta);
					}
					catch (Exception e)
					{
						e.printStackTrace();
						toDisplay = Blocks.STONE.getDefaultState();
					}

					diaphanous.setDisplayStateInternal(toDisplay);
				}
				else
				{
					diaphanous.setDisplayStateInternal(Blocks.STONE.getDefaultState());
				}

				diaphanous.setWorld(Minecraft.getMinecraft().player.world);
				diaphanous.setPos(new BlockPos(0, 0, 0));
				diaphanous.setItem();

				TileEntityRendererDispatcher.instance.render(this.diaphanous, 0D, 0D, 0D, 0F);
				return true;
			}
		}

		return false;
	}
}