package io.github.lucunji.noitacraft.entity.spell;

import io.github.lucunji.noitacraft.spell.ISpellEnum;
import io.github.lucunji.noitacraft.spell.ProjectileSpell;
import io.github.lucunji.noitacraft.spell.cast.CastHelper;
import io.github.lucunji.noitacraft.spell.cast.TriggeredSpellPoolVisitor;
import io.github.lucunji.noitacraft.util.MathHelper;
import io.github.lucunji.noitacraft.util.NBTHelper;
import io.github.lucunji.noitacraft.util.NBTHelper.NBTTypes;
import javafx.util.Pair;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Direction;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.*;

public abstract class SpellEntityBase extends Entity implements IProjectile {
    protected UUID casterUUID;

    protected boolean inGround;
    protected int age;

    protected List<ISpellEnum> castList;

    protected boolean hasTrigger;
    protected boolean hasTimer;

    public SpellEntityBase(EntityType<?> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
        this.casterUUID = null;
        this.inGround = false;
        this.age = 0;
        this.castList = new ArrayList<>();
        this.hasTrigger = false;
        this.hasTimer = false;
    }


    @Override
    protected void readAdditional(CompoundNBT compound) {
        this.casterUUID = compound.getUniqueId("Caster");
        this.inGround = compound.getBoolean("inGround");
        this.age = compound.getInt("Age");

        this.hasTrigger = NBTHelper.getBoolean(compound, "HasTrigger").orElse(true);
        this.hasTimer = NBTHelper.getBoolean(compound, "HasTimer").orElse(true);
        this.castList = NBTHelper.spellListFromNBT(compound.getList("CastList", NBTTypes.STRING.ordinal()));
    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {
        if (this.casterUUID != null) compound.putUniqueId("Caster", casterUUID);
        compound.putBoolean("inGround", inGround);
        compound.putInt("Age", this.age);

        compound.putBoolean("HasTrigger", this.hasTrigger);
        compound.putBoolean("HasTimer", this.hasTimer);

        if (this.castList != null && !this.castList.isEmpty() && (this.hasTrigger || this.hasTimer)) {
            compound.put("CastList", NBTHelper.spellNBTFromList(castList));
        }
    }

    @Override
    protected void registerData() {}

    @Override
    public void setFire(int seconds) {}

    public abstract void shoot(Entity shooter, float pitch, float yaw, float velocity, float inaccuracy);

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void tick() {
        super.tick();
        ++this.age;
        if (this.age > this.getExpireAge()) {
            this.onAgeExpire();
        } else if (this.hasTimer && this.age > this.getAgeToCast()) {
            this.castSpellTimer();
            this.hasTimer = false;
        }

        this.generateParticles();
    }

    protected void castSpellTimer() {
        if (this.world.isRemote()) return;
        castSpell(this.getMotion());
    }

    protected void castSpellTrigger(RayTraceResult traceResult) {
        if (this.world.isRemote()) return;
        if (this.castList == null || this.castList.isEmpty()) return;
        Vec3d reflectedVec;
        if (traceResult.getType() == RayTraceResult.Type.ENTITY) {
            reflectedVec = this.getMotion().scale(-1);
            AxisAlignedBB box = ((EntityRayTraceResult) traceResult).getEntity().getBoundingBox().grow(0.3f);
            Optional<Vec3d> hitPosOptional = box.rayTrace(this.getPositionVec(), this.getPositionVec().add(this.getMotion()));
            if (hitPosOptional.isPresent()) {
                Vec3d hitPos = hitPosOptional.get();
                Direction.Axis axis;
                if (hitPos.getX() == box.minX || hitPos.getX() == box.maxX) {
                    axis = Direction.Axis.X;
                } else if (hitPos.getY() == box.minY || hitPos.getY() == box.maxY) {
                    axis = Direction.Axis.Y;
                } else {
                    axis = Direction.Axis.Z;
                }
                reflectedVec = MathHelper.reflectByAxis(this.getMotion(), axis);
            }
        } else if (traceResult.getType() == RayTraceResult.Type.BLOCK) {
            reflectedVec = MathHelper.reflectByAxis(this.getMotion(), ((BlockRayTraceResult) traceResult).getFace().getAxis());
        } else {
            return;
        }

        castSpell(reflectedVec);
    }

    private void castSpell(Vec3d castVec) {
        TriggeredSpellPoolVisitor visitor = new TriggeredSpellPoolVisitor(this.castList);
        CastHelper.CastResult castResult = CastHelper.processSpellList(visitor);
        for (Pair<ProjectileSpell, List<ISpellEnum>> pair : castResult.getSpell2TriggeredSpellList()) {
            ProjectileSpell spell = pair.getKey();
            SpellEntityBase spellEntity = spell.entitySummoner().apply(world, this.getCaster());
            spellEntity.setCastList(pair.getValue());

            float speed = 0;
            int speedMin = spell.getSpeedMin();
            int speedMax = spell.getSpeedMax();
            if (speedMin < speedMax)
                speed = new Random().nextInt(speedMax - speedMin) + speedMin;
            speed += 200f;
            speed /= 600f;

            spellEntity.setPosition(this.getPosX(), this.getPosY(), this.getPosZ());
            spellEntity.shoot(castVec.getX(), castVec.getY(), castVec.getZ(), speed, 1.0f);
            world.addEntity(spellEntity);
        }
    }

    public final SpellEntityBase hasTrigger() {
        this.hasTrigger = true;
        return this;
    }

    public final SpellEntityBase hasTimer() {
        this.hasTimer = true;
        return this;
    }

    protected int getExpireAge() {
        return 13;
    }

    protected void onAgeExpire() {
        this.remove();
    }

    protected float getWaterDrag() {
        return 0.6f;
    }

    protected abstract float getGravity();

    protected float getAirDrag() {
        return 0.99f;
    }

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

    @Nullable
    public LivingEntity getCaster() {
        if (this.casterUUID == null) return null;
        return world.getPlayerByUuid(casterUUID);
    }

    protected int getAgeToCast() {
        return 13;
    }

    public void setCastList(List<ISpellEnum> castList) {
        this.castList = castList;
    }

    protected void onHit(RayTraceResult traceResult) {
        if (this.hasTrigger || this.hasTimer) {
            this.castSpellTrigger(traceResult);
            this.hasTrigger = false;
            this.hasTimer = false;
        }
    }
}
