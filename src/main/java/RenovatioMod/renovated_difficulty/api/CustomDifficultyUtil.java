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
    private static final int CATACLYSM_ID = 7;

    private CustomDifficultyUtil() {
    }

    // Inheritance rule helper:
    // Tranquil -> Easy fallback, Brutal/Nightmare/Cataclysm -> Hard fallback.
    public static Difficulty mapToVanillaEquivalent(Difficulty difficulty) {
        return switch (difficulty.getId()) {
            case TRANQUIL_ID -> Difficulty.EASY;
            case BRUTAL_ID, NIGHTMARE_ID, CATACLYSM_ID -> Difficulty.HARD;
            default -> difficulty;
        };
    }

    public static boolean isTranquilDifficulty(Difficulty difficulty) {
        return difficulty.getId() == TRANQUIL_ID;
    }

    public static int getHostileSpawnBlockLightLimitOverride(Difficulty difficulty) {
        return switch (difficulty.getId()) {
            case BRUTAL_ID -> 2;
            case NIGHTMARE_ID -> 4;
            case CATACLYSM_ID -> 7;
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
            case CATACLYSM_ID -> 150;
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
            case CATACLYSM_ID -> 1.33D;
            default -> 1.00D;
        };
    }

    public static double getNaturalRegenSaturationCostMultiplier(Difficulty difficulty) {
        return switch (difficulty.getId()) {
            case PEACEFUL_ID, TRANQUIL_ID -> 0.5D;
            case EASY_ID -> 0.67D;
            case NORMAL_ID -> 1.0D;
            case HARD_ID -> 1.33D;
            case BRUTAL_ID -> 1.67D;
            case NIGHTMARE_ID -> 2.0D;
            case CATACLYSM_ID -> 3.0D;
            default -> 1.0D;
        };
    }

    public static double getNaturalRegenSpeedMultiplier(Difficulty difficulty) {
        return switch (difficulty.getId()) {
            case PEACEFUL_ID, TRANQUIL_ID -> 1.5D;
            case EASY_ID -> 1.33D;
            case NORMAL_ID -> 1.0D;
            case HARD_ID -> 0.875D;
            case BRUTAL_ID -> 0.75D;
            case NIGHTMARE_ID -> 0.625D;
            case CATACLYSM_ID -> 0.5D;
            default -> 1.0D;
        };
    }

    // Starvation floor (HP). (V) where matching vanilla, otherwise custom.
    public static float getStarvationHealthFloor(Difficulty difficulty) {
        return switch (difficulty.getId()) {
            case TRANQUIL_ID -> 15.0F; // custom
            case EASY_ID -> 10.0F; // (V)
            case NORMAL_ID -> 1.0F; // (V)
            case HARD_ID, BRUTAL_ID, NIGHTMARE_ID, CATACLYSM_ID -> 0.0F; // Hard-style death allowed
            default -> Float.POSITIVE_INFINITY; // Peaceful / unknown
        };
    }

    public static int getStarvationExtraDamageIntervalTicks(Difficulty difficulty) {
        return switch (difficulty.getId()) {
            case BRUTAL_ID -> 80; // custom: every 4s
            case NIGHTMARE_ID -> 60; // custom: every 3s
            case CATACLYSM_ID -> 40; // custom: every 2s
            default -> 0; // (V) no extra starvation damage
        };
    }

    public static float getStarvationExtraDamageAmount(Difficulty difficulty) {
        return switch (difficulty.getId()) {
            case BRUTAL_ID, NIGHTMARE_ID -> 4.0F; // custom: 2 hearts
            case CATACLYSM_ID -> 8.0F; // custom: 4 hearts
            default -> 0.0F;
        };
    }

    // Armor equip stop chance used by vanilla loop in MobEntity.initEquipment.
    // Piece probabilities (2/3/4) follow (1-stop), (1-stop)^2, (1-stop)^3.
    public static float getMobArmorStopChance(Difficulty difficulty, float vanillaStopChance) {
        return switch (difficulty.getId()) {
            case TRANQUIL_ID -> 0.30F; // custom (70/49/34)
            case EASY_ID -> 0.25F; // (V) 75/56/42
            case NORMAL_ID -> 0.20F; // custom (80/64/51)
            case HARD_ID -> 0.15F; // custom (85/72/61)
            case BRUTAL_ID -> 0.10F; // Hard-style (V Hard)
            case NIGHTMARE_ID -> 0.05F; // custom (95/90/86)
            case CATACLYSM_ID -> 0.01F; // custom (99/98/97)
            default -> vanillaStopChance;
        };
    }

    public static float getZombieWeaponChance(Difficulty difficulty) {
        return switch (difficulty.getId()) {
            case TRANQUIL_ID -> 0.005F; // custom
            case EASY_ID -> 0.01F; // (V)
            case NORMAL_ID -> 0.025F; // custom
            case HARD_ID -> 0.05F; // (V)
            case BRUTAL_ID -> 0.10F; // custom
            case NIGHTMARE_ID -> 0.125F; // custom
            case CATACLYSM_ID -> 0.1667F; // custom
            default -> 0.0F;
        };
    }

    // Reinforcement gate uses Hard check in vanilla, so hard-like tiers map to Hard.
    public static Difficulty getZombieReinforcementGateDifficulty(Difficulty difficulty) {
        return switch (difficulty.getId()) {
            case HARD_ID, BRUTAL_ID, NIGHTMARE_ID, CATACLYSM_ID -> Difficulty.HARD;
            default -> mapToVanillaEquivalent(difficulty);
        };
    }

    public static double getZombieReinforcementChanceMultiplier(Difficulty difficulty) {
        return switch (difficulty.getId()) {
            case BRUTAL_ID -> 1.5D; // custom
            case NIGHTMARE_ID -> 2.0D; // custom
            case CATACLYSM_ID -> 3.0D; // custom
            default -> 1.0D; // (V)
        };
    }

    // random.nextInt(bound) < difficultyId form in NetherPortalBlock.randomTick
    public static int getPortalPiglinRandomBound(Difficulty difficulty) {
        return switch (difficulty.getId()) {
            case TRANQUIL_ID -> 10667; // custom ~0.0375%
            case EASY_ID, NORMAL_ID, HARD_ID -> 2000; // (V)
            case BRUTAL_ID -> 2500; // custom 0.20%
            case NIGHTMARE_ID -> 2400; // custom 0.25%
            case CATACLYSM_ID -> 2121; // custom ~0.33%
            default -> 2000;
        };
    }

    public static float getSkeletonAttackCooldownSeconds(Difficulty difficulty) {
        return switch (difficulty.getId()) {
            case TRANQUIL_ID -> 2.5F;
            case EASY_ID -> 2.25F;
            case NORMAL_ID -> 2.0F; // (V Normal)
            case HARD_ID -> 1.5F;
            case BRUTAL_ID -> 1.0F; // (V Hard)
            case NIGHTMARE_ID -> 0.875F;
            case CATACLYSM_ID -> 0.8F;
            default -> 2.0F;
        };
    }

    public static float getBoggedAttackCooldownSeconds(Difficulty difficulty) {
        return switch (difficulty.getId()) {
            case TRANQUIL_ID -> 4.0F;
            case EASY_ID -> 3.75F;
            case NORMAL_ID -> 3.5F; // (V Normal)
            case HARD_ID -> 3.0F;
            case BRUTAL_ID -> 2.5F; // (V Hard)
            case NIGHTMARE_ID -> 2.375F;
            case CATACLYSM_ID -> 2.3F;
            default -> 3.5F;
        };
    }

    public static double getSkeletonBonusArrowDamage(Difficulty difficulty) {
        return switch (difficulty.getId()) {
            case TRANQUIL_ID -> 0.0D;
            case EASY_ID -> 0.11D; // (V)
            case NORMAL_ID -> 0.22D; // (V)
            case HARD_ID -> 0.33D; // (V)
            case BRUTAL_ID -> 0.44D;
            case NIGHTMARE_ID -> 0.55D;
            case CATACLYSM_ID -> 0.66D;
            default -> 0.0D;
        };
    }

    public static float getSkeletonInaccuracy(Difficulty difficulty) {
        return switch (difficulty.getId()) {
            case TRANQUIL_ID -> 12.0F;
            case EASY_ID -> 10.0F; // (V)
            case NORMAL_ID -> 7.0F;
            case HARD_ID -> 5.0F;
            case BRUTAL_ID -> 3.0F;
            case NIGHTMARE_ID -> 2.0F; // (V Hard)
            case CATACLYSM_ID -> 0.0F;
            default -> 14.0F;
        };
    }

    public static int getPhantomGroupMin(Difficulty difficulty) {
        return switch (difficulty.getId()) {
            case BRUTAL_ID -> 2;
            case NIGHTMARE_ID -> 3;
            case CATACLYSM_ID -> 6;
            case NORMAL_ID, HARD_ID, EASY_ID, TRANQUIL_ID -> 1;
            default -> 1;
        };
    }

    public static int getPhantomGroupMax(Difficulty difficulty) {
        return switch (difficulty.getId()) {
            case TRANQUIL_ID, EASY_ID -> 2;
            case NORMAL_ID -> 3;
            case HARD_ID -> 4;
            case BRUTAL_ID -> 5;
            case NIGHTMARE_ID -> 6;
            case CATACLYSM_ID -> 8;
            default -> 2;
        };
    }

    public static int getCaveSpiderPoisonSeconds(Difficulty difficulty) {
        return switch (difficulty.getId()) {
            case NORMAL_ID -> 7;
            case HARD_ID -> 15;
            case BRUTAL_ID -> 18;
            case NIGHTMARE_ID -> 21;
            case CATACLYSM_ID -> 25;
            default -> 0;
        };
    }

    public static int getBeePoisonSeconds(Difficulty difficulty) {
        return switch (difficulty.getId()) {
            case NORMAL_ID -> 10;
            case HARD_ID -> 14;
            case BRUTAL_ID -> 18;
            case NIGHTMARE_ID -> 21;
            case CATACLYSM_ID -> 24;
            default -> 0;
        };
    }

    public static int getWitherSkullWitherSeconds(Difficulty difficulty) {
        return switch (difficulty.getId()) {
            case NORMAL_ID -> 10;
            case HARD_ID -> 15;
            case BRUTAL_ID -> 20;
            case NIGHTMARE_ID -> 30;
            case CATACLYSM_ID -> 40;
            default -> 0;
        };
    }

    public static int getRaidWaveCount(Difficulty difficulty) {
        return switch (difficulty.getId()) {
            case TRANQUIL_ID -> 2;
            case EASY_ID -> 3;
            case NORMAL_ID -> 5;
            case HARD_ID -> 7;
            case BRUTAL_ID -> 8;
            case NIGHTMARE_ID -> 10;
            case CATACLYSM_ID -> 12;
            default -> 0;
        };
    }

    public static float getZombieLeaderScale(float maxHealth) {
        // Linear scale: 20 HP -> 1.0x, 100 HP (50 hearts) -> 1.5x.
        float normalized = Math.max(0.0F, Math.min(1.0F, (maxHealth - 20.0F) / 80.0F));
        return 1.0F + normalized * 0.5F;
    }
}
