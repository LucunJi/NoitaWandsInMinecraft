package io.github.lucunji.noitacraft.setup;

import io.github.lucunji.noitacraft.NoitaCraft;
import io.github.lucunji.noitacraft.item.NoitaItems;
import io.github.lucunji.noitacraft.item.wand.DefaultWands;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.registry.Registry;

import java.util.Comparator;

public class ModSetup {

    public ItemGroup WAND_GROUP = new ItemGroup(NoitaCraft.MOD_ID + ".wands") {
        @Override
        public ItemStack createIcon() {
            return DefaultWands.HANDGUN;
        }

    };

    public ItemGroup SPELL_GROUP = new ItemGroup(NoitaCraft.MOD_ID + ".spells") {
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
