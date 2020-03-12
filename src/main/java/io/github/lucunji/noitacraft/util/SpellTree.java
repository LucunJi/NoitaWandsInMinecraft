package io.github.lucunji.noitacraft.util;

import io.github.lucunji.noitacraft.spell.ISpellEnum;
import io.github.lucunji.noitacraft.spell.ModifierSpell;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Supplier;

public class SpellTree {
    private final SpellNode root;
    private int totalDelay = 0;
    private int totalRecharge = 0;
    private int size = 0;

    public SpellTree(Iterator<ISpellEnum> iterator, Supplier<Iterator<ISpellEnum>> secondIteratorSupplier) {
        if (iterator.hasNext()) {
            this.root = new SpellNode(null, null);
            feed(this.root, iterator, null, secondIteratorSupplier);
        } else {
            this.root = null;
        }
    }

    private void feed(SpellNode node, Iterator<ISpellEnum> iterator1, Iterator<ISpellEnum> iterator2, Supplier<Iterator<ISpellEnum>> secondIteratorSupplier) {
        int peerCount = node.spell == null ? 1 : node.spell.getCastNumber();
        while ((iterator1.hasNext() || iterator2 == null || iterator2.hasNext()) && peerCount > 0) {
            ISpellEnum spellEnum;
            if (iterator1.hasNext()) {
                spellEnum = iterator1.next();
            } else if (iterator2 == null){
                iterator2 = secondIteratorSupplier.get();
                spellEnum = iterator2.next();
            } else {
                spellEnum = iterator2.next();
            }
            ++this.size;
            this.totalDelay += spellEnum.getCastDelay();
            this.totalRecharge += spellEnum.getRechargeTime();
            SpellNode child = new SpellNode(spellEnum, node);
            feed(child, iterator1, iterator2, secondIteratorSupplier);
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

    private static class SpellNode {
        private final ISpellEnum spell;
        private Collection<SpellNode> siblings;
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
