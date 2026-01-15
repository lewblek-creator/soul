package ru.yourname.soulsword.combat;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import ru.yourname.soulsword.progression.AwakeningStage;
import ru.yourname.soulsword.soul.SoulData;

import java.util.List;

public class SoulCombatLogic {

    // =========================
    // DAMAGE GROWTH
    // =========================
    private static final float MAX_BONUS_DAMAGE = 20.0f;
    private static final float MAX_GROWTH_PER_HIT = 0.5f;
    private static final float DAMAGE_GROWTH_PER_DAMAGE = 0.01f;
    private static final float MAX_HP_FOR_SCALING = 100.0f;

    public static void applyDamageGrowth(ItemStack stack, EntityLivingBase mob, float damage) {
        if (damage <= 0) return;

        float hpFactor = Math.min(mob.getMaxHealth(), MAX_HP_FOR_SCALING) / MAX_HP_FOR_SCALING;
        float growth = damage * DAMAGE_GROWTH_PER_DAMAGE * (1.0f + hpFactor);
        growth = Math.min(growth, MAX_GROWTH_PER_HIT);

        float current = SoulData.getBonusDamage(stack);
        if (current >= MAX_BONUS_DAMAGE) return;

        SoulData.setBonusDamage(stack, Math.min(current + growth, MAX_BONUS_DAMAGE));
    }

    // =========================
    // VAMPIRISM (MELEE ONLY)
    // =========================
    public static void applyVampirism(EntityPlayer player, ItemStack stack, float damage) {

        if (player.world.isRemote) return;

        AwakeningStage stage = SoulData.getAwakeningStage(stack);
        float vamp = stage.getMeleeVampirism();

        if (vamp <= 0) return;

        player.heal(damage * vamp);
    }

    // =========================
    // FRENZY (MELEE ONLY)
    // =========================
    public static float applyFrenzy(EntityPlayer player, ItemStack stack) {

        AwakeningStage stage = SoulData.getAwakeningStage(stack);
        if (!stage.hasFrenzy()) return 1.0f;

        List<EntityLivingBase> mobs = player.world.getEntitiesWithinAABB(
                EntityLivingBase.class,
                player.getEntityBoundingBox().grow(5),
                e -> e instanceof IMob && e.isEntityAlive()
        );

        if (mobs.size() < 2) return 1.0f;

        return 1.0f + stage.getFrenzyBonus();
    }
}
