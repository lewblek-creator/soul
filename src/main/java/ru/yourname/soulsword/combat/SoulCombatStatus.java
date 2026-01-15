package ru.yourname.soulsword.combat;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import java.util.UUID;

public class SoulCombatStatus {

    private static final String BLEED_UNTIL = "SoulSwordBleedUntil";
    private static final String BLEED_NEXT = "SoulSwordBleedNext";
    private static final String MARK_UNTIL = "SoulSwordMarkUntil";
    private static final String MARK_OWNER = "SoulSwordMarkOwner";

    public static void applyBleed(EntityLivingBase target, long now, int durationTicks) {
        NBTTagCompound data = target.getEntityData();
        data.setLong(BLEED_UNTIL, now + durationTicks);
        data.setLong(BLEED_NEXT, now + 20);
    }

    public static boolean shouldBleedTick(EntityLivingBase target, long now) {
        NBTTagCompound data = target.getEntityData();
        long until = data.getLong(BLEED_UNTIL);
        if (until <= 0 || now >= until) {
            if (until > 0) {
                data.removeTag(BLEED_UNTIL);
                data.removeTag(BLEED_NEXT);
            }
            return false;
        }

        long next = data.getLong(BLEED_NEXT);
        if (now >= next) {
            data.setLong(BLEED_NEXT, now + 20);
            return true;
        }
        return false;
    }

    public static void applyMark(EntityLivingBase target, EntityPlayer owner, long now, int durationTicks) {
        NBTTagCompound data = target.getEntityData();
        data.setLong(MARK_UNTIL, now + durationTicks);
        data.setString(MARK_OWNER, owner.getUniqueID().toString());
    }

    public static boolean isMarkedBy(EntityLivingBase target, EntityPlayer owner, long now) {
        NBTTagCompound data = target.getEntityData();
        long until = data.getLong(MARK_UNTIL);
        if (until <= 0 || now >= until) {
            if (until > 0) {
                data.removeTag(MARK_UNTIL);
                data.removeTag(MARK_OWNER);
            }
            return false;
        }

        String ownerId = data.getString(MARK_OWNER);
        if (ownerId.isEmpty()) return false;

        UUID uuid = owner.getUniqueID();
        return ownerId.equals(uuid.toString());
    }
}
