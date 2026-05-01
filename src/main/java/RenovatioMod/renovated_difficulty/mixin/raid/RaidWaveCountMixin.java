package RenovatioMod.renovated_difficulty.mixin.raid;

import RenovatioMod.renovated_difficulty.api.CustomDifficultyUtil;
import net.minecraft.village.raid.Raid;
import net.minecraft.world.Difficulty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Raid.class)
public abstract class RaidWaveCountMixin {
    @Inject(method = "getMaxWaves", at = @At("HEAD"), cancellable = true)
    private void renovatedDifficulty$overrideWaveCount(Difficulty difficulty, CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(CustomDifficultyUtil.getRaidWaveCount(difficulty));
    }
}
