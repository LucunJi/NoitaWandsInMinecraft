package io.github.lucunji.noitacraft.spell;

public interface ISpellEnum {
    SpellType getSpellType();

    int getManaDrain();

    int getCastDelay();

    int getRechargeTime();

    float getSpread();

    float getSpreadModifier();

    default boolean isInfinite() {
        return this.getUses() < 0;
    }

    int getUses();

    int getCastNumber();

    String name();
}
