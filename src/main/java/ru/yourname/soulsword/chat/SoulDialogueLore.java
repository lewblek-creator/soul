package ru.yourname.soulsword.chat;

import java.util.Arrays;
import java.util.List;

public class SoulDialogueLore {

    // СТРОГО ПО ПОРЯДКУ
    private static final List<String> LORE = Arrays.asList(
            "soul.dialogue.lore.1",
            "soul.dialogue.lore.2",
            "soul.dialogue.lore.3",
            "soul.dialogue.lore.4",
            "soul.dialogue.lore.5",
            "soul.dialogue.lore.6",
            "soul.dialogue.lore.7",
            "soul.dialogue.lore.8",
            "soul.dialogue.lore.9",
            "soul.dialogue.lore.10"
    );

    public static int size() {
        return LORE.size();
    }

    public static String get(int index) {
        if (index < 0 || index >= LORE.size()) return null;
        return LORE.get(index);
    }
}
