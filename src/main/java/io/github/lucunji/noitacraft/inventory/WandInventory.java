package io.github.lucunji.noitacraft.inventory;

import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;

public class WandInventory extends Inventory {
    private final ItemStack wandItemStack;

    public WandInventory(ItemStack wandItemStack) {
        super(getCount(wandItemStack));
        this.wandItemStack = wandItemStack;
        readFromStack();
    }

    private static int getCount(ItemStack wandItemStack) {
        if (wandItemStack.hasTag()) {
            return wandItemStack.getTag().getCompound("Wand").getByte("Size");
        } else {
            return 0;
        }
    }

    private void readFromStack() {
        if (wandItemStack.hasTag()) {
            CompoundNBT wandTag = wandItemStack.getTag().getCompound("Wand");
            final NonNullList<ItemStack> list = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
            ItemStackHelper.loadAllItems(wandTag.getCompound("Spells"), list);
            for (int i = 0; i < this.getSizeInventory(); ++i) {
                setInventorySlotContents(i, list.get(i));
            }
        }
    }

        public void writeToStack() {
        if (wandItemStack.hasTag()) {
            final NonNullList<ItemStack> list = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
            for (int i = 0; i < this.getSizeInventory(); ++i) {
                list.set(i, this.getStackInSlot(i));
            }
            CompoundNBT spellCompound = wandItemStack.getTag().getCompound("Wand").getCompound("Spells");
            ItemStackHelper.saveAllItems(spellCompound, list, true);
        }
    }
}
