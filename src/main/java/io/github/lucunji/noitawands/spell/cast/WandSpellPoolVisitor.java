package io.github.lucunji.noitawands.spell.cast;

import io.github.lucunji.noitawands.inventory.WandInventory;
import io.github.lucunji.noitawands.item.SpellItem;
import io.github.lucunji.noitawands.item.wand.WandData;
import io.github.lucunji.noitawands.spell.ISpellEnum;
import net.minecraft.item.ItemStack;

public class WandSpellPoolVisitor extends SpellPoolVisitor {
    private final WandData wandData;
    private final WandInventory wandInventory;
    private boolean overused;
    private int spellsRemaining;

    public WandSpellPoolVisitor(WandData wandData, WandInventory wandInventory) {
        this.wandData = wandData;
        this.wandInventory = wandInventory;
        this.overused = false;
        this.spellsRemaining = 0;
    }

    @Override
    public ISpellEnum peek() {
        if (wandData.getSpellPoolPointer() >= wandData.getSpellPool().length || overused && spellsRemaining < 1) {
            return null;
        }
        ItemStack spellItemStack = wandInventory.getStackInSlot(wandData.getSpellPool()[wandData.getSpellPoolPointer()]);
        if (spellItemStack.isDamageable() && spellItemStack.getDamage() == spellItemStack.getMaxDamage()) {
            pass();
            return peek();
        }
        return ((SpellItem)spellItemStack.getItem()).getSpell();
    }

    @Override
    public void pass() {
        if (wandData.getSpellPoolPointer() + 1 >= wandData.getSpellPool().length && !overused) {
            wandData.setSpellPoolPointer((byte)0);
            overused = true;
            spellsRemaining += wandData.getSpellPool().length;
        } else {
            wandData.setSpellPoolPointer((byte) (wandData.getSpellPoolPointer() + 1));
        }
        spellsRemaining--;
    }

    @Override
    public void passAndConsume() {
        ItemStack itemStack = wandInventory.getStackInSlot(wandData.getSpellPool()[wandData.getSpellPoolPointer()]);
        if (itemStack.isDamageable() && itemStack.getDamage() < itemStack.getMaxDamage()) {
            itemStack.setDamage(itemStack.getDamage() + 1);
            wandInventory.writeToStack();
        }
        pass();
    }
}
