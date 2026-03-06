package RenovatioMod.renovated_difficulty.api;

import net.minecraft.world.Difficulty;

public final class CustomDifficultyUtil {
    private static final int PEACEFUL_ID = 0;
    private static final int EASY_ID = 1;
    private static final int NORMAL_ID = 2;
    private static final int HARD_ID = 3;
    private static final int TRANQUIL_ID = 4;
    private static final int BRUTAL_ID = 5;
    private static final int NIGHTMARE_ID = 6;

    private CustomDifficultyUtil() {
    }

    public static boolean isTranquilDifficulty(Difficulty difficulty) {
        return difficulty.getId() == TRANQUIL_ID;
    }

    public static int getHostileSpawnBlockLightLimitOverride(Difficulty difficulty) {
        return switch (difficulty.getId()) {
            case BRUTAL_ID -> 2;
            case NIGHTMARE_ID -> 5;
            default -> -1;
        };
    }

    public static int getMonsterSpawnCap(Difficulty difficulty, int vanillaCap) {
        return switch (difficulty.getId()) {
            case EASY_ID -> 60;
            case NORMAL_ID -> 70;
            case HARD_ID -> 80;
            case TRANQUIL_ID -> 40;
            case BRUTAL_ID -> 100;
            case NIGHTMARE_ID -> 120;
            default -> vanillaCap;
        };
    }

    public static double getHostileSpeedMultiplier(Difficulty difficulty) {
        return switch (difficulty.getId()) {
            case PEACEFUL_ID, TRANQUIL_ID -> 0.85D;
            case EASY_ID -> 0.95D;
            case NORMAL_ID -> 1.00D;
            case HARD_ID -> 1.05D;
            case BRUTAL_ID -> 1.15D;
            case NIGHTMARE_ID -> 1.25D;
            default -> 1.00D;
        };
    }
}
