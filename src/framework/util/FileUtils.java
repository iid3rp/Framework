package framework.util;

import com.sun.tools.javac.Main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

public class FileUtils {

    private FileUtils() {}

    public static String loadShader(String file) {
        StringBuilder result = new StringBuilder();

        try {
            InputStream stream = Objects.requireNonNull(Main.class.getResourceAsStream("glsl/" + file));
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(stream)
            );

            String buffer;

            while ((buffer = reader.readLine()) != null) {
                result.append(buffer).append("\n");
            }
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage() + file);
        }

        return result.toString();
    }

}
