package ru.yourname.soulsword.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class SoulVisuals {

    public static void playSoulAbsorb(int souls) {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayerSP player = mc.player;

        if (player == null) return;

        // floating text
        player.sendStatusMessage(
                new TextComponentString(
                        TextFormatting.DARK_PURPLE + "+" + souls + " souls"
                ),
                true
        );

        // particles
        for (int i = 0; i < Math.min(20, souls * 2); i++) {
            mc.world.spawnParticle(
                    EnumParticleTypes.PORTAL,
                    player.posX + (mc.world.rand.nextDouble() - 0.5),
                    player.posY + 1.0,
                    player.posZ + (mc.world.rand.nextDouble() - 0.5),
                    0,
                    0.05,
                    0
            );
        }

        // sound
        player.playSound(
                net.minecraft.init.SoundEvents.ENTITY_ENDERMEN_TELEPORT,
                0.6f,
                1.2f
        );
    }
}
