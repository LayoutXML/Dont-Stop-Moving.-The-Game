package model;

import model.exceptions.InitializationException;
import model.exceptions.ResourceException;
import view.Level;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.BufferedInputStream;
import java.io.FileInputStream;

public class SoundManager {
    private boolean playLava = false;

    public void initialize(Level level) throws InitializationException {
        playLava = level.getGameItems().stream().anyMatch(gameItem -> ObjectType.LAVA.equals(gameItem.getObjectType()));

        playSounds();
    }

    public void update(boolean win) throws ResourceException {
        if (win) {
            playWinSound();
        }
    }

    private void playSounds() throws InitializationException {
        if (playLava) {
            playLavaSound();
        }
    }

    private static synchronized void playLavaSound() throws InitializationException {
        try {
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(new FileInputStream("src/sounds/lava.wav")));
            Clip clip = AudioSystem.getClip();
            clip.open(inputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        } catch (Exception e) {
            throw new InitializationException("Cannot play audio");
        }
    }

    private static synchronized void playWinSound() throws ResourceException {
        try {
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(new FileInputStream("src/sounds/win.wav")));
            Clip clip = AudioSystem.getClip();
            clip.open(inputStream);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResourceException("Cannot play audio");
        }
    }
}
