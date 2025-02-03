package edn.stratodonut.drivebywire.mixin.compat.tweaked;

import com.getitemfromblock.create_tweaked_controllers.block.TweakedLecternControllerBlock;
import edn.stratodonut.drivebywire.wire.MultiChannelWireSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static edn.stratodonut.drivebywire.compat.LinkedControllerWireServerHandler.KEY_TO_CHANNEL;
import static edn.stratodonut.drivebywire.compat.TweakedControllerWireServerHandler.AXIS_TO_CHANNEL;
import static edn.stratodonut.drivebywire.compat.TweakedControllerWireServerHandler.BUTTON_TO_CHANNEL;

@Pseudo
@Mixin(TweakedLecternControllerBlock.class)
public abstract class MixinLecternTweakedBlock implements MultiChannelWireSource {
    @Unique
    private static final List<String> wire$channels =
            Stream.concat(Arrays.stream(AXIS_TO_CHANNEL), Arrays.stream(BUTTON_TO_CHANNEL)).toList();

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
