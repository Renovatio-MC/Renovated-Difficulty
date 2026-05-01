package RenovatioMod.renovated_difficulty.mixin.zombie;

import RenovatioMod.renovated_difficulty.api.CustomDifficultyUtil;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ZombieEntity.class)
public abstract class ZombieReinforcementMixin {
    @Redirect(
            method = "damage",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getDifficulty()Lnet/minecraft/world/Difficulty;")
    )
    private Difficulty renovatedDifficulty$mapReinforcementDifficulty(World world) {
        return CustomDifficultyUtil.getZombieReinforcementGateDifficulty(world.getDifficulty());
    }

    @Redirect(
            method = "damage",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/ZombieEntity;getAttributeValue(Lnet/minecraft/registry/entry/RegistryEntry;)D")
    )
    private double renovatedDifficulty$scaleReinforcementChance(ZombieEntity self, RegistryEntry<?> attribute) {
        @SuppressWarnings("unchecked")
        RegistryEntry<EntityAttribute> typedAttribute = (RegistryEntry<EntityAttribute>) attribute;
        double vanilla = self.getAttributeValue(typedAttribute);
        return vanilla * CustomDifficultyUtil.getZombieReinforcementChanceMultiplier(self.getWorld().getDifficulty());
    }
}
