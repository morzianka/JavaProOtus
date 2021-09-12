package ru.otus.cachehw;


import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static java.util.Collections.synchronizedMap;

public class MyCache<K, V> implements HwCache<K, V> {

    private final Map<K, V> cache = synchronizedMap(new WeakHashMap<>());
    private final List<WeakReference<HwListener<K, V>>> listeners = new CopyOnWriteArrayList<>();

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
        action(key, value, "put");
    }

    @Override
    public void remove(K key) {
        V value = cache.remove(key);
        action(key, value, "remove");
    }

    @Override
    public V get(K key) {
        V value = cache.get(key);
        action(key, value, "get");
        return value;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(new WeakReference<>(listener));
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.removeIf(weakListener -> weakListener.get() == listener);
    }

    private void action(K key, V value, String action) {
        listeners.forEach(weakListener -> {
            HwListener<K, V> listener = weakListener.get();
            if (listener != null) {
                listener.notify(key, value, action);
            }
        });
    }
}
