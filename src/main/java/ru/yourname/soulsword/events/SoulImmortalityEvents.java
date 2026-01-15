package ru.yourname.soulsword.events;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ru.yourname.soulsword.SoulSwordMod;
import ru.yourname.soulsword.item.ItemSoulSword;
import ru.yourname.soulsword.soul.SoulData;

@Mod.EventBusSubscriber(modid = SoulSwordMod.MODID)
public class SoulImmortalityEvents {

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (event.getEntity().world.isRemote) return;
        if (!(event.getEntity() instanceof EntityPlayer)) return;

        EntityPlayer player = (EntityPlayer) event.getEntity();
        ItemStack stack = player.getHeldItemMainhand();
        if (stack.isEmpty() || !(stack.getItem() instanceof ItemSoulSword)) return;

        int stageId = SoulData.getAwakeningStageId(stack);
        if (stageId < 8) return;

        long now = player.world.getTotalWorldTime();
        if (now - SoulData.getLastImmortalTime(stack) < 1800) return;

        if (!SoulData.consumeSouls(stack, 5)) return;

        event.setCanceled(true);
        player.setHealth(2.0f);
        player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 60, 1));
        player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 100, 1));
        SoulData.setLastImmortalTime(stack, now);
    }
}
