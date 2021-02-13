package engine;

import model.exceptions.InitializationException;
import model.exceptions.ResourceException;

public interface GameLogic {
    void initialize(Window window) throws InitializationException, ResourceException;
    void input(Window window);
    void update(float updateInterval);
    void render(Window window);
    void free();
}
