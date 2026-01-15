package ru.yourname.soulsword.chat;

import net.minecraft.entity.player.EntityPlayer;
import ru.yourname.soulsword.progression.AwakeningStage;
import ru.yourname.soulsword.soul.SoulData;

import java.util.*;

public class SoulDialogueDirect {

    private static final Random RANDOM = new Random();

    private static final List<String> EARLY = Arrays.asList(
            "soul.dialogue.direct.early.1",
            "soul.dialogue.direct.early.2",
            "soul.dialogue.direct.early.3",
            "soul.dialogue.direct.early.4",
            "soul.dialogue.direct.early.5"
    );

    private static final List<String> MID = Arrays.asList(
            "soul.dialogue.direct.mid.1",
            "soul.dialogue.direct.mid.2",
            "soul.dialogue.direct.mid.3",
            "soul.dialogue.direct.mid.4",
            "soul.dialogue.direct.mid.5"
    );

    private static final List<String> LATE = Arrays.asList(
            "soul.dialogue.direct.late.1",
            "soul.dialogue.direct.late.2",
            "soul.dialogue.direct.late.3",
            "soul.dialogue.direct.late.4",
            "soul.dialogue.direct.late.5"
    );

    public static void trySpeak(EntityPlayer player) {

        AwakeningStage stage =
                SoulData.getAwakeningStage(player.getHeldItemMainhand());

        List<String> pool;

        if (stage.ordinal() < AwakeningStage.STIRRING.ordinal()) {
            pool = EARLY;
        } else if (stage.ordinal() < AwakeningStage.MASTER.ordinal()) {
            pool = MID;
        } else {
            pool = LATE;
        }

        String key = pool.get(RANDOM.nextInt(pool.size()));
        SoulSpeaker.speak(player, key, false, player.getName());
    }
}
