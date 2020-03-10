package io.github.lucunji.noitacraft.spell;

import io.github.lucunji.noitacraft.entity.projectile.SpellProjectileEntityBase;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

import java.util.function.BiFunction;
import java.util.function.Supplier;

public class ProjectileSpell extends StaticSpell {
    public final DamageCollection damageCollection;
    public final int radius;
    public final float spread;
    public final int speedMin;
    public final int speedMax;
    public final float spreadModifier;
    public final float criticalChance;
    public final BiFunction<World, PlayerEntity, SpellProjectileEntityBase> entitySummoner;

    public ProjectileSpell(int manaDrain, int castDelay, DamageCollection damageCollection, int radius, float spread, int speedMin, int speedMax, BiFunction<World, PlayerEntity, SpellProjectileEntityBase> entitySummoner) {
        super(manaDrain, castDelay);
        this.damageCollection = damageCollection;
        this.radius = radius;
        this.spread = spread;
        this.speedMin = speedMin;
        this.speedMax = speedMax;
        this.entitySummoner = entitySummoner;
        this.spreadModifier = 0.0f;
        this.criticalChance = 0.0f;
    }

    public ProjectileSpell(int uses, int manaDrain, int castDelay, int rechargeTime, DamageCollection damageCollection, int radius, float spread, int speedMin, int speedMax, float spreadModifier, float criticalChance, BiFunction<World, PlayerEntity, SpellProjectileEntityBase> entitySummoner) {
        super(uses, manaDrain, castDelay, rechargeTime);
        this.damageCollection = damageCollection;
        this.radius = radius;
        this.spread = spread;
        this.speedMin = speedMin;
        this.speedMax = speedMax;
        this.spreadModifier = spreadModifier;
        this.criticalChance = criticalChance;
        this.entitySummoner = entitySummoner;
    }
}
