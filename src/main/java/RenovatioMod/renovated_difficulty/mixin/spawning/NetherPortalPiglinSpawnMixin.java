package RenovatioMod.renovated_difficulty.mixin.spawning;

import RenovatioMod.renovated_difficulty.api.CustomDifficultyUtil;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(NetherPortalBlock.class)
public abstract class NetherPortalPiglinSpawnMixin {
    @ModifyConstant(method = "randomTick", constant = @Constant(intValue = 2000))
    private int renovatedDifficulty$overridePortalPiglinBound(int vanillaBound, net.minecraft.block.BlockState state, ServerWorld world) {
        return CustomDifficultyUtil.getPortalPiglinRandomBound(world.getDifficulty());
    }
}
