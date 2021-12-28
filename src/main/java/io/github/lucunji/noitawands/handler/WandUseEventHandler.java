package io.github.lucunji.noitawands.handler;

import io.github.lucunji.noitawands.item.wand.WandItem;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class WandUseEventHandler {
    @SubscribeEvent
    public static void onLivingEntityUseItemStart(final LivingEntityUseItemEvent.Start event) {
        if (event.getItem().getItem() instanceof WandItem) {
            event.setDuration(2);
        }
    }

    @SubscribeEvent
    public static void onLivingEntityUseItemTick(final LivingEntityUseItemEvent.Tick event) {
        if (event.getItem().getItem() instanceof WandItem) {
            event.setDuration(2);
        }
    }

    /**
     * Cancel out slowdown of movement caused by using wands
     */
    @SubscribeEvent
    public static void onInputUpdateEvent(final InputUpdateEvent event) {
        PlayerEntity player = event.getPlayer();
        if (player instanceof ClientPlayerEntity && player.isHandActive() && player.getActiveItemStack().getItem() instanceof WandItem) {
            ((ClientPlayerEntity) player).movementInput.moveStrafe *= 5F;
            ((ClientPlayerEntity) player).movementInput.moveForward *= 5F;
        }
    }
}
