package io.github.lucunji.noitacraft.item;

import io.github.lucunji.noitacraft.NoitaCraft;
import net.minecraft.item.Item;

public class BaseItem extends Item {
    public BaseItem(Properties properties) {
        super(properties.group(NoitaCraft.SETUP.ITEMGROUP));
    }
}