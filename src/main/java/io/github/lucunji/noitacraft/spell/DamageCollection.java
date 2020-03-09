package io.github.lucunji.noitacraft.spell;

public class DamageCollection {
    protected final float damageImpact;
    protected final float damageExplosion;
    protected final float damageSlice;
    protected final float damageElectricity;

    public DamageCollection(float damageImpact, float damageExplosion, float damageSlice, float damageElectricity) {
        this.damageImpact = damageImpact;
        this.damageExplosion = damageExplosion;
        this.damageSlice = damageSlice;
        this.damageElectricity = damageElectricity;
    }
}
