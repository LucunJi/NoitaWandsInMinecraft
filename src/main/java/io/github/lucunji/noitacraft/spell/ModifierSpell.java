package io.github.lucunji.noitacraft.spell;

public class ModifierSpell extends SpellBase {
    public ModifierSpell(int manaDrain, int castDelay, int rechargeTime, int uses) {
        super(manaDrain, castDelay, rechargeTime, uses, SpellTypes.MODIFIER);
    }
}
