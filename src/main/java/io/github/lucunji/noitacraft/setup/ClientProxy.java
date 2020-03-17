package io.github.lucunji.noitacraft.setup;

import io.github.lucunji.noitacraft.client.renderer.entity.BombSpellRenderer;
import io.github.lucunji.noitacraft.client.renderer.entity.EnergySphereSpellRenderer;
import io.github.lucunji.noitacraft.client.renderer.entity.SparkBoltDoubleTriggerSpellRenderer;
import io.github.lucunji.noitacraft.client.renderer.entity.SparkBoltSpellRenderer;
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

//        Minecraft.getInstance().particles.registerFactory(NoitaParticles.TRAIL, TrailParticle.TrailParticleFactory::new);

        RenderingRegistry.registerEntityRenderingHandler(NoitaEntityTypes.SPELL_SPARK_BOLT, SparkBoltSpellRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(NoitaEntityTypes.SPELL_SPARK_BOLT_DOUBLE_TRIGGER, SparkBoltDoubleTriggerSpellRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(NoitaEntityTypes.SPELL_BOMB, BombSpellRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(NoitaEntityTypes.SPELL_ENERGY_SPHERE, EnergySphereSpellRenderer::new);
    }
}
