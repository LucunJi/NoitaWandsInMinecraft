package io.github.lucunji.noitacraft.spell.iterator;

import io.github.lucunji.noitacraft.inventory.WandInventory;
import io.github.lucunji.noitacraft.item.SpellItem;
import io.github.lucunji.noitacraft.item.wand.WandProperty;
import io.github.lucunji.noitacraft.spell.ISpellEnum;

public class OrderedWandSpellPoolIterator extends SpellPoolIterator {
    private final WandProperty wandProperty;
    private final WandInventory wandInventory;
    private int resetCount;
    private int next;

    public OrderedWandSpellPoolIterator(WandProperty wandProperty, WandInventory wandInventory) {
        this.wandProperty = wandProperty;
        this.wandInventory = wandInventory;
        this.resetCount = 0;
        this.next = wandProperty.getNumberCasted();
        this.findNextIndex();
    }

    @Override
    public boolean hasNext() {
        return this.next < wandProperty.getCapacity() && !wandInventory.isEmpty();
    }

    @Override
    public ISpellEnum next() {
        int index = this.next;
        ++this.next;
        this.findNextIndex();
        wandProperty.setNumberCasted(next);
        return ((SpellItem) wandInventory.getStackInSlot(index).getItem()).getSpell();
    }

    private void findNextIndex() {
        while (this.next < wandProperty.getCapacity() && !(wandInventory.getStackInSlot(this.next).getItem() instanceof SpellItem)) {
            ++this.next;
        }
    }

    @Override
    public void reset() {
        ++resetCount;
        this.next = 0;
        findNextIndex();
        wandProperty.setNumberCasted(0);
    }

    @Override
    public int getResetCount() {
        return this.resetCount;
    }
}
