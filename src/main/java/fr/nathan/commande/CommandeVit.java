package fr.nathan.commande;

import fr.nathan.cellule.Cellule;

public class CommandeVit extends Commande{
    public CommandeVit(Cellule c) {
        this.cellule = c;
    }

    public void executer() {
        this.cellule.vit();
    }
}
