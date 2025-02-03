package edn.stratodonut.drivebywire.wire;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Rotation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

import static edn.stratodonut.drivebywire.wire.ShipWireNetworkManager.WORLD_REDSTONE_CHANNEL;

@JsonAutoDetect(
        fieldVisibility = JsonAutoDetect.Visibility.ANY
)
public class BiDirectionalBlockMap {
    private final Map<Long, Set<ChannelConnection>> outputToInput = new HashMap<>();
    private final Map<Long, Set<ChannelConnection>> inputToOutput = new HashMap<>();

    public BiDirectionalBlockMap() {}

    public boolean put(Long in, Long out, Direction dir, String channel) {
        ChannelConnection connection = new ChannelConnection(in, out, channel, dir.get3DDataValue());
        outputToInput.computeIfAbsent(out, k -> new LinkedHashSet<>());

        if (outputToInput.get(out).contains(connection)) return false;
        outputToInput.get(out).add(connection);

        inputToOutput.computeIfAbsent(in, k -> new LinkedHashSet<>());
        inputToOutput.get(in).add(connection);
        return true;
    }

    public boolean remove(Long in, Long out, Direction dir, String channel) {
        ChannelConnection connection = new ChannelConnection(in, out, channel, dir.get3DDataValue());

        if (!outputToInput.containsKey(out)) return false;
        outputToInput.get(out).remove(connection);

        if (!inputToOutput.containsKey(in))
            throw new IllegalStateException("Bidirectional Map points from output but not from input!");
        inputToOutput.get(in).remove(connection);
        return true;
    }

    public boolean doesAddingConnectionCreateLoop(Long start, Long newOut, String newChannel) {
        if (!WORLD_REDSTONE_CHANNEL.equals(newChannel)) return false;
        Set<Long> visited = new HashSet<>();
        Stack<Long> frontier = new Stack<>();
        frontier.add(newOut);
        Long cur = start;
        do {
            visited.add(cur);
            cur = frontier.pop();
            if (inputToOutput.containsKey(cur)) {
                frontier.addAll(inputToOutput.get(cur).stream()
                        .filter(c -> WORLD_REDSTONE_CHANNEL.equals(c.channel))
                        .map(c -> c.out).toList());
            }
            if (visited.contains(cur)) return true;
        } while (!frontier.isEmpty());

        return false;
    }

    public void deserialiseFromNbt(CompoundTag nbt, BlockPos origin, Rotation rot) {
        for (String ch : nbt.getAllKeys()) {
            long[] inOutPairs = nbt.getLongArray(ch);

            for (int i = 0; i < inOutPairs.length/3; i += 3) {
                this.put(BlockPos.of(inOutPairs[i]).offset(origin).asLong(), BlockPos.of(inOutPairs[i+1]).offset(origin).asLong(),
                        Direction.from3DDataValue((int) inOutPairs[i+2]), ch);
            }
        }
    }

    public CompoundTag serialiseToNbt(BlockPos origin) {
        Map<String, List<Long>> channels = new HashMap<>();
        CompoundTag local = new CompoundTag();
        outputToInput.values().forEach(
                channelConnections -> channelConnections.forEach(
                        connection -> {
                            channels.putIfAbsent(connection.channel, new ArrayList<>());

                            channels.get(connection.channel).add(BlockPos.of(connection.in).subtract(origin).asLong());
                            channels.get(connection.channel).add(BlockPos.of(connection.out).subtract(origin).asLong());
                            channels.get(connection.channel).add((long) connection.direction);
                        }
                )
        );

        for (String ch : channels.keySet()) {
            local.putLongArray(ch, channels.get(ch));
        }

        return local;
    }

    public boolean containsOutput(BlockPos out) {
        return outputToInput.containsKey(out.asLong());
    }

    @Nullable
    public Map<Pair<BlockPos, Direction>, Set<String>> getConnections(BlockPos in) {
        Map<Pair<BlockPos, Direction>, Set<String>> res = new HashMap<>();
        inputToOutput.computeIfAbsent(in.asLong(), k -> new HashSet<>());
        inputToOutput.get(in.asLong())
                .forEach(connection -> {
                    Pair<BlockPos, Direction> key = new Pair<>(BlockPos.of(connection.out),
                            Direction.from3DDataValue(connection.direction));
                    res.computeIfAbsent(key, k -> new HashSet<>());
                    res.get(key).add(connection.channel);
                });
        return res;
    }

    @JsonIgnore
    @Nonnull
    public Iterable<Long> getAllInputs() {
        return inputToOutput.keySet();
    }

    @Nullable
    public Set<ChannelConnection> getFromOutput(BlockPos out) {
        return outputToInput.get(out.asLong());
    }

    @Nonnull
    public List<BlockPos> getOutputs(BlockPos in) {
        if (!inputToOutput.containsKey(in.asLong())) return new ArrayList<>();
        return inputToOutput.get(in.asLong()).stream().map(
                connection -> BlockPos.of(connection.out)
        ).toList();
    }

    @JsonIgnore
    @Nonnull
    public Iterable<Long> getAllOutputs() {
        return outputToInput.keySet();
    }

    public int size() {
        return Math.max(inputToOutput.size(), outputToInput.size());
    }

    public void clear() {
        this.inputToOutput.clear();
        this.outputToInput.clear();
    }
    
    /**
     *
     * @param in
     * @param out
     * @param channel
     * @param direction The face on the TARGET that signal goes to
     */
    public record ChannelConnection(Long in, Long out, String channel, int direction) {
        @Override
        public Long in() {
            return in;
        }

        @Override
        public Long out() {
            return out;
        }

        @Override
        public String channel() {
            return channel;
        }

        @Override
        public int direction() {
            return direction;
        }
    }
}
