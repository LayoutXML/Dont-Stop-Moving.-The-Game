package engine;

import model.exceptions.InitializationException;
import model.exceptions.ResourceException;
import utils.TimerUtils;

public class GameEngine implements Runnable {
    public static final int MAX_FPS = 60;
    public static final int TICK_RATE = 30;

    private final TimerUtils timerUtils = new TimerUtils();
    private final Window window;
    private final GameLogic gameLogic;

    public GameEngine(String name, int width, int height, boolean vSyncEnabled, boolean resizable, GameLogic gameLogic) {
        window = Window.builder()
                .name(name)
                .width(width)
                .height(height)
                .vSyncEnabled(vSyncEnabled)
                .resizable(resizable)
                .build();
        this.gameLogic = gameLogic;
    }

    @Override
    public void run() {
        try {
            initialize();
            loop();
        } catch (InitializationException | ResourceException exception) {
            exception.printStackTrace();
        }
        free();
    }

    private void initialize() throws InitializationException, ResourceException {
        window.initialize();
        timerUtils.initialize();
        gameLogic.initialize(window);
    }

    private void loop() {
        float accumulatedTime = 0f;
        float updateInterval = 1f / TICK_RATE;

        while (!window.windowShouldClose()) {
            accumulatedTime += timerUtils.getElapsedTime();

            input();

            while (accumulatedTime >= updateInterval) {
                update(updateInterval);
                accumulatedTime -= updateInterval;
            }

            render();

            if (!window.isVSyncEnabled()) {
                synchronize();
            }
        }
    }

    private void input() {
        gameLogic.input(window);
    }

    private void update(float updateInterval) {
        gameLogic.update(updateInterval);
    }

    private void render() {
        gameLogic.render(window);
        window.update();
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
