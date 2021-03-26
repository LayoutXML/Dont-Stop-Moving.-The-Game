package model.utils;

import lombok.Getter;

@Getter
public class TimerUtils {
    private float previousLoopTime;

    public void reset() {
        previousLoopTime = getTimeInSeconds();
    }

    public float getTimeInSeconds() {
        return System.nanoTime() / 1000000000f;
    }

    public float getElapsedTime() {
        float currentTime = getTimeInSeconds();
        float elapsedTime = currentTime - previousLoopTime;
        previousLoopTime = currentTime;
        return elapsedTime;
    }

    public float getElapsedTimeSinceInitialization() {
        float currentTime = getTimeInSeconds();
        return currentTime - previousLoopTime;
    }
}
