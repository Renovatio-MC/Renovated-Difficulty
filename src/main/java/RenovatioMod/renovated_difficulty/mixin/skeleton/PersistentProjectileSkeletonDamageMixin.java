package RenovatioMod.renovated_difficulty.mixin.skeleton;

import RenovatioMod.renovated_difficulty.api.CustomDifficultyUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.world.Difficulty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PersistentProjectileEntity.class)
public abstract class PersistentProjectileSkeletonDamageMixin {
    @Inject(method = "applyDamageModifier", at = @At("TAIL"))
    private void renovatedDifficulty$applySkeletonDifficultyBonus(float damageModifier, CallbackInfo ci) {
        PersistentProjectileEntity self = (PersistentProjectileEntity) (Object) this;
        Entity owner = self.getOwner();
        if (!(owner instanceof AbstractSkeletonEntity)) {
            return;
        }

        Difficulty difficulty = self.getWorld().getDifficulty();
        String difficultyKey = difficulty.asString();
        double vanillaBonus = difficulty.getId() * 0.11D;
        double customBonus = switch (difficultyKey) {
            case "tranquil" -> 0.0D;
            case "brutal" -> 0.44D;
            case "nightmare" -> 0.55D;
            case "cataclysm" -> 0.66D;
            default -> CustomDifficultyUtil.getSkeletonBonusArrowDamage(difficulty);
        };
        double adjustment = customBonus - vanillaBonus;
        if (Math.abs(adjustment) > 1.0E-6D) {
            self.setDamage(self.getDamage() + adjustment);
        }
    }
}
