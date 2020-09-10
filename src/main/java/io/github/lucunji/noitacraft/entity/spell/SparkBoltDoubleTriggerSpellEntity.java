package io.github.lucunji.noitacraft.entity.spell;

import io.github.lucunji.noitacraft.item.NoitaItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class SparkBoltDoubleTriggerSpellEntity extends SparkBoltSpellEntity {

    public SparkBoltDoubleTriggerSpellEntity(EntityType<?> type, World worldIn) {
        super(type, worldIn);
        this.inGround = false;
    }

    public SparkBoltDoubleTriggerSpellEntity(EntityType<?> type, LivingEntity caster, World world) {
        this(type, world);
        this.setPosition(caster.getPosX(), caster.getPosYEye() - 0.1, caster.getPosZ());
        this.casterUUID = caster.getUniqueID();
    }

    @Override
    protected void generateParticles() {
        Vec3d motionVec = this.getMotion();
        Vec3d nextPos = this.getPositionVec().add(motionVec);
        if (this.isInWater()) {
            for (int j = 0; j < 4; ++j) {
                this.world.addParticle(ParticleTypes.BUBBLE,
                        nextPos.getX() - motionVec.getX() * 0.25D, nextPos.getY() - motionVec.getY() * 0.25D, nextPos.getZ() - motionVec.getZ() * 0.25D,
                        motionVec.getX(), motionVec.getY(), motionVec.getZ());
            }
        }
        for (int i = 0; i < 5; i++) {
            this.world.addParticle(new ItemParticleData(ParticleTypes.ITEM, new ItemStack(NoitaItems.SPELL_SPARK_BOLT_TRIGGER_DOUBLE)), this.getPosXRandom(0.25D), this.getPosYRandom(), this.getPosZRandom(0.25D), 0, 0.3, 0);
        }
    }
}
