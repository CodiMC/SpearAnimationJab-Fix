package xyz.codimc.spearanimationjabfix

import net.minecraft.world.effect.MobEffectUtil
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.component.SwingAnimation

/** Vanilla swing length: base duration, shortened by Haste, lengthened by Mining Fatigue. */
fun swingDuration(entity: LivingEntity, animation: SwingAnimation): Int {
    var duration = animation.duration()

    if (MobEffectUtil.hasDigSpeed(entity)) {
        duration -= 1 + MobEffectUtil.getDigSpeedAmplification(entity)
    } else {
        val fatigue = entity.getEffect(MobEffects.MINING_FATIGUE)
        if (fatigue != null) {
            duration += (1 + fatigue.amplifier) * 2
        }
    }

    return maxOf(1, duration)
}