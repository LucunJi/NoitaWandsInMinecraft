package io.github.lucunji.noitacraft.spell.cast;

import io.github.lucunji.noitacraft.inventory.WandInventory;
import io.github.lucunji.noitacraft.item.SpellItem;
import io.github.lucunji.noitacraft.item.wand.WandData;
import io.github.lucunji.noitacraft.spell.ISpellEnum;
import net.minecraft.item.ItemStack;

import java.util.Random;

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
        ItemStack spellItemStack = wandInventory.getStackInSlot(wandData.getSpellPoolPointer());
        if (spellItemStack.isDamageable() && spellItemStack.getDamage() == 0) {
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
        }
        wandData.setSpellPoolPointer((byte)(wandData.getSpellPoolPointer() + 1));
        spellsRemaining--;
    }

    @Override
    public void passAndConsume() {
        ItemStack wandItemStack = wandInventory.getStackInSlot(wandData.getSpellPoolPointer());
        if (wandItemStack.isDamageable() && wandItemStack.getDamage() > 0) {
            wandItemStack.setDamage(wandItemStack.getDamage() - 1);
        }
        pass();
    }
}
