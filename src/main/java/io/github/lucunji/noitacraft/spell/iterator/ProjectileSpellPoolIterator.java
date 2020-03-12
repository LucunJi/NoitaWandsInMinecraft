package io.github.lucunji.noitacraft.spell.iterator;

import io.github.lucunji.noitacraft.spell.ISpellEnum;

import java.util.Iterator;
import java.util.List;

public class ProjectileSpellPoolIterator extends SpellPoolIterator {
    Iterator<ISpellEnum> iterator;
    public ProjectileSpellPoolIterator(List<ISpellEnum> spellEntity) {
        iterator = spellEntity.iterator();
    }

    @Override
    public void reset() {
    }

    @Override
    public int getResetCount() {
        return 0;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public ISpellEnum next() {
        return iterator.next();
    }
}
