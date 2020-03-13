package io.github.lucunji.noitacraft.util;

import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ReportedException;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBTType;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NumberNBT;

import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("ConstantConditions")
public class NBTHelper {

    public enum NBTTypes {
        END, BYTE, SHORT, INT, LONG, FLOAT, DOUBLE, BYTE_ARRAY, STRING, LIST, COMPOUND, INT_ARRAY, LONG_ARRAY
    }

    public static Optional<Byte> getByte(CompoundNBT compoundNBT, String key) {
        try {
            if (compoundNBT.contains(key, 99)) {
                return Optional.of(((NumberNBT)compoundNBT.get(key)).getByte());
            }
        } catch (ClassCastException ignored) {
        }

        return Optional.empty();
    }

    public static Optional<Short> getShort(CompoundNBT compoundNBT, String key) {
        try {
            if (compoundNBT.contains(key, 99)) {
                return Optional.of(((NumberNBT)compoundNBT.get(key)).getShort());
            }
        } catch (ClassCastException ignored) {
        }

        return Optional.empty();
    }

    public static Optional<Integer> getInt(CompoundNBT compoundNBT, String key) {
        try {
            if (compoundNBT.contains(key, 99)) {
                return Optional.of(((NumberNBT)compoundNBT.get(key)).getInt());
            }
        } catch (ClassCastException ignored) {
        }
        return Optional.empty();
    }

    public static Optional<Long> getLong(CompoundNBT compoundNBT, String key) {
        try {
            if (compoundNBT.contains(key, 99)) {
                return Optional.of(((NumberNBT) compoundNBT.get(key)).getLong());
            }
        } catch (ClassCastException ignored) {
        }

        return Optional.empty();
    }

    public static Optional<Float> getFloat(CompoundNBT compoundNBT, String key) {
        try {
            if (compoundNBT.contains(key, 99)) {
                return Optional.of(((NumberNBT) compoundNBT.get(key)).getFloat());
            }
        } catch (ClassCastException ignored) {
        }

        return Optional.empty();
    }

    public static Optional<Double> getDouble(CompoundNBT compoundNBT, String key) {
        try {
            if (compoundNBT.contains(key, 99)) {
                return Optional.of(((NumberNBT) compoundNBT.get(key)).getDouble());
            }
        } catch (ClassCastException ignored) {
        }

        return Optional.empty();
    }

    public static Optional<Boolean> getBoolean(CompoundNBT compoundNBT, String key) {
        return getByte(compoundNBT, key).flatMap(aByte -> Optional.of(aByte != 0));
    }

    public static Optional<UUID> getUUID(CompoundNBT compoundNBT, String key) {
        return getLong(compoundNBT, key + "Most").flatMap(aLongMost -> getLong(compoundNBT, key + "Least").flatMap(aLongLeast -> Optional.of(new UUID(aLongMost, aLongLeast))));
    }

    public static Optional<String> getString(CompoundNBT compoundNBT, String key) {
        try {
            if (compoundNBT.contains(key, NBTTypes.STRING.ordinal())) {
                return Optional.of(compoundNBT.get(key).getString());
            }
        } catch (ClassCastException ignored) {
        }

        return Optional.empty();
    }

    public static Optional<ListNBT> getList(CompoundNBT compoundNBT, String key, NBTTypes elementType) {
        try {
            if (compoundNBT.getTagId(key) == NBTTypes.LIST.ordinal()) {
                ListNBT listnbt = (ListNBT)compoundNBT.get(key);
                if (!listnbt.isEmpty() && listnbt.getTagType() != elementType.ordinal()) {
                    return Optional.empty();
                }

                return Optional.of(listnbt);
            }
        } catch (ClassCastException classcastexception) {
            throw new ReportedException(crashReport(compoundNBT, key, ListNBT.TYPE, classcastexception));
        }

        return Optional.empty();
    }

    public static Optional<CompoundNBT> getCompound(CompoundNBT compoundNBT, String key) {
        try {
            if (compoundNBT.contains(key, NBTTypes.COMPOUND.ordinal())) {
                return Optional.of((CompoundNBT) compoundNBT.get(key));
            }
        } catch (ClassCastException classcastexception) {
            throw new ReportedException(crashReport(compoundNBT, key, CompoundNBT.TYPE, classcastexception));
        }

        return Optional.empty();
    }

    public static Optional<CompoundNBT> getCompound(ItemStack itemStack) {
        return itemStack.hasTag() ? Optional.of(itemStack.getTag()) : Optional.empty();
    }

    private static CrashReport crashReport(CompoundNBT compoundNBT, String key, INBTType<?> type, ClassCastException e) {
        CrashReport crashreport = CrashReport.makeCrashReport(e, "Reading NBT data");
        CrashReportCategory crashreportcategory = crashreport.makeCategoryDepth("Corrupt NBT tag", 1);
        crashreportcategory.addDetail("Tag type found", () -> compoundNBT.get(key).getType().func_225648_a_());
        crashreportcategory.addDetail("Tag type expected", type::func_225648_a_);
        crashreportcategory.addDetail("Tag name", key);
        return crashreport;
    }
}
