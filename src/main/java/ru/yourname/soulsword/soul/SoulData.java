package ru.yourname.soulsword.soul;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import ru.yourname.soulsword.progression.AwakeningStage;

public class SoulData {

    private static final String ROOT = "SoulSword";

    private static NBTTagCompound tag(ItemStack stack) {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }
        NBTTagCompound root = stack.getTagCompound();
        if (!root.hasKey(ROOT)) {
            root.setTag(ROOT, new NBTTagCompound());
        }
        return root.getCompoundTag(ROOT);
    }

    // =========================
    // KILLS
    // =========================

    public static int getKills(ItemStack stack) {
        return tag(stack).getInteger("Kills");
    }

    public static void addKill(ItemStack stack) {
        tag(stack).setInteger("Kills", getKills(stack) + 1);
    }

    // =========================
    // KILL STREAK
    // =========================

    public static int getKillStreak(ItemStack stack) {
        return tag(stack).getInteger("KillStreak");
    }

    public static void incrementKillStreak(ItemStack stack) {
        tag(stack).setInteger("KillStreak", getKillStreak(stack) + 1);
    }

    public static void resetKillStreak(ItemStack stack) {
        tag(stack).setInteger("KillStreak", 0);
        setLastKillStreakBlock(stack, 0);
    }

    public static int getLastKillStreakBlock(ItemStack stack) {
        return tag(stack).getInteger("LastStreakBlock");
    }

    public static void setLastKillStreakBlock(ItemStack stack, int block) {
        tag(stack).setInteger("LastStreakBlock", block);
    }

    // =========================
    // BONUS DAMAGE
    // =========================

    public static float getBonusDamage(ItemStack stack) {
        return tag(stack).getFloat("BonusDamage");
    }

    public static void addBonusDamage(ItemStack stack, float amount) {
        tag(stack).setFloat("BonusDamage", getBonusDamage(stack) + amount);
    }

    // =========================
    // AWAKENING (КАНОН)
    // =========================

    public static AwakeningStage getAwakeningStage(ItemStack stack) {
        return AwakeningStage.byKills(getKills(stack));
    }

    /**
     * ID канонической стадии (0–9)
     */
    public static int getAwakeningStageId(ItemStack stack) {
        AwakeningStage stage = getAwakeningStage(stack);
        int id = 0;

        for (AwakeningStage s : AwakeningStage.values()) {
            if (s.isLegacy()) continue;
            if (s == stage) return id;
            id++;
        }
        return 0;
    }

    public static void setAwakeningStage(ItemStack stack, int stageId) {
        int idx = 0;

        for (AwakeningStage stage : AwakeningStage.values()) {
            if (stage.isLegacy()) continue;

            if (idx == stageId) {
                tag(stack).setInteger("Kills", stage.getRequiredKills());
                return;
            }
            idx++;
        }

        tag(stack).setInteger("Kills", 0);
    }

    // =========================
    // OWNER ACCEPTANCE
    // =========================

    public static boolean isOwnerAccepted(ItemStack stack) {
        return tag(stack).getBoolean("OwnerAccepted");
    }

    public static void setOwnerAccepted(ItemStack stack, boolean accepted) {
        tag(stack).setBoolean("OwnerAccepted", accepted);
    }

    // =========================
    // HUNGER (НЕ ТРОГАЕМ)
    // =========================

    public static int getHungerStage(ItemStack stack) {
        return tag(stack).getInteger("Hunger");
    }

    public static void setHungerStage(ItemStack stack, int stage) {
        tag(stack).setInteger("Hunger", stage);
    }

    // =========================
    // FIRST HELD
    // =========================

    public static boolean wasFirstHeld(ItemStack stack) {
        return tag(stack).getBoolean("FirstHeld");
    }

    public static void markFirstHeld(ItemStack stack) {
        tag(stack).setBoolean("FirstHeld", true);
    }

    // =========================
    // ANNIVERSARY
    // =========================

    public static boolean hasAnniversary(ItemStack stack, int kills) {
        return tag(stack).getBoolean("Anniv_" + kills);
    }

    public static void markAnniversary(ItemStack stack, int kills) {
        tag(stack).setBoolean("Anniv_" + kills, true);
    }

    // =========================
    // LORE / DIALOGUE
    // =========================

    public static int getLoreIndex(ItemStack stack) {
        return tag(stack).getInteger("LoreIndex");
    }

    public static void advanceLore(ItemStack stack) {
        tag(stack).setInteger("LoreIndex", getLoreIndex(stack) + 1);
    }

    public static long getLastDialogueTime(ItemStack stack) {
        return tag(stack).getLong("LastDialogue");
    }

    public static void setLastDialogueTime(ItemStack stack, long time) {
        tag(stack).setLong("LastDialogue", time);
    }

    // =========================
    // COMBAT META
    // =========================

    public static long getLastCombatTime(ItemStack stack) {
        return tag(stack).getLong("LastCombat");
    }

    public static void setLastCombatTime(ItemStack stack, long time) {
        tag(stack).setLong("LastCombat", time);
    }
}
