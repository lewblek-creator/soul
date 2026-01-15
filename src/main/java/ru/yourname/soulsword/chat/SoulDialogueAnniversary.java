package ru.yourname.soulsword.chat;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import ru.yourname.soulsword.soul.SoulData;

import java.util.*;

public class SoulDialogueAnniversary {

    private static final Map<Integer, List<String>> PHRASES = new HashMap<>();

    static {
        PHRASES.put(100, Arrays.asList(
                "soul.dialogue.anniv.100.1"
        ));

        PHRASES.put(250, Arrays.asList(
                "soul.dialogue.anniv.250.1",
                "soul.dialogue.anniv.250.2",
                "soul.dialogue.anniv.250.3"
        ));

        PHRASES.put(500, Arrays.asList(
                "soul.dialogue.anniv.500.1",
                "soul.dialogue.anniv.500.2",
                "soul.dialogue.anniv.500.3"
        ));

        PHRASES.put(750, Arrays.asList(
                "soul.dialogue.anniv.750.1",
                "soul.dialogue.anniv.750.2"
        ));

        PHRASES.put(1000, Arrays.asList(
                "soul.dialogue.anniv.1000.1",
                "soul.dialogue.anniv.1000.2",
                "soul.dialogue.anniv.1000.3"
        ));
    }

    public static void check(EntityPlayer player, ItemStack stack) {

        int kills = SoulData.getKills(stack);

        if (!PHRASES.containsKey(kills)) return;
        if (SoulData.hasAnniversary(stack, kills)) return;

        SoulData.markAnniversary(stack, kills);

        List<String> list = PHRASES.get(kills);
        for (String key : list) {
            SoulSpeaker.speak(player, key, false);
        }
    }
}

