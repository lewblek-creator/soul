package ru.yourname.soulsword.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ru.yourname.soulsword.SoulSwordMod;
import ru.yourname.soulsword.item.ItemSoulSword;
import ru.yourname.soulsword.progression.AwakeningStage;
import ru.yourname.soulsword.soul.SoulData;

@Mod.EventBusSubscriber(modid = SoulSwordMod.MODID)
public class SoulSwordHUD {

    @SubscribeEvent
    public static void onRender(RenderGameOverlayEvent.Text event) {

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player == null) return;

        ItemStack stack = mc.player.getHeldItemMainhand();
        if (stack.isEmpty() || !(stack.getItem() instanceof ItemSoulSword)) return;

        ScaledResolution res = event.getResolution();
        int x = 8;
        int y = res.getScaledHeight() - 45;

        AwakeningStage stage = SoulData.getAwakeningStage(stack);
        AwakeningStage next = stage.next();

        int kills = SoulData.getKills(stack);
        float bonus = SoulData.getBonusDamage(stack);

        mc.fontRenderer.drawStringWithShadow(
                I18n.format("hud.soulsword.title"),
                x, y, 0xFFFFFF
        );

        mc.fontRenderer.drawStringWithShadow(
                I18n.format("hud.soulsword.stage", stage.getDisplayName()),
                x, y + 10, 0xFFFFFF
        );

        mc.fontRenderer.drawStringWithShadow(
                next != null
                        ? I18n.format("hud.soulsword.souls", kills, next.getRequiredKills())
                        : I18n.format("hud.soulsword.souls.max", kills),
                x, y + 20, 0xFFFFFF
        );

        mc.fontRenderer.drawStringWithShadow(
                I18n.format("hud.soulsword.damage", bonus),
                x, y + 30, 0xFFFFFF
        );
    }
}

