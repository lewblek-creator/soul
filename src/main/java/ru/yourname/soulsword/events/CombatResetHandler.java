package ru.yourname.soulsword.events;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import ru.yourname.soulsword.SoulSwordMod;
import ru.yourname.soulsword.item.ItemSoulSword;
import ru.yourname.soulsword.soul.SoulData;

@Mod.EventBusSubscriber(modid = SoulSwordMod.MODID)
public class CombatResetHandler {

    private static final long OUT_OF_COMBAT_TIME = 8000; // 8 секунд

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {

        if (event.phase != TickEvent.Phase.END) return;

        EntityPlayer player = event.player;
        ItemStack stack = player.getHeldItemMainhand();

        if (stack.isEmpty() || !(stack.getItem() instanceof ItemSoulSword)) return;

        long lastCombat = SoulData.getLastCombatTime(stack);
        if (lastCombat == 0) return;

        long now = System.currentTimeMillis();

        if (now - lastCombat >= OUT_OF_COMBAT_TIME) {
            SoulData.resetKillStreak(stack);
            SoulData.setLastKillStreakBlock(stack, -1);
            SoulData.setLastCombatTime(stack, 0);
        }
    }
}
