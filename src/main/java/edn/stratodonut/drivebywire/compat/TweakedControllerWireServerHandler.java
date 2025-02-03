package edn.stratodonut.drivebywire.compat;

import com.jozufozu.flywheel.util.WorldAttached;
import com.mojang.datafixers.util.Pair;
import edn.stratodonut.drivebywire.wire.ShipWireNetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.*;

public class TweakedControllerWireServerHandler {
    public static final String[] BUTTON_TO_CHANNEL = {
            "buttonA",
            "buttonB",
            "buttonX",
            "buttonY",
            "shoulderLeft",
            "shoulderRight",
            "buttonBack",
            "buttonStart",
            "buttonGuide",
            "leftJoyStickClick",
            "rightJoyStickClick",
            "dPadUp",
            "dPadRight",
            "dPadDown",
            "dPadLeft"
    };

    public static final String[] AXIS_TO_CHANNEL = {
            "axisLeftX+",
            "axisLeftX-",
            "axisLeftY+",
            "axisLeftY-",

            "axisRightX+",
            "axisRightX-",
            "axisRightY+",
            "axisRightY-",

            "axisTriggerLeft",
            "axisTriggerRight"
    };

    static final WorldAttached<Map<Pair<BlockPos, Integer>, Integer>> timeoutButtonMap = new WorldAttached<>(k -> new HashMap<>());
    static final WorldAttached<Map<Pair<BlockPos, Integer>, Integer>> timeoutAxisMap = new WorldAttached<>(k -> new HashMap<>());
    static final int TIMEOUT = 30;

    public static void tick(Level world) {
        Map<Pair<BlockPos, Integer>, Integer> tbm = timeoutButtonMap.get(world);
        for (Iterator<Map.Entry<Pair<BlockPos, Integer>, Integer>> iterator = tbm.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry<Pair<BlockPos, Integer>, Integer> entry = iterator.next();
            int ttl = entry.getValue();
            entry.setValue(--ttl);
            if (ttl <= 0) {

                ShipWireNetworkManager.trySetSignalAt(world, entry.getKey().getFirst(),
                        BUTTON_TO_CHANNEL[entry.getKey().getSecond()], 0);
                iterator.remove();
            }
        }

        Map<Pair<BlockPos, Integer>, Integer> tam = timeoutAxisMap.get(world);
        for (Iterator<Map.Entry<Pair<BlockPos, Integer>, Integer>> iterator = tam.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry<Pair<BlockPos, Integer>, Integer> entry = iterator.next();
            int ttl = entry.getValue();
            entry.setValue(--ttl);
            if (ttl <= 0) {

                ShipWireNetworkManager.trySetSignalAt(world, entry.getKey().getFirst(),
                        AXIS_TO_CHANNEL[entry.getKey().getSecond()], 0);
                iterator.remove();
            }
        }
    }

    public static void receiveAxis(Level world, BlockPos pos, List<Byte> axisStates) {
        for (int i = 0; i < axisStates.size(); i++) {
            Byte b = axisStates.get(i);
            ShipWireNetworkManager.trySetSignalAt(world, pos, AXIS_TO_CHANNEL[i], b);
            timeoutAxisMap.get(world).put(new Pair<>(pos, i), TIMEOUT);
        }
    }

    public static void receiveButton(Level world, BlockPos pos, List<Boolean> buttonStates) {
        for (int i = 0; i < buttonStates.size(); i++) {
            Boolean b = buttonStates.get(i);
            ShipWireNetworkManager.trySetSignalAt(world, pos, BUTTON_TO_CHANNEL[i], b ? 15 : 0);
            if (b) timeoutButtonMap.get(world).put(new Pair<>(pos, i), TIMEOUT);
        }
    }

    public static void reset(Level world, BlockPos pos) {
        for (int i = 0; i < AXIS_TO_CHANNEL.length; i++) {
            ShipWireNetworkManager.trySetSignalAt(world, pos, AXIS_TO_CHANNEL[i], 0);
            timeoutAxisMap.get(world).remove(new Pair<>(pos, i));
        }

        for (int i = 0; i < BUTTON_TO_CHANNEL.length; i++) {
            ShipWireNetworkManager.trySetSignalAt(world, pos, BUTTON_TO_CHANNEL[i], 0);
            timeoutButtonMap.get(world).remove(new Pair<>(pos, i));
        }
    }
}
