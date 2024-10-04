package framework;

import framework.util.Key;
import framework.util.LinkList;
import framework.util.Map;

public class Scratch1
{
    public static void main(String[] args)
    {
        Map<Character, LinkList<Double>> map = new Map<>();

        for(int i=0; i<10; i++)
            for(int j=0; j<10; j++)
                map.add((char) ((char) 65 + (Math.random() * 60)),
                        new LinkList<>(
                                (Math.random() * 10),
                                            (Math.random() * 20),
                                            (Math.random() * 30),
                                            (Math.random() * 40)));


        System.out.println(map.getKeySize());
        map.clear();
        System.out.println(map.getKeySize());

        System.out.println();

        for(int i=0; i<10; i++)
            for(int j=0; j<10; j++)
                map.add((char) ((char) 65 + (Math.random() * 60)),
                        new LinkList<>(
                                (Math.random() * 10),
                                (Math.random() * 20),
                                (Math.random() * 30),
                                (Math.random() * 40)));

        System.out.println(map.getKeySize());
        map.clear();
        System.out.println(map.getKeySize());

        System.out.println();

    }
}
