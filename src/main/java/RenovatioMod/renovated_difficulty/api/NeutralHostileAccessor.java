package RenovatioMod.renovated_difficulty.api;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.UUID;

public interface NeutralHostileAccessor {
    TagKey<EntityType<?>> RENOVATED_NEUTRAL_EXCLUDED_BOSSES = TagKey.of(
            RegistryKeys.ENTITY_TYPE,
            Identifier.of("renovated_difficulty", "neutral_behavior_excluded")
    );

    boolean renovatedDifficulty$isProvoked();

    boolean renovatedDifficulty$isProvokedBy(PlayerEntity player);

    void renovatedDifficulty$setProvoked(UUID playerUuid, long untilTick);
}
