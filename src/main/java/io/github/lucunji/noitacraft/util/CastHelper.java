package io.github.lucunji.noitacraft.util;

import io.github.lucunji.noitacraft.spell.ISpellEnum;
import io.github.lucunji.noitacraft.spell.SpellManager;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;

import java.util.ArrayList;
import java.util.List;

public class CastHelper {
    public static boolean cast(List<ISpellEnum> castList, Entity caster, World worldIn) {
        return false;
    }

    public static List<ISpellEnum> spellListFromNBT(ListNBT listNBT) {
        List<ISpellEnum> spellEnumList = new ArrayList<>();
        for (int i = 0; i < listNBT.size(); ++i) {
            String spellName = listNBT.getString(i);
            ISpellEnum spellEnum = SpellManager.getSpellByName(spellName);
            if (spellEnum != null) {
                spellEnumList.add(spellEnum);
                LogManager.getLogger().warn("Not matching spell for name: \"" + spellName + "\"");
            }
        }
        return spellEnumList;
    }

    public static ListNBT spellNBTFromList(List<ISpellEnum> spellEnumList) {
        ListNBT listNBT = new ListNBT();
        spellEnumList.stream().map(ISpellEnum::name).map(StringNBT::valueOf).forEach(listNBT::add);
        return listNBT;
    }
}
