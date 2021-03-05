package engine;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import model.exceptions.InitializationException;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

@Getter
@Setter
@Builder
public class Window {
    private final String name;
    private final boolean resizable;
    private final boolean vSyncEnabled;

    private int width;
    private int height;
    private boolean resized;
    private long windowId;

    public void initialize() throws InitializationException {
        if (!glfwInit()) {
            throw new InitializationException("IE1");
        }

        glfwWindowHint(GLFW_RESIZABLE, resizable ? GL_TRUE : GL_FALSE);

        // OSX specific
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);

        windowId = glfwCreateWindow(width, height, name, NULL, NULL);
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

        if (vSyncEnabled) {
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

//        glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
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
