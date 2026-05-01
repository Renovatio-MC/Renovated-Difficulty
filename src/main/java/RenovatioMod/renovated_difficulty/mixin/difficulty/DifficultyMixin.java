package RenovatioMod.renovated_difficulty.mixin.difficulty;

import net.minecraft.util.StringIdentifiable;
import net.minecraft.world.Difficulty;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.IntFunction;
import java.util.function.Supplier;

@Mixin(Difficulty.class)
public class DifficultyMixin {
    @Shadow(aliases = {"field_5804", "VALUES"}) @Final @Mutable
    private static Difficulty[] $VALUES;

    @Shadow(aliases = {"field_41668"}) @Final @Mutable
    private static StringIdentifiable.EnumCodec<Difficulty> CODEC;

    @Shadow(aliases = {"field_5800"}) @Final @Mutable
    private static IntFunction<Difficulty> BY_ID;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void addCustomDifficulties(CallbackInfo ci) {
        try {
            Difficulty[] v = $VALUES;
            if (v.length > 4) {
                return;
            }

            final int tranquilOrdinal = v.length;
            final int brutalOrdinal = v.length + 1;
            final int nightmareOrdinal = v.length + 2;
            final int cataclysmOrdinal = v.length + 3;

            Difficulty tranquil = DifficultyInvoker.create("TRANQUIL", tranquilOrdinal, 4, "tranquil");
            Difficulty brutal = DifficultyInvoker.create("BRUTAL", brutalOrdinal, 5, "brutal");
            Difficulty nightmare = DifficultyInvoker.create("NIGHTMARE", nightmareOrdinal, 6, "nightmare");
            Difficulty cataclysm = DifficultyInvoker.create("CATACLYSM", cataclysmOrdinal, 7, "cataclysm");

            final Difficulty[] newValues = new Difficulty[] {
                    v[0], tranquil, v[1], v[2], v[3], brutal, nightmare, cataclysm
            };

            $VALUES = newValues;

            BY_ID = id -> {
                for (Difficulty d : newValues) {
                    if (d.getId() == id) return d;
                }
                return id < 0 ? Difficulty.PEACEFUL : Difficulty.HARD;
            };

            CODEC = StringIdentifiable.createCodec(new Supplier<>() {
                @Override
                public Difficulty[] get() {
                    return newValues;
                }
            });
        } catch (Throwable t) {
            throw new RuntimeException("Failed to inject custom difficulties", t);
        }
    }
}
