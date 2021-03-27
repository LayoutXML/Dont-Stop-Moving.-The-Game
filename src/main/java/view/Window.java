package view;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import model.exceptions.InitializationException;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_MULTISAMPLE;
import static org.lwjgl.system.MemoryUtil.NULL;

@Getter
@Setter
public class Window {
    public static final String NAME = "Game";
    public static final boolean V_SYNC_ENABLED = true;
    public static final boolean RESIZEABLE = true;
    public static final boolean POLYGON_MODE = false;

    private int width = 1280;
    private int height = 720;
    private boolean resized;
    private long windowId;

    public Window() throws InitializationException {
        initialize();
    }

    public void initialize() throws InitializationException {
        if (!glfwInit()) {
            throw new InitializationException("IE1");
        }

        glfwWindowHint(GLFW_RESIZABLE, RESIZEABLE ? GL_TRUE : GL_FALSE);

        glfwWindowHint(GLFW_SAMPLES, 4);

        windowId = glfwCreateWindow(width, height, NAME, NULL, NULL);
        if (windowId == NULL) {
            throw new InitializationException("IE2");
        }

        glfwSetFramebufferSizeCallback(windowId, ((window, width, height) -> {
            this.width = width;
            this.height = height;
            this.resized = true;
        }));

        GLFWVidMode glfwVidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        if (glfwVidMode != null) {
            glfwSetWindowPos(windowId, (glfwVidMode.width() - width) / 2, (glfwVidMode.height() - height) / 2);
        }

        glfwMakeContextCurrent(windowId);

        if (V_SYNC_ENABLED) {
            glfwSwapInterval(1);
        }

        glfwShowWindow(windowId);

        GL.createCapabilities();

        setClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        glEnable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);

        glEnable(GL_MULTISAMPLE);

        glfwSetInputMode(windowId, GLFW_CURSOR, GLFW_CURSOR_HIDDEN);

        if (POLYGON_MODE) {
            glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        }
    }

    public void setClearColor(float red, float green, float blue, float alpha) {
        glClearColor(red, green, blue, alpha);
    }

    public boolean windowShouldClose() {
        return glfwWindowShouldClose(windowId);
    }

    public void update() {
        glfwSwapBuffers(windowId);
        glfwPollEvents();
    }
}
