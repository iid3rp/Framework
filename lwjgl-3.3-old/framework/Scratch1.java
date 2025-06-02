package framework;
import java.util.stream.IntStream;

public class Scratch1
{
    public static void main(String[] args)
    {
        System.out.println(
            Character.toChars(
        IntStream.range(
                    0, Character.toUpperCase(
                     Boolean.toString(
                         Boolean.TRUE)
                             .toCharArray()[0]))
                                .sum()));

        System.out.println(rec(3));

    }

    public static int rec(int range)
    {
        if(range == 0)
            return range;
        return range + rec(range - 1);
    }



}
