package ru.yourname.soulsword.chat;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import ru.yourname.soulsword.soul.HungerStage;
import ru.yourname.soulsword.soul.SoulData;

public class SoulDialogueManager {

    private static final long BASE_COOLDOWN = 5 * 60 * 1000;

    private static final float HUNGER_CHANCE = 0.15f;
    private static final float NIGHT_CHANCE  = 0.12f;
    private static final float PSYCHOSIS_CHANCE = 0.08f;
    private static final float LORE_CHANCE = 0.05f;

    private static final long PSYCHOSIS_SILENCE = 10 * 60 * 1000;

    // ========================= HUNGER =========================
    public static void tryHungerDialogue(EntityPlayer player, ItemStack stack) {

        int hungerId = SoulData.getHungerStage(stack);
        HungerStage hunger = HungerStage.values()[
                Math.min(hungerId, HungerStage.values().length - 1)
        ];

        if (hunger == HungerStage.NONE) return;
        if (!canSpeak(stack)) return;
        if (Math.random() > HUNGER_CHANCE) return;

        String line = SoulDialogueHunger.getRandom(hunger);
        if (line == null) return;

        SoulSpeaker.speak(player, line, hunger != HungerStage.LIGHT);
        markSpoken(stack);
    }

    // ========================= NIGHT =========================
    public static void tryNightDialogue(EntityPlayer player, ItemStack stack) {
        World world = player.world;
        if (world.isDaytime()) return;
        if (!canSpeak(stack)) return;
        if (Math.random() > NIGHT_CHANCE) return;

        SoulSpeaker.speak(player, SoulDialogueNight.getRandom(), false);
        markSpoken(stack);
    }

    // ========================= PSYCHOSIS =========================
    public static void tryPsychosis(EntityPlayer player, ItemStack stack) {
        long now = System.currentTimeMillis();
        long last = SoulData.getLastDialogueTime(stack);

        if (now - last < PSYCHOSIS_SILENCE) return;
        if (Math.random() > PSYCHOSIS_CHANCE) return;

        if (SoulDialoguePsychosis.isChain()) {
            int delay = 0;
            for (String line : SoulDialoguePsychosis.getChain()) {
                SoulDialogueQueue.enqueue(player, line, delay);
                delay += 12;
            }
        } else {
            SoulSpeaker.speak(player, SoulDialoguePsychosis.getSingle(), false);
        }

        markSpoken(stack);
    }

    // ========================= LORE =========================
    public static void tryLore(EntityPlayer player, ItemStack stack) {

        if (!canSpeak(stack)) return;
        if (Math.random() > LORE_CHANCE) return;

        int index = SoulData.getLoreIndex(stack);
        if (index >= SoulDialogueLore.size()) return;

        String line = SoulDialogueLore.get(index);
        if (line == null) return;

        SoulSpeaker.speak(player, line, false);
        SoulData.advanceLore(stack);
        markSpoken(stack);
    }

    // ========================= COMMON =========================
    private static boolean canSpeak(ItemStack stack) {
        long now = System.currentTimeMillis();
        long last = SoulData.getLastDialogueTime(stack);
        return now - last >= BASE_COOLDOWN;
    }

    private static void markSpoken(ItemStack stack) {
        SoulData.setLastDialogueTime(stack, System.currentTimeMillis());
    }
}

