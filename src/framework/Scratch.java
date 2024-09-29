package framework;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.awt.AWTGLCanvas;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static org.lwjgl.opengl.GL46.*;

public class Scratch extends JFrame
{
    private AWTGLCanvas canvas;

    public Scratch() {
        setTitle("LWJGL AWTGLCanvas Example");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initialize the AWTGLCanvas
        canvas = new AWTGLCanvas() {
            @Override
            public void initGL() {
                // Set up OpenGL context here
                GL.createCapabilities();
                glClearColor(0.1f, 0.2f, 0.3f, 1.0f); // Set background color
            }

            @Override
            public void paintGL() {
                // Render loop
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // Clear the framebuffer

                // Insert OpenGL drawing code here
                // For example: draw a simple triangle
                glBegin(GL_TRIANGLES);
                glColor3f(1.0f, 0.0f, 0.0f); glVertex2f(-0.5f, -0.5f);
                glColor3f(0.0f, 1.0f, 0.0f); glVertex2f( 0.5f, -0.5f);
                glColor3f(0.0f, 0.0f, 1.0f); glVertex2f( 0.0f,  0.5f);
                glEnd();

                // Swap buffers to display the rendered image
                swapBuffers();
            }
        };

        // Set up a basic layout for the canvas
        add(canvas, BorderLayout.CENTER);
        setVisible(true);

        // Start rendering loop
        startRenderLoop();
    }

    private void startRenderLoop() {
        Timer timer = new Timer(16, new ActionListener() { // 60 FPS
            @Override
            public void actionPerformed(ActionEvent e) {
                canvas.render();
            }
        });
        timer.start();
    }

    public static void main(String[] args) {
        // Make sure to run the UI on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            new Scratch();
        });
    }
}
