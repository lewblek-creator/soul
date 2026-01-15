package ru.yourname.soulsword.events;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ru.yourname.soulsword.SoulSwordMod;
import ru.yourname.soulsword.combat.SoulCombatStatus;

@Mod.EventBusSubscriber(modid = SoulSwordMod.MODID)
public class SoulBleedEvents {

    private static final DamageSource BLEED_DAMAGE =
            new DamageSource("soulsword_bleed").setMagicDamage();

    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntity().world.isRemote) return;
        if (!(event.getEntity() instanceof EntityLivingBase)) return;
        if (!(event.getEntity() instanceof IMob)) return;

        EntityLivingBase target = (EntityLivingBase) event.getEntity();
        long now = target.world.getTotalWorldTime();

        if (SoulCombatStatus.shouldBleedTick(target, now)) {
            target.attackEntityFrom(BLEED_DAMAGE, 1.0f);
        }
    }
}
