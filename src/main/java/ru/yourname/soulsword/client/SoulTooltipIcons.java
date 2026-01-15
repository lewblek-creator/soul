package ru.yourname.soulsword.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import ru.yourname.soulsword.SoulSwordMod;
import ru.yourname.soulsword.item.ItemSoulSword;
import ru.yourname.soulsword.progression.AwakeningStage;
import ru.yourname.soulsword.soul.SoulData;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = SoulSwordMod.MODID, value = Side.CLIENT)
public class SoulTooltipIcons {

    private static final int ICON_SIZE = 12;
    private static final ResourceLocation ICON_STAGE =
            new ResourceLocation(SoulSwordMod.MODID, "textures/gui/icon_stage.png");
    private static final ResourceLocation ICON_SOUL =
            new ResourceLocation(SoulSwordMod.MODID, "textures/gui/icon_soul.png");
    private static final ResourceLocation ICON_VAMPIRISM =
            new ResourceLocation(SoulSwordMod.MODID, "textures/gui/icon_vampirism.png");
    private static final ResourceLocation ICON_WAVE_PURPLE =
            new ResourceLocation(SoulSwordMod.MODID, "textures/gui/icon_wave_purple.png");
    private static final ResourceLocation ICON_WAVE_RED =
            new ResourceLocation(SoulSwordMod.MODID, "textures/gui/icon_wave_red.png");
    private static final ResourceLocation ICON_WAVE_GOLD =
            new ResourceLocation(SoulSwordMod.MODID, "textures/gui/icon_wave_gold.png");
    private static final ResourceLocation ICON_BLOOD_SHIELD =
            new ResourceLocation(SoulSwordMod.MODID, "textures/gui/icon_blood_shield.png");

    @SubscribeEvent
    public static void onTooltip(RenderTooltipEvent.PostText event) {
        ItemStack stack = event.getStack();
        if (stack == null || stack.isEmpty() || !(stack.getItem() instanceof ItemSoulSword)) return;

        AwakeningStage stage = SoulData.getAwakeningStage(stack);
        int stageId = SoulData.getAwakeningStageId(stack);

        List<ResourceLocation> icons = new ArrayList<>();
        icons.add(ICON_STAGE);
        icons.add(ICON_SOUL);
        icons.add(ICON_VAMPIRISM);

        if (stage.getMeleeVampirism() > 0f) {
            icons.add(ICON_VAMPIRISM);
        }

        ResourceLocation waveIcon = getWaveIcon(stageId);
        if (waveIcon != null) {
            icons.add(waveIcon);
        }

        if (stageId >= 4) {
            icons.add(ICON_BLOOD_SHIELD);
        }

        int x = event.getX() - ICON_SIZE - 4;
        int y = event.getY();

        Minecraft mc = Minecraft.getMinecraft();
        GlStateManager.color(1f, 1f, 1f, 1f);

        int max = Math.min(icons.size(), event.getLines().size());
        for (int i = 0; i < max; i++) {
            drawIcon(mc, icons.get(i), x, y + i * 10);
        }
    }

    private static ResourceLocation getWaveIcon(int stageId) {
        if (stageId >= 9) return ICON_WAVE_GOLD;
        if (stageId >= 5) return ICON_WAVE_RED;
        if (stageId >= 3) return ICON_WAVE_PURPLE;
        return null;
    }

    private static void drawIcon(Minecraft mc, ResourceLocation icon, int x, int y) {
        if (icon == null) return;
        mc.getTextureManager().bindTexture(icon);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, ICON_SIZE, ICON_SIZE, ICON_SIZE, ICON_SIZE);
    }
}
