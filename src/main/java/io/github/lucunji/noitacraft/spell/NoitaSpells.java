package io.github.lucunji.noitacraft.spell;

public class NoitaSpells {
    public static final ProjectileSpell ENERGY_SPHERE = new ProjectileSpell(20, (int)(0.17*20), new DamageCollection(0.0f, 0.0f, 10.0f, 0.0f), 2, 0.6f, 400, 500);
    public static final ProjectileSpell SPARK_BOLT = new ProjectileSpell(-1, 5, (int)(0.05*20), 0, new DamageCollection(3.0f, 0.0f, 0.0f, 0.0f), 2, 0.0f, 750, 850, -1.0f, 0.05f);
    public static final ProjectileSpell BOMB = new ProjectileSpell(3, 25, (int)(1.67*20), 0, new DamageCollection(0.0f, 500.0f, 0.0f, 0.0f), 60, 0.0f, 0, 0, 0.0f, 0.0f);
}
