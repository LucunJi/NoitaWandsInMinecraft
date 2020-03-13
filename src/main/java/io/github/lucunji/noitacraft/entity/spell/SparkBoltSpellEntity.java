package io.github.lucunji.noitacraft.entity.spell;

import io.github.lucunji.noitacraft.item.NoitaItems;
import io.github.lucunji.noitacraft.spell.ProjectileSpell;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class SparkBoltSpellEntity extends SpellEntityMagicalBase {

    public SparkBoltSpellEntity(EntityType<?> type, World worldIn) {
        super(type, worldIn);
        this.inGround = false;
    }

    public SparkBoltSpellEntity(EntityType<?> type, LivingEntity caster, World world) {
        this(type, world);
        this.setPosition(caster.getPosX(), caster.getPosYEye() - 0.1, caster.getPosZ());
        this.casterUUID = caster.getUniqueID();
        this.caster = caster;
    }

    @Override
    protected int getExpireAge() {
        return 13;
    }

    @Override
    protected void onHit(RayTraceResult rayTraceResult) {
        super.onHit(rayTraceResult);

        if (rayTraceResult.getType() == RayTraceResult.Type.ENTITY) {
            Entity entityHit = ((EntityRayTraceResult) rayTraceResult).getEntity();
            if (entityHit.getUniqueID().equals(this.casterUUID)) return;
            ProjectileSpell.SPARK_BOLT.damageCollection().causeDamage(this, entityHit);
        }
        if (!this.world.isRemote()) this.remove();
    }

    @Override
    protected float getGravity() {
        return 0.01f;
    }

    @Override
    protected void generateParticles() {
        super.generateParticles();
        for (int i = 0; i < 5; i++) {
            this.world.addParticle(new ItemParticleData(ParticleTypes.ITEM, new ItemStack(NoitaItems.SPELL_SPARK_BOLT)), this.getPosXRandom(0.25D), this.getPosYRandom(), this.getPosZRandom(0.25D), 0, 0, 0);
        }
    }
}
