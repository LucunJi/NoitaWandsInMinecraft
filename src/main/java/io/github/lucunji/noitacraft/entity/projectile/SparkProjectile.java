package io.github.lucunji.noitacraft.entity.projectile;

import io.github.lucunji.noitacraft.item.NoitaItems;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.*;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.UUID;

public class SparkProjectile extends Entity implements IProjectile {

    private UUID casterUUID;
    private LivingEntity caster;
    private boolean inGround;
    private int age;
    private static final int expireAge = 13;
    private static final double gravity = 0.01;

    public SparkProjectile(EntityType<SparkProjectile> type, World worldIn) {
        super(type, worldIn);
        this.age = 0;
        this.inGround = false;
    }

    public SparkProjectile(EntityType<SparkProjectile> type, LivingEntity caster, World world) {
        this(type, world);
        this.setPosition(caster.getPosX(), caster.getPosYEye() - 0.1, caster.getPosZ());
        this.casterUUID = caster.getUniqueID();
        this.caster = caster;
    }

    public void shoot(Entity shooter, float pitch, float yaw, float velocity, float inaccuracy) {
        float x = -MathHelper.sin(yaw * ((float)Math.PI / 180F)) * MathHelper.cos(pitch * ((float)Math.PI / 180F));
        float y = -MathHelper.sin(pitch * ((float)Math.PI / 180F));
        float z = MathHelper.cos(yaw * ((float)Math.PI / 180F)) * MathHelper.cos(pitch * ((float)Math.PI / 180F));
        this.shoot(x, y, z, velocity, inaccuracy);
        this.setMotion(this.getMotion().add(shooter.getMotion().x, shooter.onGround ? 0.0D : shooter.getMotion().y, shooter.getMotion().z));
    }

    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        Vec3d vec3d = (new Vec3d(x, y, z)).normalize().add(this.rand.nextGaussian() * (double)0.0075F * (double)inaccuracy, this.rand.nextGaussian() * (double)0.0075F * (double)inaccuracy, this.rand.nextGaussian() * (double)0.0075F * (double)inaccuracy).scale(velocity);
        this.setMotion(vec3d);
        float f = MathHelper.sqrt(horizontalMag(vec3d));
        this.rotationYaw = (float)(MathHelper.atan2(vec3d.x, vec3d.z) * (double)(180F / (float)Math.PI));
        this.rotationPitch = (float)(MathHelper.atan2(vec3d.y, f) * (double)(180F / (float)Math.PI));
        this.prevRotationYaw = this.rotationYaw;
        this.prevRotationPitch = this.rotationPitch;
    }

    @OnlyIn(Dist.CLIENT)
    public void setVelocity(double x, double y, double z) {
        this.setMotion(x, y, z);
        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
            float f = MathHelper.sqrt(x * x + z * z);
            this.rotationPitch = (float)(MathHelper.atan2(y, f) * (double)(180F / (float)Math.PI));
            this.rotationYaw = (float)(MathHelper.atan2(x, z) * (double)(180F / (float)Math.PI));
            this.prevRotationPitch = this.rotationPitch;
            this.prevRotationYaw = this.rotationYaw;
            this.setLocationAndAngles(this.getPosX(), this.getPosY(), this.getPosZ(), this.rotationYaw, this.rotationPitch);
        }
    }

    public void tick() {
        super.tick();
        ++this.age;
        if (this.age > expireAge) this.remove();
        Vec3d motionVec = this.getMotion();
        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
            float f = MathHelper.sqrt(horizontalMag(motionVec));
            this.rotationYaw = (float)(MathHelper.atan2(motionVec.x, motionVec.z) * (double)(180F / (float)Math.PI));
            this.rotationPitch = (float)(MathHelper.atan2(motionVec.y, f) * (double)(180F / (float)Math.PI));
            this.prevRotationYaw = this.rotationYaw;
            this.prevRotationPitch = this.rotationPitch;
        }

        BlockPos blockpos = new BlockPos(this);
        BlockState blockstate = this.world.getBlockState(blockpos);
        if (!blockstate.isAir(this.world, blockpos)) {
            VoxelShape voxelshape = blockstate.getCollisionShape(this.world, blockpos);
            if (!voxelshape.isEmpty()) {
                Vec3d vec3d1 = this.getPositionVec();

                for(AxisAlignedBB axisalignedbb : voxelshape.toBoundingBoxList()) {
                    if (axisalignedbb.offset(blockpos).contains(vec3d1)) {
                        this.inGround = true;
                        break;
                    }
                }
            }
        }

        if (this.isWet()) {
            this.extinguish();
        }

        if (this.inGround) {
            this.inGround = false;
            this.setMotion(motionVec.mul(this.rand.nextFloat() * 0.2F, this.rand.nextFloat() * 0.2F, this.rand.nextFloat() * 0.2F));

        } else {
            Vec3d positionVec = this.getPositionVec();
            Vec3d positionVecNext = positionVec.add(motionVec);

            RayTraceResult blockRayTraceResult = this.world.rayTraceBlocks(new RayTraceContext(positionVec, positionVecNext, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this));
            if (blockRayTraceResult.getType() != RayTraceResult.Type.MISS) {
                positionVecNext = blockRayTraceResult.getHitVec();
            }

            EntityRayTraceResult entityRayTraceResult = this.rayTraceEntities(positionVec, positionVecNext);
            if (entityRayTraceResult != null) {
                blockRayTraceResult = entityRayTraceResult;
            }

            if (blockRayTraceResult.getType() != RayTraceResult.Type.MISS) {
                this.onHit(blockRayTraceResult);
                this.isAirBorne = true;
            }

            motionVec = this.getMotion();
            double nextX = this.getPosX() + motionVec.x;
            double nextY = this.getPosY() + motionVec.y;
            double nextZ = this.getPosZ() + motionVec.z;
            float f1 = MathHelper.sqrt(horizontalMag(motionVec));
            this.rotationYaw = (float)(MathHelper.atan2(motionVec.x, motionVec.z) * (double)(180F / (float)Math.PI));

            this.rotationPitch = (float)(MathHelper.atan2(motionVec.y, f1) * (double)(180F / (float)Math.PI));
            while (this.rotationPitch - this.prevRotationPitch < -180.0F) {
                this.prevRotationPitch -= 360.0F;
            }

            while(this.rotationPitch - this.prevRotationPitch >= 180.0F) {
                this.prevRotationPitch += 360.0F;
            }

            while(this.rotationYaw - this.prevRotationYaw < -180.0F) {
                this.prevRotationYaw -= 360.0F;
            }

            while(this.rotationYaw - this.prevRotationYaw >= 180.0F) {
                this.prevRotationYaw += 360.0F;
            }

            this.rotationPitch = MathHelper.lerp(0.2F, this.prevRotationPitch, this.rotationPitch);
            this.rotationYaw = MathHelper.lerp(0.2F, this.prevRotationYaw, this.rotationYaw);
            float motionScale = 0.99F;
            if (this.isInWater()) {
                for(int j = 0; j < 4; ++j) {
                    this.world.addParticle(ParticleTypes.BUBBLE,
                            nextX - motionVec.getX() * 0.25D, nextY - motionVec.getY() * 0.25D, nextZ - motionVec.getZ() * 0.25D,
                            motionVec.getX(), motionVec.getY(), motionVec.getZ());
                }

                motionScale = this.getWaterDrag();
            }
            for (int i = 0; i < 5; i++) {
                this.world.addParticle(new ItemParticleData(ParticleTypes.ITEM, new ItemStack(NoitaItems.SPELL_SPARK_BOLT)), this.getPosXRandom(0.5D), this.getPosYRandom(), this.getPosZRandom(0.5D), 0, 0, 0);
            }

            motionVec = motionVec.scale(motionScale);
            this.setMotion(motionVec.x, motionVec.getY() - gravity, motionVec.getZ());

            this.setPosition(nextX, nextY, nextZ);
            this.doBlockCollisions();
        }
    }

    private void onHit(RayTraceResult rayTraceResult) {
        if (rayTraceResult.getType() == RayTraceResult.Type.ENTITY) {
            Entity entityHit = ((EntityRayTraceResult) rayTraceResult).getEntity();
            entityHit.attackEntityFrom(DamageSource.causeIndirectDamage(this, caster), 3.0f);
            entityHit.hurtResistantTime = 0;
        }
        this.remove();
    }

    private float getWaterDrag() {
        return 0.6f;
    }

    @Nullable
    protected EntityRayTraceResult rayTraceEntities(Vec3d startVec, Vec3d endVec) {
        return ProjectileHelper.rayTraceEntities(this.world, this, startVec, endVec,
                this.getBoundingBox().expand(this.getMotion()).grow(1.0D),
                (entity) -> !entity.isSpectator() && entity.isAlive() && entity.canBeCollidedWith() && (entity != this.caster));
    }

    @Override
    protected void registerData() {

    }

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
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
