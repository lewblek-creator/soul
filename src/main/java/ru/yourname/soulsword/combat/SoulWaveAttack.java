package ru.yourname.soulsword.combat;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import ru.yourname.soulsword.item.ItemSoulSword;
import ru.yourname.soulsword.progression.AwakeningStage;
import ru.yourname.soulsword.soul.SoulData;
import ru.yourname.soulsword.combat.SoulCombatStatus;

import java.util.List;

public class SoulWaveAttack {

    public static final DamageSource SOUL_WAVE_DAMAGE =
            new DamageSource("soul_wave").setMagicDamage();
    public static final DamageSource SOUL_REND_DAMAGE =
            new DamageSource("soul_rend").setMagicDamage();

    private static final double RANGE = 4.5D;

    public static void tryWave(EntityPlayer player, boolean active) {

        if (player.world.isRemote) return;

        ItemStack stack = player.getHeldItemMainhand();
        if (stack.isEmpty() || !(stack.getItem() instanceof ItemSoulSword)) return;
        AwakeningStage stage = SoulData.getAwakeningStage(stack);
        int stageId = SoulData.getAwakeningStageId(stack);

        if (active) {
            if (!stage.hasSoulWaveActive()) return;
        } else {
            if (!stage.hasSoulWavePassive()) return;
        }

        Vec3d look = player.getLookVec();

        AxisAlignedBB box = player.getEntityBoundingBox()
                .grow(RANGE, 1.5D, RANGE)
                .offset(look.x * RANGE / 2, 0, look.z * RANGE / 2);

        List<EntityLivingBase> targets = player.world.getEntitiesWithinAABB(
                EntityLivingBase.class,
                box,
                e -> e != player && e.isEntityAlive()
        );

        if (targets.isEmpty()) return;

        float baseDamage = (float) player.getEntityAttribute(
                SharedMonsterAttributes.ATTACK_DAMAGE
        ).getAttributeValue();

        float bonusDamage = SoulData.getBonusDamage(stack);
        float fullDamage = baseDamage + bonusDamage;

        float damage = fullDamage * stage.getSoulWaveMultiplier();

        for (EntityLivingBase target : targets) {
            float finalDamage = damage;
            if (stageId >= 6 && SoulCombatStatus.isMarkedBy(target, player, player.world.getTotalWorldTime())) {
                finalDamage *= 1.15f;
            }
            target.attackEntityFrom(SOUL_WAVE_DAMAGE, finalDamage);

            if (stageId >= 3 && target instanceof IMob) {
                target.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 30, 0));
            }
        }

        if (active && stageId >= 7) {
            applyFearAura(player);
        }
    }

    public static boolean trySoulRend(EntityPlayer player) {
        if (player.world.isRemote) return false;

        ItemStack stack = player.getHeldItemMainhand();
        if (stack.isEmpty() || !(stack.getItem() instanceof ItemSoulSword)) return false;

        int stageId = SoulData.getAwakeningStageId(stack);
        if (stageId < 9) return false;

        long now = player.world.getTotalWorldTime();
        if (now - SoulData.getLastSoulRendTime(stack) < 600) return false;
        if (!SoulData.consumeSouls(stack, 10)) return false;

        Vec3d look = player.getLookVec();
        double range = 10.0D;

        AxisAlignedBB box = player.getEntityBoundingBox()
                .grow(range, 2.0D, range)
                .offset(look.x * range / 2, 0, look.z * range / 2);

        List<EntityLivingBase> targets = player.world.getEntitiesWithinAABB(
                EntityLivingBase.class,
                box,
                e -> e != player && e.isEntityAlive()
        );

        if (targets.isEmpty()) return false;

        float baseDamage = (float) player.getEntityAttribute(
                SharedMonsterAttributes.ATTACK_DAMAGE
        ).getAttributeValue();

        float bonusDamage = SoulData.getBonusDamage(stack);
        float fullDamage = baseDamage + bonusDamage;

        float damage = fullDamage * 1.5f;

        for (EntityLivingBase target : targets) {
            target.attackEntityFrom(SOUL_REND_DAMAGE, damage);
            if (target instanceof IMob) {
                target.addPotionEffect(new PotionEffect(MobEffects.WITHER, 80, 0));
            }
        }

        SoulData.setLastSoulRendTime(stack, now);
        return true;
    }

    private static void applyFearAura(EntityPlayer player) {
        AxisAlignedBB box = player.getEntityBoundingBox().grow(4.0D);
        List<EntityLivingBase> targets = player.world.getEntitiesWithinAABB(
                EntityLivingBase.class,
                box,
                e -> e instanceof IMob && e.isEntityAlive()
        );

        for (EntityLivingBase target : targets) {
            target.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 60, 0));
            target.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 60, 0));
        }
    }
}
