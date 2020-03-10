package io.github.lucunji.noitacraft.item.wand;

import io.github.lucunji.noitacraft.entity.projectile.SpellProjectileEntityBase;
import io.github.lucunji.noitacraft.inventory.WandInventory;
import io.github.lucunji.noitacraft.item.SpellItem;
import io.github.lucunji.noitacraft.spell.ProjectileSpell;
import io.github.lucunji.noitacraft.spell.SpellBase;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;

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
        if (wandProperty.isShuffle) {
            caster.sendMessage(new StringTextComponent("§cShuffling wand is not supported yet!§r"));
            return new ArrayList<>();
        }

        List<SpellProjectileEntityBase> entities = new ArrayList<>();

        SpellPoolIterator spellPoolIterator = getSpellPoll(wandProperty, wandInventory);

        int accumulatedRechargeTime = 0, accumulatedCastDelay = 0;

        if (spellPoolIterator.hasNext()) {
            ItemStack spellStack = spellPoolIterator.next();
            if (spellStack.getItem() instanceof SpellItem) {
                SpellBase spell = ((SpellItem) spellStack.getItem()).getSpell();
                if (spell.manaDrain <= wandProperty.mana) {
                    if (spell instanceof ProjectileSpell) {
                        ProjectileSpell projectileSpell = (ProjectileSpell) spell;
                        SpellProjectileEntityBase projectileEntity = projectileSpell.entitySummoner.apply(world, caster);
                        float speed = 0.0f;
                        if (projectileSpell.speedMin < projectileSpell.speedMax)
                            speed = caster.getRNG().nextInt(projectileSpell.speedMax - projectileSpell.speedMin) + projectileSpell.speedMin;
                        speed /= 533.333f;
                        projectileEntity.shoot(caster, caster.rotationPitch, caster.rotationYaw, speed, 1.0f);
                        entities.add(projectileEntity);
                    }
                    accumulatedRechargeTime += spell.rechargeTime;
                    accumulatedCastDelay += spell.castDelay;
                    wandProperty.mana -= spell.manaDrain;
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
