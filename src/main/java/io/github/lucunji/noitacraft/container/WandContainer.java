package io.github.lucunji.noitacraft.container;

import io.github.lucunji.noitacraft.container.inventory.WandInventory;
import io.github.lucunji.noitacraft.item.NoitaItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

public class WandContainer extends Container {

    private final ItemStack wandStack;
    private final WandInventory wandInventory;

//    public WandContainer createClientContainer(int id, PlayerInventory playerInventory, PacketBuffer buffer) {
//
//    }

    public WandContainer(int windowId, PlayerInventory playerInventory, ItemStack wandStack) {
        super(NoitaContainers.WAND_CONTAINER, windowId);
        this.wandStack = wandStack;
        this.wandInventory = new WandInventory(wandStack);
        for (int i = 0; i < this.wandInventory.getSizeInventory(); ++i) {
            addSlot(new Slot(wandInventory, i, 10 + i * 18, 47));
        }
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
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
