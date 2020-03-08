//package io.github.lucunji.noitacraft.entity;
//
//import io.github.lucunji.noitacraft.entity.projectile.SparkProjectile;
//import net.minecraft.entity.EntityClassification;
//import net.minecraft.entity.EntityType;
//import net.minecraftforge.event.RegistryEvent;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//import net.minecraftforge.fml.common.Mod;
//
//public class NoitaEntityTypes {
//    public static EntityType<SparkProjectile> PROJECTILE_SPARK;
//
//    static {
//        PROJECTILE_SPARK = EntityType.Builder.create(SparkProjectile::new, EntityClassification.MISC).build("spark_projectile");
//    }
//
////    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
//    public static class Register {
//
////        @SubscribeEvent
//        public static void onEntityRegistry(final RegistryEvent.Register<EntityType<?>> event) {
//            event.getRegistry().register(PROJECTILE_SPARK);
//        }
//    }
//}
