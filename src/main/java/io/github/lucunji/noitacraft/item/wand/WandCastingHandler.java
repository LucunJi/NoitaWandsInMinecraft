package io.github.lucunji.noitacraft.item.wand;

import io.github.lucunji.noitacraft.entity.spell.SpellEntityBase;
import io.github.lucunji.noitacraft.inventory.WandInventory;
import io.github.lucunji.noitacraft.spell.ISpellEnum;
import io.github.lucunji.noitacraft.spell.iterator.OrderedWandSpellPoolIterator;
import io.github.lucunji.noitacraft.spell.ProjectileSpell;
import io.github.lucunji.noitacraft.spell.iterator.SpellPoolIterator;
import io.github.lucunji.noitacraft.spell.SpellTree;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class WandCastingHandler {

    private final ItemStack wandStack;
    private final WandProperty wandProperty;
    private final WandInventory wandInventory;

    public WandCastingHandler(ItemStack wandStack, WandProperty wandProperty, WandInventory wandInventory) {
        this.wandStack = wandStack;
        this.wandProperty = wandProperty;
        this.wandInventory = wandInventory;
    }

    public List<SpellEntityBase> cast(World world, PlayerEntity caster) {
        if (wandProperty.isShuffle()) {
            caster.sendMessage(new StringTextComponent("§cShuffling wand is not supported yet!§r"));
            return new ArrayList<>();
        }

        List<SpellEntityBase> entities = new ArrayList<>();
        SpellPoolIterator spellPoolIterator = getSpellPoll(wandProperty, wandInventory);

        SpellTree spellTree = new SpellTree(spellPoolIterator, wandProperty.getManaMax());
        spellTree.flatten().forEach((iSpellEnumListPair -> {
            ISpellEnum iSpellEnum = iSpellEnumListPair.getFirst();
            List<ISpellEnum> spellEnumList = iSpellEnumListPair.getSecond();

            if (iSpellEnum instanceof ProjectileSpell) {
                ProjectileSpell projectileSpell = (ProjectileSpell) iSpellEnum;
                SpellEntityBase entityBase = projectileSpell.entitySummoner().apply(world, caster);

                float speed = 0;
                int speedMin = projectileSpell.getSpeedMin();
                int speedMax = projectileSpell.getSpeedMax();
                if (speedMin < speedMax)
                    speed = caster.getRNG().nextInt(speedMax - speedMin) + speedMin;
                speed += 200f;
                speed /= 600f;

                entityBase.shoot(caster, caster.rotationPitch, caster.rotationYaw, speed, 1.0f);
                entityBase.setCastList(spellEnumList);
                entities.add(entityBase);
            }
        }));

        int coolDown = spellTree.getTotalDelay() + spellTree.getTotalRechargeTime() + wandProperty.getCastDelay();
        if (!spellPoolIterator.hasNext()) {
            spellPoolIterator.reset();
        }
        if (spellPoolIterator.getResetCount() > 0) {
            coolDown += wandProperty.getRechargeTime();
        }
        wandProperty.setCooldown(coolDown, false);
        wandProperty.setMana(wandProperty.getMana() - spellTree.getTotalMana(), false);
        wandProperty.writeProperty();

        return entities;
    }

    private SpellPoolIterator getSpellPoll(WandProperty wandProperty, WandInventory wandInventory) {
        if (wandProperty.isShuffle()) {
            return null;
        } else {
            return new OrderedWandSpellPoolIterator(wandProperty, wandInventory);
        }
    }
}
