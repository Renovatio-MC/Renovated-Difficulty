package RenovatioMod.renovated_difficulty.mixin.effects;

import RenovatioMod.renovated_difficulty.api.CustomDifficultyUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.minecraft.util.hit.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WitherSkullEntity.class)
public abstract class WitherSkullDurationMixin {
    @Inject(method = "onEntityHit", at = @At("TAIL"))
    private void renovatedDifficulty$overrideWitherDuration(EntityHitResult entityHitResult, CallbackInfo ci) {
        Entity target = entityHitResult.getEntity();
        if (!(target instanceof LivingEntity living)) {
            return;
        }
        WitherSkullEntity self = (WitherSkullEntity) (Object) this;
        int seconds = CustomDifficultyUtil.getWitherSkullWitherSeconds(self.getWorld().getDifficulty());
        if (seconds > 0) {
            living.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, seconds * 20, 1), self.getEffectCause());
        }
    }
}
