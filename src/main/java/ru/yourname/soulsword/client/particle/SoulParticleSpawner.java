package ru.yourname.soulsword.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SoulParticleSpawner {

    public static void spawnBleed(EntityLivingBase target) {
        spawnAround(target, SoulParticleRegistry.getBleedingSprite(), 0.3f);
    }

    public static void spawnMark(EntityLivingBase target) {
        spawnAround(target, SoulParticleRegistry.getMarkSprite(), 0.35f);
    }

    public static void spawnFearAura(EntityPlayer player) {
        TextureAtlasSprite sprite = SoulParticleRegistry.getFearAuraSprite();
        if (sprite == null) return;
        World world = player.world;

        for (int i = 0; i < 16; i++) {
            double angle = (Math.PI * 2) * (i / 16.0);
            double dx = Math.cos(angle) * 1.5;
            double dz = Math.sin(angle) * 1.5;
            spawnParticle(world, player.posX + dx, player.posY + 0.1, player.posZ + dz, 0, 0.02, 0, sprite, 0.6f, 20);
        }
    }

    public static void spawnSoulWave(EntityPlayer player, double range) {
        TextureAtlasSprite sprite = SoulParticleRegistry.getSoulWaveSprite();
        if (sprite == null) return;
        World world = player.world;

        Vec3d look = player.getLookVec();
        for (int i = 1; i <= 10; i++) {
            double dist = range * (i / 10.0);
            double x = player.posX + look.x * dist;
            double y = player.posY + 1.0;
            double z = player.posZ + look.z * dist;
            spawnParticle(world, x, y, z, 0, 0.01, 0, sprite, 0.5f, 12);
        }
    }

    public static void spawnSoulRend(EntityPlayer player, double range) {
        TextureAtlasSprite sprite = SoulParticleRegistry.getSoulWaveSprite();
        if (sprite == null) return;
        World world = player.world;

        Vec3d look = player.getLookVec();
        for (int i = 1; i <= 12; i++) {
            double dist = range * (i / 12.0);
            double width = 1.0 + (i / 12.0) * 2.5;
            for (int j = -1; j <= 1; j++) {
                double x = player.posX + look.x * dist + look.z * width * j;
                double z = player.posZ + look.z * dist - look.x * width * j;
                double y = player.posY + 1.0;
                spawnParticle(world, x, y, z, 0, 0.01, 0, sprite, 0.7f, 16);
            }
        }
    }

    private static void spawnAround(EntityLivingBase target, TextureAtlasSprite sprite, float scale) {
        if (sprite == null) return;
        World world = target.world;
        for (int i = 0; i < 2; i++) {
            double dx = (world.rand.nextDouble() - 0.5) * target.width;
            double dy = world.rand.nextDouble() * target.height;
            double dz = (world.rand.nextDouble() - 0.5) * target.width;
            spawnParticle(world, target.posX + dx, target.posY + dy, target.posZ + dz, 0, 0.02, 0, sprite, scale, 12);
        }
    }

    private static void spawnParticle(World world, double x, double y, double z,
                                      double vx, double vy, double vz,
                                      TextureAtlasSprite sprite, float scale, int maxAge) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.world == null) return;
        mc.effectRenderer.addEffect(new SoulParticle(world, x, y, z, vx, vy, vz, sprite, scale, maxAge));
    }
}
