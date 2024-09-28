package framework.util;

public class Key<K, V>
{
    K key;
    V value;

    Key<K, V> next;
    Key<K, V> prev;

    public Key(K key, V value, Key<K, V> next, Key<K, V> prev)
    {
        this.key = key;
        this.value = value;
    }

    public K getKey()
    {
        return key;
    }

    public void setKey(K key)
    {
        this.key = key;
    }

    public V getValue()
    {
        return value;
    }

    public void setValue(V value)
    {
        this.value = value;
    }
}
