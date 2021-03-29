package view;

import org.joml.Vector3f;
import view.graphics.FontTexture;
import lombok.Getter;
import model.exceptions.ResourceException;
import org.joml.Vector4f;
import model.utils.TimerUtils;

import java.awt.*;

@Getter
public class Status {
    private static final Font FONT = new Font("Sans-Serif", Font.PLAIN, 20);
    private static final String CHARSET = "ISO-8859-1";

    private final TextItem timerText;
    private final TextItem coordinateText;
    private final TextItem[] gameItems;

    private final TimerUtils timerUtils = new TimerUtils();
    private final FontTexture fontTexture;

    private Vector3f cameraPosition;

    public Status() throws ResourceException {
        fontTexture = new FontTexture(FONT, CHARSET);

        timerText = new TextItem("00:00", fontTexture);
        timerText.getMesh().getMaterial().setAmbient(new Vector4f(1, 1, 1, 1));

        coordinateText = new TextItem("0, 0, 0", fontTexture);
        coordinateText.getMesh().getMaterial().setAmbient(new Vector4f(1, 1, 1, 1));

        gameItems = new TextItem[]{timerText, coordinateText};
    }

    public void startTimer() {
        timerUtils.reset();
    }

    public void render(Window window) {
        renderTimerText();
        renderCoordinateText(window);
    }

    private void renderTimerText() {
        timerText.setText(generateTimerText());
        timerText.setPositionFromCoordinates(10f, 10f, 0);
    }

    private void renderCoordinateText(Window window) {
        String newCoordinateText = generateCoordinateText(cameraPosition);
        coordinateText.setText(newCoordinateText);
        coordinateText.setPositionFromCoordinates(10f, window.getHeight() - fontTexture.getHeight() - 10, 0);
    }

    public void update(Vector3f cameraPosition) {
        this.cameraPosition = cameraPosition;
    }

    private String generateTimerText() {
        float elapsedTime = timerUtils.getElapsedTimeSinceInitialization();
        int minutes = elapsedTime != 0 ? (int) (elapsedTime / 60) : 0;
        int seconds = (int) (elapsedTime - minutes * 60);

        return String.format("%02d:%02d", minutes, seconds);
    }

    private String generateCoordinateText(Vector3f cameraPosition) {
        return String.format("%.2f, %.2f, %.2f", cameraPosition.x, cameraPosition.y, cameraPosition.z);
    }

    public void free() {
        for (TextItem gameItem : gameItems) {
            gameItem.free();
        }
    }
}
