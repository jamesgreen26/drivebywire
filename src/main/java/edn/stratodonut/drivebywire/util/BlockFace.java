package edn.stratodonut.drivebywire.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

import java.util.Objects;

@JsonAutoDetect(
        fieldVisibility = JsonAutoDetect.Visibility.ANY
)
public class BlockFace {
    public final long pos;
    public final int dir;

    protected BlockFace(long pos, int dir) {
        this.pos = pos;
        this.dir = dir;
    }

    public static BlockFace of(long first, int second) {
        return new BlockFace(first, second);
    }

    public static BlockFace of(BlockPos first, Direction second) {
        return new BlockFace(first.asLong(), second.get3DDataValue());
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this)
            return true;
        if (obj instanceof BlockFace p) {
            return Objects.equals(pos, p.pos) && Objects.equals(dir, p.dir);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pos, dir);
    }

    @Override
    public String toString() {
        return "(" + pos + "," + dir + ")";
    }
}
