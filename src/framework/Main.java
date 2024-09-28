package framework;

import framework.util.LinkList;

public class Main
{
    public static void main(String[] args)
    {
        LinkList<String> list =  new LinkList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        list.add("d");
        list.add("e");
        list.add("f");
        list.add("g");

        System.out.println(list.size());

        for(String s : list.queue())
            System.out.println(s);

        System.out.println(list);

    }
}
