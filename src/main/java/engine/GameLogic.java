package engine;

import model.exceptions.InitializationException;
import model.exceptions.ResourceException;

public interface GameLogic {
    void initialize(Window window) throws InitializationException, ResourceException;
    void input(InputManager inputManager, Window window);
    void update(InputManager inputManager, float updateInterval);
    void render(Window window);
    void free();
}
