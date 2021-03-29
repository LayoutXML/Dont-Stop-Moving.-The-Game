package view.graphics;

import lombok.Getter;
import model.exceptions.ResourceException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.HashMap;
import java.util.Map;

@Getter
public class FontTexture {
    private static final int PADDING = 2;
    private static final String IMAGE_FILE_EXTENSION = "png";

    private final Font font;
    private final String name;
    private final Map<Character, CharacterInformation> characters = new HashMap<>();

    private Texture texture;
    private int height;
    private int width;

    public FontTexture(Font font, String name) throws ResourceException {
        this.font = font;
        this.name = name;

        generateTexture();
    }

    private void generateTexture() throws ResourceException {
        String allCharacters = getAllCharacters();

        generateCharacters(allCharacters);
        generateImage(allCharacters);
    }

    private void generateCharacters(String allCharacters) {
        BufferedImage bufferedImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setFont(font);

        FontMetrics fontMetrics = graphics2D.getFontMetrics();
        height = fontMetrics.getHeight();

        width = 0;
        for (int i = 0; i < allCharacters.length(); i++) {
            CharacterInformation characterInformation = new CharacterInformation(width, fontMetrics.charWidth(allCharacters.charAt(i)));
            characters.put(allCharacters.charAt(i), characterInformation);
            width += characterInformation.getWidth() + PADDING;
        }

        graphics2D.dispose();
    }

    private void generateImage(String allCharacters) throws ResourceException {
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setFont(font);
        graphics2D.setColor(Color.WHITE);

        FontMetrics fontMetrics = graphics2D.getFontMetrics();

        int currentWidth = 0;
        for (int i = 0; i < allCharacters.length(); i++) {
            CharacterInformation characterInformation = characters.get(allCharacters.charAt(i));
            graphics2D.drawString(String.valueOf(allCharacters.charAt(i)), currentWidth, fontMetrics.getAscent());
            currentWidth += characterInformation.getWidth() + PADDING;
        }

        graphics2D.dispose();

        ByteBuffer byteBuffer;
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            ImageIO.write(bufferedImage, IMAGE_FILE_EXTENSION, byteArrayOutputStream);
            byteArrayOutputStream.flush();

            byte[] data = byteArrayOutputStream.toByteArray();
            byteBuffer = ByteBuffer.allocateDirect(data.length);
            byteBuffer.put(data, 0, data.length);
            byteBuffer.flip();
        } catch (IOException e) {
            throw new ResourceException("Font error");
        }
        texture = new Texture(byteBuffer);
    }

    public CharacterInformation getCharacterInformation(char character) {
        return characters.get(character);
    }

    private String getAllCharacters() {
        CharsetEncoder charsetEncoder = Charset.forName(name).newEncoder();
        StringBuilder stringBuilder = new StringBuilder();

        for (char character = 0; character < Character.MAX_VALUE; character++) {
            if (charsetEncoder.canEncode(character)) {
                stringBuilder.append(character);
            }
        }

        return stringBuilder.toString();
    }
}
