package RenovatioMod.renovated_difficulty.mixin;

import net.minecraft.village.raid.Raid;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Raid.class)
public abstract class RaidWaveTableClampMixin {
    @Shadow @Final
    private int waveCount;

    @Inject(method = "getCount", at = @At("HEAD"), cancellable = true)
    private void renovatedDifficulty$clampMemberWaveTableIndex(
            @Coerce Object member,
            int wave,
            boolean extra,
            CallbackInfoReturnable<Integer> cir
    ) {
        int[] countInWave = ((RaidMemberAccessor) member).renovatedDifficulty$getCountInWave();
        if (countInWave.length == 0) {
            cir.setReturnValue(0);
            return;
        }

        int requestedIndex = extra ? this.waveCount : wave;
        int safeIndex = Math.max(0, Math.min(requestedIndex, countInWave.length - 1));
        cir.setReturnValue(countInWave[safeIndex]);
    }
}
