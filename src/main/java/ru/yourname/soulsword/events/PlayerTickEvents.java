package ru.yourname.soulsword.events;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import ru.yourname.soulsword.SoulSwordMod;
import ru.yourname.soulsword.chat.SoulDialogueManager;
import ru.yourname.soulsword.item.ItemSoulSword;

@Mod.EventBusSubscriber(modid = SoulSwordMod.MODID)
public class PlayerTickEvents {

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {

        if (event.player.world.isRemote) return;
        if (event.phase != TickEvent.Phase.END) return;

        EntityPlayer player = event.player;
        ItemStack stack = player.getHeldItemMainhand();

        if (stack.isEmpty() || !(stack.getItem() instanceof ItemSoulSword)) return;

        // раз в 5 секунд
        if (player.ticksExisted % 100 != 0) return;

        SoulDialogueManager.tryHungerDialogue(player, stack);
        SoulDialogueManager.tryNightDialogue(player, stack);
        SoulDialogueManager.tryPsychosis(player, stack);
        SoulDialogueManager.tryLore(player, stack);
    }
}
