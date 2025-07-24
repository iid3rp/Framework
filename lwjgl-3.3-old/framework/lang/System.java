package framework.lang;

import framework.io.Resources;

import java.awt.GraphicsEnvironment;
import java.io.InputStream;
import java.util.Scanner;

public class System
{
    public static int getRefreshRate()
    {
        return GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .getDisplayMode()
                .getRefreshRate();
    }

    public static<E> String getSourceCode(String className)
    {
        // Change ".class" to ".java" to retrieve the source file instead of the class file
        InputStream inputStream = Resources.class.getResourceAsStream(
                className + ".java"
        );

        if (inputStream != null) {
            try (Scanner scanner = new Scanner(inputStream).useDelimiter("\\A")) {
                return scanner.hasNext() ? scanner.next() : "";
            }
        } else {
            return "Unable to read source code";
        }
    }

    public static void print(Object o)
    {
        java.lang.System.out.println(o);
    }

}
