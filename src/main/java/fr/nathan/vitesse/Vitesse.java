package fr.nathan.vitesse;

public class Vitesse {
    public int cooldown;

    public Vitesse() {
        this.cooldown = 100;
    }

    public int getCooldown() {
        return this.cooldown;
    }

    public void setCooldown(int newCooldown) {
        this.cooldown = newCooldown;
    }
}
