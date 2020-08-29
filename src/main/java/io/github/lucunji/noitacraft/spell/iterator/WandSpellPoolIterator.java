package io.github.lucunji.noitacraft.spell.iterator;

import io.github.lucunji.noitacraft.inventory.WandInventory;
import io.github.lucunji.noitacraft.item.SpellItem;
import io.github.lucunji.noitacraft.item.wand.WandData;
import io.github.lucunji.noitacraft.spell.ISpellEnum;

public class WandSpellPoolIterator extends SpellPoolIterator {
    private final WandData wandData;
    private final WandInventory wandInventory;
    private int resetCount;
    private int next;

    public WandSpellPoolIterator(WandData wandData, WandInventory wandInventory) {
        this.wandData = wandData;
        this.wandInventory = wandInventory;
        this.resetCount = 0;
        this.next = wandData.getNumberCasted();
        this.findNextIndex();
    }

    @Override
    public boolean hasNext() {
        return this.next < wandData.getCapacity() && !wandInventory.isEmpty();
    }

    @Override
    public ISpellEnum next() {
        int index = this.next;
        ++this.next;
        this.findNextIndex();
        wandData.setNumberCasted(next);
        return ((SpellItem) wandInventory.getStackInSlot(index).getItem()).getSpell();
    }

    private void findNextIndex() {
        while (this.next < wandData.getCapacity() && !(wandInventory.getStackInSlot(this.next).getItem() instanceof SpellItem)) {
            ++this.next;
        }
    }

    @Override
    public void reset() {
        ++resetCount;
        this.next = 0;
        findNextIndex();
        wandData.setNumberCasted(0);
    }

    @Override
    public int getResetCount() {
        return this.resetCount;
    }
}
