package io.github.lucunji.noitacraft.entity.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.UUID;

public abstract class SpellProjectileEntityBase extends Entity implements IProjectile {
    protected UUID casterUUID;
    protected LivingEntity caster;
    protected boolean inGround;
    protected int age;

    public SpellProjectileEntityBase(EntityType<?> entityTypeIn, World worldIn) {
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
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Nullable
    protected EntityRayTraceResult rayTraceEntities(Vec3d startVec, Vec3d endVec) {
        return ProjectileHelper.rayTraceEntities(this.world, this, startVec, endVec,
                this.getBoundingBox().expand(this.getMotion()).grow(1.0D),
                (entity) -> !entity.isSpectator() && entity.isAlive() && entity.canBeCollidedWith() && (entity != this.caster));
    }
}
