package io.github.lucunji.noitacraft.spell;

/**
 * Stores data about how the spells affect wand.
 */
public abstract class SpellBase {
    public final int manaDrain;
    public final int castDelay;
    public final int rechargeTime;
    public final int uses;
    public final SpellTypes spellType;

    public SpellBase(int manaDrain, int castDelay, int rechargeTime, int uses, SpellTypes spellType) {
        this.manaDrain = manaDrain;
        this.castDelay = castDelay;
        this.rechargeTime = rechargeTime;
        this.uses = uses;
        this.spellType = spellType;
    }
}
