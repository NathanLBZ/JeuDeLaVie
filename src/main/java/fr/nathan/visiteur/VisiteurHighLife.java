package fr.nathan.visiteur;

import fr.nathan.JeuDeLaVie;
import fr.nathan.cellule.Cellule;
import fr.nathan.commande.CommandeMeurt;
import fr.nathan.commande.CommandeVit;

public class VisiteurHighLife extends Visiteur{

    public VisiteurHighLife(JeuDeLaVie j) {
        super(j);
    }

    public void visiteCelluleVivante(Cellule cellulle) {
        if (cellulle.nombreVoisinesVivantes(jeu) == 3 || cellulle.nombreVoisinesVivantes(jeu) == 2) {
            jeu.ajouteCommandes(new CommandeVit(cellulle));
        }
        else {
            jeu.ajouteCommandes(new CommandeMeurt(cellulle));
        }
    }

    public void visiteCelluleMorte(Cellule cellulle) {
        if (cellulle.nombreVoisinesVivantes(jeu) == 3 || cellulle.nombreVoisinesVivantes(jeu) == 6) {
            jeu.ajouteCommandes(new CommandeVit(cellulle));
        }
        else {
            jeu.ajouteCommandes(new CommandeMeurt(cellulle));
        }
    }
}
