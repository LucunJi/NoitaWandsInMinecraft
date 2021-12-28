package io.github.lucunji.noitawands.util;

import io.github.lucunji.noitawands.spell.ISpellEnum;
import io.github.lucunji.noitawands.spell.SpellManager;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ReportedException;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import org.apache.logging.log4j.LogManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

@SuppressWarnings("ConstantConditions")
public class NBTHelper {

    public static List<ISpellEnum> spellListFromNBT(ListNBT listNBT) {
        List<ISpellEnum> spellEnumList = new ArrayList<>();
        for (int i = 0; i < listNBT.size(); ++i) {
            String spellName = listNBT.getString(i);
            ISpellEnum spellEnum = SpellManager.getSpellByName(spellName);
            if (spellEnum != null) {
                spellEnumList.add(spellEnum);
                LogManager.getLogger().warn("Not matching spell for name: \"" + spellName + "\"");
            }
        }
        return spellEnumList;
    }

    public static ListNBT spellNBTFromList(List<ISpellEnum> spellEnumList) {
        ListNBT listNBT = new ListNBT();
        spellEnumList.stream().map(ISpellEnum::name).map(StringNBT::valueOf).forEach(listNBT::add);
        return listNBT;
    }

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

    public static CompoundNBT makeCompound(Consumer<CompoundNBT> modifier) {
        CompoundNBT compoundNBT = new CompoundNBT();
        modifier.accept(compoundNBT);
        return compoundNBT;
    }

    public static ItemStack makeItemWithTag(Item item, int count, CompoundNBT compoundNBT) {
        ItemStack itemStack = new ItemStack(item, count);
        itemStack.setTag(compoundNBT);
        return itemStack;
    }

    private static CrashReport crashReport(CompoundNBT compoundNBT, String key, INBTType<?> type, ClassCastException e) {
        CrashReport crashreport = CrashReport.makeCrashReport(e, "Reading NBT data");
        CrashReportCategory crashreportcategory = crashreport.makeCategoryDepth("Corrupt NBT tag", 1);
//        crashreportcategory.addDetail("Tag type found", () -> compoundNBT.get(key).getType().getName().func_225648_a_());
//        crashreportcategory.addDetail("Tag type expected", type::func_225648_a_);
        crashreportcategory.addDetail("Tag name", key);
        return crashreport;
    }
}
