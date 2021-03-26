import model.GameEngine;
import model.GameLogic;
import model.GameLogicImpl;

public class Main {
    private static final String NAME = "Game";
    private static final boolean V_SYNC_ENABLED = true;
    private static final boolean RESIZEABLE = true;

    public static void main(String[] args) {
        GameLogic gameLogic = new GameLogicImpl();
        GameEngine gameEngine = new GameEngine(NAME, 600, 400, V_SYNC_ENABLED, RESIZEABLE, gameLogic);
        gameEngine.run();
    }
}
