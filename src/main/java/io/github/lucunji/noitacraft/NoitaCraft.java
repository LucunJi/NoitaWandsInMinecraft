package io.github.lucunji.noitacraft;

import io.github.lucunji.noitacraft.setup.ClientProxy;
import io.github.lucunji.noitacraft.setup.IProxy;
import io.github.lucunji.noitacraft.setup.ModSetup;
import io.github.lucunji.noitacraft.setup.ServerProxy;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(NoitaCraft.MOD_ID)
public class NoitaCraft {

    public static IProxy PROXY = DistExecutor.runForDist(() -> ClientProxy::new, () -> ServerProxy::new);

    public static final String MOD_ID = "noitacraft";

    public static ModSetup SETUP = new ModSetup();

    private static final Logger LOGGER = LogManager.getLogger();

    public NoitaCraft() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    }

    private void setup(final FMLCommonSetupEvent event) {
        LOGGER.info("Noita Craft");
        SETUP.init();
        PROXY.init();
    }
}
