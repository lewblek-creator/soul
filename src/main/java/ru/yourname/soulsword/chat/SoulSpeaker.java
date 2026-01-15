package ru.yourname.soulsword.chat;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

public class SoulSpeaker {

    public static void speak(EntityPlayer player, String translationKey, boolean whisper, Object... args) {
        MinecraftServer server = player.world.getMinecraftServer();
        if (server == null) return;

        ITextComponent msg = new TextComponentTranslation(translationKey, args);

        Style style = new Style().setColor(TextFormatting.DARK_PURPLE);
        if (whisper) {
            style.setItalic(true);
        }

        msg.setStyle(style);
        server.getPlayerList()
                .sendMessage(msg);
    }
}
