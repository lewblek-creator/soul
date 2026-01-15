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
    public static void onMobKilled(ItemStack stack, EntityLivingBase mob) {
        float hp = mob.getMaxHealth();
        SoulData.addBonusDamage(stack, hp * 0.009f);
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
