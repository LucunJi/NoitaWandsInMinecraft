package io.github.lucunji.noitacraft.item.wand;

import io.github.lucunji.noitacraft.inventory.WandInventory;
import io.github.lucunji.noitacraft.inventory.container.WandContainer;
import io.github.lucunji.noitacraft.item.BaseItem;
import io.github.lucunji.noitacraft.item.NoitaItems;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class WandItem extends BaseItem {
    public WandItem(Properties properties) {
        super(properties.maxStackSize(1));
    }

    @Override
    public void onUsingTick(ItemStack stack, LivingEntity player, int count) {
        if (!player.world.isRemote() && player instanceof ServerPlayerEntity) {
            if (!player.isShiftKeyDown()) {
                cast(player.world, ((ServerPlayerEntity) player), stack);
            }
        } else {
            ((ClientPlayerEntity) player).moveStrafing *= 5f;
            ((ClientPlayerEntity) player).moveForward *= 5f;
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemStack = playerIn.getHeldItem(handIn);
        if (!worldIn.isRemote() && playerIn instanceof ServerPlayerEntity) {
            if (handIn == Hand.MAIN_HAND && itemStack.getItem().equals(NoitaItems.WAND)) {
                if (playerIn.isShiftKeyDown()) {
                    WandProperty wandProperty = WandProperty.getPropertyNotNull(itemStack, playerIn.getRNG());
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
    public boolean showDurabilityBar(ItemStack stack) {
        return true;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        WandProperty property = WandProperty.getPropertyNullable(stack);
        if (property == null) {
            return super.getDurabilityForDisplay(stack);
        } else {
            return 1.0 - (double) property.mana / (double) property.manaMax;
        }
    }

    @Override
    public int getRGBDurabilityForDisplay(ItemStack stack) {
        return new Color(51, 188, 247).getRGB();
    }

    @Override
    public boolean canContinueUsing(ItemStack oldStack, ItemStack newStack) {
        return true;
    }

    private static void cast(World world, PlayerEntity caster, ItemStack wandStack) {
        WandProperty wandProperty = WandProperty.getPropertyNotNull(wandStack, caster.getRNG());
        if (wandProperty.cooldown > 0) return;
        WandCastingHandler castingHandler = new WandCastingHandler(wandStack, wandProperty, new WandInventory(wandStack));
        castingHandler.cast(world, caster).forEach(world::addEntity);
        wandProperty.writeProperty();
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (entityIn instanceof PlayerEntity) {
            WandProperty wandProperty = WandProperty.getPropertyNotNull(stack, ((PlayerEntity) entityIn).getRNG());
            boolean needWrite = false;
            if (wandProperty.cooldown > 0) {
                --wandProperty.cooldown;
                needWrite = true;
            }
            if (wandProperty.mana < wandProperty.manaMax && worldIn.getGameTime() % 20 == 0) {
                wandProperty.mana = Math.min(wandProperty.manaMax, wandProperty.mana + wandProperty.manaChargeSpeed);
                needWrite = true;
            }
            if (needWrite) wandProperty.writeProperty();
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack wandStack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        List<ITextComponent> wandPropertyInfo = new ArrayList<>();
        boolean uninitialized = false;
        if (wandStack.hasTag()) {
            CompoundNBT wandTag = wandStack.getTag();
            if (wandTag.contains("Wand")) {
                wandTag = wandTag.getCompound("Wand");
                if (wandTag.contains("Shuffle")) wandPropertyInfo.add(new TranslationTextComponent("desc.noitacraft.wand.shuffle", String.valueOf(wandTag.getBoolean("Shuffle")))); else uninitialized = true;
                if (wandTag.contains("Casts")) wandPropertyInfo.add(new TranslationTextComponent("desc.noitacraft.wand.casts", wandTag.getByte("Casts"))); else uninitialized = true;
                if (wandTag.contains("CastDelay")) wandPropertyInfo.add(new TranslationTextComponent("desc.noitacraft.wand.cast_delay", wandTag.getInt("CastDelay") / 20.0)); else uninitialized = true;
                if (wandTag.contains("RechargeTime")) wandPropertyInfo.add(new TranslationTextComponent("desc.noitacraft.wand.recharge_time", wandTag.getInt("RechargeTime") / 20.0)); else uninitialized = true;
                if (wandTag.contains("ManaMax")) wandPropertyInfo.add(new TranslationTextComponent("desc.noitacraft.wand.mana_max", wandTag.getInt("ManaMax"))); else uninitialized = true;
                if (wandTag.contains("ManaChargeSpeed")) wandPropertyInfo.add(new TranslationTextComponent("desc.noitacraft.wand.mana_charge_speed", wandTag.getInt("ManaChargeSpeed"))); else uninitialized = true;
                if (wandTag.contains("Capacity")) wandPropertyInfo.add(new TranslationTextComponent("desc.noitacraft.wand.capacity", wandTag.getByte("Capacity"))); else uninitialized = true;
                if (wandTag.contains("Spread")) wandPropertyInfo.add(new TranslationTextComponent("desc.noitacraft.wand.spread", wandTag.getFloat("Spread"))); else uninitialized = true;
            } else {
                uninitialized = true;
            }
        } else {
            uninitialized = true;
        }
        if (uninitialized) {
            tooltip.add(new TranslationTextComponent("desc.noitacraft.wand.uninitialized"));
        } else {
            tooltip.addAll(wandPropertyInfo);
        }
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
