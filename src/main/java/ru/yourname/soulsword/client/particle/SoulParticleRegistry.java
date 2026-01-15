package ru.yourname.soulsword.client.particle;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import ru.yourname.soulsword.SoulSwordMod;

@Mod.EventBusSubscriber(modid = SoulSwordMod.MODID, value = Side.CLIENT)
public class SoulParticleRegistry {

    private static final ResourceLocation BLEEDING = new ResourceLocation(SoulSwordMod.MODID, "particle/bleeding");
    private static final ResourceLocation MARK = new ResourceLocation(SoulSwordMod.MODID, "particle/mark");
    private static final ResourceLocation FEAR_AURA = new ResourceLocation(SoulSwordMod.MODID, "particle/fear_aura");
    private static final ResourceLocation SOUL_WAVE = new ResourceLocation(SoulSwordMod.MODID, "particle/soul_wave");

    private static TextureAtlasSprite bleedingSprite;
    private static TextureAtlasSprite markSprite;
    private static TextureAtlasSprite fearAuraSprite;
    private static TextureAtlasSprite soulWaveSprite;

    @SubscribeEvent
    public static void onTextureStitchPre(TextureStitchEvent.Pre event) {
        if (!TextureMap.LOCATION_PARTICLES_TEXTURE.equals(event.getMap().getTextureLocation())) return;
        event.getMap().registerSprite(BLEEDING);
        event.getMap().registerSprite(MARK);
        event.getMap().registerSprite(FEAR_AURA);
        event.getMap().registerSprite(SOUL_WAVE);
    }

    @SubscribeEvent
    public static void onTextureStitchPost(TextureStitchEvent.Post event) {
        if (!TextureMap.LOCATION_PARTICLES_TEXTURE.equals(event.getMap().getTextureLocation())) return;

        bleedingSprite = event.getMap().getAtlasSprite(BLEEDING.toString());
        markSprite = event.getMap().getAtlasSprite(MARK.toString());
        fearAuraSprite = event.getMap().getAtlasSprite(FEAR_AURA.toString());
        soulWaveSprite = event.getMap().getAtlasSprite(SOUL_WAVE.toString());
    }

    public static TextureAtlasSprite getBleedingSprite() {
        return bleedingSprite;
    }

    public static TextureAtlasSprite getMarkSprite() {
        return markSprite;
    }

    public static TextureAtlasSprite getFearAuraSprite() {
        return fearAuraSprite;
    }

    public static TextureAtlasSprite getSoulWaveSprite() {
        return soulWaveSprite;
    }
}
