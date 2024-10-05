package framework.shader;

import framework.lang.Mat4;
import framework.lang.Vec2;
import framework.lang.Vec3;
import framework.lang.Vec4;
import framework.util.FileUtils;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL46.*;

public abstract class GLShader
{
    public static final int lights = 20;
    private String vertexFile, fragmentFile;
    private int programId, vertexShaderId, fragmentShaderId;

    private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);    // 16 is a 4 x 4 matrix

    public GLShader(String vertexPath, String fragmentPath) {
        vertexFile = FileUtils.loadShader(vertexPath);
        fragmentFile = FileUtils.loadShader(fragmentPath);

        create();
    }

    protected abstract void bindAttributes();

    protected void bindAttribute(int attribute, String variableName) {
        glBindAttribLocation(programId, attribute, variableName);
    }

    protected int getUniformLocation(String uniformName) {
        return glGetUniformLocation(programId, uniformName);
    }

    protected abstract void getAllUniformLocations();

    protected void loadFloat(int location, float value) {
        glUniform1f(location, value);
    }

    protected void loadInt(int location, int value) {
        glUniform1i(location, value);
    }

    protected void loadVector(int location, Vec3 vector)
    {
        glUniform3f(location, vector.x, vector.y, vector.z);
    }

    protected void loadVector(int location, Vec2 vector)
    {
        glUniform2f(location, vector.x, vector.y);
    }

    protected void loadBoolean(int location, boolean value)
    {
        glUniform1f(location, value ? 1 : 0);
    }

    protected void loadMatrix(int location, Mat4 matrix)
    {
        matrix.store(matrixBuffer);
        matrixBuffer.flip();
        glUniformMatrix4fv(location, false, matrixBuffer);
    }

    public void create() {
        programId = glCreateProgram();
        vertexShaderId = glCreateShader(GL_VERTEX_SHADER);

        glShaderSource(vertexShaderId, vertexFile);
        glCompileShader(vertexShaderId);

        // Error check shader program code after trying to compile it
        if (glGetShaderi(vertexShaderId, GL_COMPILE_STATUS) == GL_FALSE) {
            // The shader program didn't compile
            throw new RuntimeException("\nVertex Shader: " + glGetShaderInfoLog(vertexShaderId));
        }

        // The shader code did successfully compile
        fragmentShaderId = glCreateShader(GL_FRAGMENT_SHADER);

        glShaderSource(fragmentShaderId, fragmentFile);
        glCompileShader(fragmentShaderId);

        // Error check fragment shader program code after attempt to compile it
        if (glGetShaderi(fragmentShaderId, GL_COMPILE_STATUS) == GL_FALSE) {
            // The fragment shader program didn't compile
            throw new RuntimeException("\nFragment Shader: " + glGetShaderInfoLog(fragmentShaderId));
        }

        // The fragment shader code did successfully compile
        glAttachShader(programId, vertexShaderId);
        glAttachShader(programId, fragmentShaderId);

        bindAttributes();

        // Linking
        glLinkProgram(programId);
        if (glGetProgrami(programId, GL_LINK_STATUS) == GL_FALSE) {
            throw new RuntimeException("Program Linking: " + glGetProgramInfoLog(programId));
        }

        // Validating
        glValidateProgram(programId);

        if (glGetProgrami(programId, GL_VALIDATE_STATUS) == GL_FALSE) {
            throw new RuntimeException("Program Validation: " + glGetProgramInfoLog(programId));
        }


        getAllUniformLocations();
    }

    public void bind() {
        glUseProgram(programId);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public void dispose() {
        glDetachShader(programId, vertexShaderId);
        glDetachShader(programId, fragmentShaderId);
        glDeleteShader(vertexShaderId);
        glDeleteShader(fragmentShaderId);
        glDeleteProgram(programId);
    }

    protected void loadVector(int location, Vec4 vector)
    {
        glUniform4f(location, vector.x, vector.y, vector.z,  vector.w);
    }
}
