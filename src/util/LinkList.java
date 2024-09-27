package util;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * doubly linked list for the efficiency of the data structures
 * @param <E>
 */
public class LinkList<E> implements Iterable<E>
{
    int size = 0;
    transient volatile Node<E> first;
    transient volatile Node<E> last;

    public LinkList() {}

    public Iterator<E> it = new Iterator<>()
    {
        int i = 0;
        Node<E> e;

        @Override
        public boolean hasNext()
        {
            if(first == null)
                return false;
            if(i == 0)
                e = first;
            if(i < size)
                return true;
            else {
                i = 0;
                e = null;
                return false;
            }
        }

        @Override
        public E next()
        {
            if(!hasNext()) {
                throw new NoSuchElementException();
            }
            E element = e.e;
            e = e.next;
            i++;
            return element;
        }
    };

    public void clear()
    {
        for(Node<E> x = first; x != null; ) {
            Node<E> next = x.next;
            x.e = null;
            x.next = null;
            x.prev = null;
            x = next;
        }
        first = last = null;
        size = 0;
    }

    public boolean add(E e)
    {
        if(size == 0)
            addFirst(e);
        else
            addLast(e);
        return true;
    }

    public boolean add(E e, int index)
    {
        if(index == size)
            return addLast(e);
        else return addBefore(e, node(index));
    }

    public boolean addFirst(E e)
    {
        final Node<E> f = first;
        final Node<E> newNode = new Node<>(e, f, null);
        first = newNode;
        if(f == null)
            last = newNode;
        else
            f.prev = newNode;
        size++;
        return true;
    }

    public boolean addLast(E e)
    {
        final Node<E> l = last;
        final Node<E> newNode = new Node<>(e, null, l);
        last = newNode;
        if(l == null)
            first = newNode;
        else l.next = newNode;
        return true;
    }

    private boolean addBefore(E e, Node<E> node)
    {
        assert node != null;
        final Node<E> pred = node.prev;
        final Node<E> newNode = new Node<>(e, node, pred);
        node.prev = newNode;
        if(pred == null)
            first = newNode;
        else pred.next = newNode;
        size++;
        return true;
    }

    private Node<E> node(int index)
    {
        assert index <= size && size >= 0;
        Node<E> n;
        if(index < (size >> 1)) {
            n = first;
            for(int i = 0; i < index; i++) {
                n = n.next;
            }
        }
        else {
            n = last;
            for(int i = size - 1; i > index; i--) {
                n = n.prev;
            }
        }
        return n;
    }

    private boolean unlink(Node<E> e)
    {
        assert e != null;
        final E element = e.e;
        final Node<E> next = e.next;
        final Node<E> prev = e.prev;

        if(prev == null)
            first = next;
        else {
            prev.next = next;
            e.prev = null;
        }

        if(next == null)
            last = prev;
        else {
            next.prev = prev;
            e.next = null;
        }

        e.e = null;
        size--;
        return true;
    }

    public boolean removeFirst()
    {
        return unlink(first);
    }

    public boolean removeLast()
    {
        return unlink(last);
    }

    // implemented methods from the Iterable class
    @Override
    public Iterator<E> iterator()
    {
        return it;
    }

    @Override
    public void forEach(Consumer<? super E> action)
    {
        Iterable.super.forEach(action);
    }

    @Override
    public Spliterator<E> spliterator()
    {
        return Iterable.super.spliterator();
    }

    private static class Node<E>
    {
        E e;
        Node<E> next;
        Node<E> prev;

        public Node(E e, Node<E> next, Node<E> prev)
        {
            this.e = e;
            this.next = next;
            this.prev = prev;
        }
    }
}
