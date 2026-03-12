package fr.nathan.cellule;

public class CelluleEtatVivant implements CelluleEtat{

    private static CelluleEtatVivant instanceUnique = null;

    private CelluleEtatVivant() {

    }

    synchronized public static CelluleEtatVivant getInstance() {
		if (instanceUnique == null) {
			instanceUnique = new CelluleEtatVivant();
		}
		
		return instanceUnique;
	}
    
    public CelluleEtat vit() {
        return this;
    }
    public CelluleEtat meurt() {
        return CelluleEtatMort.getInstance();
    }
    public Boolean estVivante() {
        return true;
    }
}
