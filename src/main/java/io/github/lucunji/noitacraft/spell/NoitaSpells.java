package io.github.lucunji.noitacraft.spell;

import io.github.lucunji.noitacraft.entity.NoitaEntityTypes;
import io.github.lucunji.noitacraft.entity.projectile.BombProjectileEntity;
import io.github.lucunji.noitacraft.entity.projectile.SparkProjectileEntity;

public class NoitaSpells {
    public static final ProjectileSpell ENERGY_SPHERE = new ProjectileSpell(
            20,
            3,
            new DamageCollection(0.0f, 0.0f, 10.0f, 0.0f),
            2,
            0.6f,
            400,
            500,
            (world, playerEntity) -> new SparkProjectileEntity(NoitaEntityTypes.PROJECTILE_SPARK, playerEntity, world));
    public static final ProjectileSpell SPARK_BOLT = new ProjectileSpell(
            -1,
            5,
            1,
            0,
            new DamageCollection(3.0f, 0.0f, 0.0f, 0.0f),
            2,
            0.0f,
            750,
            850,
            -1.0f,
            0.05f,
            (world, playerEntity) -> new SparkProjectileEntity(NoitaEntityTypes.PROJECTILE_SPARK, playerEntity, world));
    public static final ProjectileSpell BOMB = new ProjectileSpell(
            3,
            25,
            33,
            0,
            new DamageCollection(0.0f, 500.0f, 0.0f, 0.0f),
            60,
            0.0f,
            0,
            0,
            0.0f,
            0.0f,
            (world, playerEntity) -> new BombProjectileEntity(NoitaEntityTypes.PROJECTILE_BOMB, playerEntity, world));
}

