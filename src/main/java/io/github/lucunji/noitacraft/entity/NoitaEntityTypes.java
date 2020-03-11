package io.github.lucunji.noitacraft.entity;

import io.github.lucunji.noitacraft.NoitaCraft;
import io.github.lucunji.noitacraft.entity.spell.BombSpellEntity;
import io.github.lucunji.noitacraft.entity.spell.EnergySphereSpellEntity;
import io.github.lucunji.noitacraft.entity.spell.SparkBoltSpellEntity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

public class NoitaEntityTypes {

    @ObjectHolder(NoitaCraft.MOD_ID + ":spark_bolt_spell")
    public static EntityType<SparkBoltSpellEntity> SPELL_SPARK_BOLT;

    @ObjectHolder(NoitaCraft.MOD_ID + ":bomb_spell")
    public static EntityType<BombSpellEntity> SPELL_BOMB;

    @ObjectHolder(NoitaCraft.MOD_ID + ":energy_sphere_spell")
    public static EntityType<EnergySphereSpellEntity> SPELL_ENERGY_SPHERE;

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Register {

        @SubscribeEvent
        public static void onEntityRegistry(final RegistryEvent.Register<EntityType<?>> event) {
            event.getRegistry().register(EntityType.Builder.<SparkBoltSpellEntity>create(SparkBoltSpellEntity::new, EntityClassification.MISC).size(NoitaSpells.SPARK_BOLT.radius * 0.2f, NoitaSpells.SPARK_BOLT.radius * 0.2f).build("spark_bolt_spell").setRegistryName(NoitaCraft.MOD_ID, "spark_bolt_spell"));
            event.getRegistry().register(EntityType.Builder.<BombSpellEntity>create(BombSpellEntity::new, EntityClassification.MISC).size(0.5f, 0.5f).build("bomb_spell").setRegistryName(NoitaCraft.MOD_ID, "bomb_spell"));
            event.getRegistry().register(EntityType.Builder.<EnergySphereSpellEntity>create(EnergySphereSpellEntity::new, EntityClassification.MISC).size(NoitaSpells.ENERGY_SPHERE.radius * 0.2f, NoitaSpells.ENERGY_SPHERE.radius * 0.2f).build("energy_sphere_spell").setRegistryName(NoitaCraft.MOD_ID, "energy_sphere_spell"));
        }
    }
}
