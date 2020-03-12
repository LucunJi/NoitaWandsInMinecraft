package io.github.lucunji.noitacraft.spell.iterator;

import io.github.lucunji.noitacraft.spell.ISpellEnum;
import net.minecraft.item.ItemStack;

import java.util.Iterator;

public abstract class SpellPoolIterator implements Iterator<ISpellEnum> {
    public abstract void reset();

    public abstract int getResetCount();
}
