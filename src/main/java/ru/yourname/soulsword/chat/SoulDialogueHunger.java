package ru.yourname.soulsword.chat;

import ru.yourname.soulsword.soul.HungerStage;

import java.util.*;

public class SoulDialogueHunger {

    private static final Random RANDOM = new Random();

    private static final Map<HungerStage, List<String>> POOLS =
            new EnumMap<>(HungerStage.class);

    static {
        POOLS.put(HungerStage.LIGHT, keys("light", 5));
        POOLS.put(HungerStage.MEDIUM, keys("medium", 5));
        POOLS.put(HungerStage.STRONG, keys("strong", 5));
    }

    private static List<String> keys(String stage, int count) {
        List<String> list = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            list.add("soul.dialogue.hunger." + stage + "." + i);
        }
        return list;
    }

    public static String getRandom(HungerStage stage) {
        List<String> pool = POOLS.get(stage);
        if (pool == null || pool.isEmpty()) return null;

        return pool.get(RANDOM.nextInt(pool.size()));
    }
}

