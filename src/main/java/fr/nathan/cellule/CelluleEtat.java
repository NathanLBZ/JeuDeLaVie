package fr.nathan.cellule;

public interface CelluleEtat {
    public CelluleEtat vit();
    public CelluleEtat meurt();
    public Boolean estVivante();
}
