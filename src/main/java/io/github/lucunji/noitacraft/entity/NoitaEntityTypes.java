package io.github.lucunji.noitacraft.entity;

import io.github.lucunji.noitacraft.NoitaCraft;
import io.github.lucunji.noitacraft.entity.projectile.SparkProjectile;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

public class NoitaEntityTypes {

    @ObjectHolder(NoitaCraft.MOD_ID + ":spark_projectile")
    public static EntityType<SparkProjectile> PROJECTILE_SPARK;

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Register {

        @SubscribeEvent
        public static void onEntityRegistry(final RegistryEvent.Register<EntityType<?>> event) {
            event.getRegistry().register(EntityType.Builder.<SparkProjectile>create(SparkProjectile::new, EntityClassification.MISC).size(0.5f, 0.5f).build("spark_projectile").setRegistryName(NoitaCraft.MOD_ID, "spark_projectile"));
        }
    }
}
