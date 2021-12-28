package io.github.lucunji.noitawands.block;

import io.github.lucunji.noitawands.NoitaWands;
import io.github.lucunji.noitawands.effect.NoitaEffects;
import io.github.lucunji.noitawands.fluid.NoitaFluids;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(NoitaWands.MODID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class NoitaBlocks {
     @ObjectHolder("berserkium") public static FlowingFluidBlock BERSERKIUM;
     @ObjectHolder("blood") public static FlowingFluidBlock BLOOD;

    @SubscribeEvent
    public static void onBlockRegistry(final RegistryEvent.Register<Block> event) {
        event.getRegistry().register(new NoitaFluidBlock(() -> NoitaFluids.BERSERKIUM,
                Block.Properties.create(Material.WATER).doesNotBlockMovement().notSolid().hardnessAndResistance(100).noDrops()){
            @Override
            public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
                if (entityIn instanceof LivingEntity) {
                    ((LivingEntity) entityIn).addPotionEffect(new EffectInstance(NoitaEffects.BERSERK, 30));
                }
            }
        }.setRegistryName(NoitaWands.MODID, "berserkium"));

        event.getRegistry().register(new NoitaFluidBlock(() -> NoitaFluids.BLOOD,
                Block.Properties.create(Material.WATER).doesNotBlockMovement().notSolid().hardnessAndResistance(100).noDrops()){
            @Override
            public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
                if (entityIn instanceof LivingEntity) {
                    ((LivingEntity) entityIn).addPotionEffect(new EffectInstance(NoitaEffects.BLOOD, 30));
                }
            }
        }.setRegistryName(NoitaWands.MODID, "blood"));
    }
}
