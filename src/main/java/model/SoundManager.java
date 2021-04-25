package model;

import model.exceptions.InitializationException;
import model.exceptions.ResourceException;
import view.Level;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class SoundManager {
    private boolean playLava = false;
    private boolean playWater = false;
    private boolean playingSounds = false;
    private static final List<Clip> runningSounds = new ArrayList<>();

    public void initialize(Level level) {
        playLava = level.getInteractiveGameItems().stream().anyMatch(gameItem -> ObjectType.LAVA.equals(gameItem.getObjectType()));
        playWater = level.getInteractiveGameItems().stream().anyMatch(gameItem -> ObjectType.WATER.equals(gameItem.getObjectType()));
    }

    // Update method design pattern
    public void update(boolean win, Level level) throws ResourceException, InitializationException {
        if (!level.isLevelLoaded()) {
            return;
        }

        if (!playingSounds) {
            playSounds();
            playingSounds = true;
        }

        if (win) {
            playSoundSingle("src/sounds/win.wav");
        }
    }

    private void playSounds() throws InitializationException {
        if (playLava) {
            playSoundLoop("src/sounds/lava.wav");
        }
        if (playWater) {
            playSoundLoop("src/sounds/water.wav");
        }
    }

    private static synchronized void playSoundLoop(String fileName) throws InitializationException {
        try {
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(new FileInputStream(fileName)));
            Clip clip = AudioSystem.getClip();
            clip.open(inputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
            runningSounds.add(clip);
        } catch (Exception e) {
            throw new InitializationException("Cannot play audio");
        }
    }

    private static synchronized void playSoundSingle(String fileName) throws ResourceException {
        try {
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(new FileInputStream(fileName)));
            Clip clip = AudioSystem.getClip();
            clip.open(inputStream);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResourceException("Cannot play audio");
        }
    }

    public void free() {
        if (!runningSounds.isEmpty()) {
            runningSounds.forEach(DataLine::stop);
        }
    }
}
