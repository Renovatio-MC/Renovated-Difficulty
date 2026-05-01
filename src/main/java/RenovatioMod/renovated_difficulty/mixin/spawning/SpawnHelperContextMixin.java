package RenovatioMod.renovated_difficulty.mixin.spawning;

import RenovatioMod.renovated_difficulty.api.SpawnCapContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SpawnHelper.class)
public abstract class SpawnHelperContextMixin {
    @Inject(method = "spawn", at = @At("HEAD"))
    private static void renovatedDifficulty$setSpawnDifficultyContext(
            ServerWorld world,
            WorldChunk chunk,
            SpawnHelper.Info info,
            boolean spawnAnimals,
            boolean spawnMonsters,
            boolean shouldSpawnAnimals,
            CallbackInfo ci
    ) {
        SpawnCapContext.setCurrentDifficulty(world.getDifficulty());
    }

    @Inject(method = "spawn", at = @At("RETURN"))
    private static void renovatedDifficulty$clearSpawnDifficultyContext(
            ServerWorld world,
            WorldChunk chunk,
            SpawnHelper.Info info,
            boolean spawnAnimals,
            boolean spawnMonsters,
            boolean shouldSpawnAnimals,
            CallbackInfo ci
    ) {
        SpawnCapContext.clear();
    }
}
