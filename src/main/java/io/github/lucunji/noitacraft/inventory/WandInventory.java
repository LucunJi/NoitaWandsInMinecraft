package io.github.lucunji.noitacraft.inventory;

import io.github.lucunji.noitacraft.item.wand.WandData;
import it.unimi.dsi.fastutil.bytes.ByteArrayList;
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

    /**
     * Write itemStacks into NBT, and update the spell pool
     */
    public void writeToStack() {
        CompoundNBT wandNBT = wandItemStack.getOrCreateChildTag("Wand");
        if (!wandNBT.contains("Spells")) wandNBT.put("Spells", new CompoundNBT());
        final NonNullList<ItemStack> list = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ByteArrayList pool = new ByteArrayList();
        for (int i = 0; i < this.getSizeInventory(); ++i) {
            list.set(i, this.getStackInSlot(i));
            if (!this.getStackInSlot(i).isEmpty()) {
                pool.add((byte)i);
            }
        }
        ItemStackHelper.saveAllItems(wandNBT.getCompound("Spells"), list, true);
        WandData wandData = new WandData((wandItemStack));
        wandData.setSpellPoll(pool.toByteArray());
        wandData.refreshWandPool();
    }
}
