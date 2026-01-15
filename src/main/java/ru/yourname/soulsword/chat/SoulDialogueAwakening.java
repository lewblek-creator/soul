package ru.yourname.soulsword.chat;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import ru.yourname.soulsword.progression.AwakeningStage;

import java.util.*;

public class SoulDialogueAwakening {

    private static final Random RANDOM = new Random();

    private static final Map<AwakeningStage, List<String>> PHRASES =
            new EnumMap<>(AwakeningStage.class);
    private static final Set<String> OTHER_PLAYER_KEYS = new HashSet<>(
            Arrays.asList("soul.awakening.hungry.2", "soul.awakening.master.3")
    );

    static {

        PHRASES.put(AwakeningStage.SLEEPING, keys("sleeping", 3));
        PHRASES.put(AwakeningStage.STIRRING, keys("stirring", 3));
        PHRASES.put(AwakeningStage.AWAKENING, keys("awakening", 3));
        PHRASES.put(AwakeningStage.HUNGRY, keys("hungry", 3));
        PHRASES.put(AwakeningStage.INSATIABLE, keys("insatiable", 3));
        PHRASES.put(AwakeningStage.DOMINATING, keys("dominating", 3));
        PHRASES.put(AwakeningStage.BREAKING, keys("breaking", 3));
        PHRASES.put(AwakeningStage.RECOGNIZING, keys("recognizing", 3));
        PHRASES.put(AwakeningStage.ACCEPTING, keys("accepting", 3));
        PHRASES.put(AwakeningStage.MASTER, keys("master", 3));
    }

    private static List<String> keys(String stage, int count) {
        List<String> list = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            list.add("soul.awakening." + stage + "." + i);
        }
        return list;
    }

    public static void speak(EntityPlayer owner, AwakeningStage stage) {

        List<String> list = PHRASES.get(stage);
        if (list == null || list.isEmpty()) return;

        String key = list.get(RANDOM.nextInt(list.size()));
        if (OTHER_PLAYER_KEYS.contains(key)) {
            EntityPlayer target = findOtherPlayer(owner);
            if (target == null) return;
            SoulSpeaker.speak(owner, key, false, target.getName());
        } else {
            SoulSpeaker.speak(owner, key, false);
        }
    }

    private static EntityPlayer findOtherPlayer(EntityPlayer owner) {
        MinecraftServer server = owner.world.getMinecraftServer();
        if (server == null) return null;

        List<EntityPlayer> players = new ArrayList<>(server.getPlayerList().getPlayers());
        players.remove(owner);
        if (players.isEmpty()) return null;

        return players.get(RANDOM.nextInt(players.size()));
    }
}
