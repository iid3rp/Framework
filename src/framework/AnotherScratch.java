package framework;

import framework.util.LinkList;

public class AnotherScratch
{
    public static void main(String[] args)
    {
        LinkList list = new LinkList();
        list.add('a');
        list.add('b');
        list.add('c');
        list.add('d');
        list.add('e');
        list.add('f');
        list.add('g');

        for(int i = 0; i < list.length(); i++)
        {
            System.out.println(list.size());
            System.out.println(list.removeFirst());
            System.out.println("\nspace\n");
        }
    }
}
