package framework.util;


/**
 * A utility class for fast trigonometric calculations using lookup tables.
 * The class provides methods to calculate sine, cosine, tangent,
 * and arc cosine values.
 */
public class LookupTable
{
    private static final int SIN_SIZE = 1000 * // 0.001 precision decimal
                                        360;   // by charli xcx
    private static final int ACOS_SIZE = 20_000; // .0001 precision decimal
    private static float[] acos;
    private static Value[] val;

    static
    {
        val = new Value[SIN_SIZE];
        acos = new float[SIN_SIZE];
        for (int i = 0; i < SIN_SIZE; i++)
        {
            Value v = new Value();
            v.sin = (float) Math.sin(i * .001);
            v.cos = (float) Math.cos(i * .001);
            v.tan = (float) Math.tan(i * .001);
            val[i] = v;
        }
        for(int i = 0; i < ACOS_SIZE; i++)
            acos[i] = (float) Math.acos(-1 + i * .0001);
    }

    private LookupTable() {}

    public static float sin(float degrees)
    {
        degrees = normalizeAngle(degrees);
        return interpolate(degrees, val);
    }

    /**
     * Get the cosine of an angle using the sine value.
     *
     * @param degrees The angle in degrees
     * @return The cosine value
     */
    public static float cos(float degrees)
    {
        return sin(degrees + 90.0f);
    }

    /**
     * Get the tangent of an angle using the sine value.
     *
     * @param degrees The angle in degrees
     * @return The tangent value
     */
    public static float tan(float degrees)
    {
        float sinValue = sin(degrees);
        float cosValue = cos(degrees);
        return cosValue != 0 ? sinValue / cosValue : Float.POSITIVE_INFINITY;
    }

    /**
     * Linear interpolation between two closest values in the table.
     *
     * @param degrees The angle in degrees
     * @param table The lookup table (sin table)
     * @return The interpolated value
     */
    private static float interpolate(float degrees, Value[] table)
    {
        float index = degrees * 1000;
        int lowerIndex = (int) index;

        if(index - (float) lowerIndex == 0.0)
            return table[lowerIndex].sin;

        return (float) Math.sin(degrees);
    }


    /**
     * Normalize the angle to be within the range [0, 360).
     *
     * @param deg The angle in degrees
     * @return The normalized angle
     */
    private static float normalizeAngle(float deg) {
        deg = deg % 360.0f;
        deg = deg < 0? deg + 360.0f : deg;
        return deg;
    }

    /**
     * Get the acos of a given cosine value using the lookup table.
     *
     * @param cosValue The cosine value
     * @return The arc cosine (acos) value in radians
     */
    public static float acos(float cosValue)
    {
        int index = (int) ((cosValue + 1.0f) * (ACOS_SIZE - 1) / 2.0f);
        index = Math.max(0, Math.min(index, ACOS_SIZE - 1));
        return acos[index];
    }

    private static class Value
    {
        float sin;
        float cos;
        float tan;
    }
}
