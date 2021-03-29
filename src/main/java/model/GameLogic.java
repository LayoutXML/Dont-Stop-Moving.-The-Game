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
    private Vector3f positionDelta = new Vector3f();
    private boolean jump = false;

    private Level level;
    private Status status;

    public GameLogic() throws ResourceException, InitializationException {
        initialize();
    }

    public void initialize() throws InitializationException, ResourceException {
        inputManager.initialize(window);

        level = new Level("1.lvl");

        status = new Status();
        status.startTimer();

        camera.setPosition(level.getStartingPosition());
    }

    public void handleInput() {
        inputManager.updateDisplayRotation(window, displayRotation);
        inputManager.updateMovementDirection(window, movementDirection, positionDelta);
        jump = inputManager.isJumping(window);
    }

    public void update() {
        positionDelta = camera.update(movementDirection, displayRotation, jump, level);
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
