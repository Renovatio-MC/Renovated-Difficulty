package RenovatioMod.renovated_difficulty.mixin;

import RenovatioMod.renovated_difficulty.api.CustomDifficultyUtil;
import RenovatioMod.renovated_difficulty.api.NeutralHostileAccessor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MobEntity.class)
public abstract class MobEntityNeutralTargetMixin {
    @Inject(method = "setTarget", at = @At("HEAD"), cancellable = true)
    private void renovatedDifficulty$preventUnprovokedPlayerTargeting(LivingEntity target, CallbackInfo ci) {
        MobEntity self = (MobEntity) (Object) this;
        if (!(self instanceof HostileEntity hostile)) {
            return;
        }

        if (!CustomDifficultyUtil.isTranquilDifficulty(self.getWorld().getDifficulty())) {
            return;
        }

        if (hostile.getType().isIn(NeutralHostileAccessor.RENOVATED_NEUTRAL_EXCLUDED_BOSSES)) {
            return;
        }

        if (!(target instanceof PlayerEntity player)) {
            return;
        }

        NeutralHostileAccessor accessor = (NeutralHostileAccessor) hostile;
        if (!accessor.renovatedDifficulty$isProvokedBy(player)) {
            ci.cancel();
        }
    }

    @Inject(method = "mobTick", at = @At("HEAD"))
    private void renovatedDifficulty$clearExpiredPlayerTarget(CallbackInfo ci) {
        MobEntity self = (MobEntity) (Object) this;
        if (!(self instanceof HostileEntity hostile)) {
            return;
        }

        if (!CustomDifficultyUtil.isTranquilDifficulty(self.getWorld().getDifficulty())) {
            return;
        }

        if (hostile.getType().isIn(NeutralHostileAccessor.RENOVATED_NEUTRAL_EXCLUDED_BOSSES)) {
            return;
        }

        LivingEntity currentTarget = self.getTarget();
        if (!(currentTarget instanceof PlayerEntity player)) {
            return;
        }

        NeutralHostileAccessor accessor = (NeutralHostileAccessor) hostile;
        if (!accessor.renovatedDifficulty$isProvokedBy(player)) {
            self.setTarget(null);
        }
    }
}
