package util;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.function.Consumer;

public class Map<K, V> implements Iterable<Key<K, V>>
{
    int keySize = 0;
    private static final int MAX_SIZE = 1 << 24; // aka 16,777,216 | good for RGB values
    private Key<K, V>[] keys;

    // list of the keys
    private Key<K, V> first;
    private Key<K, V> last;

    public Map()
    {
        identity();
    }

    Iterator<Key<K, V>> it = new Iterator<>()
    {
        int i = 0;
        Key<K, V> e;

        @Override
        public boolean hasNext()
        {
            if(first == null)
                return false;
            if(i == 0)
                e = first;
            if(i < keySize)
                return true;
            else {
                i = 0;
                e = null;
                return false;
            }
        }

        @Override
        public Key<K, V> next()
        {
            if(!hasNext()) {
                throw new NoSuchElementException();
            }
            Key<K, V> element = e;
            e = e.next;
            i++;
            return element;
        }
    };

    @SuppressWarnings("unchecked")
    private void identity()
    {
        keys = new Key[MAX_SIZE];
    }

    public void put(K key, V value)
    {
        if(!exists(key))
            add(key, value);
        else keys[hash(key)].setValue(value);
    }

    private boolean exists(K key)
    {
        return keys[hash(key)] != null && keys[hash(key)].equals(key);
    }

    private void add(K key, V value)
    {
        final Key<K, V> l = last;
        final Key<K, V> newKey = new Key<>(key, value, null, l);
        last = newKey;
        if(l == null)
            first = newKey;
        else l.next = newKey;
        keys[hash(key)] = newKey;
        keySize++;
    }

    public V get(K key)
    {
        Key<K, V> k = keys[hash(key)];
        if(k == null)
            return null;
        return k.getValue();
    }

    private int hash(K key)
    {
        return (key.hashCode() & Integer.MAX_VALUE) % MAX_SIZE;
    }

    @Override
    public Iterator<Key<K, V>> iterator()
    {
        return it;
    }

    @Override
    public void forEach(Consumer<? super Key<K, V>> action)
    {
        Iterable.super.forEach(action);
    }

    @Override
    public Spliterator<Key<K, V>> spliterator()
    {
        return Iterable.super.spliterator();
    }
}
