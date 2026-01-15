package ru.yourname.soulsword.client;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import ru.yourname.soulsword.SoulSwordMod;
import ru.yourname.soulsword.client.particle.SoulParticleSpawner;
import ru.yourname.soulsword.item.ItemSoulSword;
import ru.yourname.soulsword.progression.AwakeningStage;
import ru.yourname.soulsword.soul.SoulData;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Mod.EventBusSubscriber(modid = SoulSwordMod.MODID, value = Side.CLIENT)
public class SoulClientEffects {

    private static final Map<Integer, Long> BLEEDING = new HashMap<>();
    private static final Map<Integer, Long> MARKED = new HashMap<>();

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (!event.getEntity().world.isRemote) return;
        if (!(event.getSource().getTrueSource() instanceof EntityPlayer)) return;

        EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();
        if (player != Minecraft.getMinecraft().player) return;

        ItemStack stack = player.getHeldItemMainhand();
        if (stack.isEmpty() || !(stack.getItem() instanceof ItemSoulSword)) return;
        if (!(event.getEntity() instanceof IMob)) return;

        EntityLivingBase target = (EntityLivingBase) event.getEntity();
        long now = player.world.getTotalWorldTime();
        int stageId = SoulData.getAwakeningStageId(stack);
        AwakeningStage stage = SoulData.getAwakeningStage(stack);

        if (stageId >= 1) {
            BLEEDING.put(target.getEntityId(), now + 60);
        }

        if (stageId >= 6) {
            MARKED.put(target.getEntityId(), now + 160);
        }

        if (stage.hasSoulWavePassive()) {
            SoulParticleSpawner.spawnSoulWave(player, 4.5D);
        }
    }

    @SubscribeEvent
    public static void onRightClick(PlayerInteractEvent.RightClickItem event) {
        if (!event.getWorld().isRemote) return;
        if (!(event.getEntityPlayer() instanceof EntityPlayer)) return;

        EntityPlayer player = event.getEntityPlayer();
        ItemStack stack = event.getItemStack();
        if (stack.isEmpty() || !(stack.getItem() instanceof ItemSoulSword)) return;

        int stageId = SoulData.getAwakeningStageId(stack);
        if (stageId < 5) return;
        if (player.isSneaking()) return;
        if (player.getCooledAttackStrength(0.5f) < 1.0f) return;

        SoulParticleSpawner.spawnSoulWave(player, 4.5D);
        if (stageId >= 7) {
            SoulParticleSpawner.spawnFearAura(player);
        }
    }

    @SubscribeEvent
    public static void onUseStop(LivingEntityUseItemEvent.Stop event) {
        if (!event.getEntityLiving().world.isRemote) return;
        if (!(event.getEntityLiving() instanceof EntityPlayer)) return;

        EntityPlayer player = (EntityPlayer) event.getEntityLiving();
        ItemStack stack = event.getItem();
        if (stack.isEmpty() || !(stack.getItem() instanceof ItemSoulSword)) return;

        int stageId = SoulData.getAwakeningStageId(stack);
        if (stageId < 9 || !player.isSneaking()) return;

        int usedTicks = stack.getMaxItemUseDuration() - event.getDuration();
        if (usedTicks >= 24) {
            SoulParticleSpawner.spawnSoulRend(player, 10.0D);
        }
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.world == null) return;

        long now = mc.world.getTotalWorldTime();
        if (now % 4 != 0) return;

        tickEffects(mc, BLEEDING, now, true);
        tickEffects(mc, MARKED, now, false);
    }

    private static void tickEffects(Minecraft mc, Map<Integer, Long> map, long now, boolean bleed) {
        Iterator<Map.Entry<Integer, Long>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, Long> entry = iterator.next();
            if (now > entry.getValue()) {
                iterator.remove();
                continue;
            }

            EntityLivingBase target = (EntityLivingBase) mc.world.getEntityByID(entry.getKey());
            if (target == null) {
                iterator.remove();
                continue;
            }

            if (bleed) {
                SoulParticleSpawner.spawnBleed(target);
            } else {
                SoulParticleSpawner.spawnMark(target);
            }
        }
    }
}
