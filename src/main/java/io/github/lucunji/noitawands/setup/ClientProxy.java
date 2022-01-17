package io.github.lucunji.noitawands.setup;

import io.github.lucunji.noitawands.NoitaWands;
import io.github.lucunji.noitawands.client.renderer.entity.BombSpellRenderer;
import io.github.lucunji.noitawands.client.renderer.entity.EnergySphereSpellRenderer;
import io.github.lucunji.noitawands.client.renderer.entity.SparkBoltDoubleTriggerSpellRenderer;
import io.github.lucunji.noitawands.client.renderer.entity.SparkBoltSpellRenderer;
import io.github.lucunji.noitawands.client.screen.WandScreen;
import io.github.lucunji.noitawands.entity.NoitaEntityTypes;
import io.github.lucunji.noitawands.inventory.container.NoitaContainers;
import io.github.lucunji.noitawands.item.ModItems;
import io.github.lucunji.noitawands.item.wand.WandData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientProxy implements IProxy {
    @Override
    public World getClientWorld() {
        return Minecraft.getInstance().world;
    }

    @Override
    public void init() {
        ItemModelsProperties.registerProperty(
                ModItems.WAND,
                new ResourceLocation(NoitaWands.MODID, "texture_id"),
                (itemStack, world, entity) -> new WandData(itemStack).getTextureID());

        ScreenManager.registerFactory(NoitaContainers.WAND_CONTAINER, WandScreen::new);

        RenderingRegistry.registerEntityRenderingHandler(NoitaEntityTypes.SPELL_SPARK_BOLT, SparkBoltSpellRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(NoitaEntityTypes.SPELL_SPARK_BOLT_DOUBLE_TRIGGER, SparkBoltDoubleTriggerSpellRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(NoitaEntityTypes.SPELL_BOMB, BombSpellRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(NoitaEntityTypes.SPELL_ENERGY_SPHERE, EnergySphereSpellRenderer::new);
    }
}
