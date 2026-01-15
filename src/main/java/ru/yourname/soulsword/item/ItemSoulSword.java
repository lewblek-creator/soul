package ru.yourname.soulsword.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import ru.yourname.soulsword.combat.SoulWaveAttack;
import ru.yourname.soulsword.progression.AwakeningStage;
import ru.yourname.soulsword.soul.SoulData;

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

    // =========================
    // PASSIVE WAVE (5 стадия)
    // =========================
    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {

        if (attacker instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) attacker;
            AwakeningStage stage = SoulData.getAwakeningStage(stack);

            // 5 стадия — пассив
            if (stage == AwakeningStage.DOMINATING) {
                SoulWaveAttack.tryWave(player);
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
        AwakeningStage stage = SoulData.getAwakeningStage(stack);

        // 6 стадия и выше — ТОЛЬКО активная
        if (stage.ordinal() >= AwakeningStage.ACCEPTING.ordinal() && !world.isRemote) {
            SoulWaveAttack.tryWave(player);
            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        }

        return new ActionResult<>(EnumActionResult.PASS, stack);
    }
}
