package ru.yourname.soulsword.events;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ru.yourname.soulsword.SoulSwordMod;
import ru.yourname.soulsword.item.ItemSoulSword;
import ru.yourname.soulsword.soul.HungerStage;
import ru.yourname.soulsword.soul.SoulData;
import ru.yourname.soulsword.progression.AwakeningStage;

import java.util.List;

@Mod.EventBusSubscriber(modid = SoulSwordMod.MODID)
public class CombatEvents {

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {

        if (event.getEntity().world.isRemote) return;
        if (!(event.getSource().getTrueSource() instanceof EntityPlayer)) return;

        EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();
        ItemStack stack = player.getHeldItemMainhand();

        if (stack.isEmpty() || !(stack.getItem() instanceof ItemSoulSword)) return;
        if (!(event.getEntity() instanceof EntityLivingBase)) return;

        float damage = event.getAmount();
        damage += SoulData.getBonusDamage(stack);

        int hungerId = SoulData.getHungerStage(stack);
        HungerStage hunger = HungerStage.values()[Math.min(hungerId, HungerStage.values().length - 1)];

        if (hunger != HungerStage.NONE && hasMultipleEnemies(player)) {
            damage *= getHungerMultiplier(hunger);
        }

        event.setAmount(damage);

        // ===== ВАМПИРИЗМ =====
        if (SoulData.isOwnerAccepted(stack)) {
            player.heal(damage * 0.40f);
            return;
        }

        AwakeningStage stage = SoulData.getAwakeningStage(stack);
        float vamp = getVampirism(stage);
        if (vamp > 0) {
            player.heal(damage * vamp);
        }
    }

    private static boolean hasMultipleEnemies(EntityPlayer player) {
        List<EntityLivingBase> mobs = player.world.getEntitiesWithinAABB(
                EntityLivingBase.class,
                player.getEntityBoundingBox().grow(6),
                e -> e instanceof IMob && e.isEntityAlive()
        );
        return mobs.size() >= 2;
    }

    private static float getHungerMultiplier(HungerStage stage) {
        switch (stage) {
            case LIGHT:  return 1.05f;
            case MEDIUM: return 1.10f;
            case STRONG: return 1.15f;
            default: return 1.0f;
        }
    }

    private static float getVampirism(AwakeningStage stage) {
        switch (stage) {
            case STIRRING:   return 0.03f;
            case AWAKENING:  return 0.06f;
            case DOMINATING: return 0.10f;
            case ACCEPTING:  return 0.14f;
            case MASTER:     return 0.18f;
            default: return 0f;
        }
    }
}

