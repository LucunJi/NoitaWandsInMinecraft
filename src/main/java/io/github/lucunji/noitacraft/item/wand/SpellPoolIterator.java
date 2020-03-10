package io.github.lucunji.noitacraft.item.wand;

import net.minecraft.item.ItemStack;

import java.util.Iterator;

public abstract class SpellPoolIterator implements Iterator<ItemStack> {
    public abstract void reset();
}
