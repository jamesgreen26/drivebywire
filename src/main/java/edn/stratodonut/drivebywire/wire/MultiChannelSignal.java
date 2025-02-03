package edn.stratodonut.drivebywire.wire;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.HashMap;

@JsonAutoDetect(
        fieldVisibility = JsonAutoDetect.Visibility.ANY
)
public class MultiChannelSignal {
    @JsonIgnore
    public static MultiChannelSignal EMPTY = new MultiChannelSignal();

    private final HashMap<String, Signal> signals = new HashMap<>();

    // For Jackson
    public MultiChannelSignal() {
        // DO NOTHING
    }

    public MultiChannelSignal addOrUpdate(String ch, int newVal) {
        signals.put(ch, new Signal(newVal));
        return this;
    }

    public MultiChannelSignal remove(String ch) {
        signals.remove(ch);
        return this;
    }

    public int get(String ch) {
        return signals.getOrDefault(ch, Signal.ZERO).value;
    }

    @JsonAutoDetect(
            fieldVisibility = JsonAutoDetect.Visibility.NONE
    )
    private record Signal(Integer value) implements Comparable<Signal> {
        public static Signal ZERO = new Signal(0);

        @Override
        public int compareTo(@NotNull Signal s) {
            return Comparator.comparingInt(Signal::value).compare(this, s);
        }
    }
}
