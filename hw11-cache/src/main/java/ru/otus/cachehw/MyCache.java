package ru.otus.cachehw;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class MyCache<K, V> implements HwCache<K, V> {
    private final static Logger logger = LoggerFactory.getLogger("DefaultCacheListener");
    HwListener<K, V> defaultListener = new HwListener<>() {
        @Override
        public void notify(K key, V value, String action) {
            logger.info("key:{}, value:{}, action: {}", key, value, action);
        }
    };
    private boolean defaultLogger = true;

    private final List<HwListener<K, V>> listeners;
    private final Map<K, V> cache;

    public MyCache() {
        this.cache = new WeakHashMap<>();
        this.listeners = List.of(defaultListener);
    }

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
        listeners.forEach(listener -> listener.notify(key, value, "put"));
    }

    @Override
    public void remove(K key) {
        V remove = cache.remove(key);
        listeners.forEach(listener -> listener.notify(key, remove, "remove"));
    }

    @Override
    public V get(K key) {
        V value = cache.get(key);
        listeners.forEach(listener -> listener.notify(key, value, "get"));
        return value;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        if (defaultLogger) {
            listeners.remove(defaultListener);
        }
        listeners.add(listener);
        defaultLogger = false;
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listener);
        if (listeners.isEmpty()) {
            listeners.add(defaultListener);
            defaultLogger = true;
        }
    }
}
