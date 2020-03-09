package io.github.lucunji.noitacraft.item;

import io.github.lucunji.noitacraft.entity.NoitaEntityTypes;
import io.github.lucunji.noitacraft.entity.projectile.SparkProjectile;
import io.github.lucunji.noitacraft.inventory.container.WandContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class WandItem extends BaseItem {
    public WandItem(Properties properties) {
        super(properties.maxStackSize(1));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemStack = playerIn.getHeldItem(handIn);
        if (!worldIn.isRemote() && playerIn instanceof ServerPlayerEntity) {
            if (handIn == Hand.MAIN_HAND && itemStack.getItem().equals(NoitaItems.WAND)) {
                if (playerIn.isShiftKeyDown()) {
                    NetworkHooks.openGui((ServerPlayerEntity) playerIn, new WandContainerProvider(itemStack), buffer -> buffer.writeItemStack(itemStack));
                } else {
                    cast(worldIn, playerIn, itemStack);
                }
                return ActionResult.resultSuccess(itemStack);
            }
        }
        return ActionResult.resultFail(itemStack);
    }

    private static void cast(World world, PlayerEntity caster, ItemStack wandStack) {
        SparkProjectile sparkProjectile = new SparkProjectile(NoitaEntityTypes.PROJECTILE_SPARK, caster, world);
        sparkProjectile.shoot(caster, caster.rotationPitch, caster.rotationYaw, 1.5f, 1.0f);
        world.addEntity(sparkProjectile);
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
