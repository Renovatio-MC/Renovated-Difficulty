package RenovatioMod.renovated_difficulty.mixin;

import RenovatioMod.renovated_difficulty.api.CustomDifficultyUtil;
import RenovatioMod.renovated_difficulty.api.NeutralHostileAccessor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(LivingEntity.class)
public abstract class LivingEntityNeutralAggroMixin {
    private static final long PROVOKED_TICKS = 20L * 30L;
    private static final double CALL_FOR_HELP_RADIUS = 24.0D;

    @Inject(method = "damage", at = @At("RETURN"))
    private void renovatedDifficulty$alertNearbyWhenPlayerAttacks(
            DamageSource source,
            float amount,
            CallbackInfoReturnable<Boolean> cir
    ) {
        if (!cir.getReturnValue()) {
            return;
        }

        LivingEntity self = (LivingEntity) (Object) this;
        if (!(self instanceof HostileEntity hostile)) {
            return;
        }

        if (!CustomDifficultyUtil.isTranquilDifficulty(self.getWorld().getDifficulty())) {
            return;
        }

        if (hostile.getType().isIn(NeutralHostileAccessor.RENOVATED_NEUTRAL_EXCLUDED_BOSSES)) {
            return;
        }

        if (!(source.getAttacker() instanceof PlayerEntity player)) {
            return;
        }

        World world = self.getWorld();
        long untilTick = world.getTime() + PROVOKED_TICKS;

        NeutralHostileAccessor selfAccessor = (NeutralHostileAccessor) hostile;
        selfAccessor.renovatedDifficulty$setProvoked(player.getUuid(), untilTick);
        ((MobEntity) hostile).setTarget(player);

        List<? extends HostileEntity> nearbySameType = world.getEntitiesByClass(
                hostile.getClass(),
                hostile.getBoundingBox().expand(CALL_FOR_HELP_RADIUS),
                other -> other != hostile && other.isAlive() && other.getType() == hostile.getType()
        );

        for (HostileEntity ally : nearbySameType) {
            if (ally.getType().isIn(NeutralHostileAccessor.RENOVATED_NEUTRAL_EXCLUDED_BOSSES)) {
                continue;
            }

            NeutralHostileAccessor accessor = (NeutralHostileAccessor) ally;
            accessor.renovatedDifficulty$setProvoked(player.getUuid(), untilTick);
            ((MobEntity) ally).setTarget(player);
        }
    }
}
