package edn.stratodonut.drivebywire.jackson;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import edn.stratodonut.drivebywire.util.BlockFace;

public class BlockFaceDeserializer extends KeyDeserializer {
    @Override
    public Object deserializeKey(String raw, DeserializationContext ctxt) {
        String[] split = raw.split(",");
        long pos = Long.parseLong(split[0]);
        int dir = Integer.parseInt(split[1]);
        return BlockFace.of(pos, dir);
    }
}
