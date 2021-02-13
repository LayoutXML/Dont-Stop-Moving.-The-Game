package utils;

import lombok.Getter;

@Getter
public class TimerUtils {
    private float previousLoopTime;

    public void initialize() {
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
}
