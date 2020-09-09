package io.github.lucunji.noitacraft.spell.cast;

import io.github.lucunji.noitacraft.spell.ISpellEnum;

public abstract class SpellPoolVisitor {

    /**
     * @return the first spell in the pool,
     * or null if the pool is now empty
     */
    public abstract ISpellEnum peek();

    /**
     * deque the first spell in the pool
     */
    public abstract void pass();

    /**
     * similar to pass(), but may consume a non-infinite spell
     */
    public abstract void passAndConsume();
}
