package ru.yourname.soulsword.events;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ru.yourname.soulsword.SoulSwordMod;
import ru.yourname.soulsword.chat.SoulDialogueKillStreak;
import ru.yourname.soulsword.chat.SoulDialogueQueue;
import ru.yourname.soulsword.combat.SoulCombatStatus;
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
        long now = player.world.getTotalWorldTime();
        int stageId = SoulData.getAwakeningStageId(stack);

        // ===== COMBAT META =====
        SoulData.setLastCombatTime(stack, System.currentTimeMillis());

        // ===== SOUL PROGRESSION =====
        // КИЛЛ засчитывается ВСЕГДА
        SoulData.addSoul(stack);

        if (stageId >= 2) {
            player.addPotionEffect(new PotionEffect(MobEffects.SPEED, 100, 0));
            player.getFoodStats().addStats(1, 0.0f);
        }

        if (stageId >= 6 && SoulCombatStatus.isMarkedBy(mob, player, now)) {
            player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 60, 0));
            SoulData.addSoul(stack);
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
