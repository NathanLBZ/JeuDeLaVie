package fr.nathan.visiteur;

import fr.nathan.JeuDeLaVie;
import fr.nathan.cellule.Cellule;

public abstract class Visiteur {
    JeuDeLaVie jeu;

    public Visiteur(JeuDeLaVie j) {
        this.jeu = j;
    }

    public void visiteCelluleVivante(Cellule cellulle) {

    }

    public void visiteCelluleMorte(Cellule cellulle) {
        
    }
}
