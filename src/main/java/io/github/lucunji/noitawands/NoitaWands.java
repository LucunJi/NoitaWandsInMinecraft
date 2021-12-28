package io.github.lucunji.noitawands;

import io.github.lucunji.noitawands.setup.ClientProxy;
import io.github.lucunji.noitawands.setup.IProxy;
import io.github.lucunji.noitawands.setup.ModSetup;
import io.github.lucunji.noitawands.setup.ServerProxy;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("noitawands")
public class NoitaWands {
    private static final Logger LOGGER = LogManager.getLogger();

    public static final String MODID = "noitawands";
    public static ModSetup SETUP = new ModSetup();
    public static IProxy PROXY = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> ServerProxy::new);

    public NoitaWands() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    }

    private void setup(final FMLCommonSetupEvent event) {
        LOGGER.info("Noita Craft");
        SETUP.init();
        PROXY.init();
    }
}
