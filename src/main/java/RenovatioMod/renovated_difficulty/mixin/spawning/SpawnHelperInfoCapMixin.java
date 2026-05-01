package RenovatioMod.renovated_difficulty.mixin.spawning;

import RenovatioMod.renovated_difficulty.api.CustomDifficultyUtil;
import RenovatioMod.renovated_difficulty.api.SpawnCapContext;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.world.Difficulty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(targets = "net.minecraft.world.SpawnHelper$Info")
public abstract class SpawnHelperInfoCapMixin {
    @Redirect(
            method = "isBelowCap",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/SpawnGroup;getCapacity()I")
    )
    private int renovatedDifficulty$overrideMonsterCapInNaturalSpawn(SpawnGroup group) {
        int vanillaCap = group.getCapacity();
        if (group != SpawnGroup.MONSTER) {
            return vanillaCap;
        }

        Difficulty difficulty = SpawnCapContext.getCurrentDifficulty();
        if (difficulty == null) {
            return vanillaCap;
        }

        return CustomDifficultyUtil.getMonsterSpawnCap(difficulty, vanillaCap);
    }
}
