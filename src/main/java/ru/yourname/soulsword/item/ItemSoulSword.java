package ru.yourname.soulsword.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ru.yourname.soulsword.combat.SoulWaveAttack;
import ru.yourname.soulsword.progression.AwakeningStage;
import ru.yourname.soulsword.soul.SoulData;

import java.util.List;

public class ItemSoulSword extends ItemSword {

    public ItemSoulSword(ToolMaterial material) {
        super(material);
        setMaxStackSize(1);
        setMaxDamage(0);
        setNoRepair();
    }

    @Override
    public boolean isDamageable() {
        return false;
    }

    @Override
    public boolean isDamaged(ItemStack stack) {
        return false;
    }

    @Override
    public void setDamage(ItemStack stack, int damage) {
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 72000;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        AwakeningStage stage = SoulData.getAwakeningStage(stack);
        AwakeningStage next = stage.next();
        int stageNumber = SoulData.getAwakeningStageId(stack) + 1;
        int souls = SoulData.getSouls(stack);
        float bonus = SoulData.getBonusDamage(stack);

        tooltip.add(I18n.format("tooltip.soulsword.stage", stageNumber));
        tooltip.add(
                next != null
                        ? I18n.format("tooltip.soulsword.souls", souls, next.getRequiredKills())
                        : I18n.format("tooltip.soulsword.souls.max", souls)
        );
        tooltip.add(I18n.format("tooltip.soulsword.bonus_damage", bonus));

        if (stage.getMeleeVampirism() > 0f) {
            tooltip.add(I18n.format("tooltip.soulsword.vampirism", stage.getMeleeVampirism() * 100f));
        }

        if (stageNumber >= 10) {
            tooltip.add(I18n.format("tooltip.soulsword.wave.active_strong"));
        } else if (stageNumber >= 6) {
            tooltip.add(I18n.format("tooltip.soulsword.wave.active"));
        } else if (stageNumber >= 4) {
            tooltip.add(I18n.format("tooltip.soulsword.wave.passive"));
        }

        if (stageNumber >= 5) {
            int maxHearts = stageNumber >= 10 ? 4 : 2;
            tooltip.add(I18n.format("tooltip.soulsword.blood_shield", maxHearts));
        }
    }

    // =========================
    // PASSIVE WAVE (5 стадия)
    // =========================
    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {

        if (attacker instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) attacker;
            AwakeningStage stage = SoulData.getAwakeningStage(stack);

            // 5 стадия — пассив
            if (stage.hasSoulWavePassive()) {
                SoulWaveAttack.tryWave(player, false);
            }
        }

        return true;
    }

    // =========================
    // ACTIVE WAVE (6+ стадия)
    // =========================
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {

        ItemStack stack = player.getHeldItem(hand);
        int stageId = SoulData.getAwakeningStageId(stack);

        if (player.isSneaking()) {
            if (stageId >= 9) {
                player.setActiveHand(hand);
                return new ActionResult<>(EnumActionResult.SUCCESS, stack);
            }

            if (stageId >= 5 && !world.isRemote && tryShadowStep(player, stack)) {
                return new ActionResult<>(EnumActionResult.SUCCESS, stack);
            }
            return new ActionResult<>(EnumActionResult.PASS, stack);
        }

        // 6 стадия и выше — ТОЛЬКО активная
        if (stageId >= 5 && !world.isRemote) {
            if (player.getCooledAttackStrength(0.5f) < 1.0f) {
                return new ActionResult<>(EnumActionResult.PASS, stack);
            }
            SoulWaveAttack.tryWave(player, true);
            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        }

        return new ActionResult<>(EnumActionResult.PASS, stack);
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase entityLiving, int timeLeft) {
        if (!(entityLiving instanceof EntityPlayer)) return;

        EntityPlayer player = (EntityPlayer) entityLiving;
        int stageId = SoulData.getAwakeningStageId(stack);
        if (!player.isSneaking()) return;

        int usedTicks = getMaxItemUseDuration(stack) - timeLeft;

        if (stageId >= 9 && usedTicks >= 24) {
            if (!world.isRemote) {
                SoulWaveAttack.trySoulRend(player);
            }
            return;
        }

        if (stageId >= 5 && !world.isRemote) {
            tryShadowStep(player, stack);
        }
    }

    private boolean tryShadowStep(EntityPlayer player, ItemStack stack) {
        long now = player.world.getTotalWorldTime();
        long last = SoulData.getLastShadowStepTime(stack);
        if (now - last < 160) return false;
        if (!SoulData.consumeSouls(stack, 2)) return false;

        double step = 4.0D;
        for (double dist = step; dist >= 1.0D; dist -= 0.5D) {
            double dx = player.getLookVec().x * dist;
            double dz = player.getLookVec().z * dist;
            if (player.world.getCollisionBoxes(player, player.getEntityBoundingBox().offset(dx, 0, dz)).isEmpty()) {
                player.setPositionAndUpdate(player.posX + dx, player.posY, player.posZ + dz);
                SoulData.setLastShadowStepTime(stack, now);
                return true;
            }
        }
        return false;
    }
}
