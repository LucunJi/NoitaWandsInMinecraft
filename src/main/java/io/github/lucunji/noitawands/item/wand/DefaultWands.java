package io.github.lucunji.noitawands.item.wand;

import io.github.lucunji.noitawands.inventory.WandInventory;
import io.github.lucunji.noitawands.item.NoitaItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TranslationTextComponent;

public class DefaultWands {
    public static final ItemStack HANDGUN = makeWandStack(4, false, 30, (byte)1,
            0f, 8, (byte)3, 1, 100,
            NoitaItems.SPELL_SPARK_BOLT, NoitaItems.SPELL_SPARK_BOLT).setDisplayName(new TranslationTextComponent("item.noitaWands.wand.starting"));

    public static final ItemStack BOMB_WAND = makeWandStack(2, false, 10, (byte)1,
            0f, 1, (byte)1, 2, 100,
            NoitaItems.SPELL_BOMB).setDisplayName(new TranslationTextComponent("item.noitaWands.wand.starting"));

    public static ItemStack makeWandStack(int castDelay, boolean shuffle, int manaChargeSpeed, byte casts,
                                          float spread, int rechargeTime, byte capacity, int textureID, int manaMax, Item... spells) {
        ItemStack itemStack = new ItemStack(NoitaItems.WAND, 1);
        WandData wandData = new WandData(itemStack);
        wandData.setCastDelay(castDelay);
        wandData.setMana(manaMax);
        wandData.setShuffle(shuffle);
        wandData.setManaChargeSpeed(manaChargeSpeed);
        wandData.setCooldown(0);
        wandData.setCasts(casts);
        wandData.setSpread(spread);
        wandData.setRechargeTime(rechargeTime);
        wandData.setCapacity(capacity);
        wandData.setTextureID(textureID);
        wandData.setManaMax(manaMax);

        WandInventory inventory = new WandInventory(itemStack);
        for (int i = 0; i < spells.length; i++) {
            inventory.setInventorySlotContents(i, new ItemStack(spells[i]));
        }
        inventory.writeToStack();

        return itemStack;
    }
}
