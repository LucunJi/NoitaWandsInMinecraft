package io.github.lucunji.noitacraft.setup;

import io.github.lucunji.noitacraft.client.renderer.entity.BombProjectileRenderer;
import io.github.lucunji.noitacraft.client.renderer.entity.SparkProjectileRenderer;
import io.github.lucunji.noitacraft.client.screen.WandScreen;
import io.github.lucunji.noitacraft.entity.NoitaEntityTypes;
import io.github.lucunji.noitacraft.inventory.container.NoitaContainers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientProxy implements IProxy {
    @Override
    public World getClientWorld() {
        return Minecraft.getInstance().world;
    }

    @Override
    public void init() {
        ScreenManager.registerFactory(NoitaContainers.WAND_CONTAINER, WandScreen::new);
        RenderingRegistry.registerEntityRenderingHandler(NoitaEntityTypes.PROJECTILE_SPARK, SparkProjectileRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(NoitaEntityTypes.PROJECTILE_BOMB, BombProjectileRenderer::new);
    }
}
