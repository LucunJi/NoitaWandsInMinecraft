package io.github.lucunji.noitacraft.inventory.container;

import io.github.lucunji.noitacraft.NoitaCraft;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

public class NoitaContainers {
    @ObjectHolder(NoitaCraft.MOD_ID + ":wand_container")
    public static ContainerType<WandContainer> WAND_CONTAINER;

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Register {
        @SubscribeEvent
        public static void onContainerRegistry(final RegistryEvent.Register<ContainerType<?>> event) {
            event.getRegistry().register(IForgeContainerType.create((windowId, inv, data) ->
                    new WandContainer(windowId, inv, data.readItemStack())).setRegistryName(NoitaCraft.MOD_ID, "wand_container"));
        }
    }
}
