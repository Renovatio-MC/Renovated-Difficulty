package RenovatioMod.renovated_difficulty.mixin.zombie;

import RenovatioMod.renovated_difficulty.api.CustomDifficultyUtil;
import net.minecraft.entity.mob.ZombieEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(ZombieEntity.class)
public abstract class ZombieWeaponSpawnMixin {
    @ModifyConstant(method = "initEquipment", constant = @Constant(floatValue = 0.05F))
    private float renovatedDifficulty$overrideHardWeaponChance(float vanillaChance) {
        ZombieEntity self = (ZombieEntity) (Object) this;
        return CustomDifficultyUtil.getZombieWeaponChance(self.getWorld().getDifficulty());
    }

    @ModifyConstant(method = "initEquipment", constant = @Constant(floatValue = 0.01F))
    private float renovatedDifficulty$overrideNonHardWeaponChance(float vanillaChance) {
        ZombieEntity self = (ZombieEntity) (Object) this;
        return CustomDifficultyUtil.getZombieWeaponChance(self.getWorld().getDifficulty());
    }
}
