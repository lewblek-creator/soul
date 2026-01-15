package ru.yourname.soulsword.events;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ru.yourname.soulsword.SoulSwordMod;
import ru.yourname.soulsword.chat.SoulDialogueKillStreak;
import ru.yourname.soulsword.chat.SoulDialogueQueue;
import ru.yourname.soulsword.combat.SoulCombatLogic;
import ru.yourname.soulsword.combat.SoulWaveAttack;
import ru.yourname.soulsword.item.ItemSoulSword;
import ru.yourname.soulsword.soul.SoulData;

@Mod.EventBusSubscriber(modid = SoulSwordMod.MODID)
public class KillEvents {

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {

        if (event.getEntity().world.isRemote) return;
        if (!(event.getSource().getTrueSource() instanceof EntityPlayer)) return;
        if (!(event.getEntity() instanceof EntityLivingBase)) return;
        if (!(event.getEntity() instanceof IMob)) return;

        EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();
        ItemStack stack = player.getHeldItemMainhand();

        if (stack.isEmpty() || !(stack.getItem() instanceof ItemSoulSword)) return;

        EntityLivingBase mob = (EntityLivingBase) event.getEntity();
        DamageSource source = event.getSource();

        // ===== COMBAT META =====
        SoulData.setLastCombatTime(stack, System.currentTimeMillis());

        // ===== SOUL PROGRESSION =====
        // КИЛЛ засчитывается ВСЕГДА
        SoulData.addKill(stack);

        // Рост урона ТОЛЬКО от melee
        if (source != SoulWaveAttack.SOUL_WAVE_DAMAGE) {
            SoulCombatLogic.onMobKilled(stack, mob);
        }

        // ===== KILL STREAK =====
        SoulData.incrementKillStreak(stack);
        int streak = SoulData.getKillStreak(stack);

        int currentBlock = SoulDialogueKillStreak.getBlockIndex(streak);
        int lastBlock = SoulData.getLastKillStreakBlock(stack);

        if (currentBlock != -1 && currentBlock != lastBlock) {

            SoulDialogueKillStreak.StreakBlock block =
                    SoulDialogueKillStreak.getBlock(currentBlock);

            if (block != null) {
                int delay = 0;
                for (String key : block.keys) {
                    SoulDialogueQueue.enqueue(player, key, delay);
                    delay += 15;
                }
                SoulData.setLastKillStreakBlock(stack, currentBlock);
            }
        }
    }
}
