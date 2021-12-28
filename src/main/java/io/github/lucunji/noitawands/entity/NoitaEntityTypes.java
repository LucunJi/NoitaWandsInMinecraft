package io.github.lucunji.noitawands.entity;

import io.github.lucunji.noitawands.NoitaWands;
import io.github.lucunji.noitawands.entity.spell.BombSpellEntity;
import io.github.lucunji.noitawands.entity.spell.EnergySphereSpellEntity;
import io.github.lucunji.noitawands.entity.spell.SparkBoltDoubleTriggerSpellEntity;
import io.github.lucunji.noitawands.entity.spell.SparkBoltSpellEntity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(NoitaWands.MODID)
public class NoitaEntityTypes {

    @ObjectHolder("spark_bolt_spell") public static EntityType<SparkBoltSpellEntity> SPELL_SPARK_BOLT;
    @ObjectHolder("spark_bolt_spell_trigger_double") public static EntityType<SparkBoltSpellEntity> SPELL_SPARK_BOLT_DOUBLE_TRIGGER;
    @ObjectHolder("bomb_spell") public static EntityType<BombSpellEntity> SPELL_BOMB;
    @ObjectHolder("energy_sphere_spell") public static EntityType<EnergySphereSpellEntity> SPELL_ENERGY_SPHERE;
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD) public static class Register {

        @SubscribeEvent
        public static void onEntityRegistry(final RegistryEvent.Register<EntityType<?>> event) {
            event.getRegistry().register(EntityType.Builder.<SparkBoltSpellEntity>create(SparkBoltSpellEntity::new, EntityClassification.MISC).size(0.5f, 0.5f).build("spark_bolt_spell").setRegistryName(NoitaWands.MODID, "spark_bolt_spell"));
            event.getRegistry().register(EntityType.Builder.<SparkBoltDoubleTriggerSpellEntity>create(SparkBoltDoubleTriggerSpellEntity::new, EntityClassification.MISC).size(0.5f, 0.5f).build("spark_bolt_spell_trigger_double").setRegistryName(NoitaWands.MODID, "spark_bolt_spell_trigger_double"));
            event.getRegistry().register(EntityType.Builder.<BombSpellEntity>create(BombSpellEntity::new, EntityClassification.MISC).size(0.5f, 0.5f).build("bomb_spell").setRegistryName(NoitaWands.MODID, "bomb_spell"));
            event.getRegistry().register(EntityType.Builder.<EnergySphereSpellEntity>create(EnergySphereSpellEntity::new, EntityClassification.MISC).size(0.5f, 0.5f).build("energy_sphere_spell").setRegistryName(NoitaWands.MODID, "energy_sphere_spell"));
        }
    }
}
