package ru.yourname.soulsword.chat;

import net.minecraft.client.resources.I18n;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SoulDialogueNight {

    private static final Random RANDOM = new Random();
    private static final List<String> KEYS = new ArrayList<>();

    static {
        for (int i = 1; i <= 10; i++) {
            KEYS.add("soul.dialogue.night." + i);
        }
    }

    public static String getRandom() {
        String key = KEYS.get(RANDOM.nextInt(KEYS.size()));
        return I18n.format(key);
    }
}
