package io.github.lucunji.noitawands.setup;

import io.github.lucunji.noitawands.NoitaWands;
import io.github.lucunji.noitawands.item.NoitaItems;
import io.github.lucunji.noitawands.item.wand.DefaultWands;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.registry.Registry;

import java.util.Comparator;

public class ModSetup {

    public ItemGroup WAND_GROUP = new ItemGroup(NoitaWands.MODID + ".wands") {
        @Override
        public ItemStack createIcon() {
            return DefaultWands.HANDGUN;
        }

    };

    public ItemGroup SPELL_GROUP = new ItemGroup(NoitaWands.MODID + ".spells") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(NoitaItems.SPELL_ENERGY_SPHERE);
        }

        @Override
        public void fill(NonNullList<ItemStack> items) {
            Registry.ITEM.stream()
                    .sorted(Comparator.comparing(Item::getTranslationKey))
                    .forEach(item -> item.fillItemGroup(this, items));
        }

    };

    public void init() {

    }
}
