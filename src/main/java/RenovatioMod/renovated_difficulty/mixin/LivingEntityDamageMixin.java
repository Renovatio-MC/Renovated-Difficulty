package RenovatioMod.renovated_difficulty.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.Difficulty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityDamageMixin {
    private static final int TRANQUIL_ID = 4;
    private static final int BRUTAL_ID = 5;
    private static final int NIGHTMARE_ID = 6;

    @Inject(method = "modifyAppliedDamage", at = @At("RETURN"), cancellable = true)
    private void renovatedDifficulty$applyCustomMobScaling(
            DamageSource source,
            float damage,
            CallbackInfoReturnable<Float> cir
    ) {
        Entity attacker = source.getAttacker();
        if (!(attacker instanceof MobEntity)) {
            return;
        }

        Difficulty activeDifficulty = ((LivingEntity) (Object) this).getWorld().getDifficulty();
        int difficultyId = activeDifficulty.getId();
        float d = cir.getReturnValueF();
        float scaled = switch (difficultyId) {
            case TRANQUIL_ID -> Math.min(d, (d / 3.0f) + (2.0f / 3.0f));
            case BRUTAL_ID -> 1.75f * d;
            case NIGHTMARE_ID -> 2.25f * d;
            default -> d;
        };

        if (scaled != d) {
            cir.setReturnValue(scaled);
        }
    }
}
