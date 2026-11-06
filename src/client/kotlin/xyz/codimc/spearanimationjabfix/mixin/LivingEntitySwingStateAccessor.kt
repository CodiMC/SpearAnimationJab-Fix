package xyz.codimc.spearanimationjabfix.mixin

import net.minecraft.world.entity.LivingEntity
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.gen.Accessor

@Mixin(LivingEntity::class)
interface LivingEntitySwingStateAccessor {
    @Accessor("swingState")
    fun spearanimationjabfixGetSwingState(): LivingEntity.SwingState
}