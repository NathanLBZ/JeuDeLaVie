package fr.nathan.modePause;

public class Pause {
    private volatile Boolean isPaused;

    public Pause() {
        this.isPaused = true;
    }

    public Boolean isPaused() {
        return this.isPaused;
    }

    public void changeMode() {
        this.isPaused = !this.isPaused;
    }
}