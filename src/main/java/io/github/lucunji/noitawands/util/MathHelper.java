package io.github.lucunji.noitawands.util;

import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3d;

public class MathHelper {
    public static Vector3d reflectByAxis(Vector3d vec3d, Direction.Axis axis) {
        switch (axis) {
            case X:
                return new Vector3d(-vec3d.getX(), vec3d.getY(), vec3d.getZ());
            case Y:
                return new Vector3d(vec3d.getX(), -vec3d.getY(), vec3d.getZ());
            case Z:
                return new Vector3d(vec3d.getX(), vec3d.getY(), -vec3d.getZ());
            default:
                throw new IllegalArgumentException("Invalid axis.");
        }
    }
}
