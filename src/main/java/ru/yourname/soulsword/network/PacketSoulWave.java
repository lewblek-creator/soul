package ru.yourname.soulsword.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.entity.player.EntityPlayerMP;
import ru.yourname.soulsword.combat.SoulWaveAttack;
import ru.yourname.soulsword.item.ItemSoulSword;
import ru.yourname.soulsword.soul.SoulData;
import ru.yourname.soulsword.progression.AwakeningStage;

public class PacketSoulWave implements IMessage {

    @Override
    public void fromBytes(ByteBuf buf) {}

    @Override
    public void toBytes(ByteBuf buf) {}

    // =========================
    // HANDLER (SERVER)
    // =========================
    public static class Handler implements IMessageHandler<PacketSoulWave, IMessage> {

        @Override
        public IMessage onMessage(PacketSoulWave message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;

            player.getServerWorld().addScheduledTask(() -> {

                if (player.getHeldItemMainhand().isEmpty()) return;
                if (!(player.getHeldItemMainhand().getItem() instanceof ItemSoulSword)) return;

                AwakeningStage stage = SoulData.getAwakeningStage(player.getHeldItemMainhand());

                // Soul Wave АКТИВНАЯ только с 6 стадии
                if (!stage.hasSoulWaveActive()) return;

                SoulWaveAttack.tryWave(player, true);
            });

            return null;
        }
    }
}
