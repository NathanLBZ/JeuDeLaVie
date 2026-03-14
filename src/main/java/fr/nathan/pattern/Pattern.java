package fr.nathan.pattern;

import fr.nathan.JeuDeLaVie;
import javafx.scene.control.Button;

public enum Pattern {
    PLANNEUR(new int[][]{{0,1}, {1,2}, {2,0}, {2,1}, {2,2}}),
    CARRE(new int[][]{{0,0}, {0,1}, {1,0}, {1,1}}),
    BARRE(new int[][]{{0,0}, {0,1}, {0,2}});

    private final int[][] coordonnees;

    Pattern(int[][] coord) {
        this.coordonnees = coord;
    }

    public int[][] getCoordonnees() {
        return coordonnees;
    }

    public void executePattern(Pattern pattern, int row, int col, JeuDeLaVie jeu, Button[][] boutons) {
        for (int[] coord : this.getCoordonnees()) {
            int colTemp = coord[0];
            int rowTemp = coord[1];

            activeCellule(rowTemp + row, colTemp + col, jeu, boutons);
        }
    }


    public void executePatternPLANNEUR(int row, int col, JeuDeLaVie jeu, Button[][] boutons) {
        
    }
    public void executePatternCARRE(int row, int col, JeuDeLaVie jeu, Button[][] boutons) {
        
    }
    public void executePatternBARRE(int row, int col, JeuDeLaVie jeu, Button[][] boutons) {
        
    }



    public void activeCellule(int row, int col, JeuDeLaVie jeu, Button[][] boutons) {
        if (!jeu.getGrilleXY(col, row).estVivante()) {
            boutons[row][col].setStyle("-fx-background-radius: 0; -fx-background-color: pink;");
            jeu.inverserEtat(jeu.getGrilleXY(col, row));
        }
    }
}