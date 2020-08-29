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
            items.removeIf(itemStack -> itemStack.getItem() instanceof WandItem);
            items.add(DefaultWands.HANDGUN);
            items.add(DefaultWands.BOMB_WAND);
        }
    }

    /******************** Cast spells ********************/

    @Override
    public void onUsingTick(ItemStack stack, LivingEntity player, int count) {
        if (!player.world.isRemote() && player instanceof ServerPlayerEntity) {
            if (!player.isShiftKeyDown()) {
                this.cast(player.world, ((ServerPlayerEntity) player), stack);
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
        WandData wandData = new WandData(wandStack);
        if (wandData.getCooldown() > 0) return;
        WandCastingHandler castingHandler = new WandCastingHandler(wandStack, wandData, new WandInventory(wandStack));
        castingHandler.cast(world, caster).forEach(world::addEntity);
    }

    /**
     * Cooldown and Mana regen.
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
        wandPropertyInfo.add(new TranslationTextComponent("desc.noitacraft.wand.shuffle", String.valueOf(wandData.isShuffle())));
        wandPropertyInfo.add(new TranslationTextComponent("desc.noitacraft.wand.casts", wandData.getCasts()));
        wandPropertyInfo.add(new TranslationTextComponent("desc.noitacraft.wand.cast_delay", wandData.getCastDelay() / 20.0));
        wandPropertyInfo.add(new TranslationTextComponent("desc.noitacraft.wand.recharge_time", wandData.getRechargeTime() / 20.0));
        wandPropertyInfo.add(new TranslationTextComponent("desc.noitacraft.wand.mana_max", wandData.getManaMax()));
        wandPropertyInfo.add(new TranslationTextComponent("desc.noitacraft.wand.mana_charge_speed", wandData.getManaChargeSpeed()));
        wandPropertyInfo.add(new TranslationTextComponent("desc.noitacraft.wand.capacity", wandData.getCapacity()));
        wandPropertyInfo.add(new TranslationTextComponent("desc.noitacraft.wand.spread", wandData.getSpread()));
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
