package io.github.lucunji.noitawands.item.wand;

import io.github.lucunji.noitawands.NoitaWands;
import io.github.lucunji.noitawands.entity.spell.SpellEntityBase;
import io.github.lucunji.noitawands.inventory.WandInventory;
import io.github.lucunji.noitawands.inventory.container.WandContainer;
import io.github.lucunji.noitawands.item.BaseItem;
import io.github.lucunji.noitawands.item.ModItems;
import io.github.lucunji.noitawands.spell.ISpellEnum;
import io.github.lucunji.noitawands.spell.ProjectileSpell;
import io.github.lucunji.noitawands.spell.cast.CastHelper;
import io.github.lucunji.noitawands.spell.cast.WandSpellPoolVisitor;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.*;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * About Wand:
 * {@link WandItem} how wand is used, displayed or ticked in inventory as an {@link net.minecraft.item.Item} Singleton
 * {@link WandData} how data of wand is stored, accessed and modified as an instance of {@link ItemStack}
 * {@link WandInventory} how the inventory(spell list) is stored, accessed and modified as an instance of {@link ItemStack}
 *
 * Most direct operations of NBT should be kept in {@link WandData} and {@link WandInventory}
 */
public class WandItem extends BaseItem {
    public WandItem(Properties properties) {
        super(properties.maxStackSize(1).group(NoitaWands.SETUP.WAND_GROUP));
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        super.fillItemGroup(group, items);
        if (this.isInGroup(group)) {
            items.removeIf(itemStack -> itemStack.getItem() instanceof WandItem);
            items.add(DefaultWands.HANDGUN);
            items.add(DefaultWands.BOMB_WAND);
        }
    }

    /******************** Cast spells ********************/

    @Override
    public void onUsingTick(ItemStack stack, LivingEntity player, int count) {
        if (!player.world.isRemote() && player instanceof ServerPlayerEntity) {
            if (!player.isSneaking()) {
                this.cast(player.world, ((ServerPlayerEntity) player), stack);
            }
        } else {
            player.moveStrafing *= 5f;
            player.moveForward *= 5f;
        }
    }

    /**
     * Only effective in main hand.
     * Shift + right click = open GUI to edit wand
     */
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemStack = playerIn.getHeldItem(handIn);
        if (!worldIn.isRemote() && playerIn instanceof ServerPlayerEntity) {
            if (handIn == Hand.MAIN_HAND && itemStack.getItem().equals(ModItems.WAND)) {
                if (playerIn.isSneaking()) {
                    new WandData(itemStack);
                    NetworkHooks.openGui((ServerPlayerEntity) playerIn, new WandContainerProvider(itemStack), buffer -> buffer.writeItemStack(itemStack));
                } else {
                    playerIn.setActiveHand(handIn);
                }
                return ActionResult.resultSuccess(itemStack);
            }
        }
        return ActionResult.resultFail(itemStack);
    }

    @Override
    public boolean canContinueUsing(ItemStack oldStack, ItemStack newStack) {
        return true;
    }

    private void cast(World world, PlayerEntity caster, ItemStack wandStack) {
        LogManager.getLogger().debug("Start casting...");
        WandData wandData = new WandData(wandStack);
        if (wandData.getCooldown() > 0) {
            LogManager.getLogger().debug("Cooldown is not over!");
            return;
        }
        LogManager.getLogger().debug("Spell pool is {}", Arrays.toString(wandData.getSpellPool()));
        LogManager.getLogger().debug("Current spell pool pointer: {}", wandData.getSpellPoolPointer());

        int oldPoolPointer = wandData.getSpellPoolPointer();
        CastHelper.CastResult castResult = CastHelper.processSpellList(new WandSpellPoolVisitor(wandData, new WandInventory(wandStack)), wandData.getMana());
        wandData.setMana(wandData.getMana() - castResult.getManaDrain());
        if (wandData.getSpellPoolPointer() < wandData.getSpellPool().length && wandData.getSpellPoolPointer() > oldPoolPointer) {
            wandData.setCooldown(castResult.getCastDelay() + wandData.getCastDelay());
        } else {
            wandData.setCooldown(Math.max(castResult.getRechargeTime() + wandData.getRechargeTime(), castResult.getCastDelay() + wandData.getCastDelay()));
            wandData.refreshWandPool();
        }

        LogManager.getLogger().debug("Start summoning spells...");
        for (Pair<ProjectileSpell, List<ISpellEnum>> pair : castResult.getSpell2TriggeredSpellList()) {
            ProjectileSpell spell = pair.getKey();
            SpellEntityBase spellEntity = spell.entitySummoner().apply(world, caster);
            spellEntity.setCastList(pair.getValue());

            float speed = 0;
            int speedMin = spell.getSpeedMin();
            int speedMax = spell.getSpeedMax();
            if (speedMin < speedMax)
                speed = new Random().nextInt(speedMax - speedMin) + speedMin;
            speed += 200f;
            speed /= 600f;

            spellEntity.shoot(caster, caster.rotationPitch, caster.rotationYaw, speed, 1.0f);
            world.addEntity(spellEntity);
        }
        LogManager.getLogger().debug("Casting finish!");
    }

    /**
     * Cooldown and Mana regen.
     * If wand is not in main hand, rest its spell pool.
     */
    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (entityIn instanceof PlayerEntity) {
            WandData wandData = new WandData(stack);
            if (wandData.getCooldown() > 0) {
                wandData.setCooldown(wandData.getCooldown() - 1);
            }
            if (wandData.getMana() < wandData.getManaMax()) {
                wandData.setMana(wandData.getMana() + wandData.getManaChargeSpeed() / 20f);
            }

            if (((PlayerEntity) entityIn).getHeldItemMainhand() != stack) {
                wandData.resetSpellPool();
            }
        }
    }

    /******************** Client-side info ********************/

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return stack.hasTag();
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        WandData property = new WandData(stack);
        if (property.getManaMax() == 0)
            return super.getDurabilityForDisplay(stack);
        return 1.0 - (double) property.getMana() / (double) property.getManaMax();
    }

    @Override
    public int getRGBDurabilityForDisplay(ItemStack stack) {
        return new Color(51, 188, 247).getRGB();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack wandStack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        List<ITextComponent> wandPropertyInfo = new ArrayList<>();
        WandData wandData = new WandData(wandStack);
        wandPropertyInfo.add(new TranslationTextComponent("desc.noitaWands.wand.shuffle", String.valueOf(wandData.isShuffle())));
        wandPropertyInfo.add(new TranslationTextComponent("desc.noitaWands.wand.casts", wandData.getCasts()));
        wandPropertyInfo.add(new TranslationTextComponent("desc.noitaWands.wand.cast_delay", wandData.getCastDelay() / 20.0));
        wandPropertyInfo.add(new TranslationTextComponent("desc.noitaWands.wand.recharge_time", wandData.getRechargeTime() / 20.0));
        wandPropertyInfo.add(new TranslationTextComponent("desc.noitaWands.wand.mana_max", wandData.getManaMax()));
        wandPropertyInfo.add(new TranslationTextComponent("desc.noitaWands.wand.mana_charge_speed", wandData.getManaChargeSpeed()));
        wandPropertyInfo.add(new TranslationTextComponent("desc.noitaWands.wand.capacity", wandData.getCapacity()));
        wandPropertyInfo.add(new TranslationTextComponent("desc.noitaWands.wand.spread", wandData.getSpread()));
        tooltip.addAll(wandPropertyInfo);
    }

    private static class WandContainerProvider implements INamedContainerProvider {
        private final ItemStack wandItemStack;

        public WandContainerProvider(ItemStack wandItemStack) {
            this.wandItemStack = wandItemStack;
        }

        @Override
        public ITextComponent getDisplayName() {
            return wandItemStack.getDisplayName();
        }

        @Nullable
        @Override
        public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity playerEntity) {
            return new WandContainer(id, playerInventory, wandItemStack);
        }
    }
}
