package framework.shader;

import framework.entity.Camera;
import framework.lang.Mat4;

public class EntityShader
{
    private static String vertex = GLShader.vertexPrefix + "EntityVertexShader.glsl";
    private static String fragment = GLShader.fragmentPrefix + "EntityFragmentShader.glsl";
    private static Shader shader;

    private static class Shader extends GLShader
    {
        public Shader()
        {
            super(vertex, fragment);
        }

        @Override
        protected void getAllUniformLocations()
        {
            addUniform("transformationMatrix");
            addUniform("projectionMatrix");
            addUniform("viewMatrix");
        }

        @Override
        protected void bindAttributes()
        {
            bindAttribute(0, "pos");
            bindAttribute(1, "texCoords");
        }

    }

    public static void setShader()
    {
        shader = new Shader();
    }

    public static void bind()
    {
        shader.bind();
    }

    public static void unbind()
    {
        shader.unbind();
    }

    public static void destroy()
    {
        shader.destroy();
    }

    public static void loadUniform(String name, float x, float y)
    {
        shader.loadUniform(name, x, y);
    }

    public static void loadUniform(String name, float x, float y, float z)
    {
        shader.loadUniform(name, x, y, z);
    }

    public static void loadUniform(String name, float x, float y, float z, float w)
    {
        shader.loadUniform(name, x, y, z, w);
    }

    public static void loadUniform(String name, int value)
    {
        shader.loadUniform(name, value);
    }

    public static void loadUniform(String name, boolean flag)
    {
        shader.loadUniform(name, flag);
    }

    public static void loadUniform(String name, Mat4 matrix)
    {
        shader.loadUniform(name, matrix);
    }

}
