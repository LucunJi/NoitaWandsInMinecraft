package io.github.lucunji.noitawands.spell;

public interface IModifier {
    void onCast();

    void onTick();

    void onHit();

    void onDamage();
}
