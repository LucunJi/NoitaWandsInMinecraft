package io.github.lucunji.noitacraft.client.screen;

import io.github.lucunji.noitacraft.NoitaCraft;
import io.github.lucunji.noitacraft.container.NoitaContainers;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = NoitaCraft.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class NoitaScreens {
    @SubscribeEvent
    public static void register(FMLClientSetupEvent event) {
        ScreenManager.registerFactory(NoitaContainers.WAND_CONTAINER, WandScreen::new);
    }
}