import model.GameEngine;
import model.GameLogic;
import model.exceptions.InitializationException;
import model.exceptions.ResourceException;

public class Main {

    public static void main(String[] args) {
        GameLogic gameLogic = null;
        try {
            gameLogic = new GameLogic();
        } catch (InitializationException | ResourceException exception) {
            exception.printStackTrace();
        }
        GameEngine gameEngine = new GameEngine(gameLogic);
        gameEngine.run();
    }
}
