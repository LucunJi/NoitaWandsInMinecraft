package io.github.lucunji.noitacraft.item.wand;

import com.google.common.collect.Lists;
import io.github.lucunji.noitacraft.spell.ISpellEnum;
import io.github.lucunji.noitacraft.spell.ModifierSpell;
import io.github.lucunji.noitacraft.spell.ProjectileSpell;
import io.github.lucunji.noitacraft.spell.iterator.SpellPoolIterator;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class CastHelper {

    /**
     * We construct a "spell tree" with the following rules:
     * All the nodes are projectiles(static/non-static); all the projectiles are nodes.
     * Only projectiles with triggers and timers can have children nodes.
     * Multicasts nesting in multicasts and triggers expand the number of children, rather than forming a new layer.
     * A modifier "belong" to the projectiles "directly" after it.
     *  "Belong" means the modifier merges into the owner projectile since it cannot be a node alone.
     *  "Directly" means the there are no other projectiles between the modifer and its owner projectile.
     * Besides its owner projectile, a modifier also has effects on the peer nodes of its owner.
     */

    /**
     * Only separates out nodes on the first layer of spell tree, with corresponding children and modifiers.
     */
    public static CastResult processSpellList(SpellPoolIterator iterator) {
        CastResult castResult = new CastResult();

        int spellCount = 1;
        while (spellCount > 0 && iterator.hasNext()) {
            ISpellEnum spell = iterator.next();
            castResult.manaDrain += spell.getManaDrain();
            castResult.castDelay += spell.getCastDelay();
            castResult.rechargeTime += spell.getRechargeTime();
            if (!(spell instanceof ModifierSpell)) {
                spellCount--;
            }

            if (spell instanceof ProjectileSpell) {
                List<ISpellEnum> triggeredList = new ArrayList<>();
                Pair<ProjectileSpell, List<ISpellEnum>> spell2TriggeredSpells = new Pair<>((ProjectileSpell)spell, triggeredList);
                castResult.spell2TriggeredSpellList.add(spell2TriggeredSpells);
                if (spell.getCastNumber() > 0) {
                    gatherTriggeredSpells(spell.getCastNumber(), iterator, triggeredList, castResult);
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
    private static void gatherTriggeredSpells(int triggeredSpellCount, SpellPoolIterator iterator, List<ISpellEnum> triggeredSpellList, CastResult castResult) {
        while (triggeredSpellCount > 0 && iterator.hasNext()) {
            ISpellEnum triggeredSpell = iterator.next();
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
