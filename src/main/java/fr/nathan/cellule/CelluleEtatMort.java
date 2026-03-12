package fr.nathan.cellule;

public class CelluleEtatMort implements CelluleEtat {

    private static CelluleEtatMort instanceUnique = null;
    
    private CelluleEtatMort() {
        
    }

    synchronized public static CelluleEtatMort getInstance() {
		if (instanceUnique == null) {
			instanceUnique = new CelluleEtatMort();
		}
		
		return instanceUnique;
	}
    
    public CelluleEtat vit() {
        return CelluleEtatVivant.getInstance();
    }
    public CelluleEtat meurt() {
        return this;
    }
    public Boolean estVivante() {
        return false;
    }
}
