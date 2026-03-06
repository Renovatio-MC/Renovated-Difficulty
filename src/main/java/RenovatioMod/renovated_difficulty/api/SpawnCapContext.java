package RenovatioMod.renovated_difficulty.api;

import net.minecraft.world.Difficulty;

public final class SpawnCapContext {
    private static final ThreadLocal<Difficulty> CURRENT_DIFFICULTY = new ThreadLocal<>();

    private SpawnCapContext() {
    }

    public static void setCurrentDifficulty(Difficulty difficulty) {
        CURRENT_DIFFICULTY.set(difficulty);
    }

    public static Difficulty getCurrentDifficulty() {
        return CURRENT_DIFFICULTY.get();
    }

    public static void clear() {
        CURRENT_DIFFICULTY.remove();
    }
}
