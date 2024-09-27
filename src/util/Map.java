package util;

public class Map<K, V>
{
    private static final int MAX_SIZE = 1 << 24; // aka 16,777,216 | good for RGB values
    private Key<K, V>[] keys;

    public Map()
    {
        identity();
    }

    @SuppressWarnings("unchecked")
    private void identity()
    {
        keys = new Key[MAX_SIZE];
    }

    public void put(K key, V value)
    {
        keys[hash(key)] = new Key<>(key, value);
    }

    private int hash(K key)
    {
        return (key.hashCode() & Integer.MAX_VALUE) % MAX_SIZE;
    }

    private static class Key<K, V>
    {
        K key;
        V value;

        public Key(K key, V value)
        {
            this.key = key;
            this.value = value;
        }
    }
}
