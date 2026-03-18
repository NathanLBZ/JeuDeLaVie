package fr.nathan.visiteur;

import fr.nathan.JeuDeLaVie;
import fr.nathan.cellule.Cellule;
import fr.nathan.commande.CommandeMeurt;
import fr.nathan.commande.CommandeVit;

public class VisiteurCovid extends Visiteur{

    public VisiteurCovid(JeuDeLaVie j) {
        super(j);
    }

    public void visiteCelluleMorte(Cellule cellulle) {
        double nbRandom = Math.random();
        if (cellulle.nombreVoisinesVivantes(jeu) > 0 && nbRandom < 0.02) {
            jeu.ajouteCommandes(new CommandeVit(cellulle));
        }
        else {
            jeu.ajouteCommandes(new CommandeMeurt(cellulle));
        }
    }
}
