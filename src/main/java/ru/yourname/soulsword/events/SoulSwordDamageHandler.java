package ru.yourname.soulsword.events;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ru.yourname.soulsword.combat.SoulCombatLogic;
import ru.yourname.soulsword.combat.SoulWaveAttack;
import ru.yourname.soulsword.item.ItemSoulSword;

public class SoulSwordDamageHandler {

    @SubscribeEvent
    public void onLivingHurt(LivingHurtEvent event) {

        if (!(event.getSource().getTrueSource() instanceof EntityPlayer)) return;

        // Исключаем Soul Wave
        if (event.getSource() == SoulWaveAttack.SOUL_WAVE_DAMAGE) return;

        EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();
        ItemStack stack = player.getHeldItemMainhand();

        if (stack.isEmpty() || !(stack.getItem() instanceof ItemSoulSword)) return;

        float damage = event.getAmount();

        float frenzyMultiplier =
                SoulCombatLogic.applyFrenzy(player, stack);

        float finalDamage = damage * frenzyMultiplier;
        event.setAmount(finalDamage);

        SoulCombatLogic.applyVampirism(player, stack, finalDamage);
    }
}
