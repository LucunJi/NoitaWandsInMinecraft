package io.github.lucunji.noitacraft.item;

import io.github.lucunji.noitacraft.NoitaCraft;
import io.github.lucunji.noitacraft.spell.ISpellEnum;
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
    protected final ISpellEnum spell;

    public SpellItem(Properties properties, ISpellEnum spell) {
        super(properties.maxStackSize(1).group(NoitaCraft.SETUP.SPELL_GROUP));
        this.spell = spell;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent(this.getTranslationKey() + ".detail"));
        tooltip.add(new TranslationTextComponent("desc.noitacraft.spell.mana_drain", spell.getManaDrain()));
        tooltip.add(new TranslationTextComponent("desc.noitacraft.spell.cast_delay", spell.getCastDelay() / 20.0));
        tooltip.add(new TranslationTextComponent("desc.noitacraft.spell.recharge_time", spell.getRechargeTime() / 20.0));
//        if (spell instanceof StaticSpell && ((StaticSpell) spell).uses > -1) {
//            tooltip.add(new TranslationTextComponent("desc.noitacraft.spell.uses_remain", stack.getDamage()));
//        }
    }

    public ISpellEnum getSpell() {
        return spell;
    }
}
