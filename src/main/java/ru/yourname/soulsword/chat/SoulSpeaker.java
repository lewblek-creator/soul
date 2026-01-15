package ru.yourname.soulsword.chat;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;

public class SoulSpeaker {

    public static void speak(EntityPlayer player, String text, boolean whisper) {
        TextComponentString msg = new TextComponentString(text);

        Style style = new Style().setColor(TextFormatting.DARK_PURPLE);
        if (whisper) {
            style.setItalic(true);
        }

        msg.setStyle(style);
        player.world.getMinecraftServer()
                .getPlayerList()
                .sendMessage(msg);
    }
}
