package ru.yourname.soulsword.client;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.common.Mod;
import ru.yourname.soulsword.SoulSwordMod;
import ru.yourname.soulsword.network.PacketSoulWave;

@Mod.EventBusSubscriber(modid = SoulSwordMod.MODID, value = Side.CLIENT)
public class SoulWaveKeyHandler {

    @SubscribeEvent
    public static void onKey(InputEvent.KeyInputEvent event) {

        if (SoulWaveKeybind.KEY == null) return;

        if (SoulWaveKeybind.KEY.isPressed()) {
            SoulSwordMod.NETWORK.sendToServer(new PacketSoulWave());
        }
    }
}
