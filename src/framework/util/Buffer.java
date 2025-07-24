package framework.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class Buffer
{

    private Buffer() {
    }

    public static ByteBuffer createByteBuffer(byte[] array) {
        return ByteBuffer.allocateDirect(array.length).order(ByteOrder.nativeOrder())
            .put(array)
            .flip();
    }

    public static FloatBuffer createFloatBuffer(int size)
    {
        return ByteBuffer.allocateDirect(size << 2).order(ByteOrder.nativeOrder()).asFloatBuffer();
    }

    public static FloatBuffer createFloatBuffer(float[] array) {
        return ByteBuffer.allocateDirect(array.length << 2).order(ByteOrder.nativeOrder()).asFloatBuffer()
            .put(array)
            .flip();
    }

    public static IntBuffer createIntBuffer(int[] array) {
        return ByteBuffer.allocateDirect(array.length << 2).order(ByteOrder.nativeOrder()).asIntBuffer()
        .put(array)
        .flip();
    }

}
