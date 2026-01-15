package ru.yourname.soulsword.client.particle;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SoulParticle extends Particle {

    public SoulParticle(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn,
                        double xSpeedIn, double ySpeedIn, double zSpeedIn,
                        TextureAtlasSprite sprite, float scale, int maxAge) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        if (sprite != null) {
            setParticleTexture(sprite);
        }
        this.particleScale = scale;
        this.particleMaxAge = maxAge;
    }
}
