package ru.yourname.soulsword.events;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ru.yourname.soulsword.SoulSwordMod;
import net.minecraft.potion.PotionEffect;
import net.minecraft.init.MobEffects;
import ru.yourname.soulsword.combat.SoulCombatLogic;
import ru.yourname.soulsword.combat.SoulCombatStatus;
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

        EntityLivingBase target = (EntityLivingBase) event.getEntity();
        long now = player.world.getTotalWorldTime();
        int stageId = SoulData.getAwakeningStageId(stack);

        float damage = event.getAmount();
        damage += SoulData.getBonusDamage(stack);

        int hungerId = SoulData.getHungerStage(stack);
        HungerStage hunger = HungerStage.values()[Math.min(hungerId, HungerStage.values().length - 1)];

        if (hunger != HungerStage.NONE && hasMultipleEnemies(player)) {
            damage *= getHungerMultiplier(hunger);
        }

        if (target instanceof IMob) {
            if (stageId >= 1) {
                SoulCombatStatus.applyBleed(target, now, 60);
            }

            if (stageId >= 3) {
                target.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 30, 0));
            }

            if (stageId >= 6) {
                if (!SoulCombatStatus.isMarkedBy(target, player, now)) {
                    SoulCombatStatus.applyMark(target, player, now, 160);
                }
            }

            if (SoulCombatStatus.isMarkedBy(target, player, now)) {
                damage *= 1.15f;
            }
        }

        event.setAmount(damage);

        if (target instanceof IMob) {
            SoulCombatLogic.applyDamageGrowth(stack, target, damage);
            SoulData.setLastHitTime(stack, now);
            SoulData.setLastShieldDecayTime(stack, now);
        }

        // ===== ВАМПИРИЗМ =====
        AwakeningStage stage = SoulData.getAwakeningStage(stack);
        float vamp = stage.getMeleeVampirism();
        if (SoulData.isOwnerAccepted(stack)) {
            vamp = Math.max(vamp, 0.40f);
        }

        if (vamp > 0f) {
            applyVampirism(player, stack, stageId, damage * vamp);
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

    private static void applyVampirism(EntityPlayer player, ItemStack stack, int stageId, float heal) {
        if (heal <= 0f) return;

        float maxHealth = player.getMaxHealth();
        float current = player.getHealth();
        float missing = Math.max(0f, maxHealth - current);
        float actualHeal = Math.min(heal, missing);
        float overheal = heal - actualHeal;

        if (actualHeal > 0f) {
            player.heal(actualHeal);
        }

        if (stageId >= 4 && overheal > 0f) {
            float maxAbsorption = stageId >= 9 ? 8.0f : 4.0f;
            float gained = overheal * 0.5f;
            float newAbsorption = Math.min(player.getAbsorptionAmount() + gained, maxAbsorption);
            player.setAbsorptionAmount(newAbsorption);
        }
    }
}

