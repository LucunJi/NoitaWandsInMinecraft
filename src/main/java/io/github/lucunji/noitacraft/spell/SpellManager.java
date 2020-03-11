package io.github.lucunji.noitacraft.spell;

import com.google.common.collect.Maps;

import javax.annotation.Nullable;
import java.util.Map;

public class SpellManager {
    protected static final Map<String, ISpellEnum> SPELL_MAP = Maps.newHashMap();

    @Nullable
    public static ISpellEnum getSpellByName(String name) {
        return SPELL_MAP.get(name);
    }
}
