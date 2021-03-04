package engine;

import engine.graphics.FontTexture;
import lombok.Getter;
import model.GameItem;
import model.TextItem;
import model.exceptions.ResourceException;
import org.joml.Vector4f;
import utils.TimerUtils;

import java.awt.*;

@Getter
public class Status {
    private static final Font FONT = new Font("Sans-Serif", Font.PLAIN, 20);
    private static final String CHARSET = "ISO-8859-1";

    private final GameItem[] gameItems;
    private final TextItem timerText;

    private TimerUtils timerUtils = new TimerUtils();

    public Status() throws ResourceException {
        FontTexture fontTexture = new FontTexture(FONT, CHARSET);
        timerText = new TextItem("00:00", fontTexture);
        timerText.getMesh().getMaterial().setAmbient(new Vector4f(1, 1, 1, 1));

        gameItems = new GameItem[]{timerText};
    }

    public void startTimer() {
        timerUtils.initialize();
    }

    public void render(Window window) {
        timerText.setPositionFromCoordinates(10f, 10f, 0);

        float elapsedTime = timerUtils.getElapsedTimeSinceInitialization();
        int minutes = elapsedTime != 0 ? (int) (elapsedTime / 60) : 0;
        int seconds = (int) (elapsedTime - minutes * 60);

        timerText.setText(String.format("%02d:%02d", minutes, seconds));
        timerText.getMesh().getMaterial().setAmbient(new Vector4f(1, 1, 1, 1));
    }

    public void free() {
        for (GameItem gameItem : gameItems) {
            gameItem.free();
        }
    }

}
