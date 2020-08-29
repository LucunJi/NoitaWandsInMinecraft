package io.github.lucunji.noitacraft.inventory;

import io.github.lucunji.noitacraft.item.wand.WandData;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;

public class WandInventory extends Inventory {
    private final ItemStack wandItemStack;

    public WandInventory(ItemStack wandItemStack) {
        super(new WandData(wandItemStack).getCapacity());
        this.wandItemStack = wandItemStack;
        readFromStack();
    }

    private void readFromStack() {
        if (wandItemStack.hasTag()) {
            CompoundNBT wandTag = wandItemStack.getOrCreateChildTag("Wand");
            final NonNullList<ItemStack> list = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
            ItemStackHelper.loadAllItems(wandTag.getCompound("Spells"), list);
            for (int i = 0; i < this.getSizeInventory(); ++i) {
                setInventorySlotContents(i, list.get(i));
            }
        }
    }

    public void writeToStack() {
        CompoundNBT wandNBT = wandItemStack.getOrCreateChildTag("Wand");
        if (!wandNBT.contains("Spells")) wandNBT.put("Spells", new CompoundNBT());
        if (wandItemStack.hasTag()) {
            final NonNullList<ItemStack> list = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
            for (int i = 0; i < this.getSizeInventory(); ++i) {
                list.set(i, this.getStackInSlot(i));
            }
            ItemStackHelper.saveAllItems(wandNBT.getCompound("Spells"), list, true);
        }
        new WandData(wandItemStack).setNumberCasted(0);
    }
}
