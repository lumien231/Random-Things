package lumien.randomthings.item;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBiomeCrystal extends ItemBase
{
	public ItemBiomeCrystal()
	{
		super("biomeCrystal");

		ChestGenHooks.addItem(ChestGenHooks.PYRAMID_JUNGLE_CHEST, new WeightedRandomChestContent(new ItemStack(this), 1, 15, 20));
		ChestGenHooks.addItem(ChestGenHooks.PYRAMID_DESERT_CHEST, new WeightedRandomChestContent(new ItemStack(this), 1, 15, 20));
		ChestGenHooks.addItem(ChestGenHooks.VILLAGE_BLACKSMITH, new WeightedRandomChestContent(new ItemStack(this), 1, 1, 1));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack stack, int renderPass)
	{
		EntityPlayerSP thePlayer = FMLClientHandler.instance().getClientPlayerEntity();
		WorldClient theWorld = FMLClientHandler.instance().getWorldClient();
		BlockPos pos = new BlockPos(thePlayer.posX, thePlayer.posY, thePlayer.posZ);

		int foliageColor = BiomeColorHelper.getFoliageColorAtPos(theWorld, pos);
		int waterColor = BiomeColorHelper.getWaterColorAtPos(theWorld, pos);
		int grassColor = BiomeColorHelper.getGrassColorAtPos(theWorld, pos);

		return foliageColor;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack stack)
	{
		EntityPlayerSP thePlayer = FMLClientHandler.instance().getClientPlayerEntity();
		WorldClient theWorld = FMLClientHandler.instance().getWorldClient();
		BlockPos pos = new BlockPos(thePlayer.posX, thePlayer.posY, thePlayer.posZ);

		BiomeGenBase biome = theWorld.getBiomeGenForCoords(pos);
		return BiomeDictionary.isBiomeOfType(biome, Type.MAGICAL);
	}
}
