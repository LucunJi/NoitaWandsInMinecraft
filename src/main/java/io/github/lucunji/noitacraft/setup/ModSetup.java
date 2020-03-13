package io.github.lucunji.noitacraft.setup;

import io.github.lucunji.noitacraft.NoitaCraft;
import io.github.lucunji.noitacraft.item.NoitaItems;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ModSetup {

    public ItemGroup WAND_GROUP = new ItemGroup(NoitaCraft.MOD_ID + ".wands") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(NoitaItems.WAND);
        }
    };

    public ItemGroup SPELL_GROUP = new ItemGroup(NoitaCraft.MOD_ID + ".spells") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(NoitaItems.SPELL_ENERGY_SPHERE);
        }
    };

    public void init() {

    }
}
