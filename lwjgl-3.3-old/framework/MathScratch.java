package framework;

import framework.lang.Vec3;
import net.jafama.FastMath;

public class MathScratch
{
    public static void main(String... args)
    {
        Vec3 m0 = new Vec3(6f, 10f, 8f);
        Vec3 _0 = new Vec3(-16f, 8f, 2f);

        Vec3 result = Vec3.cross(m0, _0, null);
        System.out.println(result);
    }
}
