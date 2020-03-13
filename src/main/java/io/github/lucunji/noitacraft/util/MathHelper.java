package io.github.lucunji.noitacraft.util;

import net.minecraft.util.Direction;
import net.minecraft.util.math.Vec3d;

public class MathHelper {
    public static Vec3d reflectByAxis(Vec3d vec3d, Direction.Axis axis) {
        switch (axis) {
            case X:
                return new Vec3d(-vec3d.getX(), vec3d.getY(), vec3d.getZ());
            case Y:
                return new Vec3d(vec3d.getX(), -vec3d.getY(), vec3d.getZ());
            case Z:
                return new Vec3d(vec3d.getX(), vec3d.getY(), -vec3d.getZ());
            default:
                throw new IllegalArgumentException("Invalid axis.");
        }
    }
}
