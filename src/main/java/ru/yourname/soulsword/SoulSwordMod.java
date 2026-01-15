package ru.yourname.soulsword;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import ru.yourname.soulsword.network.PacketSoulWave;
import ru.yourname.soulsword.client.SoulWaveKeybind;

@Mod(
        modid = SoulSwordMod.MODID,
        name = SoulSwordMod.NAME,
        version = SoulSwordMod.VERSION
)
public class SoulSwordMod {

    public static final String MODID = "soulsword";
    public static final String NAME = "Soul Sword";
    public static final String VERSION = "1.0";

    public static SimpleNetworkWrapper NETWORK;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {

        // ===== NETWORK =====
        NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
        NETWORK.registerMessage(
                PacketSoulWave.Handler.class,
                PacketSoulWave.class,
                0,
                Side.SERVER
        );

        // ===== CLIENT KEYBIND =====
        if (event.getSide().isClient()) {
            SoulWaveKeybind.register();
        }
    }
}
