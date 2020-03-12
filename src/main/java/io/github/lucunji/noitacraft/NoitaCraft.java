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

    public static final String SOMETHING =
            "Verum sine mendacio, certum, et verissimum.\n" +
            "Quod est inferius, est sicut quod est superius.\n" +
            "Et quod est superius, est sicut quod est inferius, ad perpetranda miracula rei unius.\n" +
            "Et sicut res omnes fuerunt ab uno, meditatione [sic] unius, sic omnes res natae ab hac una re, adaptatione.\n" +
            "Pater eius est Sol, mater eius est Luna.\n" +
            "Portavit illud ventus in ventre suo.\n" +
            "Nutrix eius terra est.\n" +
            "Pater omnis telesmi totius mundi est hic.\n" +
            "Vis eius integra est, si versa fuerit in terram.\n" +
            "Separabis terram ab igne, subtile ab spisso, suaviter cum magno ingenio.\n" +
            "Ascendit a terra in coelum, iterumque descendit in terram, et recipit vim superiorum et inferiorum.\n" +
            "Sic habebis gloriam totius mundi.\n" +
            "Ideo fugiet a te omnis obscuritas.\n" +
            "Haec est totius fortitudinis fortitudo fortis, quia vincet omnem rem subtilem, omnemque solidam penetrabit.\n" +
            "Sic mundus creatus est.\n" +
            "Hinc erunt adaptationes mirabiles, quarum modus hic est.\n" +
            "Itaque vocatus sum Hermes Trismegistus, habens tres partes philosophiae totius mundi.\n" +
            "Completum est, quod dixi de operatione Solis. ";

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
