package framework.util;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.function.Consumer;

public class Map<K, V> implements Iterable<Key<K, V>>
{
    int keySize;
    private static final int MAX_SIZE = 1 << 24; // aka 16,777,216 | good for RGB values
    private Key<K, V>[] keys;

    // list of the keys
    private Key<K, V> first;
    private Key<K, V> last;

    public Map()
    {
        identity();
    }

    private Iterable<Key<K, V>> queue = new Iterable<>()
    {
        private Iterator<Key<K, V>> it = new Iterator<>()
        {
            int i = 0;
            Key<K, V> key;

            @Override
            public boolean hasNext()
            {
                if(first == null)
                    return false;
                if(i == 0)
                    key = first;
                if(i < keySize)
                    return true;
                else {
                    i = 0;
                    key = null;
                    return false;
                }
            }

            @Override
            public Key<K, V> next()
            {
                if(!hasNext()) {
                    throw new NoSuchElementException();
                }
                return removeFirst();
            }
        };

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
    };

    Iterator<Key<K, V>> it = new Iterator<>()
    {
        int i = 0;
        Key<K, V> key;

        @Override
        public boolean hasNext()
        {
            if(first == null)
                return false;
            if(i == 0)
                key = first;
            if(i < keySize)
                return true;
            else {
                i = 0;
                key = null;
                return false;
            }
        }

        @Override
        public Key<K, V> next()
        {
            if (!hasNext()) throw new NoSuchElementException();
            Key<K, V> element = key;
            key = key.next;
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
        return keys[hash(key)] != null;
    }

    public boolean add(K key, V value)
    {
        if(keySize == 0)
            addFirst(key, value);
        else
            addLast(key, value);
        return true;
    }

    private void addFirst(K key, V value)
    {
        final Key<K, V> f = first;
        final Key<K, V> newKey = new Key<>(key, value, f, null);
        first = newKey;
        if(f == null)
            last = newKey;
        else
            f.prev = newKey;
        keys[hash(key)] = newKey;
        keySize++;
    }

    private void addLast(K key, V value)
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

    public Iterable<Key<K, V>> queue()
    {
        return queue;
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

    public void clear()
    {
        for(Key<K, V> ignored : queue());
    }

    public Key<K, V> removeFirst()
    {
        return unlink(first);
    }

    public Key<K, V> removeLast()
    {
        return unlink(last);
    }

    private Key<K, V> unlink(Key<K, V> key)
    {
        assert key != null;
        final Key<K, V> element = key;
        final Key<K, V> next = key.next;
        final Key<K, V> prev = key.prev;

        if(prev == null)
            first = next;
        else {
            prev.next = next;
            key.prev = null;
        }

        if(next == null)
            last = prev;
        else {
            next.prev = prev;
            key.next = null;
        }

        keys[hash(key.getKey())] = null;

        keySize--;
        return element;
    }

    public int getKeySize()
    {
        return keySize;
    }
}
