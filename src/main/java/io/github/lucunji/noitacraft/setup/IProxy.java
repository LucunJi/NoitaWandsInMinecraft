package io.github.lucunji.noitacraft.setup;

import net.minecraft.world.World;

public interface IProxy {
    World getClientWorld();

    void init();
}
