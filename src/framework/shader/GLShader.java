package framework.shader;

import framework.lang.Mat4;
import framework.io.Resources;
import framework.util.Buffer;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.lwjgl.opengl.GL11C.GL_FALSE;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glBindAttribLocation;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteProgram;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glDetachShader;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniform2f;
import static org.lwjgl.opengl.GL20.glUniform3f;
import static org.lwjgl.opengl.GL20.glUniform4f;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glValidateProgram;

public abstract class GLShader
{
    public static final String vertexPrefix = "glsl" + File.separator + "vertex" + File.separator;
    public static final String fragmentPrefix =  "glsl" + File.separator + "fragment" + File.separator;
    private static FloatBuffer matrix = Buffer.createFloatBuffer(16);
    protected Map<String, Integer> uniforms;
    private int programId;
    private  int vertexShader;
    private int fragmentShader;

    public GLShader(String vertex, String fragment)
    {
        uniforms = new HashMap<>();
        vertexShader = loadShader(vertex,GL_VERTEX_SHADER);
        fragmentShader = loadShader(fragment, GL_FRAGMENT_SHADER);
        programId = glCreateProgram();
        glAttachShader(programId, vertexShader);
        glAttachShader(programId, fragmentShader);
        bindAttributes();
        glLinkProgram(programId);
        glValidateProgram(programId);
        getAllUniformLocations();
    }

    protected abstract void getAllUniformLocations();

    protected int getUniformLocation(String name)
    {
        return glGetUniformLocation(programId, name);
    }

    protected void addUniform(String name)
    {
        uniforms.put(name, getUniformLocation(name));
    }

    protected void loadUniform(String name, float value)
    {
        loadFloat(uniforms.get(name), value);
    }

    protected void loadUniform(String name, float x, float y)
    {
        loadVector(uniforms.get(name), x, y);
    }

    protected void loadUniform(String name, float x, float y, float z)
    {
        loadVector(uniforms.get(name), x, y, z);
    }

    protected void loadUniform(String name, float x, float y, float z, float w)
    {
        loadVector(uniforms.get(name), x, y, z, w);
    }

    protected void loadUniform(String name, int value)
    {
        loadInteger(uniforms.get(name), value);
    }

    protected void loadUniform(String name, boolean flag)
    {
        loadBool(uniforms.get(name), flag);
    }

    protected void loadUniform(String name, Mat4 matrix)
    {
        loadMatrix(uniforms.get(name), matrix);
    }

    private void loadInteger(int location, int value)
    {
        glUniform1i(location, value);
    }

    private void loadFloat(int location, float value)
    {
        glUniform1f(location, value);
    }

    private void loadVector(int location, float x, float y)
    {
        glUniform2f(location, x, y);
    }

    private void loadVector(int location, float x, float y, float z)
    {
        glUniform3f(location, x, y, z);
    }

    private void loadVector(int location, float x, float y, float z, float w)
    {
        glUniform4f(location, x, y, z, w);
    }

    private void loadBool(int location,boolean flag)
    {
        glUniform1f(location, flag? 1 : 0);
    }

    private void loadMatrix(int location, Mat4 matrix)
    {
        GLShader.matrix.clear();
        matrix.store(GLShader.matrix);
        GLShader.matrix.flip();
        glUniformMatrix4fv(location, false, GLShader.matrix);
    }

    public void destroy()
    {
        unbind();
        glDetachShader(programId, vertexShader);
        glDetachShader(programId, fragmentShader);
        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);
        glDeleteProgram(programId);
    }

    protected void bindAttribute(int attribute, String variable)
    {
        glBindAttribLocation(programId, attribute, variable);
    }

    public void unbind()
    {
        glUseProgram(0);
    }

    public void bind()
    {
        glUseProgram(programId);
    }
    protected abstract void bindAttributes();

    private static int loadShader(String file, int type)
    {
        StringBuilder source = new StringBuilder();
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(Resources.class.getResourceAsStream(file))))) {
            String line;
            while((line = reader.readLine()) != null)
                source.append(line).append("\n");
        }
        catch(IOException e) {
            throw new RuntimeException("Could not read file!");
        }
        int shader = glCreateShader(type);
        glShaderSource(shader, source.toString());
        glCompileShader(shader);
        if(glGetShaderi(shader, GL_COMPILE_STATUS) == GL_FALSE)
            throw new RuntimeException(glGetShaderInfoLog(shader) + "\nCould not compile shader: " + file);
        return shader;
    }
}