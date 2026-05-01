package RenovatioMod.renovated_difficulty.mixin;

import RenovatioMod.renovated_difficulty.api.CustomDifficultyUtil;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ZombieEntity.class)
public abstract class ZombieLeaderSpawnMixin {
    @Inject(method = "initialize", at = @At("TAIL"))
    private void renovatedDifficulty$syncLeaderHealthAndScale(
            ServerWorldAccess world,
            LocalDifficulty difficulty,
            SpawnReason spawnReason,
            EntityData entityData,
            CallbackInfoReturnable<EntityData> cir
    ) {
        ZombieEntity self = (ZombieEntity) (Object) this;
        float maxHealth = self.getMaxHealth();

        if (maxHealth > self.getHealth()) {
            self.setHealth(maxHealth);
        }

        if (maxHealth > 20.0F) {
            float scale = CustomDifficultyUtil.getZombieLeaderScale(maxHealth);
            EntityAttributeInstance scaleAttribute = self.getAttributeInstance(EntityAttributes.GENERIC_SCALE);
            if (scaleAttribute != null) {
                scaleAttribute.setBaseValue(scale);
            }
        }
    }
}
