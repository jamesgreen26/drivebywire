package edn.stratodonut.drivebywire.mixin.compat;

import com.simibubi.create.content.redstone.link.controller.LecternControllerBlock;
import edn.stratodonut.drivebywire.wire.MultiChannelWireSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

import static edn.stratodonut.drivebywire.compat.LinkedControllerWireServerHandler.KEY_TO_CHANNEL;

@Mixin(LecternControllerBlock.class)
public abstract class MixinLecternControllerBlock implements MultiChannelWireSource {
    @Unique
    private static final List<String> wire$channels = Arrays.stream(KEY_TO_CHANNEL).toList();

    @Unique
    @Override
    public List<String> wire$getChannels() {
        return wire$channels;
    }

    @Unique
    @Override
    public @Nonnull String wire$nextChannel(String current, boolean forward) {
        int curIndex = wire$channels.indexOf(current);
        if (curIndex == -1) {
            return wire$channels.get(0);
        } else {
            return wire$channels.get(Math.floorMod(curIndex + (forward ? 1 : -1), wire$channels.size()));
        }
    }
}
