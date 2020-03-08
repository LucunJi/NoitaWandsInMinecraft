package io.github.lucunji.noitacraft.item;

import io.github.lucunji.noitacraft.NoitaCraft;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

public class NoitaItems {

    @ObjectHolder(NoitaCraft.MOD_ID + ":spell_energy_sphere")
    public static Item SPELL_ENERGY_SPHERE;

    @ObjectHolder(NoitaCraft.MOD_ID + ":wand")
    public static Item WAND;

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Register {

        @SubscribeEvent
        public static void onEntityRegistry(final RegistryEvent.Register<Item> event) {
            event.getRegistry().register(new WandItem(new Item.Properties()).setRegistryName(NoitaCraft.MOD_ID, "wand"));

            event.getRegistry().register(new SpellItem(new Item.Properties()).setRegistryName(NoitaCraft.MOD_ID, "spell_energy_sphere"));
            event.getRegistry().register(new SpellItem(new Item.Properties()).setRegistryName(NoitaCraft.MOD_ID, "spell_spark_bolt"));
            event.getRegistry().register(new SpellItem(new Item.Properties()).setRegistryName(NoitaCraft.MOD_ID, "spell_bomb"));
        }
    }
}
