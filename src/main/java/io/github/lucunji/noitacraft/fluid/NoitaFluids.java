package io.github.lucunji.noitacraft.fluid;

import io.github.lucunji.noitacraft.NoitaCraft;
import io.github.lucunji.noitacraft.block.NoitaBlocks;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

import java.awt.*;

@ObjectHolder(NoitaCraft.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class NoitaFluids {
    @ObjectHolder("berserkium") public static FlowingFluid BERSERKIUM;
    @ObjectHolder("flowing_berserkium") public static FlowingFluid FLOWING_BERSERKIUM;

    @SubscribeEvent
    public static void onFluidRegistry(final RegistryEvent.Register<Fluid> event) {
        ForgeFlowingFluid.Properties berserkiumProperties = new ForgeFlowingFluid.Properties(
                () -> BERSERKIUM,
                () -> FLOWING_BERSERKIUM,
                FluidAttributes.builder(
                        new ResourceLocation("block/water_still"),
                        new ResourceLocation("block/water_flow"))
                        .overlay(new ResourceLocation("block/water_overlay"))
                        .color(new Color(241, 97, 63).getRGB() + 0xFF000000)
                .luminosity(3)
        ).block(() -> NoitaBlocks.BERSERKIUM);
        event.getRegistry().registerAll(
                new ForgeFlowingFluid.Source(berserkiumProperties).setRegistryName(NoitaCraft.MOD_ID, "berserkium"),
                new ForgeFlowingFluid.Flowing(berserkiumProperties).setRegistryName(NoitaCraft.MOD_ID, "flowing_berserkium")
        );
    }
}
