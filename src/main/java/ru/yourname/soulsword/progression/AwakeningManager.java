package ru.yourname.soulsword.progression;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import ru.yourname.soulsword.chat.SoulDialogueAwakening;
import ru.yourname.soulsword.soul.SoulData;

public class AwakeningManager {

    private static final int[] KILL_THRESHOLDS = new int[] {
            0,
            10,
            50,
            150,
            400,
            800
    };

    public static boolean checkAwakening(ItemStack stack, EntityPlayer player) {

        int kills = SoulData.getKills(stack);
        AwakeningStage currentStage = SoulData.getAwakeningStage(stack);
        int current = currentStage.ordinal();

        if (current >= AwakeningStage.values().length - 1) return false;

        int next = current + 1;
        if (kills < KILL_THRESHOLDS[next]) return false;

        SoulData.setAwakeningStage(stack, next);
        SoulDialogueAwakening.speak(player, AwakeningStage.values()[next]);
        return true;
    }
}
