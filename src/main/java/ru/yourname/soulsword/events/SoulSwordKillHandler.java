package ru.yourname.soulsword.events;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import ru.yourname.soulsword.combat.SoulCombatLogic;
import ru.yourname.soulsword.item.ItemSoulSword;
import ru.yourname.soulsword.soul.SoulData;

public class SoulSwordKillHandler {

    @SubscribeEvent
    public void onKill(PlayerEvent.ItemPickupEvent event) {
        // заглушка чтобы класс не удаляли
    }

    @SubscribeEvent
    public void onMobKilled(net.minecraftforge.event.entity.living.LivingDeathEvent event) {

        if (!(event.getSource().getTrueSource() instanceof EntityPlayer)) return;

        EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();
        if (!(event.getEntityLiving() instanceof IMob)) return;

        ItemStack stack = player.getHeldItemMainhand();
        if (stack.isEmpty() || !(stack.getItem() instanceof ItemSoulSword)) return;

        EntityLivingBase mob = event.getEntityLiving();

        SoulData.addKill(stack);
        SoulCombatLogic.onMobKilled(stack, mob);
    }
}
