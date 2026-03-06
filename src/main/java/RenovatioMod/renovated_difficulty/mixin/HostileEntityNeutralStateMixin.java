package RenovatioMod.renovated_difficulty.mixin;

import RenovatioMod.renovated_difficulty.api.NeutralHostileAccessor;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.UUID;

@Mixin(HostileEntity.class)
public abstract class HostileEntityNeutralStateMixin implements NeutralHostileAccessor {
    @Unique
    private long renovatedDifficulty$provokedUntilTick;

    @Unique
    private UUID renovatedDifficulty$provokedBy;

    @Override
    public boolean renovatedDifficulty$isProvoked() {
        HostileEntity self = (HostileEntity) (Object) this;
        return self.getWorld().getTime() < this.renovatedDifficulty$provokedUntilTick;
    }

    @Override
    public boolean renovatedDifficulty$isProvokedBy(PlayerEntity player) {
        return this.renovatedDifficulty$isProvoked()
                && player != null
                && player.getUuid().equals(this.renovatedDifficulty$provokedBy);
    }

    @Override
    public void renovatedDifficulty$setProvoked(UUID playerUuid, long untilTick) {
        this.renovatedDifficulty$provokedBy = playerUuid;
        this.renovatedDifficulty$provokedUntilTick = untilTick;
    }
}
