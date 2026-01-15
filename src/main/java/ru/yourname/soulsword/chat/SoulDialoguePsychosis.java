package ru.yourname.soulsword.chat;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class SoulDialoguePsychosis {

    private static final Random RANDOM = new Random();

    // ОДИНОЧНЫЕ ВСПЫШКИ
    private static final List<String> SINGLE = Arrays.asList(
            "soul.dialogue.psychosis.single.1",
            "soul.dialogue.psychosis.single.2",
            "soul.dialogue.psychosis.single.3",
            "soul.dialogue.psychosis.single.4",
            "soul.dialogue.psychosis.single.5"
    );

    // СЕРИИ (ЛОМАЮТ ТИШИНУ)
    private static final List<List<String>> CHAINS = Arrays.asList(

            Arrays.asList(
                    "soul.dialogue.psychosis.chain.1.1",
                    "soul.dialogue.psychosis.chain.1.2",
                    "soul.dialogue.psychosis.chain.1.3"
            ),

            Arrays.asList(
                    "soul.dialogue.psychosis.chain.2.1",
                    "soul.dialogue.psychosis.chain.2.2",
                    "soul.dialogue.psychosis.chain.2.3"
            ),

            Arrays.asList(
                    "soul.dialogue.psychosis.chain.3.1",
                    "soul.dialogue.psychosis.chain.3.2",
                    "soul.dialogue.psychosis.chain.3.3"
            ),

            Arrays.asList(
                    "soul.dialogue.psychosis.chain.4.1",
                    "soul.dialogue.psychosis.chain.4.2",
                    "soul.dialogue.psychosis.chain.4.3"
            )
    );

    public static boolean isChain() {
        return RANDOM.nextFloat() < 0.4f; // 40% шанс серии
    }

    public static String getSingle() {
        return SINGLE.get(RANDOM.nextInt(SINGLE.size()));
    }

    public static List<String> getChain() {
        return CHAINS.get(RANDOM.nextInt(CHAINS.size()));
    }
}
