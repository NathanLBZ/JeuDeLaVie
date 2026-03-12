package fr.nathan.UI;

import fr.nathan.JeuDeLaVie;

public class ConsoleUI implements Observateur{
    public static JeuDeLaVie jeu;
    

    public ConsoleUI() {
    }

    public static void setJeuStatic(JeuDeLaVie jeuParam) {
        jeu = jeuParam;
    }

    @Override
    public void actualise() {
        for(int i = 0; i < 50; i++) {
            System.out.println();
        }

        int sizeX = jeu.getXmax(); // nombre de colonnes
        int sizeY = jeu.getYmax(); // nombre de lignes

        for (int row = 0; row < sizeY; row++) {
            for (int col = 0; col < sizeX; col++) {
                System.out.print(jeu.grille[col][row]);
            }
            System.out.println();
        }
    }
}