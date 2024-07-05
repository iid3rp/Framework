package terrain;

import java.util.Random;

public class HeightGenerator
{
    private static final float AMPLITUDE = 50f;
    public static final int OCTAVES = 3;
    public static final float ROUGHNESS = 0.3f;

    private Random random = new Random();
    private int seed;
    public HeightGenerator()
    {
        this.seed = random.nextInt(1_000_000_000);
    }

    public float generateHeight(int x, int z)
    {
        float total = getInterpolation(x / 12f, z / 12f) * AMPLITUDE * 3f;
        total += getInterpolation(x / 6f, z / 6f) * AMPLITUDE;
        total += getInterpolation(x / 3f, z / 3f) * AMPLITUDE / 3f;
        total += getInterpolation(x, z) * AMPLITUDE / 9f;
        return total;
    }

    private float getInterpolation(float x, float z)
    {
        int intX = (int) x;
        int intZ = (int) z;
        float fractionX = x - intX;
        float fractionZ = z - intZ;

        float v1 = getSmoothness(intX, intZ);
        float v2 = getSmoothness(intX + 1, intZ);
        float v3 = getSmoothness(intX, intZ + 1);
        float v4 = getSmoothness(intX + 1, intZ + 1);

        float i1 = interpolate(v1, v2, fractionX);
        float i2 = interpolate(v3, v4, fractionX);

        return interpolate(i1, i2, fractionZ);

    }

    private float interpolate(float  a, float b, float blend)
    {
        double theta = blend * Math.PI;
        float f = (float) ((1f - Math.cos(theta)) * 0.5f);
        return a * (1f - f) + b * f;
    }

    private float getSmoothness(int x, int z)
    {
        float corners = ((
                getNoise(x - 1,z - 1) +
                getNoise(x + 1,z - 1) +
                getNoise(x - 1,z + 1) +
                getNoise(x + 1,z + 1))
                / 16f);
        float sides = ((
                getNoise(x - 1, z) +
                getNoise(x + 1, z) +
                getNoise(x, z - 1) +
                getNoise(x, z + 1)
                ) / 8f);
        float center = getNoise(x,z) / 4f;
        return corners + sides + center;
    }

    private float getNoise(int x, int z)
    {
        random.setSeed(x * 200000L + z * 4000000L + seed);
        return random.nextFloat() * 2f - 1f;
    }

}
