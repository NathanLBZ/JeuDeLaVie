package fr.nathan.cellule;

import fr.nathan.visiteur.Visiteur;

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
    
    @Override
    public CelluleEtat vit() {
        return CelluleEtatVivant.getInstance();
    }
    @Override
    public CelluleEtat meurt() {
        return this;
    }
    @Override
    public Boolean estVivante() {
        return false;
    }

    @Override
    public void accepte(Visiteur visiteur, Cellule cellule) {
        visiteur.visiteCelluleMorte(cellule);
    }
}
