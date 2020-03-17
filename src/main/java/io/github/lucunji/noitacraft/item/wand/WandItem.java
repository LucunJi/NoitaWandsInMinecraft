package io.github.lucunji.noitacraft.item.wand;

import io.github.lucunji.noitacraft.NoitaCraft;
import io.github.lucunji.noitacraft.inventory.WandInventory;
import io.github.lucunji.noitacraft.inventory.container.WandContainer;
import io.github.lucunji.noitacraft.item.BaseItem;
import io.github.lucunji.noitacraft.item.NoitaItems;
import io.github.lucunji.noitacraft.util.NBTHelper;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
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
        super(properties.maxStackSize(1).group(NoitaCraft.SETUP.WAND_GROUP));
        this.addPropertyOverride(new ResourceLocation(NoitaCraft.MOD_ID, "texture_id"), (itemStack, world, entity) ->
                NBTHelper.getCompound(itemStack).flatMap(stackTag ->
                    NBTHelper.getCompound(stackTag, "Wand").flatMap(compoundNBT ->
                            NBTHelper.getInt(compoundNBT, "TextureID"))).orElse(-1)
        );
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        super.fillItemGroup(group, items);
        if (this.isInGroup(group)) {
            items.add(DefaultWands.HANDGUN);
            items.add(DefaultWands.BOMB_WAND);
        }
    }

    /******************** Cast spells ********************/

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
    public boolean canContinueUsing(ItemStack oldStack, ItemStack newStack) {
        return true;
    }

    private static void cast(World world, PlayerEntity caster, ItemStack wandStack) {
        WandProperty wandProperty = WandProperty.getPropertyNotNull(wandStack, caster.getRNG());
        if (wandProperty.getCooldown() > 0) return;
        WandCastingHandler castingHandler = new WandCastingHandler(wandStack, wandProperty, new WandInventory(wandStack));
        castingHandler.cast(world, caster).forEach(world::addEntity);
    }

    /**
     * Cooldown and Mana regen.
     */
    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (entityIn instanceof PlayerEntity) {
            WandProperty wandProperty = WandProperty.getPropertyNotNull(stack, ((PlayerEntity) entityIn).getRNG());
            boolean flush = false;
            if (wandProperty.getCooldown() > 0) {
                wandProperty.setCooldown(wandProperty.getCooldown() - 1, false);
                flush = true;
            }
            if (wandProperty.getMana() < wandProperty.getManaMax()) {
                wandProperty.setMana(wandProperty.getMana() + wandProperty.getManaChargeSpeed() / 20f, false);
                flush = true;
            }
            if (flush) wandProperty.writeProperty();
        }
    }

    /******************** Client-side info ********************/

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return stack.hasTag();
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        WandProperty property = WandProperty.getPropertyNullable(stack);
        if (property == null) {
            return super.getDurabilityForDisplay(stack);
        } else {
            return 1.0 - (double) property.getMana() / (double) property.getManaMax();
        }
    }

    @Override
    public int getRGBDurabilityForDisplay(ItemStack stack) {
        return new Color(51, 188, 247).getRGB();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack wandStack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        List<ITextComponent> wandPropertyInfo = new ArrayList<>();
        if (wandStack.hasTag()) {
            CompoundNBT wandTag = wandStack.getTag();
            if (wandTag.contains("Wand")) {
                wandTag = wandTag.getCompound("Wand");
                wandPropertyInfo.add(new TranslationTextComponent("desc.noitacraft.wand.shuffle", String.valueOf(wandTag.getBoolean("Shuffle"))));
                wandPropertyInfo.add(new TranslationTextComponent("desc.noitacraft.wand.casts", wandTag.getByte("Casts")));
                wandPropertyInfo.add(new TranslationTextComponent("desc.noitacraft.wand.cast_delay", wandTag.getInt("CastDelay") / 20.0));
                wandPropertyInfo.add(new TranslationTextComponent("desc.noitacraft.wand.recharge_time", wandTag.getInt("RechargeTime") / 20.0));
                wandPropertyInfo.add(new TranslationTextComponent("desc.noitacraft.wand.mana_max", wandTag.getInt("ManaMax")));
                wandPropertyInfo.add(new TranslationTextComponent("desc.noitacraft.wand.mana_charge_speed", wandTag.getInt("ManaChargeSpeed")));
                wandPropertyInfo.add(new TranslationTextComponent("desc.noitacraft.wand.capacity", wandTag.getByte("Capacity")));
                wandPropertyInfo.add(new TranslationTextComponent("desc.noitacraft.wand.spread", wandTag.getFloat("Spread")));
                tooltip.addAll(wandPropertyInfo);
                return;
            }
        }
        tooltip.add(new TranslationTextComponent("desc.noitacraft.wand.uninitialized"));
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
