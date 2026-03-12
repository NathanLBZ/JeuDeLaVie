package fr.nathan.threads;

import fr.nathan.JeuDeLaVie;

public class GameLoop extends Thread {
    JeuDeLaVie jeu;

    public GameLoop(JeuDeLaVie jeu) {
        this.jeu = jeu;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
                this.jeu.calculerGenerationSuivante();
            } 
            catch (InterruptedException e) {
                e.printStackTrace();
            } 

        }
    }
}