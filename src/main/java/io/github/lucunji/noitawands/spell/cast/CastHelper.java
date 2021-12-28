package io.github.lucunji.noitawands.spell.cast;

import com.google.common.collect.Lists;
import io.github.lucunji.noitawands.spell.ISpellEnum;
import io.github.lucunji.noitawands.spell.ModifierSpell;
import io.github.lucunji.noitawands.spell.ProjectileSpell;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>This class handles the algorithm of translating a spell list into spells to cast.
 * It only builds the first layer of a "spell tree".
 * Any further layers are roughly separated respect to first layer nodes.
 *
 * <p>We construct a "spell tree" with the following rules:
 * All the nodes are projectiles(static/non-static); all the projectiles are nodes.
 * Only projectiles with triggers and timers can have children nodes.
 * Multicasts nesting in multicasts and triggers expand the number of children, rather than forming a new layer.
 * A modifier "belong" to the projectiles "directly" after it.
 *  "Belong" means the modifier merges into the owner projectile since it cannot be a node alone.
 *  "Directly" means the there are no other projectiles between the modifier and its owner projectile.
 * Besides its owner projectile, a modifier also has effects on the peer nodes of its owner.
 *
 * <p>The nature of a "Formation" multicast: it is also a modifier, making the spells evenly spread in a fixed interval
 *
 * <p>If the wand runs out of mana, it will skip all spells exceed its current mana storage.
 */
public class CastHelper {

    public static CastResult processSpellList(SpellPoolVisitor visitor) {
        return processSpellList(visitor, Float.MAX_VALUE);
    }

    /**
     * Only separates out nodes on the first layer of spell tree, with corresponding children and modifiers.
     */
    public static CastResult processSpellList(SpellPoolVisitor visitor, final float manaLimit) {
        LogManager.getLogger().debug("Start process spell list...");
        CastResult castResult = new CastResult();

        int spellCount = 1;
        while (spellCount > 0) {
            ISpellEnum spell = visitor.peek();
            LogManager.getLogger().debug("Loop, current spell: {}", spell == null ? "null" : spell.name());
            if (spell == null) {
                break;
            }
            if (spell.getManaDrain() + castResult.manaDrain > manaLimit) {
                visitor.pass();
                continue;
            } else {
                visitor.passAndConsume();
            }

            castResult.manaDrain += spell.getManaDrain();
            castResult.castDelay += spell.getCastDelay();
            castResult.rechargeTime += spell.getRechargeTime();
            if (!(spell instanceof ModifierSpell)) {
                spellCount--;
            }

            if (spell instanceof ProjectileSpell) {
                List<ISpellEnum> triggeredList = new ArrayList<>();
                Pair<ProjectileSpell, List<ISpellEnum>> spell2TriggeredSpells = Pair.of((ProjectileSpell)spell, triggeredList);
                castResult.spell2TriggeredSpellList.add(spell2TriggeredSpells);
                if (spell.getCastNumber() > 0) {
                    gatherTriggeredSpells(spell.getCastNumber(), visitor, manaLimit, triggeredList, castResult);
                }
            } else if (spell instanceof ModifierSpell) {
                castResult.modifiers.add((ModifierSpell)spell);
            }
        }

        return castResult;
    }

    /**
     * Only gathers triggered spells. Does not do any further process.
     */
    private static void gatherTriggeredSpells(int triggeredSpellCount, SpellPoolVisitor visitor, final float manaLimit, List<ISpellEnum> triggeredSpellList, CastResult castResult) {
        LogManager.getLogger().debug("Start gather triggered spells...");
        while (triggeredSpellCount > 0) {
            ISpellEnum triggeredSpell = visitor.peek();
            LogManager.getLogger().debug("Loop, current triggeredSpell: {}", triggeredSpell == null ? "null" : triggeredSpell.name());
            if (triggeredSpell == null) {
                break;
            }
            if (triggeredSpell.getManaDrain() + castResult.manaDrain > manaLimit) {
                visitor.pass();
                continue;
            } else {
                visitor.passAndConsume();
            }
            triggeredSpellList.add(triggeredSpell);

            castResult.manaDrain += triggeredSpell.getManaDrain();
            // Only spells directly casted by the wand increases castDelay
            castResult.rechargeTime += triggeredSpell.getRechargeTime();
            triggeredSpellCount += triggeredSpell.getCastNumber();
            if (!(triggeredSpell instanceof ModifierSpell)) {
                triggeredSpellCount--;
            }
        }
    }

    public static class CastResult {
        private int manaDrain;
        private int castDelay;
        private int rechargeTime;
        private List<Pair<ProjectileSpell, List<ISpellEnum>>> spell2TriggeredSpellList;
        private List<ModifierSpell> modifiers;

        private CastResult() {
            manaDrain = 0;
            castDelay = 0;
            rechargeTime = 0;
            spell2TriggeredSpellList = Lists.newArrayList();
            modifiers = Lists.newArrayList();
        }

        public int getManaDrain() {
            return manaDrain;
        }

        public int getCastDelay() {
            return castDelay;
        }

        public int getRechargeTime() {
            return rechargeTime;
        }

        public List<Pair<ProjectileSpell, List<ISpellEnum>>> getSpell2TriggeredSpellList() {
            return spell2TriggeredSpellList;
        }

        public List<ModifierSpell> getModifiers() {
            return modifiers;
        }
    }
}
