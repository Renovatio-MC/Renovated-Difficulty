package RenovatioMod.renovated_difficulty.mixin.hunger;

import RenovatioMod.renovated_difficulty.api.CustomDifficultyUtil;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.Difficulty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HungerManager.class)
public abstract class HungerManagerScalingMixin {
    @Shadow private int foodLevel;
    @Shadow private float saturationLevel;
    @Shadow private float exhaustion;
    @Shadow private int foodTickTimer;
    @Shadow private int prevFoodLevel;

    @Shadow public abstract void addExhaustion(float exhaustion);

    @Inject(method = "update", at = @At("HEAD"), cancellable = true)
    private void renovatedDifficulty$scaleNaturalRegenAndStarvation(PlayerEntity player, CallbackInfo ci) {
        Difficulty difficulty = player.getWorld().getDifficulty();
        this.prevFoodLevel = this.foodLevel;

        if (this.exhaustion > 4.0F) {
            this.exhaustion -= 4.0F;
            if (this.saturationLevel > 0.0F) {
                this.saturationLevel = Math.max(this.saturationLevel - 1.0F, 0.0F);
            } else if (difficulty != Difficulty.PEACEFUL) {
                this.foodLevel = Math.max(this.foodLevel - 1, 0);
            }
        }

        boolean naturalRegen = player.getWorld().getGameRules().getBoolean(net.minecraft.world.GameRules.NATURAL_REGENERATION);
        float regenSpeedMultiplier = (float) CustomDifficultyUtil.getNaturalRegenSpeedMultiplier(difficulty);
        float saturationCostMultiplier = (float) CustomDifficultyUtil.getNaturalRegenSaturationCostMultiplier(difficulty);
        int fastRegenInterval = Math.max(1, Math.round(10.0F / regenSpeedMultiplier));
        int slowRegenInterval = Math.max(1, Math.round(80.0F / regenSpeedMultiplier));
        float starvationFloor = CustomDifficultyUtil.getStarvationHealthFloor(difficulty);

        if (naturalRegen && this.saturationLevel > 0.0F && player.canFoodHeal() && this.foodLevel >= 20) {
            this.foodTickTimer++;
            if (this.foodTickTimer >= fastRegenInterval) {
                float heal = Math.min(this.saturationLevel, 6.0F);
                player.heal(heal / 6.0F);
                this.addExhaustion(heal * saturationCostMultiplier);
                this.foodTickTimer = 0;
            }
        } else if (naturalRegen && this.foodLevel >= 18 && player.canFoodHeal()) {
            this.foodTickTimer++;
            if (this.foodTickTimer >= slowRegenInterval) {
                player.heal(1.0F);
                this.addExhaustion(6.0F * saturationCostMultiplier);
                this.foodTickTimer = 0;
            }
        } else if (this.foodLevel <= 0) {
            this.foodTickTimer++;
            if (this.foodTickTimer >= 80) {
                DamageSource starvation = player.getDamageSources().starve();
                if (player.getHealth() > starvationFloor) {
                    player.damage(starvation, 1.0F);
                }

                int extraInterval = CustomDifficultyUtil.getStarvationExtraDamageIntervalTicks(difficulty);
                float extraDamage = CustomDifficultyUtil.getStarvationExtraDamageAmount(difficulty);
                if (extraInterval > 0 && extraDamage > 0.0F && this.foodTickTimer % extraInterval == 0) {
                    player.damage(starvation, extraDamage);
                }
                this.foodTickTimer = 0;
            }
        } else {
            this.foodTickTimer = 0;
        }

        ci.cancel();
    }
}
