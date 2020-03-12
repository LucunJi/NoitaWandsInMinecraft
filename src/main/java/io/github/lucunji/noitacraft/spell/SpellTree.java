package io.github.lucunji.noitacraft.spell;

import com.mojang.datafixers.util.Pair;
import io.github.lucunji.noitacraft.spell.iterator.SpellPoolIterator;
import io.github.lucunji.noitacraft.spell.ISpellEnum;
import io.github.lucunji.noitacraft.spell.ModifierSpell;
import io.github.lucunji.noitacraft.util.IntHolder;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class SpellTree {
    private final SpellNode root;
    private int totalDelay = 0;
    private int totalRecharge = 0;
    private int totalMana = 0;
    private int size = 0;

    public SpellTree(SpellPoolIterator iterator) {
        this(iterator, Integer.MAX_VALUE);
    }

    public SpellTree(SpellPoolIterator iterator, int manaLimit) {
        this.root = new SpellNode(null, null);
        if (iterator.hasNext()) {
            feed(this.root, iterator, false, new IntHolder(manaLimit));
        }
    }

    private void feed(SpellNode node, SpellPoolIterator iterator, boolean resetDone, IntHolder manaLimit) {
        int peerCount = node.spell == null ? 1 : node.spell.getCastNumber();
        while ((iterator.hasNext() || !resetDone) && peerCount > 0) {
            ISpellEnum spellEnum;
            if (!iterator.hasNext()) {
                iterator.reset();
                resetDone = true;
            }
            spellEnum = iterator.next();

            int manaCost = spellEnum.getManaDrain();
            if (manaCost > manaLimit.value) {
                continue;
            }
            manaLimit.value -= manaCost;

            ++this.size;
            this.totalDelay += spellEnum.getCastDelay();
            this.totalRecharge += spellEnum.getRechargeTime();
            this.totalMana += manaCost;

            SpellNode child = new SpellNode(spellEnum, node);
            feed(child, iterator, resetDone, manaLimit);
            node.siblings.add(child);
            if (spellEnum instanceof ModifierSpell) {
                peerCount += spellEnum.getCastNumber();
            } else {
                --peerCount;
            }
        }
    }

    public int getSize() {
        return size;
    }

    public int getTotalDelay() {
        return totalDelay;
    }

    public int getTotalRechargeTime() {
        return totalRecharge;
    }

    public int getTotalMana() {
        return totalMana;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public Collection<ISpellEnum> getFirstCasts() {
        return root.siblings.stream().map(SpellNode::getSpell).collect(Collectors.toList());
    }

    public List<Pair<ISpellEnum, List<ISpellEnum>>> flatten() {
        List<Pair<ISpellEnum, List<ISpellEnum>>> flattenMap = new ArrayList<>();
        this.root.siblings.forEach(sibling -> {
            List<ISpellEnum> castList = new ArrayList<>();
            flattenHelper(castList, sibling.siblings);
            flattenMap.add(new Pair<>(sibling.spell, castList));
        });
        return flattenMap;
    }

    private static void flattenHelper(List<ISpellEnum> castList, Collection<SpellNode> siblings) {
        for (SpellNode sibling : siblings) {
            castList.add(sibling.spell);
            flattenHelper(castList, sibling.siblings);
        }
    }

    private static class SpellNode {
        private final ISpellEnum spell;
        private final Collection<SpellNode> siblings;
        private final SpellNode parent;

        private SpellNode(@Nullable ISpellEnum spell, @Nullable SpellNode parent) {
            this.spell = spell;
            this.parent = parent;
            this.siblings = new ArrayList<>();
        }

        public SpellNode getParent() {
            return parent;
        }

        public ISpellEnum getSpell() {
            return spell;
        }
    }
}
