package io.github.lucunji.noitacraft.spell;

import io.github.lucunji.noitacraft.entity.spell.SpellEntityBase;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;

/**
 * Different types of damage source
 * Impact: projectile impact damage
 */
public class DamageCollection {
    private final float damageImpact;
    private final float damageExplosion;
    private final float damageSlice;
    private final float damageElectricity;

    public DamageCollection(float damageImpact, float damageExplosion, float damageSlice, float damageElectricity) {
        this.damageImpact = damageImpact;
        this.damageExplosion = damageExplosion;
        this.damageSlice = damageSlice;
        this.damageElectricity = damageElectricity;
    }

    public void causeDamage(SpellEntityBase spellEntity, Entity targetEntity) {
        if (damageImpact > 0) {
            targetEntity.hurtResistantTime = 0;
            targetEntity.attackEntityFrom(DamageSource.causeThrownDamage(spellEntity, spellEntity.getCaster()), damageImpact);
        }
        if (damageExplosion > 0) {
            targetEntity.hurtResistantTime = 0;
            targetEntity.attackEntityFrom(DamageSource.causeExplosionDamage(spellEntity.getCaster()), damageExplosion);
        }
        if (damageSlice > 0) {
            targetEntity.hurtResistantTime = 0;
            targetEntity.attackEntityFrom(DamageSource.causeIndirectDamage(spellEntity, spellEntity.getCaster()).setDamageBypassesArmor(), damageSlice);
        }
        if (damageElectricity > 0) {
            targetEntity.hurtResistantTime = 0;
            targetEntity.attackEntityFrom(DamageSource.causeIndirectMagicDamage(spellEntity, spellEntity.getCaster()), damageElectricity);
        }
    }
}
