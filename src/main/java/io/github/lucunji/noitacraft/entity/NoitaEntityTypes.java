package io.github.lucunji.noitacraft.entity;

import io.github.lucunji.noitacraft.NoitaCraft;
import io.github.lucunji.noitacraft.entity.projectile.BombProjectileEntity;
import io.github.lucunji.noitacraft.entity.projectile.SparkProjectileEntity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

public class NoitaEntityTypes {

    @ObjectHolder(NoitaCraft.MOD_ID + ":spark_projectile")
    public static EntityType<SparkProjectileEntity> PROJECTILE_SPARK;

    @ObjectHolder(NoitaCraft.MOD_ID + ":bomb_projectile")
    public static EntityType<BombProjectileEntity> PROJECTILE_BOMB;

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Register {

        @SubscribeEvent
        public static void onEntityRegistry(final RegistryEvent.Register<EntityType<?>> event) {
            event.getRegistry().register(EntityType.Builder.<SparkProjectileEntity>create(SparkProjectileEntity::new, EntityClassification.MISC).size(0.5f, 0.5f).build("spark_projectile").setRegistryName(NoitaCraft.MOD_ID, "spark_projectile"));
            event.getRegistry().register(EntityType.Builder.<BombProjectileEntity>create(BombProjectileEntity::new, EntityClassification.MISC).size(0.5f, 0.5f).build("bomb_projectile").setRegistryName(NoitaCraft.MOD_ID, "bomb_projectile"));
        }
    }
}
