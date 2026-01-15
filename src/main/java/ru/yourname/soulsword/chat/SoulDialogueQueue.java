package ru.yourname.soulsword.chat;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.LinkedList;
import java.util.Queue;

@Mod.EventBusSubscriber
public class SoulDialogueQueue {

    private static class QueuedLine {
        EntityPlayer player;
        String text;
        int delay;

        QueuedLine(EntityPlayer player, String text, int delay) {
            this.player = player;
            this.text = text;
            this.delay = delay;
        }
    }

    private static final Queue<QueuedLine> QUEUE = new LinkedList<>();

    public static void enqueue(EntityPlayer player, String text, int delayTicks) {
        QUEUE.add(new QueuedLine(player, text, delayTicks));
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {

        if (event.phase != TickEvent.Phase.END) return;
        if (QUEUE.isEmpty()) return;

        QueuedLine line = QUEUE.peek();
        line.delay--;

        if (line.delay <= 0) {

            String resolved = I18n.hasKey(line.text)
                    ? I18n.format(line.text)
                    : line.text;

            SoulSpeaker.speak(line.player, resolved, false);
            QUEUE.poll();
        }
    }
}
