package RenovatioMod.renovated_difficulty.mixin;

import RenovatioMod.renovated_difficulty.api.CustomDifficultyUtil;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LightType;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HostileEntity.class)
public abstract class HostileEntitySpawnMixin {
    @Inject(method = "isSpawnDark", at = @At("HEAD"), cancellable = true)
    private static void renovatedDifficulty$overrideBlockLightSpawnRule(
            ServerWorldAccess world,
            BlockPos pos,
            Random random,
            CallbackInfoReturnable<Boolean> cir
    ) {
        int customLightLimit = CustomDifficultyUtil.getHostileSpawnBlockLightLimitOverride(world.getDifficulty());
        if (customLightLimit < 0) {
            return;
        }

        if (world.getLightLevel(LightType.SKY, pos) > random.nextInt(32)) {
            cir.setReturnValue(false);
            return;
        }

        DimensionType dimension = world.getDimension();
        int blockLightLimit = dimension.monsterSpawnBlockLightLimit();
        if (blockLightLimit < 15) {
            int effectiveBlockLimit = Math.max(blockLightLimit, customLightLimit);
            if (world.getLightLevel(LightType.BLOCK, pos) > effectiveBlockLimit) {
                cir.setReturnValue(false);
                return;
            }
        }

        int lightTestValue = world.toServerWorld().isThundering()
                ? world.getLightLevel(pos, 10)
                : world.getLightLevel(pos);
        cir.setReturnValue(lightTestValue <= dimension.monsterSpawnLightTest().get(random));
    }
}
