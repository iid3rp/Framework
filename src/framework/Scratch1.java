package framework;

import framework.lang.BigInt;

public class Scratch1
{
    public static void main(String[] args)
    {
        BigInt x = BigInt.pow2(2048);

        System.out.println(x);
        System.out.println(x.toString().length());

    }
}
