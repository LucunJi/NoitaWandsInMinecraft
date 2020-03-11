package io.github.lucunji.noitacraft.entity.spell;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.*;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class BombSpellEntity extends SpellEntityBase {

    public BombSpellEntity(EntityType<BombSpellEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public BombSpellEntity(EntityType<BombSpellEntity> type, LivingEntity caster, World world) {
        this(type, world);
        this.setPosition(caster.getPosX(), caster.getPosYEye() - 0.1, caster.getPosZ());
        this.casterUUID = caster.getUniqueID();
        this.caster = caster;
    }

    @Override
    public void shoot(Entity shooter, float pitch, float yaw, float velocity, float inaccuracy) {
        float x = -MathHelper.sin(yaw * ((float)Math.PI / 180F)) * MathHelper.cos(pitch * ((float)Math.PI / 180F));
        float y = -MathHelper.sin(pitch * ((float)Math.PI / 180F));
        float z = MathHelper.cos(yaw * ((float)Math.PI / 180F)) * MathHelper.cos(pitch * ((float)Math.PI / 180F));
        this.shoot(x, y, z, velocity, inaccuracy);
        this.setMotion(this.getMotion().add(shooter.getMotion().x, shooter.onGround ? 0.0D : shooter.getMotion().y, shooter.getMotion().z));
    }

    @Override
    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        Vec3d vec3d = (new Vec3d(x, y, z)).normalize().add(this.rand.nextGaussian() * (double)0.0075F * (double)inaccuracy, this.rand.nextGaussian() * (double)0.0075F * (double)inaccuracy, this.rand.nextGaussian() * (double)0.0075F * (double)inaccuracy).scale(velocity);
        this.setMotion(vec3d);
        float f = MathHelper.sqrt(horizontalMag(vec3d));
        this.rotationYaw = (float)(MathHelper.atan2(vec3d.x, vec3d.z) * (double)(180F / (float)Math.PI));
        this.rotationPitch = (float)(MathHelper.atan2(vec3d.y, f) * (double)(180F / (float)Math.PI));
        this.prevRotationYaw = this.rotationYaw;
        this.prevRotationPitch = this.rotationPitch;
    }


    public void tick() {
        super.tick();

        this.prevPosX = this.getPosX();
        this.prevPosY = this.getPosY();
        this.prevPosZ = this.getPosZ();
        this.setMotion(this.getMotion().add(0.0D, -0.04D, 0.0D));

        if (this.world.isRemote) {
            this.noClip = false;
        } else {
            this.noClip = !this.world.func_226669_j_(this);
            if (this.noClip) {
                this.pushOutOfBlocks(this.getPosX(), (this.getBoundingBox().minY + this.getBoundingBox().maxY) / 2.0D, this.getPosZ());
            }
        }

        if (!this.onGround || horizontalMag(this.getMotion()) > (double)1.0E-5F || (this.ticksExisted + this.getEntityId()) % 4 == 0) {
            this.move(MoverType.SELF, this.getMotion());
            float horizontalFactor = 0.98F;
            if (this.onGround) {
                BlockPos pos = new BlockPos(this.getPosX(), this.getPosY() - 1.0D, this.getPosZ());
                horizontalFactor = this.world.getBlockState(pos).getSlipperiness(this.world, pos, this) * 0.98F;
            }

            this.setMotion(this.getMotion().mul(horizontalFactor, 0.98D, horizontalFactor));
            if (this.onGround) {
                this.setMotion(this.getMotion().mul(1.0D, -0.5D, 1.0D));
            }
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

        Vec3d motionVec = this.getMotion();
        if (this.isInWater()) {
            this.setMotion(motionVec.scale(this.getWaterDrag()));
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (!this.world.isRemote()) {
            if (source.isExplosion()) {
                this.age = this.getExpireAge() - 2;
                return false;
            }
        }
        return super.attackEntityFrom(source, amount);
    }
    @Override
    protected float getWaterDrag() {
        return 0.6f;
    }

    @Override
    protected float getGravity() {
        return 0;
    }

    @Override
    protected int getExpireAge() {
        return 80;
    }

    @Override
    protected void onAgeExpire() {
        if (!this.world.isRemote()) {
            this.world.createExplosion(this, DamageSource.causeExplosionDamage(this.caster), this.getPosX(), this.getPosY(), this.getPosZ(), 3, true, Explosion.Mode.BREAK);
            this.remove();
        }
    }

    @Override
    protected void generateParticles() {
        super.generateParticles();
        this.world.addParticle(ParticleTypes.SMOKE, this.getPosXRandom(0.5D), this.getPosYRandom(), this.getPosZRandom(0.5D), 0, 0, 0);
    }
}
