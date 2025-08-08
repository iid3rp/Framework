package framework;

import framework.util.LinkList;

public class AnotherScratch
{
    public static void main(String[] args)
    {
        LinkList<Character> list = new LinkList<>();
        list.addAll('a');
        list.addAll('b');
        list.addAll('c');
        list.addAll('d');
        list.addAll('e');
        list.addAll('f');
        list.addAll('g');

        for(int i = 0; i < list.length(); i++)
        {
            System.out.println(list.size());
            System.out.println(list.removeFirst());
            System.out.println("\nspace\n");
        }
    }
}
