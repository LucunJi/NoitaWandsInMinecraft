package io.github.lucunji.noitacraft.entity.spell;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.UUID;

public abstract class SpellEntityBase extends Entity implements IProjectile {
    protected UUID casterUUID;
    protected LivingEntity caster;
    protected boolean inGround;
    protected int age;

    public SpellEntityBase(EntityType<? extends SpellEntityBase> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
        this.age = 0;
    }

    public abstract void shoot(Entity shooter, float pitch, float yaw, float velocity, float inaccuracy);

    @Override
    protected void readAdditional(CompoundNBT compound) {
        if (compound.contains("Caster")) {
            this.casterUUID = compound.getUniqueId("Caster");
            if (!this.world.isRemote() && this.casterUUID != null) {
                ((ServerWorld) this.world).getEntityByUuid(this.casterUUID);
            }
        }
        if (compound.contains("inGround")) this.inGround = compound.getBoolean("inGround");
        if (compound.contains("Age")) this.age = compound.getInt("Age");
    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {
        if (this.casterUUID != null) compound.putUniqueId("Caster", casterUUID);
        compound.putBoolean("inGround", inGround);
        compound.putInt("Age", this.age);
    }

    @Override
    protected void registerData() {

    }

    @Override
    public void setFire(int seconds) {

    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Nullable
    protected EntityRayTraceResult rayTraceEntities(Vec3d startVec, Vec3d endVec) {
        return ProjectileHelper.rayTraceEntities(this.world, this, startVec, endVec,
                this.getBoundingBox().expand(this.getMotion()).grow(1.0D),
                (entity) -> !entity.isSpectator() && entity.isAlive() && entity.canBeCollidedWith() && (entity != this.caster));
    }

    @Override
    public void tick() {
        super.tick();
        ++this.age;
        if (this.age > this.getExpireAge()) onAgeExpire();
        this.generateParticles();
    }

    protected abstract int getExpireAge();

    protected abstract void onAgeExpire();

    protected abstract float getWaterDrag();

    protected abstract float getGravity();

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

    }
}
