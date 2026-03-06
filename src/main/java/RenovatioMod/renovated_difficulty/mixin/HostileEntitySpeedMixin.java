package RenovatioMod.renovated_difficulty.mixin;

import RenovatioMod.renovated_difficulty.api.CustomDifficultyUtil;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HostileEntity.class)
public abstract class HostileEntitySpeedMixin {
    @Unique
    private static final Identifier RENOVATED_SPEED_MODIFIER_ID = Identifier.of(
            "renovated_difficulty",
            "hostile_speed_scaling"
    );

    @Unique
    private double renovatedDifficulty$lastAppliedSpeedMultiplier = Double.NaN;

    @Inject(method = "tickMovement", at = @At("HEAD"))
    private void renovatedDifficulty$applyDifficultySpeedScaling(CallbackInfo ci) {
        HostileEntity self = (HostileEntity) (Object) this;
        if (self.getWorld().isClient()) {
            return;
        }

        EntityAttributeInstance speed = self.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
        if (speed == null) {
            return;
        }

        double multiplier = CustomDifficultyUtil.getHostileSpeedMultiplier(self.getWorld().getDifficulty());
        if (Math.abs(multiplier - this.renovatedDifficulty$lastAppliedSpeedMultiplier) < 1.0E-6) {
            return;
        }

        speed.removeModifier(RENOVATED_SPEED_MODIFIER_ID);

        if (Math.abs(multiplier - 1.0D) > 1.0E-6) {
            speed.addTemporaryModifier(new EntityAttributeModifier(
                    RENOVATED_SPEED_MODIFIER_ID,
                    multiplier - 1.0D,
                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
            ));
        }

        this.renovatedDifficulty$lastAppliedSpeedMultiplier = multiplier;
    }
}
