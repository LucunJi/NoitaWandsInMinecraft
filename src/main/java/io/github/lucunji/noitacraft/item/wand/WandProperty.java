package io.github.lucunji.noitacraft.item.wand;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nullable;
import java.util.Random;

public class WandProperty {
    private static final Random RNG = new Random(0);

    private boolean isShuffle;
    private byte casts;
    private int castDelay;
    private int rechargeTime;
    private int manaMax;
    private int manaChargeSpeed;
    private byte capacity;
    private float spread;

    private int cooldown;
    private float mana;
    private int numberCasted;

    private int textureID;

    private final ItemStack wandStack;

    private WandProperty(ItemStack wandStack, Random random) {
        this.wandStack = wandStack;

        this.isShuffle = random.nextBoolean();
        this.casts = (byte) random.nextInt(6);
        this.castDelay = (int)Math.max(0.0, random.nextGaussian() * 20 * (isShuffle? 1 : 2));
        this.rechargeTime = (int)Math.max(0.0, random.nextGaussian() * 20 * (isShuffle? 2 : 3));
        this.manaMax = (int)Math.max(70.0, random.nextGaussian() * (isShuffle? 100 : 175) + (isShuffle? 300 : 650));
        this.manaChargeSpeed = (int)Math.max(10.0, random.nextGaussian() * (isShuffle? 25 : 50) + 10);
        this.capacity = (byte) MathHelper.clamp(random.nextGaussian() * (isShuffle? 1.5 : 2.5) + (isShuffle? 3 : 8), 1, 16);
        this.spread = (float)MathHelper.clamp(random.nextGaussian() - (isShuffle? 1 : 0), -10, 10);

        this.cooldown = 0;
        this.mana = manaMax;
        this.numberCasted = 0;

        this.textureID = random.nextInt(10);
    }

    @SuppressWarnings("ConstantConditions")
    @Nullable
    public static WandProperty getPropertyNullable(ItemStack wandStack) {
        if (wandStack.hasTag()) {
            CompoundNBT wandTag = wandStack.getTag();
            if (wandTag.contains("Wand")) {
                wandTag = wandTag.getCompound("Wand");
                if (!wandTag.contains("Shuffle")) return null;
                if (!wandTag.contains("Casts")) return null;
                if (!wandTag.contains("CastDelay")) return null;
                if (!wandTag.contains("RechargeTime")) return null;
                if (!wandTag.contains("ManaMax")) return null;
                if (!wandTag.contains("ManaChargeSpeed")) return null;
                if (!wandTag.contains("Capacity")) return null;
                if (!wandTag.contains("Spread")) return null;

                if (!wandTag.contains("Cooldown")) return null;
                if (!wandTag.contains("Mana")) return null;
                if (!wandTag.contains("NumberCasted")) return null;

                if (!wandTag.contains("TextureID")) return null;
                return getPropertyNotNull(wandStack, RNG);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Get properties from an item stack of wand.
     * @return a WandProperty. Missing tags will be randomly initialized.
     */
    @SuppressWarnings("ConstantConditions")
    public static WandProperty getPropertyNotNull(ItemStack wandStack, Random random) {
        boolean needFlush = false;
        WandProperty property = new WandProperty(wandStack, random);
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

                if (wandTag.contains("Cooldown")) property.cooldown = wandTag.getInt("Cooldown"); else needFlush = true;
                if (wandTag.contains("Mana")) property.mana = wandTag.getFloat("Mana"); else needFlush = true;
                if (wandTag.contains("NumberCasted")) property.numberCasted = wandTag.getInt("NumberCasted"); else needFlush = true;

                if (wandTag.contains("TextureID")) property.textureID = wandTag.getInt("TextureID"); else needFlush = true;
            } else {
                needFlush = true;
            }
        } else {
            needFlush = true;
        }
        if (needFlush) {
            property.writeProperty();
        }
        return property;
    }

    public void writeProperty() {
        CompoundNBT tag = wandStack.getTag();
        if (tag == null) {
            tag = new CompoundNBT();
            wandStack.setTag(tag);
        }
        CompoundNBT wandNBT = new CompoundNBT();
        if (tag.contains("Wand")) {
            wandNBT = tag.getCompound("Wand");
        } else {
            tag.put("Wand", wandNBT);
        }
        wandNBT.putBoolean("Shuffle", this.isShuffle);
        wandNBT.putByte("Casts", this.casts);
        wandNBT.putInt("CastDelay", this.castDelay);
        wandNBT.putInt("RechargeTime", this.rechargeTime);
        wandNBT.putInt("ManaMax", this.manaMax);
        wandNBT.putInt("ManaChargeSpeed", this.manaChargeSpeed);
        wandNBT.putByte("Capacity", this.capacity);
        wandNBT.putFloat("Spread", this.spread);

        wandNBT.putInt("Cooldown", this.cooldown);
        wandNBT.putFloat("Mana", this.mana);
        wandNBT.putInt("NumberCasted", this.numberCasted);

        wandNBT.putInt("TextureID", this.textureID);
    }

    public boolean isShuffle() {
        return this.isShuffle;
    }

    public int getCooldown() {
        return cooldown;
    }

    public float getMana() {
        return mana;
    }

    public int getManaMax() {
        return manaMax;
    }

    public int getManaChargeSpeed() {
        return manaChargeSpeed;
    }

    public int getCastDelay() {
        return castDelay;
    }

    public int getRechargeTime() {
        return rechargeTime;
    }

    public int getNumberCasted() {
        return numberCasted;
    }

    public byte getCapacity() {
        return capacity;
    }

    public void setMana(float mana) {
        this.setMana(mana, true);
    }

    public void setMana(float mana, boolean flush) {
        this.mana = MathHelper.clamp(mana, 0f, manaMax);
        if(flush) this.writeProperty();
    }

    public void setCooldown(int cooldown) {
        this.setCooldown(cooldown, true);
    }

    public void setCooldown(int cooldown, boolean flush) {
        this.cooldown = Math.max(cooldown, 0);
        if (flush) this.writeProperty();
    }

    public void setNumberCasted(int numberCasted) {
        this.setNumberCasted(numberCasted, true);
    }

    public void setNumberCasted(int numberCasted, boolean flush) {
        this.numberCasted = numberCasted;
        if (flush) this.writeProperty();
    }
}
