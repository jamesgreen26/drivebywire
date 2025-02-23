package edn.stratodonut.drivebywire.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ImmutableHashMap<K, V> implements Map<K, V> {
    private Map<K, V> internal;

    public ImmutableHashMap() {
        this.internal = new HashMap<>();
    }

    public ImmutableHashMap(Map<K, V> map) {
        this.internal = map;
    }

    @Override
    public int size() {
        return internal.size();
    }

    @Override
    public boolean isEmpty() {
        return internal.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return internal.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return internal.containsKey(value);
    }

    @Override
    public V get(Object key) {
        return internal.get(key);
    }

    @Nullable
    @Override
    public V put(K key, V value) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(Object key) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putAll(@NotNull Map<? extends K, ? extends V> m) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @NotNull
    @Override
    public Set<K> keySet() {
        return internal.keySet();
    }

    @NotNull
    @Override
    public Collection<V> values() {
        return internal.values();
    }

    @NotNull
    @Override
    public Set<Entry<K, V>> entrySet() {
        return internal.entrySet();
    }
}
