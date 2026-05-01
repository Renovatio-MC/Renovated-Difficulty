package RenovatioMod.renovated_difficulty.mixin;

import RenovatioMod.renovated_difficulty.api.CustomDifficultyUtil;
import net.minecraft.entity.mob.MobEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(MobEntity.class)
public abstract class MobEntityArmorScalingMixin {
    @ModifyVariable(method = "initEquipment", at = @At("STORE"), ordinal = 0)
    private float renovatedDifficulty$overrideArmorStopChance(float vanillaStopChance) {
        MobEntity self = (MobEntity) (Object) this;
        return CustomDifficultyUtil.getMobArmorStopChance(self.getWorld().getDifficulty(), vanillaStopChance);
    }
}
