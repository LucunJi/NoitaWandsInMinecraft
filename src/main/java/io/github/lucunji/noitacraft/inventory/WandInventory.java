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
        if (wandItemStack.hasTag() && wandItemStack.getTag().contains("Wand") && wandItemStack.getTag().getCompound("Wand").contains("Capacity")) {
            return wandItemStack.getTag().getCompound("Wand").getByte("Capacity");
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
        CompoundNBT tag = wandItemStack.getTag();
        if (tag == null) {
            tag = new CompoundNBT();
            wandItemStack.setTag(tag);
        }
        CompoundNBT wandNBT = new CompoundNBT();
        if (tag.contains("Wand")) {
            wandNBT = tag.getCompound("Wand");
            tag.put("Wand", wandNBT);
        }
        if (!wandNBT.contains("Spells")) wandNBT.put("Spells", new CompoundNBT());
        if (wandItemStack.hasTag()) {
            final NonNullList<ItemStack> list = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
            for (int i = 0; i < this.getSizeInventory(); ++i) {
                list.set(i, this.getStackInSlot(i));
            }
            ItemStackHelper.saveAllItems(wandNBT.getCompound("Spells"), list, true);
        }
        wandNBT.putInt("NumberCasted", 0);
    }
}
