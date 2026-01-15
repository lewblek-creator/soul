package ru.yourname.soulsword.client;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.common.Mod;
import ru.yourname.soulsword.SoulSwordMod;
import ru.yourname.soulsword.client.particle.SoulParticleSpawner;
import ru.yourname.soulsword.item.ItemSoulSword;
import ru.yourname.soulsword.network.PacketSoulWave;
import ru.yourname.soulsword.soul.SoulData;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

@Mod.EventBusSubscriber(modid = SoulSwordMod.MODID, value = Side.CLIENT)
public class SoulWaveKeyHandler {

    @SubscribeEvent
    public static void onKey(InputEvent.KeyInputEvent event) {

        if (SoulWaveKeybind.KEY == null) return;

        if (SoulWaveKeybind.KEY.isPressed()) {
            EntityPlayer player = Minecraft.getMinecraft().player;
            if (player != null) {
                ItemStack stack = player.getHeldItemMainhand();
                if (!stack.isEmpty() && stack.getItem() instanceof ItemSoulSword) {
                    int stageId = SoulData.getAwakeningStageId(stack);
                    if (stageId >= 5) {
                        SoulParticleSpawner.spawnSoulWave(player, 4.5D);
                        if (stageId >= 7) {
                            SoulParticleSpawner.spawnFearAura(player);
                        }
                    }
                }
            }
            SoulSwordMod.NETWORK.sendToServer(new PacketSoulWave());
        }
    }
}
