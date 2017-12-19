package lumien.randomthings.handler;

import lumien.randomthings.enchantment.ModEnchantments;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.Field;
import java.util.Iterator;

// This version of ItemCatcher is adapted from Iridescent's instantget module, with added checks for a player holding the magnetic enchantment.
public class ItemCatcher
{
	private static final Field delayBeforeCanPickup = ReflectionHelper.findField(EntityItem.class, "field_145804_b", "pickupDelay");

	public static void onDropXp(LivingExperienceDropEvent e) {
		if (!e.getEntityLiving().world.getGameRules().getBoolean("doMobLoot")) return;
		if (e.getEntityLiving().world.isRemote) return;
		if (e.getAttackingPlayer() == null) return;
		if (e.getAttackingPlayer() instanceof FakePlayer) return;
		if (!harvesterHoldsMagnetic(e.getAttackingPlayer())) return;
		World world = e.getEntityLiving().world;
		int amount = e.getDroppedExperience();
		int oldCooldown = e.getAttackingPlayer().xpCooldown;
		while (amount > 0) {
			int i = EntityXPOrb.getXPSplit(amount);
			amount -= i;
			EntityXPOrb exp = new EntityXPOrb(world, e.getEntityLiving().posX, e.getEntityLiving().posY, e.getEntityLiving().posZ, i);
			e.getAttackingPlayer().xpCooldown = 0;
			int oldDelay = exp.delayBeforeCanPickup;
			exp.delayBeforeCanPickup = 0;
			world.spawnEntity(exp);
			exp.onCollideWithPlayer(e.getAttackingPlayer());
			if (!exp.isDead) {
				exp.delayBeforeCanPickup = oldDelay;
			}
		}
		e.getAttackingPlayer().xpCooldown = oldCooldown;
		e.setDroppedExperience(0);
	}

	public static void onDrops(LivingDropsEvent e) {
		if (!e.getEntityLiving().world.getGameRules().getBoolean("doMobLoot")) return;
		if (e.getEntityLiving().world.isRemote) return;
		if (e.getSource() instanceof EntityDamageSource) {
			EntityDamageSource eds = (EntityDamageSource)e.getSource();
			if (eds.getTrueSource() instanceof EntityPlayer) {
				EntityPlayer ep = (EntityPlayer)eds.getTrueSource();
				if (ep instanceof FakePlayer) return;
				if (!harvesterHoldsMagnetic(ep)) return;
				Iterator<EntityItem> iter = e.getDrops().iterator();
				while (iter.hasNext()) {
					EntityItem ei = iter.next();
					int oldPickupDelay = 0;
					try {
						oldPickupDelay = (Integer)delayBeforeCanPickup.get(ei);
					} catch (IllegalAccessException e1) {
						oldPickupDelay = 10;
					}
					ei.setNoPickupDelay();
					e.getEntityLiving().world.spawnEntity(ei);
					ei.onCollideWithPlayer(ep);
					if (!ei.isDead) {
						ei.setPickupDelay(oldPickupDelay);
					} else {
						iter.remove();
					}
				}
			}
		}
	}

	public static void onBreak(BlockEvent.BreakEvent e) {
		if (e.getWorld().restoringBlockSnapshots) return;
		if (!e.getWorld().getGameRules().getBoolean("doTileDrops")) return;
		if (e.getWorld().isRemote) return;
		if (e.getPlayer() == null) return;
		if (e.getPlayer() instanceof FakePlayer) return;
		if (!harvesterHoldsMagnetic(e.getPlayer())) return;
		BlockPos pos = e.getPos();
		int amount = e.getExpToDrop();
		int oldCooldown = e.getPlayer().xpCooldown;
		while (amount > 0) {
			int i = EntityXPOrb.getXPSplit(amount);
			amount -= i;
			EntityXPOrb exp = new EntityXPOrb(e.getWorld(), pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, i);
			e.getPlayer().xpCooldown = 0;
			int oldDelay = exp.delayBeforeCanPickup;
			exp.delayBeforeCanPickup = 0;
			e.getWorld().spawnEntity(exp);
			exp.onCollideWithPlayer(e.getPlayer());
			if (!exp.isDead) {
				exp.delayBeforeCanPickup = oldDelay;
			}
		}
		e.getPlayer().xpCooldown = oldCooldown;
		e.setExpToDrop(0);
	}

	public static void onDrops(BlockEvent.HarvestDropsEvent e) {
		if (e.getWorld().restoringBlockSnapshots) return;
		if (!e.getWorld().getGameRules().getBoolean("doTileDrops")) return;
		if (e.getWorld().isRemote) return;
		if (e.getHarvester() == null) return;;
		if (!harvesterHoldsMagnetic(e.getHarvester())) return;
		if (e.getHarvester() instanceof FakePlayer) return;
		BlockPos pos = e.getPos();
		for (ItemStack is : e.getDrops()) {
			if (is.isEmpty()) continue;
			if (e.getWorld().rand.nextFloat() <= e.getDropChance()) {
				double xOfs = e.getWorld().rand.nextFloat() * 0.5 + 0.25;
				double yOfs = e.getWorld().rand.nextFloat() * 0.5 + 0.25;
				double zOfs = e.getWorld().rand.nextFloat() * 0.5 + 0.25;
				EntityItem ei = new EntityItem(e.getWorld(), pos.getX()+xOfs, pos.getY()+yOfs, pos.getZ()+zOfs, is);
				ei.setNoPickupDelay();
				e.getWorld().spawnEntity(ei);
				ei.onCollideWithPlayer(e.getHarvester());
				if (!ei.isDead) {
					ei.setDefaultPickupDelay();
				}
			}
		}
		e.getDrops().clear();
	}


	private static boolean harvesterHoldsMagnetic(EntityPlayer harvester) {
		return EnchantmentHelper.getEnchantmentLevel(ModEnchantments.magnetic, harvester.getHeldItemMainhand()) > 0;
	}
}
