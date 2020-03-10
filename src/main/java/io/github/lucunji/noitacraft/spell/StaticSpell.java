package io.github.lucunji.noitacraft.spell;

public class StaticSpell extends SpellBase {
    public final int uses;

    public StaticSpell(int manaDrain, int castDelay) {
        this(-1, manaDrain, castDelay, 0);
    }

    public StaticSpell(int uses, int manaDrain, int castDelay, int rechargeTime) {
        super(manaDrain, castDelay, rechargeTime);
        this.uses = uses;
    }
}
