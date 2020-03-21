package io.github.lucunji.noitacraft.setup;

import net.minecraft.world.World;

public class ServerProxy implements IProxy {
    @Override
    public World getClientWorld() {
        throw new IllegalStateException("Client only!");
    }

    @Override
    public void init() {
    }
}
