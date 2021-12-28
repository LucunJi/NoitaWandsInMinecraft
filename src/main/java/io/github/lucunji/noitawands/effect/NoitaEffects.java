package io.github.lucunji.noitawands.effect;

import io.github.lucunji.noitawands.NoitaWands;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

import java.awt.*;

@ObjectHolder(NoitaWands.MODID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class NoitaEffects {
    public static int BERSERKIUM_COLOR = new Color(241, 111, 92).getRGB();
    public static int BLOOD_COLOR = new Color(142, 14, 7).getRGB();

    public static String BERSERKIUM_UUID = "7fcd7bf0-bbfe-44f6-badd-abe53a815c9e";

    @ObjectHolder("berserk") public static Effect BERSERK;
    @ObjectHolder("blood") public static Effect BLOOD;

    @SubscribeEvent
    public static void onEffectRegistry(final RegistryEvent.Register<Effect> event) {
        event.getRegistry().register(new ModEffect(EffectType.BENEFICIAL, BERSERKIUM_COLOR).addAttributesModifier(SharedMonsterAttributes.ATTACK_DAMAGE, BERSERKIUM_UUID, 2, AttributeModifier.Operation.MULTIPLY_TOTAL).setRegistryName(NoitaWands.MODID, "berserk"));
        event.getRegistry().register(new ModEffect(EffectType.BENEFICIAL, BLOOD_COLOR).setRegistryName(NoitaWands.MODID, "blood"));
    }
}
