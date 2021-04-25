package model;

import controller.InputManager;
import model.exceptions.InitializationException;
import model.exceptions.ResourceException;
import org.joml.Vector2f;
import org.joml.Vector3f;
import view.*;

import javax.swing.*;

public class GameLogic {
    private final Window window = new Window();
    private final RenderEngine renderEngine = new RenderEngine();
    private final InputManager inputManager = new InputManager();
    private final Camera camera = new Camera();
    private final SoundManager soundManager = new SoundManager();

    private final Vector3f movementDirection = new Vector3f();
    private final Vector2f displayRotation = new Vector2f();
    private Vector3f positionDelta = new Vector3f();

    private boolean jump = false;
    private boolean exit = false;
    private boolean win = false;
    private boolean showingResults = false;

    private Level level;
    private Status status;

    public GameLogic(String levelName) throws ResourceException, InitializationException {
        initialize(levelName);
    }

    public void initialize(String levelName) throws InitializationException, ResourceException {
        inputManager.initialize(window);
        level = new Level(levelName);
        status = new Status();
        soundManager.initialize(level);
        camera.setPosition(level.getStartingPosition());
    }

    public void handleInput() {
        if (inputManager.isExit(window)) {
            exit = true;
            return;
        }

        inputManager.updateDisplayRotation(window, displayRotation);
        jump = inputManager.isJumping(window);

        if (inputManager.isRestart(window)) {
            camera.setPosition(level.getStartingPosition());
        } else {
            inputManager.updateMovementDirection(window, movementDirection, positionDelta);
        }
    }

    public void update() throws ResourceException, InitializationException {
        if (!win) {
            CameraUpdateWrapper cameraUpdate = camera.update(movementDirection, displayRotation, jump, level);
            positionDelta = cameraUpdate.getPositionDelta();
            level.update(camera.getPosition());
            status.update(camera.getPosition(), level, cameraUpdate.isPositionReset());
            soundManager.update(cameraUpdate.isWin(), level);
            win = cameraUpdate.isWin();
        } else if (!showingResults) {
            showResults(status.getTimerText().getText());
        }
    }

    private void showResults(String time) {
        JOptionPane.showMessageDialog(new JFrame(), "Congratulations! Your time: " + time, "Congratulations!", JOptionPane.PLAIN_MESSAGE);
        exit = true;
    }

    public void render() {
        renderEngine.render(window, camera, level, status);
        window.update();
    }

    public boolean endGame() {
        return exit || window.windowShouldClose();
    }

    public void free() {
        if (level != null) {
            level.free();
        }
        if (status != null) {
            status.free();
        }
        soundManager.free();
        renderEngine.free();
        window.free();
    }
}
