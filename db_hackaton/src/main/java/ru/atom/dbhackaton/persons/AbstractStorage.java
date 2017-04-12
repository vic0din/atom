package ru.atom.dbhackaton.persons;

/**
 * Created by BBPax on 12.04.17.
 */
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

public class AbstractStorage<K, V> {
    protected ConcurrentHashMap<K, V> memory;

    public AbstractStorage() {
        this.memory = new ConcurrentHashMap<>();
    }

    public boolean put(K k, V v) {
        return memory.put(k, v) == v;
    }

    public V get(K k) {
        return memory.get(k);
    }

    public V remove(K k) {
        return memory.remove(k);
    }

    public int getSize() {
        return memory.size();
    }

    public String toString() {
        throw new NotImplementedException();
    }

    public LinkedList<V> getAll() {
        LinkedList<V> result = new LinkedList<>();
        for (V value : memory.values()) {
            result.add(value);
        }
        return result;
    }
}
