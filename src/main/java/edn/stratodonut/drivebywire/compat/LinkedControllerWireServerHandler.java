package edn.stratodonut.drivebywire.compat;

import com.mojang.datafixers.util.Pair;
import com.simibubi.create.foundation.utility.WorldAttached;
import edn.stratodonut.drivebywire.wire.ShipWireNetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class LinkedControllerWireServerHandler {
    public static final String[] KEY_TO_CHANNEL = {
            "keyUp",
            "keyDown",
            "keyLeft",
            "keyRight",
            "keyJump",
            "keyShift"
    };

    static final WorldAttached<Map<Pair<BlockPos, Integer>, Integer>> timeoutMap = new WorldAttached<>(k -> new HashMap<>());
    static final int TIMEOUT = 30;

    public static void tick(Level world) {
        Map<Pair<BlockPos, Integer>, Integer> tm = timeoutMap.get(world);
        for (Iterator<Map.Entry<Pair<BlockPos, Integer>, Integer>> iterator = tm.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry<Pair<BlockPos, Integer>, Integer> entry = iterator.next();
            int ttl = entry.getValue();
            entry.setValue(--ttl);
            if (ttl <= 0) {

                ShipWireNetworkManager.trySetSignalAt(world, entry.getKey().getFirst(),
                        KEY_TO_CHANNEL[entry.getKey().getSecond()], 0);
                iterator.remove();
            }
        }
    }

    public static void receivePressed(Level world, BlockPos pos, Collection<Integer> collect,
                                      boolean pressed) {
        for (Integer i : collect) {
            ShipWireNetworkManager.trySetSignalAt(world, pos, KEY_TO_CHANNEL[i], pressed ? 15 : 0);
            if (pressed) timeoutMap.get(world).put(new Pair<>(pos, i), TIMEOUT);
        }
    }

    public static void reset(Level world, BlockPos pos) {
        for (int i = 0; i < KEY_TO_CHANNEL.length; i++) {
            ShipWireNetworkManager.trySetSignalAt(world, pos, KEY_TO_CHANNEL[i], 0);
            timeoutMap.get(world).remove(new Pair<>(pos, i));
        }
    }
}
