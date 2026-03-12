package fr.nathan.commande;

import fr.nathan.cellule.Cellule;

public class CommandeMeurt extends Commande {
    public CommandeMeurt(Cellule c) {
        this.cellule = c;
    }

    public void executer() {
        this.cellule.meurt();
    }
}
