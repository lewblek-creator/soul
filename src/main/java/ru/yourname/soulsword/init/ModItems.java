package ru.yourname.soulsword.init;

import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ru.yourname.soulsword.SoulSwordMod;
import ru.yourname.soulsword.item.ItemSoulSword;

@Mod.EventBusSubscriber(modid = SoulSwordMod.MODID)
public class ModItems {

    public static final ToolMaterial SOUL_MATERIAL = EnumHelper.addToolMaterial(
            "SOUL_MATERIAL",
            2,      // harvest level
            250,    // durability
            6.0F,   // efficiency (похуй)
            1.0F,   // ⚠️ ВАЖНО: 1 + 3 = 4 итогового урона
            10      // enchantability
    );

    public static Item SOUL_SWORD;

    @SubscribeEvent
    public static void onRegisterItems(RegistryEvent.Register<Item> event) {
        SOUL_SWORD = new ItemSoulSword(SOUL_MATERIAL)
                .setRegistryName(SoulSwordMod.MODID, "soul_sword")
                .setUnlocalizedName(SoulSwordMod.MODID + ".soul_sword");

        event.getRegistry().register(SOUL_SWORD);
    }
}

