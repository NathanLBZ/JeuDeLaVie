package fr.nathan.cellule;

import fr.nathan.JeuDeLaVie;

public class Cellule {
    CelluleEtat etat;
    int x;
    int y;

    public Cellule(int x, int y, CelluleEtat etat) {
        this.etat = etat;
        this.x = x;
        this.y = y;
    }

    public void vit() {
        this.etat = this.etat.vit();
    }

    public void meurt() {
        this.etat = this.etat.meurt();
    }

    public Boolean estVivante() {
        return this.etat.estVivante();
    }

    @Override
    public String toString() {
        if (this.etat.estVivante()) {
            return " \u25A0 ";
        }

        return " \u25A1 ";
    }

    public int nombreVoisinesVivantes(JeuDeLaVie jeu) {
        int nbVois = 0;
        for (int i = this.x - 1; i <= this.x + 1; i++) {
            for (int j = this.y - 1; j <= this.y + 1; j++) {
                Cellule cell = jeu.getGrilleXY(i, j);

                if (cell != null) {
                    if (i != this.x || j != this.y) {
                        if (cell.estVivante()) {
                            nbVois++;
                        }
                    }
                }
            }
        }      
        return nbVois;
    }

}
