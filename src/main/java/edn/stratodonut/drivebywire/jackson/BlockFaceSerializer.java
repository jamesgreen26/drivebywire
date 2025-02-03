package edn.stratodonut.drivebywire.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import edn.stratodonut.drivebywire.util.BlockFace;

import java.io.IOException;

public class BlockFaceSerializer extends JsonSerializer<BlockFace> {
    @Override
    public void serialize(BlockFace value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeFieldName(value.pos + "," + value.dir);
    }
}
