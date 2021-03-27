package model;

import controller.InputManager;
import model.exceptions.InitializationException;
import model.exceptions.ResourceException;
import org.joml.Vector2f;
import org.joml.Vector3f;
import view.*;

public class GameLogic {
    private final Window window = new Window();
    private final RenderEngine renderEngine = new RenderEngine();
    private final InputManager inputManager = new InputManager();
    private final Camera camera = new Camera();

    private final Vector3f movementDirection = new Vector3f();
    private final Vector2f displayRotation = new Vector2f();

    private Level level;
    private Status status;

    public GameLogic() throws ResourceException, InitializationException {
        initialize();
    }

    public void initialize() throws InitializationException, ResourceException {
        inputManager.initialize(window);

        level = new Level();

        status = new Status();
        status.startTimer();
    }

    public void handleInput() {
        inputManager.updateDisplayRotation(window, displayRotation);
        inputManager.updateMovementDirection(window, movementDirection);
    }

    public void update() {
        camera.update(movementDirection, displayRotation, level);
        level.update(camera.getPosition());
        status.update(camera.getPosition());
    }

    public void render() {
        renderEngine.render(window, camera, level, status);
        window.update();
    }

    public boolean endGame() {
        return window.windowShouldClose();
    }

    public void free() {
        if (level != null) {
            level.free();
        }
        if (status != null) {
            status.free();
        }
        renderEngine.free();
    }
}
