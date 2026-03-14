package fr.nathan.pattern;

import fr.nathan.JeuDeLaVie;
import javafx.scene.control.Button;

public enum Pattern {
    
    // Oscillateurs
    CRAPAUD(new int[][]{{1,0}, {2,0}, {3,0}, {0,1}, {1,1}, {2,1}}),
    PHARE(new int[][]{{0,0}, {1,0}, {0,1}, {1,1}, {2,2}, {2,3}, {3,2}, {3,3}}),
    BLINKER(new int[][]{{0,0}, {1,0}, {2,0}}),
    PENTA_DECATHLON(new int[][]{{0,0}, {1,0}, {2,-1}, {2,1}, {3,0}, {4,0}, {5,0}, {6,0}, {7,-1}, {7,1}, {8,0}, {9,0}}),
    
    // Vaisseaux
    PLANNEUR(new int[][]{{0,1}, {1,2}, {2,0}, {2,1}, {2,2}}),
    LWSS(new int[][]{{0,1}, {0,3}, {1,0}, {2,0}, {3,0}, {4,0}, {4,1}, {4,2}, {3,3}}),
    
    // Vies fixes
    BLOC(new int[][]{{0,0}, {0,1}, {1,0}, {1,1}}),
    RUCHE(new int[][]{{1,0}, {2,0}, {0,1}, {3,1}, {1,2}, {2,2}}),
    BATEAU(new int[][]{{0,0}, {1,0}, {0,1}, {2,1}, {1,2}}),
    
    // Méthusalems (Générateurs de chaos)
    R_PENTOMINO(new int[][]{{0,1}, {1,0}, {1,1}, {1,2}, {2,0}}),
    ACORN(new int[][]{{1,0}, {3,1}, {0,2}, {1,2}, {4,2}, {5,2}, {6,2}}),

    //Cannon
    CANON_GOSPER(new int[][]{
    {24,0}, {22,1}, {24,1}, {12,2}, {13,2}, {20,2}, {21,2}, {34,2}, {35,2},
    {11,3}, {15,3}, {20,3}, {21,3}, {34,3}, {35,3}, {0,4}, {1,4}, {10,4},
    {16,4}, {20,4}, {21,4}, {0,5}, {1,5}, {10,5}, {14,5}, {16,5}, {17,5},
    {22,5}, {24,5}, {10,6}, {16,6}, {24,6}, {11,7}, {15,7}, {12,8}, {13,8}}),

    // Croissance infinie
    BREEDER(new int[][]{
    {0,6}, {1,6}, {2,6}, {5,6}, 
    {0,7}, {4,8}, {5,8}, 
    {1,9}, {2,9}, {5,9}, 
    {0,10}, {2,10}, {4,10}});


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