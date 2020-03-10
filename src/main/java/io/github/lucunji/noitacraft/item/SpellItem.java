package io.github.lucunji.noitacraft.item;

import io.github.lucunji.noitacraft.spell.SpellBase;
import io.github.lucunji.noitacraft.spell.StaticSpell;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class SpellItem extends BaseItem {
    protected final SpellBase spell;

    public SpellItem(Properties properties, SpellBase spell) {
        super(properties.maxStackSize(1));
        this.spell = spell;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent(this.getTranslationKey() + ".detail"));
        tooltip.add(new TranslationTextComponent("desc.noitacraft.spell.mana_drain", spell.manaDrain));
        tooltip.add(new TranslationTextComponent("desc.noitacraft.spell.cast_delay", spell.castDelay / 20.0));
        tooltip.add(new TranslationTextComponent("desc.noitacraft.spell.recharge_time", spell.rechargeTime / 20.0));
        if (spell instanceof StaticSpell && ((StaticSpell) spell).uses > -1) {
            tooltip.add(new TranslationTextComponent("desc.noitacraft.spell.uses_remain", stack.getDamage()));
        }
    }

    public SpellBase getSpell() {
        return spell;
    }
}
