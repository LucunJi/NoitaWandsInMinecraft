package io.github.lucunji.noitacraft.entity.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IProjectile;
import net.minecraft.world.World;

public abstract class SpellProjectileEntityBase extends Entity implements IProjectile {
    public SpellProjectileEntityBase(EntityType<?> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
    }
    public abstract void shoot(Entity shooter, float pitch, float yaw, float velocity, float inaccuracy);
}
