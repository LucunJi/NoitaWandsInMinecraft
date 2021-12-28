package io.github.lucunji.noitawands.inventory.container;

import io.github.lucunji.noitawands.NoitaWands;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(NoitaWands.MODID )
public class NoitaContainers {
    @ObjectHolder("wand_container")
    public static ContainerType<WandContainer> WAND_CONTAINER;

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Register {
        @SubscribeEvent
        public static void onContainerRegistry(final RegistryEvent.Register<ContainerType<?>> event) {
            event.getRegistry().register(IForgeContainerType.create((windowId, inv, data) ->
                    new WandContainer(windowId, inv, data.readItemStack())).setRegistryName(NoitaWands.MODID, "wand_container"));
        }
    }
}
