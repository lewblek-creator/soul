package ru.yourname.soulsword.chat;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import ru.yourname.soulsword.progression.AwakeningStage;
import ru.yourname.soulsword.soul.SoulData;

import java.util.*;

public class SoulDialogueOtherPlayers {

    private static final Random RANDOM = new Random();

    private static final List<String> BASE = Arrays.asList(
            "soul.dialogue.other.base.1",
            "soul.dialogue.other.base.2",
            "soul.dialogue.other.base.3",
            "soul.dialogue.other.base.4",
            "soul.dialogue.other.base.5",
            "soul.dialogue.other.base.6",
            "soul.dialogue.other.base.7"
    );

    private static final List<String> LATE = Arrays.asList(
            "soul.dialogue.other.late.1",
            "soul.dialogue.other.late.2",
            "soul.dialogue.other.late.3"
    );

    public static void trySpeak(EntityPlayer owner) {

        MinecraftServer server = owner.world.getMinecraftServer();
        if (server == null) return;

        List<EntityPlayer> players = new ArrayList<>(server.getPlayerList().getPlayers());
        players.remove(owner);
        if (players.isEmpty()) return;

        AwakeningStage stage =
                SoulData.getAwakeningStage(owner.getHeldItemMainhand());

        List<String> pool = new ArrayList<>(BASE);
        if (stage.ordinal() >= AwakeningStage.MASTER.ordinal()) {
            pool.addAll(LATE);
        }

        String key = pool.get(RANDOM.nextInt(pool.size()));
        EntityPlayer target = players.get(RANDOM.nextInt(players.size()));

        SoulSpeaker.speak(owner, key, false, target.getName());
    }
}

