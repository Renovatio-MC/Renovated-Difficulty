package RenovatioMod.renovated_difficulty.mixin.raid;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(targets = "net.minecraft.village.raid.Raid$Member")
public interface RaidMemberAccessor {
    @Accessor("countInWave")
    int[] renovatedDifficulty$getCountInWave();
}
