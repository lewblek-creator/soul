package ru.yourname.soulsword.chat;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import ru.yourname.soulsword.soul.SoulData;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SoulDialogueFirstTouch {

    private static final Random RANDOM = new Random();
    private static final List<String> KEYS = new ArrayList<>();

    static {
        for (int i = 1; i <= 4; i++) {
            KEYS.add("soul.dialogue.first_touch." + i);
        }
    }

    public static void trySpeak(EntityPlayer player, ItemStack stack) {

        if (SoulData.wasFirstHeld(stack)) return;
        SoulData.markFirstHeld(stack);

        String key = KEYS.get(RANDOM.nextInt(KEYS.size()));
        SoulSpeaker.speak(player, key, false);
    }
}
