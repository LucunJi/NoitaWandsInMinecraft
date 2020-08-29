package io.github.lucunji.noitacraft.item.wand;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.MathHelper;

import java.util.Random;

public class WandData {
    private final CompoundNBT wandTag;

    public WandData(ItemStack wandStack) {
        wandTag = wandStack.getOrCreateChildTag("Wand");
    }

    public void randomize(Random rnd) {
        this.setShuffle(rnd.nextBoolean());
        this.setCasts((byte)rnd.nextInt(6));
        this.setCastDelay((int)Math.max(0.0, rnd.nextGaussian() * 20 * (this.isShuffle() ? 1 : 2)));
        this.setRechargeTime((int)Math.max(0.0, rnd.nextGaussian() * 20 * (this.isShuffle() ? 2 : 3)));
        this.setManaMax((int)Math.max(70.0, rnd.nextGaussian() * (this.isShuffle() ? 100 : 175) + (this.isShuffle() ? 300 : 650)));
        this.setManaChargeSpeed((int)Math.max(10.0, rnd.nextGaussian() * (this.isShuffle() ? 25 : 50) + 10));
        this.setCapacity((byte)MathHelper.clamp(rnd.nextGaussian() * (this.isShuffle() ? 1.5 : 2.5) + (this.isShuffle() ? 3 : 8), 1, 16));
        this.setSpread((float)MathHelper.clamp(rnd.nextGaussian() - (this.isShuffle() ? 1 : 0), -10, 10));

        this.setCooldown(0);
        this.setMana(this.getManaMax());
        this.setNumberCasted(0);


    }

    /** Getters **/
    public boolean isShuffle() {
        return wandTag.getBoolean("Shuffle");
    }

    public byte getCasts() {
        return wandTag.getByte("Casts");
    }

    public int getCastDelay() {
        return wandTag.getInt("CastDelay");
    }

    public int getRechargeTime() {
        return wandTag.getInt("RechargeTime");
    }

    public int getManaMax() {
        return wandTag.getInt("ManaMax");
    }

    public int getManaChargeSpeed() {
        return wandTag.getInt("ManaChargeSpeed");
    }

    public byte getCapacity() {
        return wandTag.getByte("Capacity");
    }

    public float getSpread() {
        return wandTag.getFloat("Spread");
    }

    public int getCooldown() {
        return wandTag.getInt("Cooldown");
    }

    public float getMana() {
        return wandTag.getFloat("Mana");
    }

    public int getNumberCasted() {
        return wandTag.getByte("NumberCasted");
    }

    public int getTextureID() {
        return wandTag.getInt("TextureID");
    }

    /** Setters **/
    public void setShuffle(boolean shuffle) {
        wandTag.putBoolean("Shuffle", shuffle);
    }

    public void setCasts(byte casts) {
        wandTag.putByte("Casts", casts);
    }

    public void setCastDelay(int castDelay) {
        wandTag.putInt("CastDelay", castDelay);
    }

    public void setRechargeTime(int rechargeTime) {
        wandTag.putInt("RechargeTime", rechargeTime);
    }

    public void setManaMax(int manaMax) {
        wandTag.putInt("ManaMax", manaMax);
    }

    public void setManaChargeSpeed(int manaChargeSpeed) {
        wandTag.putInt("ManaRechargeSpeed", manaChargeSpeed);
    }

    public void setCapacity(byte capacity) {
        wandTag.putByte("Capacity", capacity);
    }

    public void setSpread(float spread) {
        wandTag.putFloat("Spread", spread);
    }

    public void setCooldown(int cooldown) {
        wandTag.putInt("Cooldown", cooldown);
    }

    public void setMana(float mana) {
        wandTag.putFloat("Mana", mana);
    }

    public void setNumberCasted(int numberCasted) {
        wandTag.putInt("NumberCasted", numberCasted);
    }

    public void setTextureID(int textureID) {
        wandTag.putInt("TextureID", textureID);
    }
}
