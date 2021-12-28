package io.github.lucunji.noitawands.inventory.container;

import io.github.lucunji.noitawands.inventory.WandInventory;
import io.github.lucunji.noitawands.item.SpellItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class WandContainer extends Container {

    private final ItemStack wandStack;
    private final WandInventory wandInventory;

    public WandContainer(int windowId, PlayerInventory playerInventory, ItemStack wandStack) {
        super(NoitaContainers.WAND_CONTAINER, windowId);
        this.wandStack = wandStack;
        this.wandInventory = new WandInventory(wandStack);
        for (int i = 0; i < this.wandInventory.getSizeInventory(); ++i) {
            addSlot(new Slot(wandInventory, i, 10 + i * 18, 47) {
                @Override
                public boolean isItemValid(ItemStack stack) {
                    return stack.getItem() instanceof SpellItem;
                }
            });
        }
        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for(int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
        }
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (index < this.wandInventory.getSizeInventory()) {
                if (!this.mergeItemStack(itemstack1, this.wandInventory.getSizeInventory(), this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!(itemstack1.getItem() instanceof SpellItem)) {
                return ItemStack.EMPTY;
            } else if (!this.mergeItemStack(itemstack1, 0, this.wandInventory.getSizeInventory(), false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        wandInventory.writeToStack();
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        if (!playerIn.world.isRemote() && playerIn instanceof ServerPlayerEntity) {
            if (playerIn.getHeldItemMainhand() == wandStack) {
                return true;
            }
        }
        return true;
    }

    public ItemStack getWandStack() {
        return wandStack;
    }

    public WandInventory getWandInventory() {
        return wandInventory;
    }
}
