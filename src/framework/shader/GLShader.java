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

public final class GLShader
{
    public static final String vertexPrefix = "glsl" + File.separator + "vertex" + File.separator;
    public static final String fragmentPrefix =  "glsl" + File.separator + "fragment" + File.separator;
    private static FloatBuffer matrix = Buffer.createFloatBuffer(16);
    private static ShaderProgram currentProgram;

    public static abstract class ShaderProgram
    {
        public static class Struct
        {
            public String[] fields;
            public String name;

            public Struct(String name, String... fields)
            {
                this.name = name;
                this.fields = fields;
            }
        }
        
        private int programId;
        private int vertex;
        private int fragment;
        Map<String, Integer> uniforms;

        public ShaderProgram(int id, int vertex, int fragment)
        {
            programId = id;
            this.vertex = vertex;
            this.fragment = fragment;
            uniforms = new HashMap<>();
        }

        protected void addUniform(String name, int index)
        {
            for(int i = 0; i < index; i++)
            {
                String uniformName = name + "[" + i + "]";
                uniforms.put(uniformName, getUniformLocation(uniformName));
            }
        }

        protected void addUniform(Struct struct, int index)
        {
            for(int i = 0; i < index; i++)
                addUniform(struct.name + "[" + i + "]");
        }

        protected void addUniform(Struct struct)
        {
            for(String s : struct.fields)
            {
                String name = struct.name + "." + s;
                uniforms.put(name, getUniformLocation(name));
            }
        }

        protected void addUniform(String name)
        {
            uniforms.put(name, getUniformLocation(name));
        }

        protected int getUniformLocation(String name)
        {
            return glGetUniformLocation(programId, name);
        }

        protected void bindAttribute(int attribute, String variable)
        {
            glBindAttribLocation(programId, attribute, variable);
        }

        protected abstract void bindAttributes();

        protected abstract void getAllUniformLocations();
    }

    public static void initializeShaders()
    {
        EntityShader.initializeShader();
    }

    private static void getAllUniformLocations(ShaderProgram program)
    {
        program.getAllUniformLocations();
    }

    private static void linkAndValidateProgram(ShaderProgram program)
    {
        glLinkProgram(program.programId);
        glValidateProgram(program.programId);
    }

    public static void loadUniform(String name, float value)
    {
        loadFloat(currentProgram.uniforms.get(name), value);
    }

    public static void loadUniform(String name, float x, float y)
    {
        loadVector(currentProgram.uniforms.get(name), x, y);
    }

    public static void loadUniform(String name, float x, float y, float z)
    {
        loadVector(currentProgram.uniforms.get(name), x, y, z);
    }

    public static void loadUniform(String name, float x, float y, float z, float w)
    {
        loadVector(currentProgram.uniforms.get(name), x, y, z, w);
    }

    public static void loadUniform(String name, int value)
    {
        loadInteger(currentProgram.uniforms.get(name), value);
    }

    public static void loadUniform(String name, boolean flag)
    {
        loadBool(currentProgram.uniforms.get(name), flag);
    }

    public static void loadUniform(String name, Mat4 matrix)
    {
        loadMatrix(currentProgram.uniforms.get(name), matrix);
    }

    public static void loadInteger(int location, int value)
    {
        glUniform1i(location, value);
    }

    public static void loadFloat(int location, float value)
    {
        glUniform1f(location, value);
    }

    public static void loadVector(int location, float x, float y)
    {
        glUniform2f(location, x, y);
    }

    public static void loadVector(int location, float x, float y, float z)
    {
        glUniform3f(location, x, y, z);
    }

    public static void loadVector(int location, float x, float y, float z, float w)
    {
        glUniform4f(location, x, y, z, w);
    }

    public static void loadBool(int location,boolean flag)
    {
        glUniform1f(location, flag? 1 : 0);
    }

    public static void loadMatrix(int location, Mat4 matrix)
    {
        GLShader.matrix.clear();
        matrix.store(GLShader.matrix);
        GLShader.matrix.flip();
        glUniformMatrix4fv(location, false, GLShader.matrix);
    }
    
    public static void unbind()
    {
        glUseProgram(0);
        currentProgram = null;
    }

    public static void bind(ShaderProgram program)
    {
        currentProgram = program;
        glUseProgram(currentProgram.programId);
    }

    protected static void bindAttributes(ShaderProgram program)
    {
        program.bindAttributes();
    }

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

    private static void destroyShaderProgram(ShaderProgram program)
    {
        glDetachShader(program.programId, program.vertex);
        glDetachShader(program.programId, program.fragment);
        glDeleteShader(program.vertex);
        glDeleteShader(program.fragment);
        glDeleteProgram(program.programId);
    }

    public static void destroy()
    {
        EntityShader.destroy();
    }

    /*
    ===========================================================================================================================
    Shader Classes

        - These are for shaders that are used for this engine
    ===========================================================================================================================
     */
    public static class EntityShader
    {
        private static String vertex = GLShader.vertexPrefix + "EntityVertexShader.glsl";
        private static String fragment = GLShader.fragmentPrefix + "EntityFragmentShader.glsl";
        public static ShaderProgram program;

        public static void initializeShader()
        {
            int vert = loadShader(vertex,GL_VERTEX_SHADER);
            int frag = loadShader(fragment,GL_FRAGMENT_SHADER);
            int id = glCreateProgram();
            program = new ShaderProgram(id, vert, frag)
            {
                @Override
                protected void bindAttributes()
                {
                    bindAttribute(0, "pos");
                    bindAttribute(1, "texCoords");
                }

                @Override
                protected void getAllUniformLocations()
                {
                    addUniform("transformationMatrix");
                    addUniform("projectionMatrix");
                    addUniform("viewMatrix");
                    addUniform("hasTexture");
                    addUniform("backgroundColor");
                    addUniform(new Struct("lights", "pos", "color", "intensity", "constant", "linear", "quadratic", "distance"));
                }
            };
            glAttachShader(id, vert);
            glAttachShader(id, frag);
            bindAttributes(program);
            linkAndValidateProgram(program);
            getAllUniformLocations(program);
        }

        public static void destroy()
        {
            unbind();
            destroyShaderProgram(program);
        }
    }
}