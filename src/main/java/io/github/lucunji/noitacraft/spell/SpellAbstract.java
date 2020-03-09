package io.github.lucunji.noitacraft.spell;

public abstract class SpellAbstract {
    public final int manaDrain;
    public final int castDelay;
    public final int rechargeTime;

    public SpellAbstract(int manaDrain, int castDelay, int rechargeTime) {
        this.manaDrain = manaDrain;
        this.castDelay = castDelay;
        this.rechargeTime = rechargeTime;
    }
}
