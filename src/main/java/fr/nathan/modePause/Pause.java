package fr.nathan.modePause;

public class Pause {
    public Boolean isPaused;

    public Pause() {
        this.isPaused = false;
    }

    public Boolean isPaused() {
        return this.isPaused;
    }

    public void changeMode() {
        this.isPaused = !this.isPaused;
    }
}