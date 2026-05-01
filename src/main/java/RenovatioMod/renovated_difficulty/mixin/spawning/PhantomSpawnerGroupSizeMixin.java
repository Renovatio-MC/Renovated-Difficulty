package RenovatioMod.renovated_difficulty.mixin.spawning;

import RenovatioMod.renovated_difficulty.api.CustomDifficultyUtil;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Difficulty;
import net.minecraft.world.spawner.PhantomSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(PhantomSpawner.class)
public abstract class PhantomSpawnerGroupSizeMixin {
    @Redirect(
            method = "spawn",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/random/Random;nextInt(I)I"),
            slice = @Slice(
                    from = @At(value = "INVOKE", target = "Lnet/minecraft/world/LocalDifficulty;getGlobalDifficulty()Lnet/minecraft/world/Difficulty;"),
                    to = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/PhantomEntity;initialize(Lnet/minecraft/world/ServerWorldAccess;Lnet/minecraft/world/LocalDifficulty;Lnet/minecraft/entity/SpawnReason;Lnet/minecraft/entity/EntityData;)Lnet/minecraft/entity/EntityData;")
            )
    )
    private int renovatedDifficulty$overridePhantomGroupRoll(Random random, int bound, ServerWorld world, boolean spawnMonsters, boolean spawnAnimals) {
        Difficulty difficulty = world.getDifficulty();
        int min = CustomDifficultyUtil.getPhantomGroupMin(difficulty);
        int max = CustomDifficultyUtil.getPhantomGroupMax(difficulty);
        int range = Math.max(1, max - min + 1);
        return (min - 1) + random.nextInt(range);
    }
}
