package io.github.lucunji.noitacraft.item;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTTypes;
import net.minecraft.util.math.MathHelper;

import java.util.Random;

public class WandProperty {
    private boolean isShuffle;
    private byte casts;
    private int castDelay;
    private int rechargeTime;
    private int manaMax;
    private int manaChargeSpeed;
    private byte capacity;
    private float spread;

    private int mana;
    private int numberCasted;

    private WandProperty(Random random) {
        this.isShuffle = random.nextBoolean();
        this.casts = (byte) random.nextInt(6);
        this.castDelay = (int)Math.max(0.0, random.nextGaussian() * 20 * (isShuffle? 1 : 2));
        this.rechargeTime = (int)Math.max(0.0, random.nextGaussian() * 20 * (isShuffle? 2 : 3));
        this.manaMax = (int)Math.max(70.0, random.nextGaussian() * (isShuffle? 100 : 175) + (isShuffle? 300 : 650));
        this.manaChargeSpeed = (int)Math.max(10.0, random.nextGaussian() * (isShuffle? 25 : 50) + 10);
        this.capacity = (byte) MathHelper.clamp(random.nextGaussian() * (isShuffle? 1.5 : 2.5) + (isShuffle? 3 : 8), 1, 16);
        this.spread = (float)MathHelper.clamp(random.nextGaussian() - (isShuffle? 1 : 0), -10, 10);

        this.mana = manaMax;
        this.numberCasted = 0;
    }

    /**
     * Get properties from an item stack of wand.
     * @return a WandProperty. Missing tags will be randomly initialized.
     */
    public static WandProperty getProperty(ItemStack wandStack, Random random) {
        boolean needFlush = false;
        WandProperty property = new WandProperty(random);
        if (wandStack.hasTag()) {
            CompoundNBT wandTag = wandStack.getTag();
            if (wandTag.contains("Wand")) {
                wandTag = wandTag.getCompound("Wand");
                if (wandTag.contains("Shuffle")) property.isShuffle = wandTag.getBoolean("Shuffle"); else needFlush = true;
                if (wandTag.contains("Casts")) property.casts = wandTag.getByte("Casts"); else needFlush = true;
                if (wandTag.contains("CastDelay")) property.castDelay = wandTag.getInt("CastDelay"); else needFlush = true;
                if (wandTag.contains("RechargeTime")) property.rechargeTime = wandTag.getInt("RechargeTime"); else needFlush = true;
                if (wandTag.contains("ManaMax")) property.manaMax = wandTag.getInt("ManaMax"); else needFlush = true;
                if (wandTag.contains("ManaChargeSpeed")) property.manaChargeSpeed = wandTag.getInt("ManaChargeSpeed"); else needFlush = true;
                if (wandTag.contains("Capacity")) property.capacity = wandTag.getByte("Capacity"); else needFlush = true;
                if (wandTag.contains("Spread")) property.spread = wandTag.getFloat("Spread"); else needFlush = true;

                if (wandTag.contains("Mana")) property.mana = wandTag.getInt("Mana"); else needFlush = true;
                if (wandTag.contains("NumberCasted")) property.numberCasted = wandTag.getInt("NumberCasted"); else needFlush = true;
            } else {
                needFlush = true;
            }
        } else {
            needFlush = true;
        }
        if (needFlush) {
            property.writeProperty(wandStack);
        }
        return property;
    }

    public void writeProperty(ItemStack wandStack) {
        CompoundNBT tag = wandStack.getTag();
        if (tag == null) tag = new CompoundNBT();
        CompoundNBT wandNBT = new CompoundNBT();
        wandNBT.putBoolean("Shuffle", this.isShuffle);
        wandNBT.putByte("Casts", this.casts);
        wandNBT.putInt("CastDelay", this.castDelay);
        wandNBT.putInt("RechargeTime", this.rechargeTime);
        wandNBT.putInt("ManaMax", this.manaMax);
        wandNBT.putInt("ManaChargeSpeed", this.manaChargeSpeed);
        wandNBT.putByte("Capacity", this.capacity);
        wandNBT.putFloat("Spread", this.spread);

        wandNBT.putInt("Mana", this.mana);
        wandNBT.putInt("NumberCasted", this.numberCasted);
        tag.put("Wand", wandNBT);
        wandStack.setTag(tag);
    }
}
