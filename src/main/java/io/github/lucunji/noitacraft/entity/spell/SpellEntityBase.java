package io.github.lucunji.noitacraft.entity.spell;

import io.github.lucunji.noitacraft.spell.ISpellEnum;
import io.github.lucunji.noitacraft.spell.ProjectileSpell;
import io.github.lucunji.noitacraft.spell.SpellTree;
import io.github.lucunji.noitacraft.spell.iterator.ProjectileSpellPoolIterator;
import io.github.lucunji.noitacraft.util.CastHelper;
import io.github.lucunji.noitacraft.util.MathHelper;
import io.github.lucunji.noitacraft.util.NBTHelper;
import io.github.lucunji.noitacraft.util.NBTHelper.NBTTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class SpellEntityBase extends Entity implements IProjectile {
    protected UUID casterUUID;
    protected LivingEntity caster;

    protected boolean inGround;
    protected int age;

    protected List<ISpellEnum> castList;
    protected boolean casted;

    public SpellEntityBase(EntityType<?> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
        this.age = 0;
        this.castList = new ArrayList<>();
        this.casted = false;
    }


    @Override
    protected void readAdditional(CompoundNBT compound) {
        if (compound.contains("Caster")) {
            this.casterUUID = compound.getUniqueId("Caster");
            if (!this.world.isRemote() && this.casterUUID != null) {
                ((ServerWorld) this.world).getEntityByUuid(this.casterUUID);
            }
        }
        this.inGround = compound.getBoolean("inGround");
        this.age = compound.getInt("Age");

        this.casted = NBTHelper.getBoolean(compound, "Casted").orElse(true);
        this.castList = CastHelper.spellListFromNBT(compound.getList("CastList", NBTTypes.STRING.ordinal()));
    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {
        if (this.casterUUID != null) compound.putUniqueId("Caster", casterUUID);
        compound.putBoolean("inGround", inGround);
        compound.putInt("Age", this.age);

        if (this.castList != null && !this.castList.isEmpty()) {
            compound.putBoolean("Casted", this.casted);
            compound.put("CastList", CastHelper.spellNBTFromList(castList));
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
        if (this.age > this.getExpireAge()) onAgeExpire();
//        if (!this.casted && this.age > this.getAgeToCast()) {
//            this.casted = true;
//            this.castSpell();
//        }

        this.generateParticles();
    }

    protected void castSpellOnHit(RayTraceResult traceResult) {
        if (this.world.isRemote()) return;
        if (this.castList == null || this.castList.isEmpty()) return;
        Vec3d reflectedVec;
        if (traceResult.getType() == RayTraceResult.Type.ENTITY) {
            reflectedVec = this.getMotion().scale(-1);
        } else if (traceResult.getType() == RayTraceResult.Type.BLOCK) {
            reflectedVec = MathHelper.reflectByAxis(this.getMotion(), ((BlockRayTraceResult) traceResult).getFace().getAxis());
        } else {
            return;
        }

        ProjectileSpellPoolIterator iterator = new ProjectileSpellPoolIterator(castList);
        while (iterator.hasNext()) {
            SpellTree spellTree = new SpellTree(iterator);
            spellTree.flatten().forEach((iSpellEnumListPair -> {
                ISpellEnum iSpellEnum = iSpellEnumListPair.getFirst();
                List<ISpellEnum> spellEnumList = iSpellEnumListPair.getSecond();

                if (iSpellEnum instanceof ProjectileSpell) {
                    ProjectileSpell projectileSpell = (ProjectileSpell) iSpellEnum;
                    SpellEntityBase entityBase = projectileSpell.entitySummoner().apply(world, (PlayerEntity)this.caster);

                    float speed = 0;
                    int speedMin = projectileSpell.getSpeedMin();
                    int speedMax = projectileSpell.getSpeedMax();
                    if (speedMin < speedMax)
                        speed = caster.getRNG().nextInt(speedMax - speedMin) + speedMin;
                    speed += 200f;
                    speed /= 600f;

                    entityBase.shoot(reflectedVec.getX(), reflectedVec.getY(), reflectedVec.getZ(), speed, 1.0f);
                    entityBase.setPosition(this.getPosX(), this.getPosY(), this.getPosZ());
                    entityBase.setCastList(spellEnumList);
                    this.world.addEntity(entityBase);
                }
            }));
        }
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

    public LivingEntity getCaster() {
        return caster;
    }

//    protected int getAgeToCast() {
//        return Integer.MAX_VALUE;
//    }

    public void setCastList(List<ISpellEnum> castList) {
        this.castList = castList;
    }

    protected void onHit(RayTraceResult traceResult) {
        if (!this.casted) {
            this.castSpellOnHit(traceResult);
        }
    }
}
