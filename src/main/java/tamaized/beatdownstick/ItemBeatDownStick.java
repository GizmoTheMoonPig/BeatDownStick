package tamaized.beatdownstick;

import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.entity.PartEntity;

public class ItemBeatDownStick extends Item {

	private final boolean superStick;

	public ItemBeatDownStick(Properties properties, boolean superStick) {
		super(properties);
		this.superStick = superStick;
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
		boolean flag = false;
		float dmg;
		boolean dontOneShot = entity.getType().is(BeatDownStick.DONT_ONE_SHOT);
		DamageSource source = this.superStick ? player.damageSources().source(BeatDownStick.ANNIHILATE) : player.damageSources().generic();
		if (entity instanceof LivingEntity living) {
			dmg = this.superStick ? Float.MAX_VALUE : dontOneShot ? (living.getMaxHealth() / 10) : (living.getMaxHealth()); // do 10% instead of 100% dmg to bosses
			player.playSound(BeatDownStick.WHAM.get(), 0.6F, 0.5F + player.getRandom().nextFloat());
			if (living.hurt(source, dmg))
				flag = true;
		} else if (entity instanceof PartEntity<?> part) {
			if (part.getParent() instanceof EnderDragon dragon) { // Must be DamageSource.Player for dragon
				player.playSound(BeatDownStick.WHAM.get(), 0.6F, 0.5F + player.getRandom().nextFloat());
				if (part.hurt(player.damageSources().playerAttack(player), this.superStick ? Float.MAX_VALUE : (dragon.getMaxHealth() / 10)))
					flag = true;
			} else if (part.getParent() instanceof LivingEntity living) {
				dmg = this.superStick ? Float.MAX_VALUE : dontOneShot ? (living.getMaxHealth() / 10) : (living.getMaxHealth());
				player.playSound(BeatDownStick.WHAM.get(), 0.6F, 0.5F + player.getRandom().nextFloat());
				if (living.hurt(source, dmg))
					flag = true;
			}
		}
		if (flag) {
			if (!player.getAbilities().instabuild && !this.superStick)
				stack.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
			if (player.level() instanceof ServerLevel server) {
				server.getChunkSource().broadcastAndSend(player, new ClientboundAnimatePacket(entity, 5));
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean isEnchantable(ItemStack stack) {
		return false;
	}

	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		return false;
	}

	@Override
	public float getXpRepairRatio(ItemStack stack) {
		return 0.0F;
	}
}
