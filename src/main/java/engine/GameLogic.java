package engine;

import model.exceptions.InitializationException;

public interface GameLogic {
    void initialize(Window window) throws InitializationException;
    void input(Window window);
    void update(float updateInterval);
    void render(Window window);
    void free();
}
