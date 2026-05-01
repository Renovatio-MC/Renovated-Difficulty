package RenovatioMod.renovated_difficulty.mixin.effects;

import RenovatioMod.renovated_difficulty.api.CustomDifficultyUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.BeeEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BeeEntity.class)
public abstract class BeePoisonMixin {
    @Inject(method = "tryAttack", at = @At("RETURN"))
    private void renovatedDifficulty$overridePoisonDuration(Entity target, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue() || !(target instanceof LivingEntity living)) {
            return;
        }
        BeeEntity self = (BeeEntity) (Object) this;
        int seconds = CustomDifficultyUtil.getBeePoisonSeconds(self.getWorld().getDifficulty());
        if (seconds > 0) {
            living.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, seconds * 20, 0), self);
        }
    }
}
