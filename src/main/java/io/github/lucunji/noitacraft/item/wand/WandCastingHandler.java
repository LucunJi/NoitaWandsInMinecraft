package io.github.lucunji.noitacraft.item.wand;

import io.github.lucunji.noitacraft.entity.projectile.SpellProjectileEntityBase;
import io.github.lucunji.noitacraft.inventory.WandInventory;
import io.github.lucunji.noitacraft.item.SpellItem;
import io.github.lucunji.noitacraft.spell.ProjectileSpell;
import io.github.lucunji.noitacraft.spell.SpellBase;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
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

    public List<SpellProjectileEntityBase> cast(World world, PlayerEntity caster) {
        List<SpellProjectileEntityBase> entities = new ArrayList<>();

        SpellPoolIterator spellPoolIterator = getSpellPoll(wandProperty, wandInventory);

        if (spellPoolIterator.hasNext()) {
            ItemStack spellStack = spellPoolIterator.next();
            if (spellStack.getItem() instanceof SpellItem) {
                SpellBase spell = ((SpellItem) spellStack.getItem()).getSpell();
                if (spell instanceof ProjectileSpell) {
                    SpellProjectileEntityBase projectileEntity = ((ProjectileSpell) spell).entitySummoner.apply(world, caster);
                    projectileEntity.shoot(caster, caster.rotationPitch, caster.rotationYaw, 1.5f, 1.0f);
                    entities.add(projectileEntity);
                }
            }
        }
        if (!spellPoolIterator.hasNext()) {
            spellPoolIterator.reset();
            wandProperty.cooldown += wandProperty.rechargeTime;
        } else {
            wandProperty.cooldown += wandProperty.castDelay;
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
