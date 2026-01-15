package ru.yourname.soulsword.client;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

public class SoulWaveKeybind {

    public static KeyBinding KEY;

    public static void register() {
        KEY = new KeyBinding(
                "key.soulsword.soulwave",
                Keyboard.KEY_R,
                "key.categories.soulsword"
        );
        ClientRegistry.registerKeyBinding(KEY);
    }
}
