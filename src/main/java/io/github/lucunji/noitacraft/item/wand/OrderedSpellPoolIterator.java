package io.github.lucunji.noitacraft.item.wand;

import io.github.lucunji.noitacraft.inventory.WandInventory;
import net.minecraft.item.ItemStack;

import java.util.Iterator;

public class OrderedSpellPoolIterator extends SpellPoolIterator {
    private final WandProperty wandProperty;
    private final WandInventory wandInventory;

    public OrderedSpellPoolIterator(WandProperty wandProperty, WandInventory wandInventory) {
        this.wandProperty = wandProperty;
        this.wandInventory = wandInventory;
    }

    @Override
    public boolean hasNext() {
        return wandProperty.numberCasted < wandProperty.capacity;
    }

    @Override
    public ItemStack next() {
        ItemStack stackToCast = wandInventory.getStackInSlot(wandProperty.numberCasted);
        do {
            wandProperty.numberCasted++;
        } while (wandProperty.numberCasted < wandProperty.capacity && wandInventory.getStackInSlot(wandProperty.numberCasted).isEmpty());
        wandProperty.writeProperty();
        return stackToCast;
    }

    @Override
    public void reset() {
        wandProperty.numberCasted = 0;
        wandProperty.writeProperty();
    }
}
