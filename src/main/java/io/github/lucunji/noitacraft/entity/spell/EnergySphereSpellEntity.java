package io.github.lucunji.noitacraft.entity.spell;

import io.github.lucunji.noitacraft.item.NoitaItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

public class EnergySphereSpellEntity extends SpellEntityMagicalBase {

    public EnergySphereSpellEntity(EntityType<? extends SpellEntityMagicalBase> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
    }

    public EnergySphereSpellEntity(EntityType<EnergySphereSpellEntity> type, LivingEntity caster, World world) {
        this(type, world);
        this.setPosition(caster.getPosX(), caster.getPosYEye() - 0.1, caster.getPosZ());
        this.casterUUID = caster.getUniqueID();
        this.caster = caster;
    }

    @Override
    protected int getExpireAge() {
        return 26;
    }

    @Override
    protected void onAgeExpire() {
        this.remove();
    }

    @Override
    protected float getWaterDrag() {
        return 0.6f;
    }

    @Override
    protected float getGravity() {
        return 0.03f;
    }

    @Override
    protected void onHit(RayTraceResult rayTraceResult) {
        if (rayTraceResult.getType() == RayTraceResult.Type.ENTITY) {
            Entity entityHit = ((EntityRayTraceResult) rayTraceResult).getEntity();
            if (entityHit.getUniqueID().equals(this.casterUUID)) return;
            entityHit.attackEntityFrom(DamageSource.causeIndirectDamage(this, caster), 10.0f);
            entityHit.hurtResistantTime = 0;
            if (!this.world.isRemote()) this.remove();
        } else if (rayTraceResult.getType() == RayTraceResult.Type.BLOCK) {
            Vec3d motion = this.getMotion();
            Direction.Axis reflectionAxis = ((BlockRayTraceResult) rayTraceResult).getFace().getAxis();
            Vec3d normalVec = new Vec3d(((BlockRayTraceResult) rayTraceResult).getFace().getDirectionVec());
            /*
               180.0 - Math.acos((motion.dotProduct(normalVec))/(motion.length())) / Math.PI * 180.0 > 60.0
            =>   -acos(dot(norm)/length) / pi * 180 > -120
            =>   acos(dot(norm)/length) < 120*pi/180
            =>   dot(norm)/length > cos(120*pi/180) = -0.5
            =>   dot(norm) > -0.5 * length;
            =>   dot(norm)^2 < (-0.5 * length)^2
             */
            double d0 = motion.dotProduct(normalVec);
            double d1 = motion.length() / 2;
            if (d0 * d0 >= d1 * d1 && !this.world.isRemote()) {
                this.remove();
                return;
            }
            switch (reflectionAxis) {
                case X:
                    motion = new Vec3d(-motion.getX(), motion.getY(), motion.getZ());
                    break;
                case Y:
                    motion = new Vec3d(motion.getX(), -motion.getY(), motion.getZ());
                    break;
                case Z:
                    motion = new Vec3d(motion.getX(), motion.getY(), -motion.getZ());
                    break;
            }
            this.setMotion(motion.getX() * 0.8, motion.getY() * 0.7, motion.getZ() * 0.8);
        }
    }

    @Override
    protected void generateParticles() {
        super.generateParticles();
        for (int i = 0; i < 5; i++) {
            this.world.addParticle(new ItemParticleData(ParticleTypes.ITEM, new ItemStack(NoitaItems.SPELL_ENERGY_SPHERE)), this.getPosXRandom(0.25D), this.getPosYRandom(), this.getPosZRandom(0.25D), 0, 0, 0);
        }
    }
}
