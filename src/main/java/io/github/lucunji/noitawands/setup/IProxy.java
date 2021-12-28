package io.github.lucunji.noitawands.setup;

import net.minecraft.world.World;

public interface IProxy {
    World getClientWorld();

    void init();
}
