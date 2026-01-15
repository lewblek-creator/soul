package ru.yourname.soulsword.progression;

public enum AwakeningStage {

    // ===== ОСНОВНЫЕ 10 СТАДИЙ (КАНОН) =====
    SLEEPING("Спящий", 0),
    STIRRING("Шевелящийся", 25),
    AWAKENING("Пробуждающийся", 75),
    HUNGRY("Алчущий", 150),
    INSATIABLE("Ненасытный", 300),
    DOMINANT("Доминирующий", 500),
    BREAKING("Ломающий", 800),
    ACKNOWLEDGING("Признающий", 1200),
    ACCEPTING("Принимающий", 1800),
    MASTER("Хозяин", 20000),

    // ===== LEGACY (ТОЛЬКО ДЛЯ СОВМЕСТИМОСТИ) =====
    DOMINATING("Доминирующий", 500),
    RECOGNIZING("Признающий", 1200),
    MASTER_ACCEPTED("Принимающий", 1800),
    AWAKENED_MASTER("Хозяин", 20000);

    private final String displayName;
    private final int requiredKills;

    AwakeningStage(String displayName, int requiredKills) {
        this.displayName = displayName;
        this.requiredKills = requiredKills;
    }

    // =========================
    // GETTERS
    // =========================

    public String getDisplayName() {
        return displayName;
    }

    public int getRequiredKills() {
        return requiredKills;
    }

    // =========================
    // LEGACY
    // =========================

    public boolean isLegacy() {
        return this == DOMINATING
                || this == RECOGNIZING
                || this == MASTER_ACCEPTED
                || this == AWAKENED_MASTER;
    }

    public boolean isMaster() {
        return this == MASTER || this == AWAKENED_MASTER;
    }

    // =========================
    // PROGRESSION (КАНОН)
    // =========================

    public static AwakeningStage byKills(int kills) {
        AwakeningStage result = SLEEPING;

        for (AwakeningStage stage : values()) {
            if (stage.isLegacy()) continue;

            if (kills >= stage.requiredKills) {
                result = stage;
            }
        }
        return result;
    }

    /**
     * Следующая КАНОНИЧЕСКАЯ стадия.
     * Legacy стадии игнорируются.
     * MASTER -> null
     */
    public AwakeningStage next() {
        boolean returnNext = false;

        for (AwakeningStage stage : values()) {
            if (stage.isLegacy()) continue;

            if (returnNext) {
                return stage;
            }

            if (stage == this) {
                returnNext = true;
            }
        }
        return null;
    }

    // =========================
    // MECHANICS FLAGS
    // =========================

    public boolean hasFrenzy() {
        return this == STIRRING
                || this == INSATIABLE
                || this == DOMINANT
                || this == ACCEPTING
                || this == MASTER;
    }

    public boolean hasMeleeVampirism() {
        return this == STIRRING
                || this == AWAKENING
                || this == HUNGRY
                || this == INSATIABLE
                || this == DOMINANT
                || this == BREAKING
                || this == ACKNOWLEDGING
                || this == ACCEPTING
                || this == MASTER;
    }

    public boolean hasSoulWavePassive() {
        return this == HUNGRY;
    }

    public boolean hasSoulWaveActive() {
        return this == DOMINANT
                || this == BREAKING
                || this == ACKNOWLEDGING
                || this == ACCEPTING
                || this == MASTER;
    }

    // =========================
    // CAPS
    // =========================

    public float getFrenzyBonus() {
        switch (this) {
            case STIRRING:   return 0.10f;
            case INSATIABLE: return 0.18f;
            case DOMINANT:   return 0.25f;
            case MASTER:     return 0.30f;
            default:         return 0.0f;
        }
    }

    public float getMeleeVampirism() {
        switch (this) {
            case STIRRING:      return 0.03f;
            case AWAKENING:     return 0.045f;
            case HUNGRY:        return 0.06f;
            case INSATIABLE:    return 0.075f;
            case DOMINANT:      return 0.09f;
            case BREAKING:      return 0.105f;
            case ACKNOWLEDGING: return 0.12f;
            case ACCEPTING:     return 0.135f;
            case MASTER:        return 0.15f;
            default:         return 0.0f;
        }
    }

    public float getSoulWaveMultiplier() {
        return this == MASTER ? 1.0f : 0.5f;
    }

    public boolean soulWaveHasVampirism() {
        return this == MASTER;
    }
}
