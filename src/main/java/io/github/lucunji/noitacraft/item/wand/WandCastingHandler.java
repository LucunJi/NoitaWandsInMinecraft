package io.github.lucunji.noitacraft.item.wand;

import io.github.lucunji.noitacraft.entity.spell.SpellEntityBase;
import io.github.lucunji.noitacraft.inventory.WandInventory;
import io.github.lucunji.noitacraft.item.SpellItem;
import io.github.lucunji.noitacraft.spell.ISpellEnum;
import io.github.lucunji.noitacraft.spell.ProjectileSpell;
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
        if (wandProperty.isShuffle) {
            caster.sendMessage(new StringTextComponent("§cShuffling wand is not supported yet!§r"));
            return new ArrayList<>();
        }

        List<SpellEntityBase> entities = new ArrayList<>();

        SpellPoolIterator spellPoolIterator = getSpellPoll(wandProperty, wandInventory);

        int accumulatedRechargeTime = 0, accumulatedCastDelay = 0;

        if (spellPoolIterator.hasNext()) {
            ItemStack spellStack = spellPoolIterator.next();
            if (spellStack.getItem() instanceof SpellItem) {
                ISpellEnum spell = ((SpellItem) spellStack.getItem()).getSpell();
                if (spell.getManaDrain() <= wandProperty.mana) {
                    if (spell instanceof ProjectileSpell) {
                        ProjectileSpell projectileProjectileSpell = (ProjectileSpell) spell;
                        SpellEntityBase projectileEntity = projectileProjectileSpell.entitySummoner().apply(world, caster);
                        float speed = 0.0f;
                        int speedMin = projectileProjectileSpell.getSpeedMin();
                        int speedMax = projectileProjectileSpell.getSpeedMax();
                        if (speedMin < speedMax)
                            speed = caster.getRNG().nextInt(speedMax - speedMin) + speedMin;
                        speed += 200f;
                        speed /= 600f;
                        projectileEntity.shoot(caster, caster.rotationPitch, caster.rotationYaw, speed, 1.0f);
                        entities.add(projectileEntity);
                    }
                    accumulatedRechargeTime += spell.getRechargeTime();
                    accumulatedCastDelay += spell.getCastDelay();
                    wandProperty.mana -= spell.getManaDrain();
                }
            }
        }
        if (!spellPoolIterator.hasNext()) {
            spellPoolIterator.reset();
            wandProperty.cooldown = Math.max(wandProperty.rechargeTime + accumulatedRechargeTime, wandProperty.castDelay + accumulatedCastDelay);
        } else {
            wandProperty.cooldown = wandProperty.castDelay + accumulatedCastDelay;
        }
        return entities;
    }

    private SpellPoolIterator getSpellPoll(WandProperty wandProperty, WandInventory wandInventory) {
        if (wandProperty.isShuffle) {
            return null;
        } else {
            return new OrderedSpellPoolIterator(wandProperty, wandInventory);
        }
    }


}
