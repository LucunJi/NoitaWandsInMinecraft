package io.github.lucunji.noitacraft.item;

import io.github.lucunji.noitacraft.spell.ProjectileSpell;
import io.github.lucunji.noitacraft.spell.SpellAbstract;
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
    protected final SpellAbstract spell;

    public SpellItem(Properties properties, SpellAbstract spell) {
        super(properties.maxStackSize(1));
        this.spell = spell;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent(this.getTranslationKey() + ".detail"));
        tooltip.add(new TranslationTextComponent("desc.noitacraft.spell.mana_drain").appendText(spell.manaDrain + ""));
        tooltip.add(new TranslationTextComponent("desc.noitacraft.spell.cast_delay").appendText((spell.castDelay/20f) + "s"));
        tooltip.add(new TranslationTextComponent("desc.noitacraft.spell.recharge_time").appendText((spell.rechargeTime/20f) + "s"));
//        if (spell instanceof ProjectileSpell) {
//            ProjectileSpell projectileSpell = ((ProjectileSpell) spell);
//            tooltip.add(new TranslationTextComponent("desc.noitacraft.spell.recharge_time").appendText(spell.rechargeTime + ""));
//        }
        if (spell instanceof StaticSpell && ((StaticSpell) spell).uses > -1) {
            tooltip.add(new TranslationTextComponent("desc.noitacraft.spell.uses_remain").appendText(stack.getDamage() + ""));
        }
    }
}
