package RenovatioMod.renovated_difficulty.mixin;

import net.minecraft.world.Difficulty;
import net.minecraft.util.StringIdentifiable;
import com.mojang.serialization.Codec;
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

    // Using Yarn names + Aliases ensures the Mixin finds the field
    // regardless of whether the RefMap or Remapper is working.
    @Shadow(aliases = {"field_5804"}) @Final @Mutable
    private static Difficulty[] field_5804;

    @Shadow(aliases = {"field_41668"}) @Final @Mutable
    private static Codec<Difficulty> field_41668;

    @Shadow(aliases = {"field_5800"}) @Final @Mutable
    private static IntFunction<Difficulty> field_5800;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void addCustomDifficulties(CallbackInfo ci) {
        System.out.println("[Renovatio] Executing Ultra-Stable Injection...");

        try {
            Difficulty tranquil = DifficultyInvoker.create("TRANQUIL", -1, 4, "tranquil");
            Difficulty brutal = DifficultyInvoker.create("BRUTAL", -1, 5, "brutal");
            Difficulty nightmare = DifficultyInvoker.create("NIGHTMARE", -1, 6, "nightmare");

            Difficulty[] v = field_5804;
            final Difficulty[] newValues = new Difficulty[] {
                    v[0], tranquil, v[1], v[2], v[3], brutal, nightmare
            };

            field_5804 = newValues;

            // Using an Anonymous Inner Class instead of a Lambda (->) to prevent Bootstrap Errors
            field_5800 = new IntFunction<Difficulty>() {
                @Override
                public Difficulty apply(int id) {
                    for (Difficulty d : newValues) {
                        if (d.getId() == id) return d;
                    }
                    return Difficulty.PEACEFUL;
                }
            };

            // Rebuilding the Codec using a Supplier class to avoid Method References (::)
            field_41668 = StringIdentifiable.createCodec(new Supplier<Difficulty[]>() {
                @Override
                public Difficulty[] get() {
                    return newValues;
                }
            });

            System.out.println("[Renovatio] Difficulty Surgery Successful.");
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}