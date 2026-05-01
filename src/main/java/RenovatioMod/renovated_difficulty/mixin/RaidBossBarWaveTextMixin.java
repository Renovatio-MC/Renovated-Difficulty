package RenovatioMod.renovated_difficulty.mixin;

import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.text.Text;
import net.minecraft.village.raid.Raid;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Raid.class)
public abstract class RaidBossBarWaveTextMixin {
    @Shadow @Final
    private static Text EVENT_TEXT;

    @Shadow
    private int wavesSpawned;

    @Shadow @Final
    private int waveCount;

    @Shadow
    private int preRaidTicks;

    @Shadow
    private int badOmenLevel;

    @Shadow
    public abstract int getRaiderCount();

    @Redirect(
            method = "tick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/boss/ServerBossBar;setName(Lnet/minecraft/text/Text;)V")
    )
    private void renovatedDifficulty$setWaveAwareRaidBarName(ServerBossBar bossBar, Text name) {
        if (name == EVENT_TEXT) {
            bossBar.setName(this.renovatedDifficulty$getWaveAwareBossBarName());
            return;
        }

        bossBar.setName(name);
    }

    private Text renovatedDifficulty$getWaveAwareBossBarName() {
        if (this.renovatedDifficulty$isCompletedWaveState()) {
            return Text.translatable(
                    "renovated_difficulty.raid.wave_completed",
                    EVENT_TEXT,
                    Integer.valueOf(this.renovatedDifficulty$getClampedWave()),
                    Integer.valueOf(this.renovatedDifficulty$getTotalWaveCount())
            );
        }

        return Text.translatable(
                "renovated_difficulty.raid.wave_active",
                EVENT_TEXT,
                Integer.valueOf(this.renovatedDifficulty$getDisplayedWave())
        );
    }

    private boolean renovatedDifficulty$isCompletedWaveState() {
        if (this.wavesSpawned <= 0) {
            return false;
        }

        if (this.preRaidTicks > 0 && this.renovatedDifficulty$shouldSpawnMoreGroups()) {
            return true;
        }

        return this.getRaiderCount() == 0 && !this.renovatedDifficulty$shouldSpawnMoreGroups();
    }

    private int renovatedDifficulty$getDisplayedWave() {
        if (this.wavesSpawned <= 0) {
            return 1;
        }

        return this.renovatedDifficulty$getClampedWave();
    }

    private int renovatedDifficulty$getClampedWave() {
        return Math.max(1, Math.min(this.wavesSpawned, this.renovatedDifficulty$getTotalWaveCount()));
    }

    private int renovatedDifficulty$getTotalWaveCount() {
        return this.waveCount + (this.renovatedDifficulty$hasExtraWave() ? 1 : 0);
    }

    private boolean renovatedDifficulty$shouldSpawnMoreGroups() {
        if (this.renovatedDifficulty$hasExtraWave()) {
            return !this.renovatedDifficulty$hasSpawnedExtraWave();
        }

        return !this.renovatedDifficulty$hasSpawnedFinalWave();
    }

    private boolean renovatedDifficulty$hasSpawnedFinalWave() {
        return this.wavesSpawned == this.waveCount;
    }

    private boolean renovatedDifficulty$hasExtraWave() {
        return this.badOmenLevel > 1;
    }

    private boolean renovatedDifficulty$hasSpawnedExtraWave() {
        return this.wavesSpawned > this.waveCount;
    }
}
