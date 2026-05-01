package RenovatioMod.renovated_difficulty.mixin.skeleton;

import RenovatioMod.renovated_difficulty.api.CustomDifficultyUtil;
import net.minecraft.entity.mob.BoggedEntity;
import net.minecraft.world.Difficulty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BoggedEntity.class)
public abstract class BoggedAttackCooldownMixin {
    @Inject(method = "getRegularAttackInterval", at = @At("HEAD"), cancellable = true)
    private void renovatedDifficulty$overrideRegularAttackInterval(CallbackInfoReturnable<Integer> cir) {
        BoggedEntity self = (BoggedEntity) (Object) this;
        Difficulty difficulty = self.getWorld().getDifficulty();
        int intervalTicks = Math.max(1, Math.round(CustomDifficultyUtil.getBoggedAttackCooldownSeconds(difficulty) * 20.0F));
        cir.setReturnValue(intervalTicks);
    }

    @Inject(method = "getHardAttackInterval", at = @At("HEAD"), cancellable = true)
    private void renovatedDifficulty$overrideHardAttackInterval(CallbackInfoReturnable<Integer> cir) {
        BoggedEntity self = (BoggedEntity) (Object) this;
        Difficulty difficulty = self.getWorld().getDifficulty();
        int intervalTicks = Math.max(1, Math.round(CustomDifficultyUtil.getBoggedAttackCooldownSeconds(difficulty) * 20.0F));
        cir.setReturnValue(intervalTicks);
    }
}
