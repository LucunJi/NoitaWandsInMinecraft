package io.github.lucunji.noitawands.item;

import io.github.lucunji.noitawands.NoitaWands;
import io.github.lucunji.noitawands.item.wand.WandItem;
import io.github.lucunji.noitawands.spell.ProjectileSpell;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(NoitaWands.MODID)
public class ModItems {

    @ObjectHolder("spell_spark_bolt") public static Item SPELL_SPARK_BOLT;
    @ObjectHolder("spell_spark_bolt_timer") public static Item SPELL_SPARK_BOLT_TIMER;
    @ObjectHolder("spell_spark_bolt_trigger") public static Item SPELL_SPARK_BOLT_TRIGGER;
    @ObjectHolder("spell_spark_bolt_trigger_double") public static Item SPELL_SPARK_BOLT_TRIGGER_DOUBLE;
    @ObjectHolder("spell_bomb") public static Item SPELL_BOMB;
    @ObjectHolder("spell_energy_sphere") public static Item SPELL_ENERGY_SPHERE;
    @ObjectHolder("spell_energy_sphere_timer") public static Item SPELL_ENERGY_SPHERE_TIMER;

    @ObjectHolder("wand") public static Item WAND;

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Register {

        @SubscribeEvent
        public static void onEntityRegistry(final RegistryEvent.Register<Item> event) {
            event.getRegistry().register(new WandItem(new Item.Properties()).setRegistryName(NoitaWands.MODID, "wand"));

            event.getRegistry().register(new SpellItem(new Item.Properties(), ProjectileSpell.SPARK_BOLT).setRegistryName(NoitaWands.MODID, "spell_spark_bolt"));
            event.getRegistry().register(new SpellItem(new Item.Properties(), ProjectileSpell.SPARK_BOLT_TIMER).setRegistryName(NoitaWands.MODID, "spell_spark_bolt_timer"));
            event.getRegistry().register(new SpellItem(new Item.Properties(), ProjectileSpell.SPARK_BOLT_TRIGGER).setRegistryName(NoitaWands.MODID, "spell_spark_bolt_trigger"));
            event.getRegistry().register(new SpellItem(new Item.Properties(), ProjectileSpell.SPARK_BOLT_TRIGGER_DOUBLE).setRegistryName(NoitaWands.MODID, "spell_spark_bolt_trigger_double"));
            event.getRegistry().register(new SpellItem(new Item.Properties(), ProjectileSpell.BOMB).setRegistryName(NoitaWands.MODID, "spell_bomb"));
            event.getRegistry().register(new SpellItem(new Item.Properties(), ProjectileSpell.ENERGY_SPHERE).setRegistryName(NoitaWands.MODID, "spell_energy_sphere"));
            event.getRegistry().register(new SpellItem(new Item.Properties(), ProjectileSpell.ENERGY_SPHERE_TIMER).setRegistryName(NoitaWands.MODID, "spell_energy_sphere_timer"));
        }
    }
}
