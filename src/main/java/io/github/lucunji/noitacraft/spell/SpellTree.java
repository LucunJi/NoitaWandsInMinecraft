package io.github.lucunji.noitacraft.spell;

import com.mojang.datafixers.util.Pair;
import io.github.lucunji.noitacraft.spell.iterator.SpellPoolIterator;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class SpellTree {
    private final SpellNode root;
    private int totalDelay = 0;
    private int totalRecharge = 0;
    private int totalMana = 0;
    private int size = 0;
    private final int manaLimit;
    private boolean end = false;

    public SpellTree(SpellPoolIterator iterator) {
        this(iterator, Integer.MAX_VALUE);
    }

    public SpellTree(SpellPoolIterator iterator, int manaLimit) {
        this.root = new SpellNode(null, null);
        this.manaLimit = manaLimit;
        if (iterator.hasNext()) {
            feed(this.root, iterator, false);
        }
    }

    private void feed(SpellNode node, SpellPoolIterator iterator, boolean resetDone) {

//        Stack<SpellNode> spellNodeStack = new Stack<>();
//        spellNodeStack.push(new SpellNode(iterator.next(), null));
//        while (spellNodeStack.size() > 0) {
//            SpellNode currentNode = spellNodeStack.pop();
//
//        }

        int peerCount = node.spell == null ? 1 : node.spell.getCastNumber();

        while ((iterator.hasNext() || (!resetDone && !(node.spell instanceof ProjectileSpell))) && peerCount > 0 && !end) {
            ISpellEnum spellEnum;
            if (!iterator.hasNext()) {
                iterator.reset();
                resetDone = true;
            }
            if (iterator.hasNext()) {
                spellEnum = iterator.next();
            } else {
                break;
            }

            int manaCost = spellEnum.getManaDrain();
            if (manaCost + totalMana > manaLimit) {
                end = true;
                continue;
            }

            ++this.size;
            this.totalDelay += spellEnum.getCastDelay();
            this.totalRecharge += spellEnum.getRechargeTime();
            this.totalMana += manaCost;

            SpellNode child = new SpellNode(spellEnum, node);
            if (spellEnum.getCastNumber() > 0) feed(child, iterator, resetDone);
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

        public Collection<SpellNode> getSiblings() {
            return siblings;
        }
    }
}
