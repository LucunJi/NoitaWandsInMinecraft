package io.github.lucunji.noitacraft.spell;

public interface IModifier {
    void onCast();

    void onTick();

    void onHit();

    void onDamage();
}
