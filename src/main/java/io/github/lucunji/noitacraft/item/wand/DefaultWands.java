package io.github.lucunji.noitacraft.item.wand;

import io.github.lucunji.noitacraft.item.NoitaItems;
import io.github.lucunji.noitacraft.util.NBTHelper;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class DefaultWands {
    public static final ItemStack HANDGUN = NBTHelper.makeItemWithTag(NoitaItems.WAND, 1, NBTHelper.makeCompound(
            compoundNBT -> compoundNBT.put("Wand", NBTHelper.makeCompound(
                    wandNBT -> {
                        wandNBT.putInt("CastDelay", 4);
                        wandNBT.putFloat("Mana", 100);
                        wandNBT.putBoolean("Shuffle", false);
                        wandNBT.putInt("ManaChargeSpeed", 30);
                        wandNBT.putInt("Cooldown", 0);
                        wandNBT.putByte("Casts", (byte)1);
                        wandNBT.putFloat("Spread", 0);
                        wandNBT.putInt("RechargeTime", 8);
                        wandNBT.putByte("Capacity", (byte)3);
                        wandNBT.put("Spells", NBTHelper.makeCompound(spellListNBT -> ItemStackHelper.saveAllItems(spellListNBT, NonNullList.from(ItemStack.EMPTY,
                                new ItemStack(NoitaItems.SPELL_SPARK_BOLT),
                                new ItemStack(NoitaItems.SPELL_SPARK_BOLT)), true)));
                        wandNBT.putInt("TextureID", 0);
                        wandNBT.putInt("NumberCasted", 0);
                        wandNBT.putInt("ManaMax", 100);
                    }))
    )).setDisplayName(new StringTextComponent("item.noitacraft.wand.starting"));

    public static final ItemStack BOMB_WAND = NBTHelper.makeItemWithTag(NoitaItems.WAND, 1, NBTHelper.makeCompound(
            compoundNBT -> compoundNBT.put("Wand", NBTHelper.makeCompound(
                    wandNBT -> {
                        wandNBT.putInt("CastDelay", 2);
                        wandNBT.putFloat("Mana", 100);
                        wandNBT.putBoolean("Shuffle", false);
                        wandNBT.putInt("ManaChargeSpeed", 10);
                        wandNBT.putInt("Cooldown", 0);
                        wandNBT.putByte("Casts", (byte)1);
                        wandNBT.putFloat("Spread", 0);
                        wandNBT.putInt("RechargeTime", 1);
                        wandNBT.putByte("Capacity", (byte)1);
                        wandNBT.put("Spells", NBTHelper.makeCompound(spellListNBT -> ItemStackHelper.saveAllItems(spellListNBT, NonNullList.from(ItemStack.EMPTY,
                                new ItemStack(NoitaItems.SPELL_BOMB)), true)));
                        wandNBT.putInt("TextureID", 1);
                        wandNBT.putInt("NumberCasted", 0);
                        wandNBT.putInt("ManaMax", 100);
                    }))
    )).setDisplayName(new TranslationTextComponent("item.noitacraft.wand.starting"));
}
