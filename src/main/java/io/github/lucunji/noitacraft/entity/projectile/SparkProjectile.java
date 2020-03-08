//package io.github.lucunji.noitacraft.entity.projectile;
//
//import net.minecraft.entity.EntityType;
//import net.minecraft.entity.projectile.ThrowableEntity;
//import net.minecraft.util.math.RayTraceResult;
//import net.minecraft.world.World;
//
//public class SparkProjectile extends ThrowableEntity {
//
//    public SparkProjectile(EntityType<? extends ThrowableEntity> type, World worldIn) {
//        super(type, worldIn);
//    }
//
//    @Override
//    protected void onImpact(RayTraceResult result) {
//        if (!world.isRemote()) {
//            this.remove();
//        }
//    }
//
//    @Override
//    protected void registerData() {
//
//    }
//}
