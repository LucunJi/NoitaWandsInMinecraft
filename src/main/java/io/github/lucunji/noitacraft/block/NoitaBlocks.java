package io.github.lucunji.noitacraft.block;

import io.github.lucunji.noitacraft.NoitaCraft;
import io.github.lucunji.noitacraft.fluid.NoitaFluids;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(NoitaCraft.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class NoitaBlocks {
     @ObjectHolder("berserkium") public static FlowingFluidBlock BERSERKIUM;

    @SubscribeEvent
    public static void onBlockRegistry(final RegistryEvent.Register<Block> event) {
        event.getRegistry().register(new FlowingFluidBlock(() -> NoitaFluids.BERSERKIUM,
                Block.Properties.create(Material.WATER).doesNotBlockMovement().notSolid().hardnessAndResistance(100).noDrops()) {
            @Override
            public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
                if (entityIn instanceof LivingEntity) {
                    ((LivingEntity) entityIn).addPotionEffect(new EffectInstance(Effects.STRENGTH, 30));
                }
            }
        }
                .setRegistryName(NoitaCraft.MOD_ID, "berserkium"));
    }
}
