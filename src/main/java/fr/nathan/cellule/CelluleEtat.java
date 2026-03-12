package fr.nathan.cellule;

import fr.nathan.visiteur.Visiteur;

public interface CelluleEtat {
    public CelluleEtat vit();
    public CelluleEtat meurt();
    public Boolean estVivante();
    public void accepte(Visiteur visiteur, Cellule cellule);
}
