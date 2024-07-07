package framework.shader;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;

/**
 * Abstract class representing a GLShader used for handling
 * OpenGL shaders.
 * Provides methods for loading various types of data into shaders,
 * starting and stopping shader programs,
 * disposing of shaders, and binding attributes.
 */
public abstract class GLShader
{
    protected static int lights = 100;
    private final int programID;
    private final int vertexShaderID;
    private final int fragmentShaderID;
    private static final FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
    public GLShader(String vertexFile, String fragmentFile)
    {
        vertexShaderID = loadShader(vertexFile, GL20.GL_VERTEX_SHADER);
        fragmentShaderID = loadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER);
        programID = GL20.glCreateProgram();
        GL20.glAttachShader(programID, vertexShaderID);
        GL20.glAttachShader(programID, fragmentShaderID);
        bindAttributes();
        GL20.glLinkProgram(programID);
        GL20.glValidateProgram(programID);
        getAllUniformLocations();

    }

    protected int getUniformLocation(String uniformName)
    {
        return GL20.glGetUniformLocation(programID, uniformName);
    }

    protected void loadFloat(int location, float value)
    {
        GL20.glUniform1f(location, value);
    }

    protected void loadInteger(int location, int value)
    {
        GL20.glUniform1i(location, value);
    }

    protected void loadVector3f(int location, Vector3f vector3f)
    {
        GL20.glUniform3f(location, vector3f.x, vector3f.y, vector3f.z);
    }
    protected void loadVector4f(int location, Vector4f vector4f)
    {
        GL20.glUniform4f(location, vector4f.x, vector4f.y, vector4f.z, vector4f.w);
    }

    protected  void loadVector2f(int location, Vector2f vector2f)
    {
        GL20.glUniform2f(location, vector2f.x, vector2f.y);
    }

    protected void loadBoolean(int location, boolean value)
    {
        float toLoad = value? 1f : 0f;
        GL20.glUniform1f(location, toLoad);
    }

    protected void loadMatrix(int location, Matrix4f matrix4f)
    {
        matrix4f.store(buffer);
        buffer.flip();
        GL20.glUniformMatrix4(location, false, buffer);
    }

    protected abstract void getAllUniformLocations();

    public void start()
    {
        GL20.glUseProgram(programID);
    }

    public void stop()
    {
        GL20.glUseProgram(0);
    }

    public void dispose()
    {
        stop();
        GL20.glDetachShader(programID, vertexShaderID);
        GL20.glDetachShader(programID, fragmentShaderID);
        GL20.glDeleteShader(vertexShaderID);
        GL20.glDeleteShader(fragmentShaderID);
        GL20.glDeleteProgram(programID);
    }

    protected void bindAttribute(int attrib, String varName)
    {
        GL20.glBindAttribLocation(programID, attrib, varName);
    }

    protected abstract void bindAttributes();

    private static int loadShader(String file, int type)
    {
        StringBuilder source = new StringBuilder();
        try
        {
            InputStream in = GLShader.class.getResourceAsStream(file);
            System.out.println(GLShader.class.getResourceAsStream(file));
            assert in != null;
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while((line = reader.readLine()) != null)
            {
                source.append(line).append("\n");
            }
            reader.close();
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
        int shaderID = GL20.glCreateShader(type);
        GL20.glShaderSource(shaderID, source);
        GL20.glCompileShader(shaderID);
        if(GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) // if the thing is equal to zero...
        {
            System.err.println(GL20.glGetShaderInfoLog(shaderID, 500));
            throw new RuntimeException("Could not compile shader file: " + file);
        }
        return shaderID;
    }
}
