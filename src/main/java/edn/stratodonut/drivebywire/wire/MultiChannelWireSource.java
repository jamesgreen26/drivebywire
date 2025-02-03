package edn.stratodonut.drivebywire.wire;

import javax.annotation.Nonnull;
import java.util.List;

public interface MultiChannelWireSource {
    List<String> wire$getChannels();

    @Nonnull
    String wire$nextChannel(String current, boolean forward);
}
