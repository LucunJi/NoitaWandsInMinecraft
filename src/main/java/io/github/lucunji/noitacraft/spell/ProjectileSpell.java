package io.github.lucunji.noitacraft.spell;

import io.github.lucunji.noitacraft.entity.NoitaEntityTypes;
import io.github.lucunji.noitacraft.entity.spell.BombSpellEntity;
import io.github.lucunji.noitacraft.entity.spell.EnergySphereSpellEntity;
import io.github.lucunji.noitacraft.entity.spell.SparkBoltSpellEntity;
import io.github.lucunji.noitacraft.entity.spell.SpellEntityBase;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

import java.util.function.BiFunction;

public enum ProjectileSpell implements ISpellEnum {
    SPARK_BOLT(
            SpellType.PROJECTILE_MAGICAL,
            5,
            new DamageCollection(3.0f, 0, 2, 0, 0),
            750,
            850,
            1,
            0,
            0,
            -1,
            0.05f,
            -1,
            (world, playerEntity) -> new SparkBoltSpellEntity(NoitaEntityTypes.SPELL_SPARK_BOLT, playerEntity, world)
    ),
    SPARK_BOLT_TRIGGER(
            SPARK_BOLT,
            1
    ),
    SPARK_BOLT_TRIGGER_DOUBLE(
            SPARK_BOLT,
            2
    ),
    ENERGY_SPHERE(
            SpellType.PROJECTILE_MAGICAL,
            20,
            new DamageCollection(0, 0, 2, 10, 0),
            400,
            500,
            3,
            0,
            0.6f,
            0,
            0,
            -1,
            (world, playerEntity) -> new EnergySphereSpellEntity(NoitaEntityTypes.SPELL_ENERGY_SPHERE, playerEntity, world)
    ),
    BOMB(
            SpellType.PROJECTILE_MAGICAL,
            25,
            new DamageCollection(0, 0, 2, 10, 0),
            0,
            0,
            33,
            0,
            0,
            0,
            0,
            3,
            (world, playerEntity) -> new BombSpellEntity(NoitaEntityTypes.SPELL_BOMB, playerEntity, world)
    );

    private final SpellType spellType;

    private final int manaDrain;
    private final DamageCollection damageCollection;
    private final int speedMin;
    private final int speedMax;

    private final int castDelay;
    private final int rechargeTime;
    private final float spread;
    private final float spreadModifier;
    private final float criticalChance;

    private final int uses;

    private final BiFunction<World, PlayerEntity, SpellEntityBase> entitySummoner;

    private int castNumber;

    ProjectileSpell(ProjectileSpell spellBase, int castNumber) {
        this(spellBase.spellType, spellBase.manaDrain, spellBase.damageCollection, spellBase.speedMin, spellBase.speedMax, spellBase.castDelay, spellBase.rechargeTime, spellBase.spread, spellBase.spreadModifier, spellBase.criticalChance, spellBase.uses, spellBase.entitySummoner);
        this.castNumber = castNumber;
    }

    ProjectileSpell(SpellType spellType, int manaDrain, DamageCollection damageCollection, int speedMin, int speedMax, int castDelay, int rechargeTime, float spread, float spreadModifier, float criticalChance, int uses, BiFunction<World, PlayerEntity, SpellEntityBase> entitySummoner) {
        this.spellType = spellType;
        this.manaDrain = manaDrain;
        this.damageCollection = damageCollection;
        this.speedMin = speedMin;
        this.speedMax = speedMax;
        this.castDelay = castDelay;
        this.rechargeTime = rechargeTime;
        this.spread = spread;
        this.spreadModifier = spreadModifier;
        this.criticalChance = criticalChance;
        this.uses = uses;
        this.entitySummoner = entitySummoner;
        SpellManager.SPELL_MAP.put(this.name(), this);
        this.castNumber = 0;
    }

    @Override
    public SpellType getSpellType() {
        return spellType;
    }

    @Override
    public int getManaDrain() {
        return manaDrain;
    }

    public DamageCollection damageCollection() {
        return damageCollection;
    }

    public int getSpeedMin() {
        return speedMin;
    }

    public int getSpeedMax() {
        return speedMax;
    }

    @Override
    public int getCastDelay() {
        return castDelay;
    }

    @Override
    public int getRechargeTime() {
        return rechargeTime;
    }

    @Override
    public float getSpread() {
        return spread;
    }

    @Override
    public float getSpreadModifier() {
        return spreadModifier;
    }

    @Override
    public int getUses() {
        return uses;
    }

    @Override
    public int getCastNumber() {
        return castNumber;
    }

    public BiFunction<World, PlayerEntity, SpellEntityBase> entitySummoner() {
        return entitySummoner;
    }


}
