package ru.yourname.soulsword.chat;

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
        String key;
        Object[] args;
        int delay;

        QueuedLine(EntityPlayer player, String key, int delay, Object[] args) {
            this.player = player;
            this.key = key;
            this.delay = delay;
            this.args = args;
        }
    }

    private static final Queue<QueuedLine> QUEUE = new LinkedList<>();

    public static void enqueue(EntityPlayer player, String key, int delayTicks, Object... args) {
        QUEUE.add(new QueuedLine(player, key, delayTicks, args));
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {

        if (event.phase != TickEvent.Phase.END) return;
        if (QUEUE.isEmpty()) return;

        QueuedLine line = QUEUE.peek();
        line.delay--;

        if (line.delay <= 0) {
            SoulSpeaker.speak(line.player, line.key, false, line.args);
            QUEUE.poll();
        }
    }
}
