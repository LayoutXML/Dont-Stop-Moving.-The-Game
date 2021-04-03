package model;

import lombok.SneakyThrows;
import model.exceptions.ResourceException;
import model.utils.TimerUtils;

public class GameEngine implements Runnable {
    public static final int MAX_FPS = 60;
    public static final int TICK_RATE = 30;

    private final TimerUtils timerUtils = new TimerUtils();
    private final GameLogic gameLogic;

    public GameEngine(GameLogic gameLogic) {
        this.gameLogic = gameLogic;
    }

    @SneakyThrows
    @Override
    public void run() {
        loop();
        free();
    }

    // Game loop design pattern
    private void loop() throws ResourceException {
        float accumulatedTime = 0f;
        float updateInterval = 1f / TICK_RATE;

        while (!gameLogic.endGame()) {
            accumulatedTime += timerUtils.getElapsedTime();

            gameLogic.handleInput();

            while (accumulatedTime >= updateInterval) {
                gameLogic.update();
                accumulatedTime -= updateInterval;
            }

            gameLogic.render();

            synchronize();
        }
    }

    private void synchronize() {
        float frameTime = 1f / MAX_FPS;
        double endTime = timerUtils.getPreviousLoopTime() + frameTime;
        while (timerUtils.getTimeInSeconds() < endTime) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException ignored) {
            }
        }
    }

    private void free() {
        gameLogic.free();
    }
}
