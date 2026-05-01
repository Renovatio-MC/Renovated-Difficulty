package RenovatioMod.renovated_difficulty.mixin.zombie;

import RenovatioMod.renovated_difficulty.api.CustomDifficultyUtil;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ZombieEntity.class)
public abstract class ZombieLeaderSpawnMixin {
    private static final Identifier RENOVATED_LEADER_ATTACK_MODIFIER_ID =
            Identifier.of("renovated_difficulty", "zombie_leader_attack_multiplier");
    private static final Identifier RENOVATED_LEADER_SPEED_MODIFIER_ID =
            Identifier.of("renovated_difficulty", "zombie_leader_speed_multiplier");

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

        if (maxHealth <= 20.0F) {
            return;
        }

        float scale = CustomDifficultyUtil.getZombieLeaderScale(maxHealth);
        EntityAttributeInstance scaleAttribute = self.getAttributeInstance(EntityAttributes.GENERIC_SCALE);
        if (scaleAttribute != null) {
            scaleAttribute.setBaseValue(scale);
        }

        float attackMultiplier = CustomDifficultyUtil.getZombieLeaderAttackMultiplier(maxHealth);
        EntityAttributeInstance attackAttribute = self.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE);
        if (attackAttribute != null) {
            attackAttribute.removeModifier(RENOVATED_LEADER_ATTACK_MODIFIER_ID);
            double attackBonus = attackMultiplier - 1.0D;
            if (attackBonus > 0.0D) {
                attackAttribute.addPersistentModifier(new EntityAttributeModifier(
                        RENOVATED_LEADER_ATTACK_MODIFIER_ID,
                        attackBonus,
                        EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                ));
            }
        }

        float speedMultiplier = CustomDifficultyUtil.getZombieLeaderSpeedMultiplier(maxHealth);
        EntityAttributeInstance speedAttribute = self.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
        if (speedAttribute != null) {
            speedAttribute.removeModifier(RENOVATED_LEADER_SPEED_MODIFIER_ID);
            double speedDelta = speedMultiplier - 1.0D;
            if (speedDelta < 0.0D) {
                speedAttribute.addPersistentModifier(new EntityAttributeModifier(
                        RENOVATED_LEADER_SPEED_MODIFIER_ID,
                        speedDelta,
                        EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                ));
            }
        }
    }
}
