package edn.stratodonut.drivebywire.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public enum FaceOutlines {
    DOWN(Direction.DOWN),
    UP(Direction.UP),
    NORTH(Direction.NORTH),
    SOUTH(Direction.SOUTH),
    WEST(Direction.WEST),
    EAST(Direction.EAST);

    private final AABB outline;

    FaceOutlines(Direction dir) {
        Vec3 shrink = new Vec3(dir.step()).scale(8/16f);

        outline = new AABB(BlockPos.ZERO)
                .inflate(-Math.abs(shrink.x), -Math.abs(shrink.y), -Math.abs(shrink.z))
                .move(new Vec3(dir.step()).scale(8/16f));
    }

    public static AABB getOutline(Direction dir) {
        return switch (dir) {
            case DOWN -> FaceOutlines.DOWN.getOutline();
            case NORTH -> FaceOutlines.NORTH.getOutline();
            case SOUTH -> FaceOutlines.SOUTH.getOutline();
            case WEST -> FaceOutlines.WEST.getOutline();
            case EAST -> FaceOutlines.EAST.getOutline();

            default -> FaceOutlines.UP.getOutline();
        };
    }

    public AABB getOutline() {
        return outline;
    }
}
