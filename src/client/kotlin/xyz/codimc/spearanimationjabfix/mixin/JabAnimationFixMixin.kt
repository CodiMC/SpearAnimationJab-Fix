package xyz.codimc.spearanimationjabfix.mixin

import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.SwingAnimationType
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import xyz.codimc.spearanimationjabfix.swingDuration

/**
 * Since 26.3 snapshot 7, a playing swing keeps the animation it started with even when the
 * held item changes, so swapping off a spear mid-lunge plays the stab on the new item.
 *
 * Each tick, just before the swing state advances, if a stab swing is playing but the
 * swinging hand's item no longer uses a stab animation, swap in the animation of the item
 * actually held.
 *
 * Client-side only: this class patches LivingEntity, which the integrated server also runs,
 * so every path on a non-client level returns before anything is read or written.
 */
@Mixin(LivingEntity::class)
abstract class JabAnimationFixMixin {

    @Suppress("CAST_NEVER_SUCCEEDS")
    @Inject(
        method = ["baseTick"],
        at = [At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/LivingEntity\$SwingState;tick()V"
        )]
    )
    private fun spearanimationjabfixDropStaleStab(ci: CallbackInfo) {
        val self = this as Any as LivingEntity

        // Only players, and only on the logical client (never the integrated server).
        if (self !is Player) return
        if (!self.level().isClientSide) return

        val state = (self as LivingEntitySwingStateAccessor).spearanimationjabfixGetSwingState()
        val accessor = state as SwingStateCurrentSwingAccessor

        val current = accessor.spearanimationjabfixGetCurrentSwing() ?: return
        if (current.animation().type() != SwingAnimationType.STAB) return

        val held = self.getItemInHand(current.hand())
        val replacement = held.getAttackAnimation()
        if (replacement.type() == SwingAnimationType.STAB) return // held item legitimately stabs

        accessor.spearanimationjabfixSetCurrentSwing(
            LivingEntity.SwingDescription(
                current.hand(),
                replacement,
                swingDuration(self, replacement)
            )
        )
    }
}