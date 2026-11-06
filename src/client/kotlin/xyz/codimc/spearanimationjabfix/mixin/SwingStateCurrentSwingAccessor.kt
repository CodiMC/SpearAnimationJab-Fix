package xyz.codimc.spearanimationjabfix.mixin

import net.minecraft.world.entity.LivingEntity
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.gen.Accessor

@Mixin(LivingEntity.SwingState::class)
interface SwingStateCurrentSwingAccessor {
    @Accessor("currentSwing")
    fun spearanimationjabfixGetCurrentSwing(): LivingEntity.SwingDescription?

    @Accessor("currentSwing")
    fun spearanimationjabfixSetCurrentSwing(swing: LivingEntity.SwingDescription?)
}