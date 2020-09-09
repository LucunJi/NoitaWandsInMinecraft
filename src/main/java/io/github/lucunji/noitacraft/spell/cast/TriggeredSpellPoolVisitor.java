package io.github.lucunji.noitacraft.spell.cast;

import io.github.lucunji.noitacraft.spell.ISpellEnum;

import java.util.Iterator;
import java.util.List;

public class TriggeredSpellPoolVisitor extends SpellPoolVisitor {
    private final Iterator<ISpellEnum> iterator;
    private ISpellEnum spell;

    public TriggeredSpellPoolVisitor(List<ISpellEnum> pool) {
        this.iterator = pool.iterator();
        spell = iterator.hasNext() ? iterator.next() : null;
    }

    @Override
    public ISpellEnum peek() {
        return spell;
    }

    @Override
    public void pass() {
        spell = iterator.hasNext() ? iterator.next() : null;
    }

    @Override
    public void passAndConsume() {
        pass();
    }
}
