package ru.yourname.soulsword.combat;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import ru.yourname.soulsword.progression.AwakeningStage;
import ru.yourname.soulsword.soul.SoulData;

import java.util.List;

public class SoulWaveAttack {

    public static final DamageSource SOUL_WAVE_DAMAGE =
            new DamageSource("soul_wave").setMagicDamage();

    private static final double RANGE = 4.5D;

    public static void tryWave(EntityPlayer player) {

        if (player.world.isRemote) return;

        ItemStack stack = player.getHeldItemMainhand();
        AwakeningStage stage = SoulData.getAwakeningStage(stack);

        if (!stage.hasSoulWavePassive() && !stage.hasSoulWaveActive()) return;

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
            target.attackEntityFrom(SOUL_WAVE_DAMAGE, damage);

            if (stage.soulWaveHasVampirism()) {
                player.heal(damage * 0.40f);
            }
        }
    }
}
