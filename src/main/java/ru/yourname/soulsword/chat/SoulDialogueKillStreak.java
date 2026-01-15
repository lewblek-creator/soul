package ru.yourname.soulsword.chat;

import java.util.*;

public class SoulDialogueKillStreak {

    public static class StreakBlock {
        public final int min;
        public final int max;
        public final List<String> keys;

        public StreakBlock(int min, int max, List<String> keys) {
            this.min = min;
            this.max = max;
            this.keys = keys;
        }

        public boolean matches(int streak) {
            return streak >= min && (max < 0 || streak <= max);
        }
    }

    private static final List<StreakBlock> BLOCKS = Arrays.asList(

        new StreakBlock(3, 5, Arrays.asList(
                "soul.dialogue.streak.3_5.1",
                "soul.dialogue.streak.3_5.2",
                "soul.dialogue.streak.3_5.3"
        )),

        new StreakBlock(10, -1, Arrays.asList(
                "soul.dialogue.streak.10+.1",
                "soul.dialogue.streak.10+.2",
                "soul.dialogue.streak.10+.3",
                "soul.dialogue.streak.10+.4"
        )),

        new StreakBlock(20, -1, Arrays.asList(
                "soul.dialogue.streak.20+.1",
                "soul.dialogue.streak.20+.2",
                "soul.dialogue.streak.20+.3"
        ))
    );

    public static int getBlockIndex(int streak) {
        for (int i = 0; i < BLOCKS.size(); i++) {
            if (BLOCKS.get(i).matches(streak)) {
                return i;
            }
        }
        return -1;
    }

    public static StreakBlock getBlock(int index) {
        if (index < 0 || index >= BLOCKS.size()) return null;
        return BLOCKS.get(index);
    }
}

