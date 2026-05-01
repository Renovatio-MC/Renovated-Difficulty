package RenovatioMod.renovated_difficulty.mixin.skeleton;

import RenovatioMod.renovated_difficulty.api.CustomDifficultyUtil;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.world.Difficulty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractSkeletonEntity.class)
public abstract class AbstractSkeletonEntityCombatMixin {
    @Inject(method = "getRegularAttackInterval", at = @At("HEAD"), cancellable = true)
    private void renovatedDifficulty$overrideRegularAttackInterval(CallbackInfoReturnable<Integer> cir) {
        AbstractSkeletonEntity self = (AbstractSkeletonEntity) (Object) this;
        Difficulty difficulty = self.getWorld().getDifficulty();
        int intervalTicks = Math.max(1, Math.round(CustomDifficultyUtil.getSkeletonAttackCooldownSeconds(difficulty) * 20.0F));
        cir.setReturnValue(intervalTicks);
    }

    @Inject(method = "getHardAttackInterval", at = @At("HEAD"), cancellable = true)
    private void renovatedDifficulty$overrideHardAttackInterval(CallbackInfoReturnable<Integer> cir) {
        AbstractSkeletonEntity self = (AbstractSkeletonEntity) (Object) this;
        Difficulty difficulty = self.getWorld().getDifficulty();
        int intervalTicks = Math.max(1, Math.round(CustomDifficultyUtil.getSkeletonAttackCooldownSeconds(difficulty) * 20.0F));
        cir.setReturnValue(intervalTicks);
    }

    @ModifyArg(
            method = "shootAt",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/projectile/PersistentProjectileEntity;setVelocity(DDDFF)V"
            ),
            index = 4
    )
    private float renovatedDifficulty$overrideInaccuracy(float vanillaInaccuracy) {
        AbstractSkeletonEntity self = (AbstractSkeletonEntity) (Object) this;
        return CustomDifficultyUtil.getSkeletonInaccuracy(self.getWorld().getDifficulty());
    }
}
