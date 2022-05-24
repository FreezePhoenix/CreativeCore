package team.creative.creativecore.common.util.math.box;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import team.creative.creativecore.common.util.math.base.Axis;
import team.creative.creativecore.common.util.math.base.Facing;
import team.creative.creativecore.common.util.math.vec.Vec3d;

public class CreativeAABB extends AABB {
    
    public CreativeAABB(double x1, double y1, double z1, double x2, double y2, double z2) {
        super(x1, y1, z1, x2, y2, z2);
    }
    
    public CreativeAABB(BlockPos pos) {
        super(pos);
    }
    
    public CreativeAABB(BlockPos pos1, BlockPos pos2) {
        super(pos1, pos2);
    }
    
    public boolean contains(Vec3d vec) {
        if (vec.x > this.minX && vec.x < this.maxX) {
            if (vec.y > this.minY && vec.y < this.maxY) {
                return vec.z > this.minZ && vec.z < this.maxZ;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    
    protected double get(Facing facing) {
        return switch (facing) {
        case EAST -> maxX;
        case WEST -> minX;
        case UP -> maxY;
        case DOWN -> minY;
        case SOUTH -> maxZ;
        case NORTH -> minZ;
        };
    }
    
    public Vec3d getCorner(BoxCorner corner) {
        return new Vec3d(getCornerX(corner), getCornerY(corner), getCornerZ(corner));
    }
    
    public double getCornerValue(BoxCorner corner, Axis axis) {
        return get(corner.getFacing(axis));
    }
    
    public double getCornerX(BoxCorner corner) {
        return get(corner.x);
    }
    
    public double getCornerY(BoxCorner corner) {
        return get(corner.y);
    }
    
    public double getCornerZ(BoxCorner corner) {
        return get(corner.z);
    }
    
    public Vec3d getSizeVec() {
        return new Vec3d(maxX - minX, maxY - minY, maxZ - minZ);
    }
    
    public double getVolume() {
        return (maxX - minX) * (maxY - minY) * (maxZ - minZ);
    }
    
    public double getIntersectionVolume(AABB other) {
        double d0 = Math.max(this.minX, other.minX);
        double d1 = Math.max(this.minY, other.minY);
        double d2 = Math.max(this.minZ, other.minZ);
        double d3 = Math.min(this.maxX, other.maxX);
        double d4 = Math.min(this.maxY, other.maxY);
        double d5 = Math.min(this.maxZ, other.maxZ);
        if (d0 < d3 && d1 < d4 && d2 < d5)
            return Math.abs((d3 - d0) * (d4 - d1) * (d5 - d2));
        return 0;
    }
    
    public double getSize(Axis axis) {
        return switch (axis) {
        case X -> maxX - minX;
        case Y -> maxY - minY;
        case Z -> maxZ - minZ;
        };
    }
    
    public double getMin(Axis axis) {
        return switch (axis) {
        case X -> minX;
        case Y -> minY;
        case Z -> minZ;
        };
    }
    
    public double getMax(Axis axis) {
        return switch (axis) {
        case X -> maxX;
        case Y -> maxY;
        case Z -> maxZ;
        };
    }
    
    public static double get(AABB bb, Facing facing) {
        return switch (facing) {
        case EAST -> bb.maxX;
        case WEST -> bb.minX;
        case UP -> bb.maxY;
        case DOWN -> bb.minY;
        case SOUTH -> bb.maxZ;
        case NORTH -> bb.minZ;
        };
    }
    
    public static double getMin(AABB bb, Axis axis) {
        return switch (axis) {
        case X -> bb.minX;
        case Y -> bb.minY;
        case Z -> bb.minZ;
        default -> 0;
        };
    }
    
    public static double getMax(AABB bb, Axis axis) {
        return switch (axis) {
        case X -> bb.maxX;
        case Y -> bb.maxY;
        case Z -> bb.maxZ;
        default -> 0;
        };
    }
    
    public static Vec3d getCorner(AABB bb, BoxCorner corner) {
        return new Vec3d(getCornerX(bb, corner), getCornerY(bb, corner), getCornerZ(bb, corner));
    }
    
    public static double getCornerValue(AABB bb, BoxCorner corner, Axis axis) {
        return get(bb, corner.getFacing(axis));
    }
    
    public static double getCornerX(AABB bb, BoxCorner corner) {
        return get(bb, corner.x);
    }
    
    public static double getCornerY(AABB bb, BoxCorner corner) {
        return get(bb, corner.y);
    }
    
    public static double getCornerZ(AABB bb, BoxCorner corner) {
        return get(bb, corner.z);
    }
}
