import com.google.common.collect.Lists;
import io.github.lucunji.noitacraft.item.wand.CastHelper;
import io.github.lucunji.noitacraft.spell.ISpellEnum;
import io.github.lucunji.noitacraft.spell.ModifierSpell;
import io.github.lucunji.noitacraft.spell.ProjectileSpell;
import io.github.lucunji.noitacraft.spell.iterator.SpellPoolIterator;
import javafx.util.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class MyTest {
    @Test
    public void testSpellPoolIterator() {
        List<ISpellEnum> spellList = Lists.newArrayList(ProjectileSpell.SPARK_BOLT, null, ProjectileSpell.SPARK_BOLT, null, null, ProjectileSpell.SPARK_BOLT_TRIGGER_DOUBLE, ProjectileSpell.SPARK_BOLT_TRIGGER, ProjectileSpell.BOMB);
        SpellPoolIteratorExample iteratorExample = new SpellPoolIteratorExample(spellList);
        for (int i = 0; i < spellList.size() * 2; i++) {
            ISpellEnum spellExpected = spellList.get(i % spellList.size());
            if (spellExpected != null) {
                ISpellEnum spell = iteratorExample.next();
                Assertions.assertSame(spellExpected, spell);
            }
        }
        
        spellList = Lists.newArrayList();
        iteratorExample = new SpellPoolIteratorExample(spellList);
        for (int i = 0; i < spellList.size() * 2; i++) {
            ISpellEnum spellExpected = spellList.get(i % spellList.size());
            if (spellExpected != null) {
                ISpellEnum spell = iteratorExample.next();
                Assertions.assertSame(spellExpected, spell);
            }
        }
        
        spellList = Lists.newArrayList((ISpellEnum)null);
        iteratorExample = new SpellPoolIteratorExample(spellList);
        for (int i = 0; i < spellList.size() * 2; i++) {
            ISpellEnum spellExpected = spellList.get(i % spellList.size());
            if (spellExpected != null) {
                ISpellEnum spell = iteratorExample.next();
                Assertions.assertSame(spellExpected, spell);
            }
        }
    }

    @Test
    public void testCastHelper() {
        List<ISpellEnum> spellList = Lists.newArrayList(
                null,
                ProjectileSpell.SPARK_BOLT_TRIGGER_DOUBLE, 
                null,
                ProjectileSpell.SPARK_BOLT_TRIGGER, 
                ProjectileSpell.BOMB, 
                null,
                null,
                ProjectileSpell.SPARK_BOLT,
                ProjectileSpell.SPARK_BOLT,
                null);
        SpellPoolIteratorExample iteratorExample = new SpellPoolIteratorExample(spellList);
        CastHelper.CastResult castResult = CastHelper.processSpellList(iteratorExample);
        int expectedManaDrain = ProjectileSpell.SPARK_BOLT_TRIGGER_DOUBLE.getManaDrain() + ProjectileSpell.SPARK_BOLT_TRIGGER.getManaDrain() + ProjectileSpell.BOMB.getManaDrain() + ProjectileSpell.SPARK_BOLT.getManaDrain() ;
        int expectedCastDelay = ProjectileSpell.SPARK_BOLT_TRIGGER_DOUBLE.getCastDelay();
        int expectedRechargeTime = ProjectileSpell.SPARK_BOLT_TRIGGER_DOUBLE.getRechargeTime() + ProjectileSpell.SPARK_BOLT_TRIGGER.getRechargeTime() + ProjectileSpell.BOMB.getRechargeTime() + ProjectileSpell.SPARK_BOLT.getRechargeTime() ;
        List<Pair<ProjectileSpell, List<ISpellEnum>>> expectedSpellListOut = Lists.newArrayList(new Pair<>(ProjectileSpell.SPARK_BOLT_TRIGGER_DOUBLE, Lists.newArrayList(ProjectileSpell.SPARK_BOLT_TRIGGER, ProjectileSpell.BOMB, ProjectileSpell.SPARK_BOLT)));
        List<ModifierSpell> expectedModifiers = Lists.newArrayList();
        Assertions.assertEquals(expectedManaDrain, castResult.getManaDrain());
        Assertions.assertEquals(expectedCastDelay, castResult.getCastDelay());
        Assertions.assertEquals(expectedRechargeTime, castResult.getRechargeTime());
        Assertions.assertEquals(expectedSpellListOut, castResult.getSpell2TriggeredSpellList());
        Assertions.assertEquals(expectedModifiers, castResult.getModifiers());
    }

    private static class SpellPoolIteratorExample extends SpellPoolIterator {
        private final List<ISpellEnum> spellList;
        private boolean reset;
        private int nextIndex;

        public SpellPoolIteratorExample(List<ISpellEnum> spellList) {
            this.spellList = spellList;
            reset = false;
            nextIndex = -1;
            updateNextIndex();
        }

        @Override
        public void reset() {
            //remove this
        }

        @Override
        public int getResetCount() {
            return reset ? 1 : 0;
            //change this to isResetDone
        }

        @Override
        public boolean hasNext() {
            return !reset || nextIndex < spellList.size();
        }

        @Override
        public ISpellEnum next() {
            ISpellEnum spell = spellList.get(nextIndex);
            updateNextIndex();
            return spell;
        }

        private void updateNextIndex() {
            do {
                nextIndex++;
                if (nextIndex >= spellList.size() && !reset) {
                    nextIndex = 0;
                    reset = true;
                }
            } while (nextIndex < spellList.size() && spellList.get(nextIndex) == null);
        }
    }
}
