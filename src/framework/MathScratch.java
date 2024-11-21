package framework;

import net.jafama.FastMath;

public class MathScratch
{
    public static void main(String... args)
    {
        for(int i = 0; i < 1_000_000; i++)
        {
            System.out.println(Math.sin(i));
            System.out.println(FastMath.sin(i));
        }
    }
}
