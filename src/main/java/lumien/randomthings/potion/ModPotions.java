package lumien.randomthings.potion;

import lumien.randomthings.item.ModItems;
import lumien.randomthings.potion.effects.EffectCollapse;
import lumien.randomthings.potion.imbues.ImbueCollapse;
import lumien.randomthings.potion.imbues.ImbueExperience;
import lumien.randomthings.potion.imbues.ImbueFire;
import lumien.randomthings.potion.imbues.ImbuePoison;
import lumien.randomthings.potion.imbues.ImbueWither;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.potion.PotionType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameData;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModPotions
{
	public static ImbueFire imbueFire;
	public static ImbuePoison imbuePoison;
	public static ImbueExperience imbueExperience;
	public static ImbueWither imbueWither;

	// TODO: Remove
	public static ImbueCollapse imbueCollapse;

	public static EffectCollapse collapse;


	// Potion Types
	public static PotionType collapseType;
	public static PotionType collapseTypeLong;
	public static PotionType collapseTypeStrong;

	public static void preInit(FMLPreInitializationEvent event)
	{
		imbueFire = new ImbueFire();
		imbuePoison = new ImbuePoison();
		imbueExperience = new ImbueExperience();
		imbueWither = new ImbueWither();
		imbueCollapse = new ImbueCollapse();

		collapse = new EffectCollapse();

		collapseType = new PotionType("rtcollapse", new PotionEffect(collapse, 450));
		collapseTypeLong = new PotionType("rtcollapse", new PotionEffect(collapse, 900));
		collapseTypeStrong = new PotionType("rtcollapse", new PotionEffect(collapse, 300, 1));
		
		collapseType.setRegistryName(new ResourceLocation("randomthings", "collapse"));
		collapseTypeLong.setRegistryName(new ResourceLocation("randomthings", "long_collapse"));
		collapseTypeStrong.setRegistryName(new ResourceLocation("randomthings", "strong_collapse"));

		GameRegistry.register(collapseType);
		GameRegistry.register(collapseTypeLong);
		GameRegistry.register(collapseTypeStrong);
		
		// Brewing
		PotionHelper.func_193356_a(PotionTypes.AWKWARD, Ingredient.fromStacks(new ItemStack(ModItems.ingredients, 1, 0)), collapseType);
		PotionHelper.func_193357_a(collapseType, Items.REDSTONE, collapseTypeLong);
		PotionHelper.func_193357_a(collapseType, Items.GLOWSTONE_DUST, collapseTypeStrong);
	}
}
